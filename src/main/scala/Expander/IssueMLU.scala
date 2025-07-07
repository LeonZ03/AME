package Expander

import chisel3._
import chisel3.util._

import common._
import MMAU._


class IssueMLU extends Module{
  val io = IO(new Bundle {
    val FSM_MLU_io = new FSM_MLU_IO //连接下层MLU
    val IssueMLU_Excute_io = new IssueMLU_Excute_IO //连接ExcuteHandler
  })

  val subTileHandler = Module(new TileHandler_MLU)
  val subFSM = Module(new FSM_MLU)

  //存该条指令有关信息
  val reg_mtilem = RegInit(0.U(log2Ceil(Consts.tileM+1).W))
  val reg_mtilen = RegInit(0.U(log2Ceil(Consts.tileN+1).W))
  val reg_mtilek = RegInit(0.U(log2Ceil(Consts.tileK+1).W))
  val reg_rs1 = RegInit(0.U(Consts.rs1_LEN.W))
  val reg_rs2 = RegInit(0.U(Consts.rs2_LEN.W))
  val reg_md = RegInit(0.U(Consts.All_ADDR_LEN.W))

  val reg_is_mlbe8 = RegInit(false.B)
  val reg_is_mlae8 = RegInit(false.B)
  val reg_is_mlce32 = RegInit(false.B)

  when(io.IssueMLU_Excute_io.sigStart){//接到start信号，更新info
    reg_mtilem := io.IssueMLU_Excute_io.mtilem
    reg_mtilen := io.IssueMLU_Excute_io.mtilen
    reg_mtilek := io.IssueMLU_Excute_io.mtilek
    reg_rs1 := io.IssueMLU_Excute_io.rs1
    reg_rs2 := io.IssueMLU_Excute_io.rs2
    reg_md := io.IssueMLU_Excute_io.in_md

    reg_is_mlbe8 := io.IssueMLU_Excute_io.is_mlbe8
    reg_is_mlae8 := io.IssueMLU_Excute_io.is_mlae8
    reg_is_mlce32 := io.IssueMLU_Excute_io.is_mlce32
  }

// debug
printf(p"[debug_IsseuMLU] MLU_sigStart = ${io.IssueMLU_Excute_io.sigStart}, " +
       p"mtilem=${io.IssueMLU_Excute_io.mtilem}, mtilen=${io.IssueMLU_Excute_io.mtilen}, " +
       p"mtilek=${io.IssueMLU_Excute_io.mtilek}, rs1=${io.IssueMLU_Excute_io.rs1}, " +
       p"rs2=${io.IssueMLU_Excute_io.rs2}, in_md=${io.IssueMLU_Excute_io.in_md}\n")


printf(p"[debug_IsseuMLU] MLU_sigStart = ${io.IssueMLU_Excute_io.sigStart}, " +
       p"mtilem=${reg_mtilem}, mtilen=${reg_mtilen}, " +
       p"mtilek=${reg_mtilek}\n")



  /*    between Top and TileHandler    */
  subTileHandler.io.is_mlbe8 := reg_is_mlbe8
  subTileHandler.io.is_mlae8 := reg_is_mlae8
  subTileHandler.io.is_mlce32 := reg_is_mlce32

  /*    between Top and regInfo    */
  //done above
  io.IssueMLU_Excute_io.out_md := reg_md

  /*    between Top and FSM    */
  subFSM.io.sigStart := io.IssueMLU_Excute_io.sigStart
  subFSM.io.FSM_MLU_io <> io.FSM_MLU_io
  subFSM.io.is_loadAB := reg_is_mlbe8 || reg_is_mlae8
  subFSM.io.is_loadC := reg_is_mlce32

  io.IssueMLU_Excute_io.sigDone := subFSM.io.sigDone //!!!
  // io.IssueMLU_Excute_io.sigDone := subFSM.io.sigReqDone //for debug，暂时认为sigReqDone等同于done（不考虑L2的访存延时）


  /*    between regInfo and FSM    */
  subFSM.io.rs1 := reg_rs1
  subFSM.io.rs2 := reg_rs2
  subFSM.io.md := reg_md

  /*    between regInfo and TileHandler    */
  subTileHandler.io.mtileConfig_io.mtilem := reg_mtilem
  subTileHandler.io.mtileConfig_io.mtilen := reg_mtilen
  subTileHandler.io.mtileConfig_io.mtilek := reg_mtilek

  /*    between FSM and TileHandler    */
  subFSM.io.TileHandler_MLU_io <> subTileHandler.io.TileHandler_MLU_io

}