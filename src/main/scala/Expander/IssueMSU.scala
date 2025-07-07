package Expander

import chisel3._
import chisel3.util._

import common._
import MMAU._


class IssueMSU extends Module{
  val io = IO(new Bundle {
    val FSM_MSU_io = new FSM_MSU_IO //连接下层MSU
    val IssueMSU_Excute_io = new IssueMSU_Excute_IO //连接IssueArbiter
  })

  val subTileHandler = Module(new TilePadding_MSU)
  val subFSM = Module(new FSM_MSU)

  //存该条指令有关信息
  val reg_mtilem = RegInit(0.U(log2Ceil(Consts.tileM+1).W))
  val reg_mtilen = RegInit(0.U(log2Ceil(Consts.tileN+1).W))
  val reg_mtilek = RegInit(0.U(log2Ceil(Consts.tileK+1).W))
  val reg_rs1 = RegInit(0.U(Consts.rs1_LEN.W))
  val reg_rs2 = RegInit(0.U(Consts.rs2_LEN.W))
  val reg_md = RegInit(0.U(Consts.All_ADDR_LEN.W))

  val reg_is_msce32 = RegInit(false.B)

  when(io.IssueMSU_Excute_io.sigStart){//接到start信号，更新info
    reg_mtilem := io.IssueMSU_Excute_io.mtilem
    reg_mtilen := io.IssueMSU_Excute_io.mtilen
    reg_mtilek := io.IssueMSU_Excute_io.mtilek
    reg_rs1 := io.IssueMSU_Excute_io.rs1
    reg_rs2 := io.IssueMSU_Excute_io.rs2
    reg_md := io.IssueMSU_Excute_io.in_md

    reg_is_msce32 := io.IssueMSU_Excute_io.is_msce32
  }

// debug
printf(p"[IssueMSU] MSU_sigStart = ${io.IssueMSU_Excute_io.sigStart}, " +
       p"mtilem=${io.IssueMSU_Excute_io.mtilem}, mtilen=${io.IssueMSU_Excute_io.mtilen}, " +
       p"mtilek=${io.IssueMSU_Excute_io.mtilek}, rs1=${io.IssueMSU_Excute_io.rs1}, " +
       p"rs2=${io.IssueMSU_Excute_io.rs2}, in_md=${io.IssueMSU_Excute_io.in_md}\n")


// printf(p"[IssueMSU] MSU_sigStart = ${io.IssueMSU_Excute_io.sigStart}, " +
//        p"mtilem=${reg_mtilem}, mtilen=${reg_mtilen}, " +
//        p"mtilek=${reg_mtilek}\n")



  /*    between Top and TileHandler    */
  subTileHandler.io.is_msce32 := reg_is_msce32

  /*    between Top and regInfo    */
  //done above
  io.IssueMSU_Excute_io.out_md := reg_md

  /*    between Top and FSM    */
  subFSM.io.sigStart := io.IssueMSU_Excute_io.sigStart
  subFSM.io.FSM_MSU_io <> io.FSM_MSU_io
  subFSM.io.is_storeC := reg_is_msce32

  io.IssueMSU_Excute_io.sigDone := subFSM.io.sigDone //!!!
  // io.IssueMSU_Excute_io.sigDone := subFSM.io.sigReqDone //for debug，暂时认为sigReqDone等同于done（不考虑L2的访存延时）


  /*    between regInfo and FSM    */
  subFSM.io.rs1 := reg_rs1
  subFSM.io.rs2 := reg_rs2
  subFSM.io.md := reg_md

  /*    between regInfo and TileHandler    */
  subTileHandler.io.mtileConfig_io.mtilem := reg_mtilem
  subTileHandler.io.mtileConfig_io.mtilen := reg_mtilen
  subTileHandler.io.mtileConfig_io.mtilek := reg_mtilek

  /*    between FSM and TileHandler    */
  subFSM.io.TilePadding_MSU_io <> subTileHandler.io.TilePadding_MSU_io

}