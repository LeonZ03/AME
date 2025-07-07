package Expander

import chisel3._
import chisel3.util._

import common._
import MMAU._



class ExcuteHandler extends Module {
  val io = IO(new Bundle {
    val Uop_io = new Uop_IO
    val ScoreboardVisit_io = new ScoreboardVisit_IO

    val IssueMMAU_Excute_io = Flipped(new IssueMMAU_Excute_IO)
    val IssueMLU_Excute_io  = Flipped(new IssueMLU_Excute_IO)
    val IssueMSU_Excute_io  = Flipped(new IssueMSU_Excute_IO)
  })

  // ----------- 别名简化 -----------
  val uop = io.Uop_io
  val sb = io.ScoreboardVisit_io

  val ms1 = uop.Operands_io.ms1
  val ms2 = uop.Operands_io.ms2
  val md  = uop.Operands_io.md
  val rs1 = uop.Operands_io.rs1
  val rs2 = uop.Operands_io.rs2

  val MLU_Bit  = 0
  val MSU_Bit  = 1
  val MMAU_Bit = 2
  val MISC_Bit = 3
  val EWU_Bit  = 4

  // ----------- 默认值 -----------
  sb.writeMaskAlloc_RF   := 0.U
  sb.writeMaskAlloc_Unit := 0.U
  sb.writeMaskFree_RF    := 0.U
  sb.writeMaskFree_Unit  := 0.U


  // MMAU赋默认值
  io.IssueMMAU_Excute_io.sigStart := false.B
  io.IssueMMAU_Excute_io.in_ms1   := 0.U
  io.IssueMMAU_Excute_io.in_ms2   := 0.U
  io.IssueMMAU_Excute_io.in_md    := 0.U
  io.IssueMMAU_Excute_io.mtilem   := 0.U
  io.IssueMMAU_Excute_io.mtilen   := 0.U
  io.IssueMMAU_Excute_io.mtilek   := 0.U

  // MLU赋默认值
  io.IssueMLU_Excute_io.sigStart := false.B
  io.IssueMLU_Excute_io.rs1      := 0.U
  io.IssueMLU_Excute_io.rs2      := 0.U
  io.IssueMLU_Excute_io.in_md    := 0.U
  io.IssueMLU_Excute_io.mtilem   := 0.U
  io.IssueMLU_Excute_io.mtilen   := 0.U
  io.IssueMLU_Excute_io.mtilek   := 0.U

  io.IssueMLU_Excute_io.is_mlbe8 := false.B
  io.IssueMLU_Excute_io.is_mlae8 := false.B
  io.IssueMLU_Excute_io.is_mlce32 := false.B
                                                          //新的指令加在空行中,由上往下

  val is_MLU_ins = Wire(Bool()) //指令为Load指令
  is_MLU_ins := uop.InsType_io.is_mlbe8 || uop.InsType_io.is_mlae8 || uop.InsType_io.is_mlce32

  // MSU赋默认值
  io.IssueMSU_Excute_io.sigStart := false.B
  io.IssueMSU_Excute_io.rs1      := 0.U
  io.IssueMSU_Excute_io.rs2      := 0.U
  io.IssueMSU_Excute_io.in_md    := 0.U
  io.IssueMSU_Excute_io.mtilem   := 0.U
  io.IssueMSU_Excute_io.mtilen   := 0.U
  io.IssueMSU_Excute_io.mtilek   := 0.U

  io.IssueMSU_Excute_io.is_msce32 := false.B
                                                          //新的指令加在空行中,由上往下

  val is_MSU_ins = Wire(Bool()) //指令为Load指令
  is_MSU_ins := uop.InsType_io.is_msce32

  // ----------- ready 信号仲裁 -----------
  val mma_ready = uop.InsType_io.is_mmacc && {
    val regFree = (sb.read_RF(ms1) | sb.read_RF(ms2) | sb.read_RF(md)) === 0.U
    val unitFree = sb.read_Unit(MMAU_Bit) === 0.U
    regFree && unitFree
  }

  val mlu_ready = is_MLU_ins && {
    val regFree = sb.read_RF(md) === 0.U
    val unitFree = sb.read_Unit(MLU_Bit) === 0.U
    regFree && unitFree
  }

  val msu_ready = is_MSU_ins && {
    val regFree = sb.read_RF(md) === 0.U
    val unitFree = sb.read_Unit(MSU_Bit) === 0.U
    regFree && unitFree
  }

  val is_ready = mma_ready || mlu_ready || msu_ready
  val is_shaked = is_ready && uop.ShakeHands_io.valid
  uop.ShakeHands_io.ready := is_ready

  // ----------- Done 信号仲裁 -----------  
  val mma_FreeRF = WireDefault(0.U(Consts.RF_LEN.W))
  val mma_FreeUnit = WireDefault(0.U(Consts.Unit_LEN.W))

  val mlu_FreeRF = WireDefault(0.U(Consts.RF_LEN.W))
  val mlu_FreeUnit = WireDefault(0.U(Consts.Unit_LEN.W))

  val msu_FreeRF = WireDefault(0.U(Consts.RF_LEN.W))
  val msu_FreeUnit = WireDefault(0.U(Consts.Unit_LEN.W))


  sb.writeMaskFree_RF   := mma_FreeRF | mlu_FreeRF | msu_FreeRF
  sb.writeMaskFree_Unit := mma_FreeUnit | mlu_FreeUnit | msu_FreeUnit



  // ----------- MMAU逻辑 -----------
  when (uop.InsType_io.is_mmacc) {
    io.IssueMMAU_Excute_io.sigStart := is_shaked
    io.IssueMMAU_Excute_io.in_ms1   := ms1
    io.IssueMMAU_Excute_io.in_ms2   := ms2
    io.IssueMMAU_Excute_io.in_md    := md
    io.IssueMMAU_Excute_io.mtilem   := uop.mtileConfig_io.mtilem
    io.IssueMMAU_Excute_io.mtilen   := uop.mtileConfig_io.mtilen
    io.IssueMMAU_Excute_io.mtilek   := uop.mtileConfig_io.mtilek

    when (is_shaked) { //分配资源
      sb.writeMaskAlloc_RF   := (1.U << ms1) | (1.U << ms2) | (1.U << md)
      sb.writeMaskAlloc_Unit := (1.U << MMAU_Bit)
    }

    
  }

  when (io.IssueMMAU_Excute_io.sigDone) { //释放资源
      // sb.writeMaskFree_RF   := (1.U << io.IssueMMAU_Excute_io.out_ms1) | (1.U << io.IssueMMAU_Excute_io.out_ms2) | (1.U << io.IssueMMAU_Excute_io.out_md)
      // sb.writeMaskFree_Unit := (1.U << MMAU_Bit)
      mma_FreeRF   := (1.U << io.IssueMMAU_Excute_io.out_ms1) | (1.U << io.IssueMMAU_Excute_io.out_ms2) | (1.U << io.IssueMMAU_Excute_io.out_md)
      mma_FreeUnit := (1.U << MMAU_Bit)
    }

  // ----------- MLU逻辑 -----------
  

  when (is_MLU_ins) {
    io.IssueMLU_Excute_io.sigStart := is_shaked
    io.IssueMLU_Excute_io.rs1      := rs1
    io.IssueMLU_Excute_io.rs2      := rs2
    io.IssueMLU_Excute_io.in_md    := md
    io.IssueMLU_Excute_io.mtilem   := uop.mtileConfig_io.mtilem
    io.IssueMLU_Excute_io.mtilen   := uop.mtileConfig_io.mtilen
    io.IssueMLU_Excute_io.mtilek   := uop.mtileConfig_io.mtilek

    io.IssueMLU_Excute_io.is_mlbe8 := uop.InsType_io.is_mlbe8
    io.IssueMLU_Excute_io.is_mlae8 := uop.InsType_io.is_mlae8
    io.IssueMLU_Excute_io.is_mlce32 := uop.InsType_io.is_mlce32


    when (is_shaked) { //分配资源
      sb.writeMaskAlloc_RF   := (1.U << md)
      sb.writeMaskAlloc_Unit := (1.U << MLU_Bit)
    }

    
  }

  when (io.IssueMLU_Excute_io.sigDone) { //释放资源
      // sb.writeMaskFree_RF   := (1.U << io.IssueMLU_Excute_io.out_md)
      // sb.writeMaskFree_Unit := (1.U << MLU_Bit)
      mlu_FreeRF   := (1.U << io.IssueMLU_Excute_io.out_md)
      mlu_FreeUnit := (1.U << MLU_Bit)
    }

  // ----------- MSU逻辑 -----------
  

  when (is_MSU_ins) {
    io.IssueMSU_Excute_io.sigStart := is_shaked
    io.IssueMSU_Excute_io.rs1      := rs1
    io.IssueMSU_Excute_io.rs2      := rs2
    io.IssueMSU_Excute_io.in_md    := md
    io.IssueMSU_Excute_io.mtilem   := uop.mtileConfig_io.mtilem
    io.IssueMSU_Excute_io.mtilen   := uop.mtileConfig_io.mtilen
    io.IssueMSU_Excute_io.mtilek   := uop.mtileConfig_io.mtilek

    io.IssueMSU_Excute_io.is_msce32 := uop.InsType_io.is_msce32


    when (is_shaked) {
      sb.writeMaskAlloc_RF   := (1.U << md)
      sb.writeMaskAlloc_Unit := (1.U << MSU_Bit)
    }

    
  }

  when (io.IssueMSU_Excute_io.sigDone) { //释放资源
      // sb.writeMaskFree_RF   := (1.U << io.IssueMSU_Excute_io.out_md)
      // sb.writeMaskFree_Unit := (1.U << MSU_Bit)
      msu_FreeRF   := (1.U << io.IssueMSU_Excute_io.out_md)
      msu_FreeUnit := (1.U << MSU_Bit)
    }
}


