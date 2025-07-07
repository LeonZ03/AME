package MLU

import chisel3._
import chisel3.util._

import common._
import Expander._
import RegFile._
import IssueQueen._

/*
MLU模块用于实现矩阵Load指令的数据搬运和写回，连接FSM、L2和寄存器堆。
主要功能：
  1. 缓冲和路由L2读请求，支持多路Cacheline并发处理。
  2. 对L2返回数据进行分割、重组和路由，写入目标寄存器。
  3. 支持多种Load模式（如Load AB、Load C），灵活分配写端口和数据。
  4. 连接FSM控制信号，实现数据流的时序与有效性管理。
*/

class MLU extends Module{
    val io = IO(new Bundle {
        val FSM_MLU_io = Flipped(new FSM_MLU_IO)    //接FSM
        val MLU_L2_io = new MLU_L2_IO   //访问L2
        val RegFileAllWrite_io = Flipped(new RegFileAllWrite_IO)  //写RF
    })

    /*  read from L2 */
    val readBuffer = Seq.fill(8)(Module(new SimpleHandshakeFIFO(depth = 10, width = Consts.L2_ADDR_LEN + Consts.L2_ID_LEN))) //L2读请求缓冲
    for(y <- 0 until 8){//对每个Cacheline
        //写入FIFO
        readBuffer(y).io.enq_valid := io.FSM_MLU_io.Cacheline_Read_io(y).valid
        io.FSM_MLU_io.Cacheline_Read_io(y).ready := readBuffer(y).io.enq_ready  //实际没有作用,FSM不管这个ready信号(他直接输入,无视ready),因此FIFO要足够大
        readBuffer(y).io.enq_bits := Cat(io.FSM_MLU_io.Cacheline_Read_io(y).addr , io.FSM_MLU_io.Cacheline_Read_io(y).id) 

        //读出FIFO
        io.MLU_L2_io.Cacheline_Read_io(y).valid := readBuffer(y).io.deq_valid
        readBuffer(y).io.deq_ready := io.MLU_L2_io.Cacheline_Read_io(y).ready
        io.MLU_L2_io.Cacheline_Read_io(y).addr := readBuffer(y).io.deq_bits(Consts.L2_ADDR_LEN + Consts.L2_ID_LEN - 1 , Consts.L2_ID_LEN)
        io.MLU_L2_io.Cacheline_Read_io(y).id := readBuffer(y).io.deq_bits(Consts.L2_ID_LEN - 1 , 0)
    }
    // io.FSM_MLU_io.Cacheline_Read_io <> io.MLU_L2_io.Cacheline_Read_io

    /*    写RF    */
    val width = 128 + 5 + 3
    // val wireSplit = Wire(Vec(8, Vec(8, UInt(width.W))))  //接MultiInputMux，每个cacheline分8路(以Load AB为例)
    val wireSplit = WireInit(VecInit(Seq.fill(8)(VecInit(Seq.fill(8)(0.U(width.W))))))//接MultiInputMux，每个cacheline分8路(以Load AB为例)
    val subMultiInputBuffer = Seq.fill(8)(Module(new MultiInputBuffer(numWay = 8, width = 136 , depth = 32)))    //8个cacheline , 每个cacheline拆成8路，每路是8B的向量，外加一个5bit的id ,再加一个index的偏移量offset（0～7）(以Load AB为例)又因为需要和C复用,故选取较大的128+5

    io.RegFileAllWrite_io.addr := io.FSM_MLU_io.md    //选择写入的寄存器
    io.RegFileAllWrite_io.act := io.FSM_MLU_io.sigPortState // 由FSM控制

    for(i <- 0 until Consts.numAllBank){    //写端口赋缺省值
        io.RegFileAllWrite_io.w(i).req.bits.setIdx := 0.U
        io.RegFileAllWrite_io.w(i).req.bits.data.head := 0.U
        io.RegFileAllWrite_io.w(i).req.valid := false.B
    }

    // 为每个实例的所有输入端口赋缺省值
    for (buf <- subMultiInputBuffer) {
        // 初始化所有输入数据线 (io.in)
        for (i <- 0 until 8) {  // numWay = 8
            buf.io.in(i) := 0.U(136.W)  // width = 136
            buf.io.in_valid(i) := false.B
        }
    }
    
    
    for(y <- 0 until 8){//为每个Cacheline分时缓冲,路由

        // 入，8路/4路
        val id = io.MLU_L2_io.Cacheline_ReadBack_io(y).id
        val valid = io.MLU_L2_io.Cacheline_ReadBack_io(y).valid
        val indata = io.MLU_L2_io.Cacheline_ReadBack_io(y).data

// printf(p"y=$y: id=${id}, valid=${valid}, data=0x${Hexadecimal(indata)}\n")

        when(io.FSM_MLU_io.is_loadAB) {//每个MultiInputBuffer对应一个Cacheline

            for(i <- 0 until 8){//cacheline内部拆8份
                wireSplit(y)(i) := Cat( (7 - i).U(3.W) , indata(i * 64 + 63 , i * 64) , id )    //内部index + split_data + id
                subMultiInputBuffer(y).io.in(i) := wireSplit(y)(i)
                subMultiInputBuffer(y).io.in_valid(i) := valid  //有效

// printf(p"[debug] y=$y: 0x${Hexadecimal(subMultiInputBuffer(y).io.in(i)(68 , 5))}, ${subMultiInputBuffer(y).io.in_valid(i)}\n")
            }

        }.elsewhen(io.FSM_MLU_io.is_loadC) {//每个MultiInputBuffer对应一个Bank
            for(i <- 0 until 4){//大端,即对bank0~bank3,bank0数据由cachline返回的数据的高位段获得
                wireSplit(y)(3 - i) := Cat( y.U(3.W) , indata(i * 128 + 127 , i * 128) , id )  //原Cacheline编号 + split_data + id
            }

            for(i <- 0 until 4){
                if(y % 2 == 0){//cachline y为偶数
                    subMultiInputBuffer(i).io.in(y / 2) := wireSplit(y)(i)
                    subMultiInputBuffer(i).io.in_valid(y / 2) := valid
                }else{
                    subMultiInputBuffer(i + 4).io.in(y / 2) := wireSplit(y)(i)
                    subMultiInputBuffer(i + 4).io.in_valid(y / 2) := valid
                }
            }

            for(i <- 0 until 8){//每个MultiInputBuffer的余下4个入口弃用
                for(j <- 4 until 8){
                    subMultiInputBuffer(i).io.in_valid(j) := false.B
                }
            }
        }

        
        
        // 出，1路

        subMultiInputBuffer(y).io.out.ready := true.B  //始终希望读

        val readvalid = Wire(Bool())
        readvalid := subMultiInputBuffer(y).io.out.valid    //判断读出的数据是否有意义

        val output = Wire(UInt(width.W))
        output := subMultiInputBuffer(y).io.out.bits //分时输出数据

        // val output = subMultiInputBuffer(y).io.out.bits
        // val readvalid = subMultiInputBuffer(y).io.out.valid

        io.FSM_MLU_io.sigLineDone(y) := Mux(readvalid , true.B , false.B)   //每写入成功一次，则上报一次

        when(io.FSM_MLU_io.is_loadAB) {

            val outdata = Wire(UInt(64.W))
            outdata := output(68 , 5)   //数据段

            val bankId = Wire(UInt(5.W))
            bankId := Cat(output(1,0), y.U(3.W)) //bank号

            val index = Wire(UInt(Consts.All_INDEX_LEN.W))
            index := Mux(output(2) === 0.U , output(2) * 4.U + output(4,3) * 8.U , output(2) * 4.U + output(4,3) * 8.U + 28.U)  //bank内地址
            
            val offset = Wire(UInt(Consts.All_INDEX_LEN.W))
            offset := output(71 , 69)    //偏移量，加上index才是真正的地址

// printf(p"[DEBUG12345] outdata = 0x${Hexadecimal(outdata)} , readvalid = $readvalid\n")

            for (i <- 0 until 4) {//对每条特定的cacheline，仅有4个路由目标
                when(bankId === (i * 8 + y).U) { //！！！
                    io.RegFileAllWrite_io.w(i * 8 + y).req.bits.setIdx := index + offset
                    io.RegFileAllWrite_io.w(i * 8 + y).req.bits.data.head := outdata
                    io.RegFileAllWrite_io.w(i * 8 + y).req.valid := readvalid

                                    // // debug
// printf(p"[DEBUGGGG] y = $y -> bankId = $bankId\n")
// printf(p"[DEBUGGGG] WritePort[${i * 8 + y}] setIdx = 0x${Hexadecimal(io.RegFileAllWrite_io.w({i * 8 + y}).req.bits.setIdx)}\n")
// printf(p"[DEBUGGGG] WritePort[${i * 8 + y}] data = 0x${Hexadecimal(io.RegFileAllWrite_io.w({i * 8 + y}).req.bits.data.head.asUInt)}\n")
// printf(p"[DEBUGGGG] WritePort[${i * 8 + y}] valid  = ${io.RegFileAllWrite_io.w({i * 8 + y}).req.valid}\n")
                }.otherwise {
                    io.RegFileAllWrite_io.w(i * 8 + y).req.bits.setIdx := 0.U
                    io.RegFileAllWrite_io.w(i * 8 + y).req.bits.data.head := 0.U
                    io.RegFileAllWrite_io.w(i * 8 + y).req.valid := false.B
                }



            }

            

        }.elsewhen(io.FSM_MLU_io.is_loadC) {
            
            val outdata = Wire(UInt(128.W))
            outdata := output(132 , 5)   //数据段

            val cachelineID = Wire(UInt(3.W))
            cachelineID := output(135 , 133)    //原cachelineID

            val row = output(3 , 0)
            val col = output(4)

            val index = Wire(UInt(Consts.All_INDEX_LEN.W))
            when(row < 8.U && col === 0.U) { //第2象限

                index := row * 4.U + cachelineID(2 , 1)
                
            }.elsewhen(row >= 8.U && col === 1.U) { //第4象限

                index := row * 4.U + cachelineID(2 , 1) + 64.U

            }.otherwise {   //第1 , 3象限

                index := row * 4.U + cachelineID(2 , 1) + 32.U
            }

            //无需路由,每个MultiInputBuffer即对应相应Bank
            io.RegFileAllWrite_io.w(y).req.bits.setIdx := index 
            io.RegFileAllWrite_io.w(y).req.bits.data.head := outdata
            io.RegFileAllWrite_io.w(y).req.valid := readvalid
        
        }

    }


}






















