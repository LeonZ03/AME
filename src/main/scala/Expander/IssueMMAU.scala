package Expander

import chisel3._
import chisel3.util._

import common._
import MMAU._




class IssueMMAU extends Module{
  val io = IO(new Bundle {
    val IssueMMAU_Excute_io = new IssueMMAU_Excute_IO   //连接IssueArbiter
    val FSM_MMAU_io = Flipped(new FSM_MMAU_IO)    //传给MMAU

  })    

  val subTileHandler = Module(new TilePadding_MMAU)
  val subFSM = Module(new FSM_MMAU)
  //存该条指令有关信息
  val reg_mtilem = RegInit(0.U(log2Ceil(Consts.tileM+1).W))
  val reg_mtilen = RegInit(0.U(log2Ceil(Consts.tileN+1).W))
  val reg_mtilek = RegInit(0.U(log2Ceil(Consts.tileK+1).W))
  val reg_ms1 = RegInit(0.U(Consts.All_ADDR_LEN.W))
  val reg_ms2 = RegInit(0.U(Consts.All_ADDR_LEN.W))
  val reg_md = RegInit(0.U(Consts.All_ADDR_LEN.W))

  when(io.IssueMMAU_Excute_io.sigStart){
    reg_mtilem := io.IssueMMAU_Excute_io.mtilem
    reg_mtilen := io.IssueMMAU_Excute_io.mtilen
    reg_mtilek := io.IssueMMAU_Excute_io.mtilek
    reg_ms1 := io.IssueMMAU_Excute_io.in_ms1
    reg_ms2 := io.IssueMMAU_Excute_io.in_ms2
    reg_md := io.IssueMMAU_Excute_io.in_md
  }.otherwise{
    reg_mtilem := reg_mtilem
    reg_mtilen := reg_mtilen
    reg_mtilek := reg_mtilek
    reg_ms1 := reg_ms1
    reg_ms2 := reg_ms2
    reg_md := reg_md
  }

  /*    between Top and reg_xxx     */
  io.IssueMMAU_Excute_io.out_ms1 := reg_ms1
  io.IssueMMAU_Excute_io.out_ms2 := reg_ms2
  io.IssueMMAU_Excute_io.out_md := reg_md

  /*    between Top and FSM     */
  io.IssueMMAU_Excute_io.sigDone := subFSM.io.sigDone
  io.FSM_MMAU_io <> subFSM.io.FSM_MMAU_io
  subFSM.io.sigStart := io.IssueMMAU_Excute_io.sigStart

  /*    between Top and TileHandler     */
  //nothing

  /*    between reg_xxx and TileHandler     */
  subTileHandler.io.mtileConfig_io.mtilem := reg_mtilem
  subTileHandler.io.mtileConfig_io.mtilen := reg_mtilen
  subTileHandler.io.mtileConfig_io.mtilek := reg_mtilek

  /*    between reg_xxx and FSM     */
  subFSM.io.Ops_io.ms1 := reg_ms1(1,0)  //从0～7的寄存器寻址到Tr和Acc内部寻址（0～3）
  subFSM.io.Ops_io.ms2 := reg_ms2(1,0)
  subFSM.io.Ops_io.md := reg_md(1,0)

  /*    between TileHandler and FSM     */
  subFSM.io.TilePadding_MMAU_io <> subTileHandler.io.TilePadding_MMAU_io


}