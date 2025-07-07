package MMAU

import chisel3._
import chisel3.util._

import common._
import Expander._








/*    FSM与MMAU解耦    */
class MMAU extends MMAUFormat {
  val io = IO(new Bundle {
    val FSM_MMAU_io = new FSM_MMAU_IO

    val vecA = Input(Vec(m, UInt((k * 8).W)))  // vecA 为 m 长度的向量，每个元素为 UInt(k*8.W)
    val vecB = Input(Vec(n, UInt((k * 8).W)))  // vecB 为 n 长度的向量，每个元素为 UInt(k*8.W)
    val vecCin = Input(Vec(n/4 , UInt((32 * 4).W)))
    
    val vecCout = Output(Vec(n/4 , UInt((32 * 4).W)))
    val addrReadA = Output(Vec(m , UInt(Tr_INDEX_LEN.W)))
    val addrReadB = Output(Vec(n , UInt(Tr_INDEX_LEN.W)))
    val addrReadC = Output(Vec(n/4 , UInt(Acc_INDEX_LEN.W)))
    val addrWriteC = Output(Vec(n/4 , UInt(Acc_INDEX_LEN.W)))
    val sigEnWriteC = Output(Vec(n/4 , Bool()))    //C写使能

    val actPortReadA = Output(Bool())  //A读端口激活
    val actPortReadB = Output(Bool())  //B读端口激活
    val actPortReadC = Output(Bool())  //C读端口激活
    val actPortWriteC = Output(Bool())  //C写端口激活

    val Ops_io = Flipped(new Ops_IO)
  })

  val subCUBE = Module(new CUBE)
  val subADD = Module(new ADD)
  val subCTRL = Module(new CTRL)
  
  /*    about subCUBE     */

  for(i <- 0 until m){
    subCUBE.io.vecA(i) := io.vecA(i)
  }

  for(i <- 0 until n){
    subCUBE.io.vecB(i) := io.vecB(i)
  }

  for(i <- 0 until m){
    for(j <- 0 until n/4){
        subCUBE.io.muxCtrlC(i)(j) := subCTRL.io.muxCtrlC(i)(j)
        subCUBE.io.muxCtrlSum(i)(j) := subCTRL.io.muxCtrlSum(i)(j)
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

  /*    about subCTRL     */
  
  subCTRL.io.FSM_MMAU_io <> io.FSM_MMAU_io

  for(i <- 0 until m){
    io.addrReadA(i) := subCTRL.io.addrReadA(i)
  }

  for(i <- 0 until n/4){
    io.addrReadB(i * 4 + 0) := subCTRL.io.addrReadB(i)
    io.addrReadB(i * 4 + 1) := subCTRL.io.addrReadB(i)
    io.addrReadB(i * 4 + 2) := subCTRL.io.addrReadB(i)
    io.addrReadB(i * 4 + 3) := subCTRL.io.addrReadB(i)

    io.addrReadC(i) := subCTRL.io.addrReadC(i)
    io.addrWriteC(i) := subCTRL.io.addrWriteC(i)
    io.sigEnWriteC(i) := subCTRL.io.sigEnWriteC(i)
  }
  
  /*    about actPort   */
  io.actPortReadA := io.FSM_MMAU_io.actPortReadA
  io.actPortReadB := io.FSM_MMAU_io.actPortReadB
  io.actPortReadC := io.FSM_MMAU_io.actPortReadC
  io.actPortWriteC := io.FSM_MMAU_io.actPortWriteC

  /*    about Ops   */
  io.Ops_io <> io.FSM_MMAU_io.Ops_io
 
}

















