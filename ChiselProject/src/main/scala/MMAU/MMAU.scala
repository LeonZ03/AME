package MMAU

import chisel3._
import chisel3.util._

import common._

class MMAU extends MMAUFormat {
  val io = IO(new Bundle {
    val sigStart = Input(Bool())    //启动信号
    val vecA = Input(Vec(m, UInt((k * 8).W)))  // vecA 为 m 长度的向量，每个元素为 UInt(k*8.W)
    val vecB = Input(Vec(n, UInt((k * 8).W)))  // vecB 为 n 长度的向量，每个元素为 UInt(k*8.W)
    val vecCin = Input(Vec(n/4 , UInt((32 * 4).W)))
    
    val vecCout = Output(Vec(n/4 , UInt((32 * 4).W)))
    val addrReadA = Output(Vec(m , UInt(ADDR_LEN.W)))
    val addrReadB = Output(Vec(n , UInt(ADDR_LEN.W)))
    val addrReadC = Output(Vec(n/4 , UInt(ADDR_LEN.W)))
    val addrWriteC = Output(Vec(n/4 , UInt(ADDR_LEN.W)))
    val sigEnWriteC = Output(Vec(n/4 , Bool()))    //C写使能
    val sigDone = Output(Bool())    //结束信号
  })

  val subCUBE = Module(new CUBE)
  val subADD = Module(new ADD)
  val subFSM = Module(new FSM)
  
  /*    about subCUBE     */

  for(i <- 0 until m){
    subCUBE.io.vecA(i) := io.vecA(i)
  }

  for(i <- 0 until n){
    subCUBE.io.vecB(i) := io.vecB(i)
  }

  for(i <- 0 until m){
    for(j <- 0 until n/4){
        subCUBE.io.muxCtrlC(i)(j) := subFSM.io.muxCtrlC(i)(j)
        subCUBE.io.muxCtrlSum(i)(j) := subFSM.io.muxCtrlSum(i)(j)
    }
  }

  for(i <- 0 until n){
    subADD.io.eleCin(i) := subCUBE.io.eleC(i)
  }

  /*    about subADD     */
  
  for(i <- 0 until n/4){
    subADD.io.vecCin(i) := io.vecCin(i)
    io.vecCout(i) := subADD.io.vecCout(i) 
  }

  /*    about subFSM     */
  
  subFSM.io.sigStart := io.sigStart

  for(i <- 0 until m){
    io.addrReadA(i) := subFSM.io.addrReadA(i)
  }

  for(i <- 0 until n/4){
    io.addrReadB(i * 4 + 0) := subFSM.io.addrReadB(i)
    io.addrReadB(i * 4 + 1) := subFSM.io.addrReadB(i)
    io.addrReadB(i * 4 + 2) := subFSM.io.addrReadB(i)
    io.addrReadB(i * 4 + 3) := subFSM.io.addrReadB(i)

    io.addrReadC(i) := subFSM.io.addrReadC(i)
    io.addrWriteC(i) := subFSM.io.addrWriteC(i)
    io.sigEnWriteC(i) := subFSM.io.sigEnWriteC(i)
    io.sigDone := subFSM.io.sigDone
  }
  

}


object Main extends App {
  (new chisel3.stage.ChiselStage).emitVerilog(new MMAU)
}

