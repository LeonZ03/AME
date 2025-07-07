package MSU

import chisel3._
import chisel3.util._

import common._
import Expander._
import RegFile._
import IssueQueen._


/*
MSU模块用于实现矩阵Store指令的数据搬运和写回，连接FSM、L2和寄存器堆。
主要功能：
  1. 从寄存器堆读取数据，按需拼接后写入L2。
  2. 支持多路Cacheline并发写入，缓冲写请求。
  3. 连接FSM控制信号，实现数据流的时序与有效性管理。
  4. 写入成功后向FSM上报完成状态。
*/

class MSU extends Module {
    val io = IO(new Bundle {
        val FSM_MSU_io = Flipped(new FSM_MSU_IO)    //上级FSM传来
        val MSU_L2_io = new MSU_L2_IO   //访问L2,写入
        val RegFileAllRead_io = Flipped(new RegFileAllRead_IO)  //读RF
    })

    val writeBuffer = Seq.fill(2)(Module(new SimpleHandshakeFIFO(depth = 10, width = Consts.L2_ADDR_LEN + Consts.L2_DATA_LEN))) //L2写请求缓冲

    /*  read from RF    */
    io.RegFileAllRead_io := DontCare    

    io.RegFileAllRead_io.addr := io.FSM_MSU_io.md    //选择读取的寄存器
    io.RegFileAllRead_io.act := io.FSM_MSU_io.sigPortState // 由FSM控制

    printf(p"[MSU] RFport = ${io.RegFileAllRead_io.act}\n")
    printf(p"[MSU] RFaddr = ${io.RegFileAllRead_io.addr}\n")

    when(io.FSM_MSU_io.is_storeC) {
        for(i <- 0 until Consts.numAccBank){//读RF
            io.RegFileAllRead_io.r(i).req.bits.setIdx := io.FSM_MSU_io.FSM_MSU_Output_io(0).index   //这里上面传来的两路index相同
            io.RegFileAllRead_io.r(i).req.valid := true.B
            printf(p"[MSU] index = ${io.RegFileAllRead_io.r(i).req.bits.setIdx}\n")
        }
    }


    /*  write L2  */

    //由于RF访存会有延迟,所以需要寄存器打一拍
    val regAddr = RegInit(VecInit(Seq.fill(2)(0.U(Consts.L2_ADDR_LEN.W))))
    val regValid = RegInit(VecInit(Seq.fill(2)(false.B)))

    val wireData = Wire(Vec(2, UInt(Consts.L2_DATA_LEN.W)))
    wireData := VecInit(Seq.fill(2)(0.U))


    when(io.FSM_MSU_io.is_storeC) {
        val RF_io = io.RegFileAllRead_io    //简写,提高代码可读性

        //cacheline 0 
        regAddr(0) := io.FSM_MSU_io.FSM_MSU_Output_io(0).addr
        regValid(0) := io.FSM_MSU_io.FSM_MSU_Output_io(0).valid
        wireData(0) := Cat(RF_io.r(0).resp.data.head.asUInt , RF_io.r(1).resp.data.head.asUInt , RF_io.r(2).resp.data.head.asUInt , RF_io.r(3).resp.data.head.asUInt)

        // printf(p"[MSU] wireData(0) = ${wireData(0)}\n")
        printf(p"[MSU] wireData(0) = 0x${Hexadecimal(wireData(0))}\n")
        printf(p"[MSU] r(0) = 0x${Hexadecimal(RF_io.r(0).resp.data(0).asUInt)}\n")

        //cacheline 1 
        regAddr(1) := io.FSM_MSU_io.FSM_MSU_Output_io(1).addr
        regValid(1) := io.FSM_MSU_io.FSM_MSU_Output_io(1).valid
        wireData(1) := Cat(RF_io.r(4).resp.data.head.asUInt , RF_io.r(5).resp.data.head.asUInt , RF_io.r(6).resp.data.head.asUInt , RF_io.r(7).resp.data.head.asUInt)

    }

    for(i <- 0 until 2){//buffer缓冲,写入L2
        //入buffer
        writeBuffer(i).io.enq_valid := regValid(i)
        writeBuffer(i).io.enq_bits := Cat(wireData(i) , regAddr(i))

        //出buffer
        io.MSU_L2_io.Cacheline_Write_io(i).addr := writeBuffer(i).io.deq_bits(Consts.L2_ADDR_LEN - 1 , 0)
        io.MSU_L2_io.Cacheline_Write_io(i).data := writeBuffer(i).io.deq_bits(Consts.L2_ADDR_LEN + Consts.L2_DATA_LEN - 1 , Consts.L2_ADDR_LEN)
        io.MSU_L2_io.Cacheline_Write_io(i).valid := writeBuffer(i).io.deq_valid
        writeBuffer(i).io.deq_ready := io.MSU_L2_io.Cacheline_Write_io(i).ready

        //写入成功上报  
        io.FSM_MSU_io.sigLineDone(i) :=  io.MSU_L2_io.Cacheline_WriteBack_io(i).valid  //每写入成功一次，则上报一次
    }

    


}