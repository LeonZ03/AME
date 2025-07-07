package Expander

import chisel3._
import chisel3.util._

import common._
import MMAU._






//MMAU latency = numm * numn * numk + 2*sramlatency + m + n/4 -2
class FSM_MMAU extends MMAUFormat{
  val io = IO(new Bundle {
    val sigStart = Input(Bool())    //启动信号
    val Ops_io = new Ops_IO //操作数矩阵
    val FSM_MMAU_io = Flipped(new FSM_MMAU_IO) //输入计算单元的信号，均为寄存器链首数据
    val TileHandler_MMAU_io = Flipped(new TileHandler_MMAU_IO)

    val sigDone = Output(Bool())    //结束信号

  })

  /*  表示工作状态或空闲状态 */
  val regFSM_MMAU_is_busy = RegInit(false.B) //true：工作状态；false：空闲状态，此时C写使能必为false



  /*  表示工作状态内部的状态  */
  val regOffM = RegInit(0.U(log2Ceil(numM).W))   //log2,向上取整
  val regOffN = RegInit(0.U(log2Ceil(numN).W))
  val regOffK = RegInit(0.U(log2Ceil(numK).W))   //表示nState内部的numK个kState状态
  val regOffK_1H = RegInit(1.U(numK.W))    //独热编码，表示nState内部的numK个kState状态
  
  
  regOffM := Mux(io.sigStart === true.B , 0.U , 
                Mux(regOffN === io.TileHandler_MMAU_io.numn - 1.U && regOffK === io.TileHandler_MMAU_io.numk - 1.U , 
                  Mux(regOffM === io.TileHandler_MMAU_io.numm - 1.U , 0.U , regOffM + 1.U) , regOffM)) //可能会有一定开销，可以加一个regOffN_1H优化

  regOffN := Mux(io.sigStart === true.B , 0.U ,
                Mux(regOffK === io.TileHandler_MMAU_io.numk - 1.U , 
                  Mux(regOffN === io.TileHandler_MMAU_io.numn - 1.U , 0.U , regOffN + 1.U) , regOffN))    //regOffK最大时更新

  regOffK := Mux(io.sigStart === true.B , 0.U , 
                Mux(regOffK === io.TileHandler_MMAU_io.numk - 1.U , 0.U , regOffK + 1.U))  //递增，循环，最大到numk-1
  
  regOffK_1H := Mux(io.sigStart === true.B , 1.U , 
                  Mux(regOffK === io.TileHandler_MMAU_io.numk - 1.U , 1.U , Cat(regOffK_1H(numK-2 , 0) , regOffK_1H(numK-1))))   //循环左移,最左到bit[numk-1]
  
  
  /*    muxCtrlC muxCtrlSum    */

  io.FSM_MMAU_io.firstMuxCtrl := regOffK_1H(m-1 , 0)

  /*    read matrixA    */
  
  io.FSM_MMAU_io.firstAddrReadA := regOffM * (numK).U + regOffK


  /*    read matrixB    */

  io.FSM_MMAU_io.firstAddrReadB := regOffN * (numK).U + regOffK


  /*    read & write matrixC    */

  
  val wireNumStep = Wire(UInt(log2Ceil(numM * numN).W))

  when(regOffN === 0.U && regOffM === 0.U){
    wireNumStep := (io.TileHandler_MMAU_io.numm - 1.U) * numN.U + (io.TileHandler_MMAU_io.numn - 1.U)
  }.elsewhen(regOffN === 0.U){
    wireNumStep := numN.U * regOffM + regOffN - (numN.U - io.TileHandler_MMAU_io.numn + 1.U)
  }.otherwise{
    wireNumStep := numN.U * regOffM + regOffN - 1.U
  }
  io.FSM_MMAU_io.firstAddrPublicC := wireNumStep * m.U + regOffK  //只有kState(0) ~ kState(m-1) 的数据是有意义的

  //write C enable

  val regCntZero = RegInit(0.U(1.W))  //记录kState为 numk-1 的次数,1次之后不再更新
  when(io.sigStart === true.B){
    regCntZero := 0.U
  }.elsewhen(regCntZero === 1.U){
    regCntZero := regCntZero
  }.elsewhen(regOffK === io.TileHandler_MMAU_io.numk - 1.U){
    regCntZero := regCntZero + 1.U
  }.otherwise{
    regCntZero := regCntZero
  }

  val regCntDone = RegInit(0.U(log2Ceil(n/4 + m).W)) //上电时即为满，是属于sigDone相关寄存器，C的第一个bank的index0结束后（regCntDone开始计时）,还需等待n/4 + m 个周期,所有bank结束

  when(m.U < io.TileHandler_MMAU_io.numk){
    io.FSM_MMAU_io.firstEnWriteC := Mux(regCntZero === 1.U && regOffK >= 0.U && regOffK <= (m-1).U && regCntDone < m.U - sramLatency.U && regFSM_MMAU_is_busy, true.B , false.B)
  }.otherwise{
    io.FSM_MMAU_io.firstEnWriteC := Mux(regCntZero === 1.U && regCntDone < m.U - sramLatency.U && regFSM_MMAU_is_busy, true.B , false.B)
  }


  /*    signal done    */
  
  val wireDone = Wire(Bool()) //C的第一个bank的index0结束
  wireDone := Mux(regOffM === 0.U && regOffN === 0.U && regOffK === 1.U && regCntZero === 1.U && regFSM_MMAU_is_busy === true.B, true.B , false.B)  //仅在工作时才有意义



  //结束时Done拉高，随后拉低
  when(io.sigStart === true.B){
    regCntDone := 0.U
  }.elsewhen(regCntDone === (n/4 + m - 1).U){ //满了则归零，Done信号不会一直拉高
    regCntDone := 0.U
  }.elsewhen(wireDone === true.B || regCntDone > 0.U){
    regCntDone := regCntDone + 1.U
  }.otherwise{
    regCntDone := regCntDone
  }

  val wireSigDone = Wire(Bool())
  wireSigDone := Mux(regCntDone === (n/4 + m - 1).U , true.B , false.B)
  io.sigDone := wireSigDone //上电时，默认输出true



  /*  表示FSM是否工作 */
  //由空闲进入工作只能是start拉高，反之只能是done拉高
  when(io.sigStart){
    regFSM_MMAU_is_busy := true.B  //start拉高，进入工作状态
  }.elsewhen(wireSigDone){
    regFSM_MMAU_is_busy := false.B //done为真，进入空闲状态
  }.otherwise{
    regFSM_MMAU_is_busy := regFSM_MMAU_is_busy
  }



  /*    actPort   */
  val regActPort = RegInit(false.B)

  when(io.sigStart){  //仅在启动和结束之间的时间激活
    regActPort := true.B
  }.elsewhen(io.sigDone){
    regActPort := false.B
  }.otherwise{
    regActPort := regActPort
  }

  io.FSM_MMAU_io.actPortReadA := regActPort
  io.FSM_MMAU_io.actPortReadB := regActPort
  io.FSM_MMAU_io.actPortReadC := regActPort
  io.FSM_MMAU_io.actPortWriteC := regActPort

  /*    Ops   */
  io.FSM_MMAU_io.Ops_io <> io.Ops_io

  // 打印提示信息（仅仿真有效）
  // when(io.FSM_MMAU_io.firstEnWriteC) {
  //   printf(p"[INFO] firstEnWriteC is HIGH at cycle, offK = ${regOffK}, offN = ${regOffN}, offM = ${regOffM}\n")
  // }

  // printf(p"offK = ${regOffK}, offN = ${regOffN}, offM = ${regOffM}    \n")
  // printf(p"addrA = ${io.FSM_MMAU_io.firstAddrReadA} , addrB = ${io.FSM_MMAU_io.firstAddrReadB} , addrC = ${io.FSM_MMAU_io.firstAddrPublicC}\n")
  // printf(p"sigStart = ${io.sigStart} , sigDone = ${io.sigDone}  , regCntDone = ${regCntDone}  regFSM_MMAU_is_busy = ${regFSM_MMAU_is_busy}\n")
}




class FSM_MLU extends Module{ //MLU状态机
  val io = IO(new Bundle {
    val sigStart = Input(Bool())    //启动信号
    val rs1 = Input(UInt(Consts.rs1_LEN.W))  
    val rs2 = Input(UInt(Consts.rs2_LEN.W))
    val md = Input(UInt(Consts.All_ADDR_LEN.W))
    val is_loadAB = Input(Bool()) //是load A/B指令
    val is_loadC = Input(Bool()) //是load C指令
    
    val TileHandler_MLU_io = Flipped(new TileHandler_MLU_IO)  //padding后用于设置状态机
    val FSM_MLU_io = new FSM_MLU_IO //连接下层MLU

    // val sigReqDone = Output(Bool()) //读请求信号不再产生，不同于数据全部搬运完 
    val sigDone = Output(Bool()) //Load完成的信号，拉高再拉低
  })

  /*  TileHandler_MLU_io  */
  val nRow = io.TileHandler_MLU_io.nRow 
  val nCol = io.TileHandler_MLU_io.nCol 

  /*  op  */
  val baseAddr = io.rs1
  val stride = io.rs2

  /* 表示状态 */
  val regRow = RegInit(0.U(Consts.nRow_LEN.W))  //因为和nRow那里共用nRow_LEN，所以实际上多一位是浪费的
  val regCol = RegInit(0.U(Consts.nCol_LEN.W))


  regRow := Mux(io.sigStart , 0.U , 
              Mux(regRow === nRow - 1.U , 0.U , regRow + 1.U))

  regCol := Mux(io.sigStart , 0.U , 
              Mux(regCol === nCol - 1.U && regRow === nRow - 1.U, 0.U , 
                Mux(regRow === nRow - 1.U , regCol + 1.U , regCol)))

  /*  FSM_MLU_io  */
  for(y <- 0 until 8){  //default
      io.FSM_MLU_io.Cacheline_Read_io(y).addr := 0.U
      io.FSM_MLU_io.Cacheline_Read_io(y).id := 0.U
  }




  when(io.is_loadAB) {  //load A或load B类

    for(y <- 0 until 8){
      io.FSM_MLU_io.Cacheline_Read_io(y).addr := baseAddr + (regRow * 8.U + y.U) * stride + regCol * 64.U
      io.FSM_MLU_io.Cacheline_Read_io(y).id := Cat(regCol(1 , 0) , regRow(2 , 0))
    }
    
  }.elsewhen(io.is_loadC) { //load C类

    for(y <- 0 until 8){
      io.FSM_MLU_io.Cacheline_Read_io(y).addr := baseAddr + (regRow * 4.U + (y / 2).U ) * stride + 128.U * regCol + (y % 2).U * 64.U
      io.FSM_MLU_io.Cacheline_Read_io(y).id := Cat(regCol(0) , regRow(3 , 0))
    }

  }

  io.FSM_MLU_io.md := io.md
  io.FSM_MLU_io.is_loadAB := io.is_loadAB
  io.FSM_MLU_io.is_loadC := io.is_loadC


  



  /*  sigDone  */
  val wireDone = Wire(Bool())
  val regCntDone = RegInit(VecInit(Seq.fill(8)(0.U(log2Ceil(32 * 8 + 1).W)))) //对应8条sigLineDone，用于计数，从0开始，计到nRow * nCol * 8 则满(以load A/B 8bit为例)

  for(i <- 0 until 8){
    when(io.sigStart || wireDone){  //启动时归0；Load结束后也归零，这样Done信号就不会一直拉高
      regCntDone(i) := 0.U
    }.elsewhen(io.FSM_MLU_io.sigLineDone(i)){ //由MLU告知成功写入一条数据，则+1
      regCntDone(i) := regCntDone(i) + 1.U
    }.otherwise{  //其他情况不变
      regCntDone(i) := regCntDone(i)
    }

//debug
// printf(p"regCntDone($i) = ${regCntDone(i)} , io.FSM_MLU_io.sigLineDone($i) = ${io.FSM_MLU_io.sigLineDone(i)}\n")  
  } 
// printf(p"nRow = $nRow , nCol = $nCol\n") 

  val allDone = Wire(Bool())
  allDone := false.B
  
  when(io.is_loadAB) {
    allDone := regCntDone.map(_ === (nRow * nCol * 8.U)).reduce(_ && _)   //均为nRow * nCol * 8(以load A/B 8bit为例)
  }.elsewhen(io.is_loadC) {
    allDone := regCntDone.map(_ === (nRow * nCol * 4.U)).reduce(_ && _)   //均为nRow * nCol * 4(以load C 32bit为例)
  }
  

  val allZero = regCntDone.map(_ === 0.U).reduce(_ && _)  //均为0
  wireDone := allDone && !allZero   //所有regCntDone均为nRow * nCol * 8(以load A/B 8bit为例)，则Load结束(该逻辑仅在Load执行期间生效，其余时间默认为false)

  io.sigDone := wireDone





  /*  valid   */
  val regL2State = RegInit(false.B)  //指示L2是否为工作状态，影响访问L2信号valid是否有效
  val wireReqDone = Mux(regRow === nRow - 1.U && regCol === nCol - 1.U , true.B , false.B)  //暂时先这么处理把

  when(io.sigStart){
    regL2State := true.B
  }.elsewhen(wireReqDone){
    regL2State := false.B
  }

  for(y <- 0 until 8){
    io.FSM_MLU_io.Cacheline_Read_io(y).valid := regL2State
  }

  /*  Port State  */

  val regPortState = RegInit(false.B)  //sigStart信号开始，直到Load指令结束，用于RF的Port的激活或注销

  when(io.sigStart){//
    regPortState := true.B
  }.elsewhen(wireDone){
    regPortState := false.B
  }

  io.FSM_MLU_io.sigPortState := regPortState  //告知MLU是否处于工作状态，用于Port的激活或注销
  

  // /*    debug   */
  // printf(p"regRow = $regRow , regCol = $regCol , sigStart = ${io.sigStart} , sigReqDone = ${io.sigReqDone} , stride = ${stride} , valid = ${regL2State} \n")
  // for(i <- 0 until 8){
  //   printf(p"$i : addr = ${io.FSM_MLU_io.Cacheline_Read_io(i).addr} , id = ${io.FSM_MLU_io.Cacheline_Read_io(i).id}\n")
  // }
  // printf(p"\n\n")



}







class FSM_MSU extends Module {
  val io = IO(new Bundle {
    val sigStart = Input(Bool())    //启动信号
    val rs1 = Input(UInt(Consts.rs1_LEN.W))  
    val rs2 = Input(UInt(Consts.rs2_LEN.W))
    val md = Input(UInt(Consts.All_ADDR_LEN.W))

    val is_storeC = Input(Bool()) //是store C指令
    
    val TileHandler_MSU_io = Flipped(new TileHandler_MSU_IO)  //padding后用于设置状态机
    val FSM_MSU_io = new FSM_MSU_IO //连接下层MSU

    val sigDone = Output(Bool()) //Store完成的信号，拉高再拉低
  })


  /*  TileHandler_MSU_io  */
  val nRow = io.TileHandler_MSU_io.nRow 
  val nCol = io.TileHandler_MSU_io.nCol 

  /*  op  */
  val baseAddr = io.rs1
  val stride = io.rs2

  /* 表示状态 */
  val regRow = RegInit(0.U(Consts.nRow_LEN.W))  //因为和nRow那里共用nRow_LEN，所以实际上多一位是浪费的
  val regCol = RegInit(0.U(Consts.nCol_LEN.W))


  regRow := Mux(io.sigStart , 0.U , 
              Mux(regRow === nRow - 1.U , 0.U , regRow + 1.U))

  regCol := Mux(io.sigStart , 0.U , 
              Mux(regCol === nCol - 1.U && regRow === nRow - 1.U, 0.U , 
                Mux(regRow === nRow - 1.U , regCol + 1.U , regCol)))

  /*  FSM_MSU_io  */
  for(y <- 0 until 2){
    io.FSM_MSU_io.FSM_MSU_Output_io(y).addr := 0.U
    io.FSM_MSU_io.FSM_MSU_Output_io(y).index := 0.U
    io.FSM_MSU_io.FSM_MSU_Output_io(y).valid := false.B
  }


  when(io.is_storeC) {

    for(y <- 0 until 2){
      io.FSM_MSU_io.FSM_MSU_Output_io(y).addr := baseAddr + regRow * stride + regCol * 128.U + 64.U * y.U

      when(regRow < 32.U && regCol === 0.U) {//区域1
        io.FSM_MSU_io.FSM_MSU_Output_io(y).index := regRow
      }.elsewhen(regRow >= 32.U && regCol === 1.U) {//区域4
        io.FSM_MSU_io.FSM_MSU_Output_io(y).index := regRow + 64.U
      }.otherwise {//区域2,3
        io.FSM_MSU_io.FSM_MSU_Output_io(y).index := regRow + 32.U
      }
    }
    
  }

  io.FSM_MSU_io.md := io.md
  io.FSM_MSU_io.is_storeC := io.is_storeC


  /*  valid   */
  val regL2State = RegInit(false.B)  //指示L2是否为工作状态，影响访问L2信号valid是否有效
  val wireReqDone = Mux(regRow === nRow - 1.U && regCol === nCol - 1.U , true.B , false.B)  //暂时先这么处理把

  when(io.sigStart){
    regL2State := true.B
  }.elsewhen(wireReqDone){
    regL2State := false.B
  }

  for(y <- 0 until 2){
    io.FSM_MSU_io.FSM_MSU_Output_io(y).valid := regL2State
  }

  io.FSM_MSU_io.sigPortState := regL2State  //RF的激活注销,这里可以共用



  /*  sigDone  */
  val wireDone = Wire(Bool())
  val regCntDone = RegInit(VecInit(Seq.fill(2)(0.U(log2Ceil(128 + 1).W)))) //对应2条sigLineDone，用于计数，从0开始，计到nRow * nCol  则满(以load C为例)

  for(i <- 0 until 2){
    when(io.sigStart || wireDone){  //启动时归0；Load结束后也归零，这样Done信号就不会一直拉高
      regCntDone(i) := 0.U
    }.elsewhen(io.FSM_MSU_io.sigLineDone(i)){ //由MSU告知成功写入一条数据，则+1
      regCntDone(i) := regCntDone(i) + 1.U
    }.otherwise{  //其他情况不变
      regCntDone(i) := regCntDone(i)
    }
  }

//debug
// printf(p"regCntDone($i) = ${regCntDone(i)} , io.FSM_MSU_io.sigLineDone($i) = ${io.FSM_MSU_io.sigLineDone(i)}\n")  
//   } 
// printf(p"nRow = $nRow , nCol = $nCol\n") 

  val allDone = Wire(Bool())
  allDone := false.B

  when(io.is_storeC) {
    allDone := regCntDone.map(_ === (nRow * nCol)).reduce(_ && _)  //均为nRow * nCol
  }
  
  val allZero = regCntDone.map(_ === 0.U).reduce(_ && _)  //均为0

  wireDone := allDone && !allZero   //计数到达,同时排除全为0的情况(此时一般不在Store指令执行期间)

  io.sigDone := wireDone




}




