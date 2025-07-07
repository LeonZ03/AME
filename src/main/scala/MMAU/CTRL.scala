package MMAU

import chisel3._
import chisel3.util._

import common._
import Expander._



/*    FSM与MMAU解耦，CTRL不包含任何状态机，纯数据通路，考虑sramLatency    */
class CTRL extends MMAUFormat{
  val io = IO(new Bundle {

    val FSM_MMAU_io = new FSM_MMAU_IO


    val muxCtrlC = Output(Vec(m , Vec(n/4 , Bool())))     
    val muxCtrlSum = Output(Vec(m , Vec(n/4 , Bool())))          
    val addrReadA = Output(Vec(m , UInt(Tr_INDEX_LEN.W)))
    val addrReadB = Output(Vec(n/4 , UInt(Tr_INDEX_LEN.W)))
    val addrReadC = Output(Vec(n/4 , UInt(Acc_INDEX_LEN.W)))
    val addrWriteC = Output(Vec(n/4 , UInt(Acc_INDEX_LEN.W)))
    val sigEnWriteC = Output(Vec(n/4 , Bool()))    //C写使能
    

  })


  /*    muxCtrlC muxCtrlSum    */
  require(sramLatency >= 1)
  val regLatency = Reg(Vec(sramLatency , UInt(m.W)))  //考虑访存延迟引入
  regLatency.foreach(_ := 0.U)
  regLatency(0) := io.FSM_MMAU_io.firstMuxCtrl
  for (i <- 1 until sramLatency) {
    regLatency(i) := regLatency(i - 1)
  }


  for(i <- 0 until m){//这很巧妙，muxCtrl直接复用regOffK
    io.muxCtrlC(i)(0) := regLatency(sramLatency-1)(i)
    io.muxCtrlSum(i)(0) := regLatency(sramLatency-1)(i)
  }

  if(n > 4){  //此时不止一列tile,需要寄存器打拍
    val regMuxCrtl = Reg(Vec(n/4 - 1 , UInt(m.W)))
    regMuxCrtl.foreach(_ := 0.U)
    for(i <- 1 until n/4 -1) { regMuxCrtl(i) := regMuxCrtl(i-1)}
    regMuxCrtl(0) := regLatency(sramLatency-1)

    for(i <- 0 until m){
      for(j <- 1 until n/4){
        io.muxCtrlC(i)(j) := regMuxCrtl(j-1)(i)
        io.muxCtrlSum(i)(j) := regMuxCrtl(j-1)(i)
      }
    }
  }


  /*    read matrixA    */
  
  val regDelayA = Reg(Vec(m-1, UInt(Tr_INDEX_LEN.W)))  // 定义寄存器组
  regDelayA.foreach(_ := 0.U)  // 初始化所有寄存器为 0
  
  regDelayA(0) := io.FSM_MMAU_io.firstAddrReadA
  for(i <- 1 until m-1){
    regDelayA(i) := regDelayA(i-1)   //reg组内部连接,实现"数据流动"的效果
  }

  io.addrReadA(0) := io.FSM_MMAU_io.firstAddrReadA
  for(i <- 1 until m){
    io.addrReadA(i) := regDelayA(i-1)
  }

  /*    read matrixB    */

  io.addrReadB(0) := io.FSM_MMAU_io.firstAddrReadB  

  if(n > 4){
    val regDelayB = Reg(Vec(n/4 - 1, UInt(Tr_INDEX_LEN.W)))  // 定义寄存器组
    regDelayB.foreach(_ := 0.U)  // 初始化所有寄存器为 0

    regDelayB(0) := io.FSM_MMAU_io.firstAddrReadB
    for(i <- 1 until n/4 - 1){
      regDelayB(i) := regDelayB(i-1)   //reg组内部连接,实现"数据流动"的效果
    }

    for(i <- 1 until n/4){
      io.addrReadB(i) := regDelayB(i-1)
    }
  }


  /*    read & write matrixC    */


  private val numRegDelayC = n/4 - 1 + sramLatency
  val regDelayC = Reg(Vec(numRegDelayC , UInt(Acc_INDEX_LEN.W)))
  regDelayC.foreach(_ := 0.U)

  regDelayC(0) := io.FSM_MMAU_io.firstAddrPublicC
  for(i <- 1 until numRegDelayC){
    regDelayC(i) := regDelayC(i-1)
  }

  io.addrReadC(0) := io.FSM_MMAU_io.firstAddrPublicC
  for(i <- 1 until n/4){
    io.addrReadC(i) := regDelayC(i-1)
  } 


  for(i <- 0 until n/4){    //比read慢sramLatency拍
    io.addrWriteC(i) := regDelayC(i-1+sramLatency)
  } 


  //write C enable

  val regDelayEnWriteCLatency =  Reg(Vec(sramLatency , Bool()))
  regDelayEnWriteCLatency(0) := io.FSM_MMAU_io.firstEnWriteC
  for(i <- 1 until sramLatency){
      regDelayEnWriteCLatency(i) := regDelayEnWriteCLatency(i-1)
  }

  io.sigEnWriteC(0) := regDelayEnWriteCLatency(sramLatency - 1)

  if(n/4 > 1){
    val regDelayEnWriteC = Reg(Vec(n/4 - 1 , Bool()))
    regDelayEnWriteC(0) := regDelayEnWriteCLatency(sramLatency - 1)
    for(i <- 1 until n/4 - 1){
      regDelayEnWriteC(i) := regDelayEnWriteC(i-1)
    }

    for(i <- 1 until n/4){
      io.sigEnWriteC(i) := regDelayEnWriteC(i-1)
    }
  }
  

  /*    signal done    */
  

}

























