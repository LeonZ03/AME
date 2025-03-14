package MMAU

import chisel3._
import chisel3.util._

import common._


class PEcube extends MMAUFormat {
  val io = IO(new Bundle {
    val vecAin = Input(UInt((k * 8).W))
    val vecBin = Input(UInt((k * 8).W))
    val eleCin = Input(SInt(32.W))
    val muxCtrlC = Input(Bool())
    val muxCtrlSum = Input(Bool())         // 用于控制DPA内部累加寄存器更新逻辑（累加 or 归位）

    val vecAout = Output(UInt((k * 8).W))
    val vecBout = Output(UInt((k * 8).W))
    val eleCout = Output(SInt(32.W))
  })

  val subDPA = Module(new DPA)
  val regR = RegInit(0.U((k * 8).W))

  subDPA.io.vecA := io.vecAin
  subDPA.io.vecB := io.vecBin
  subDPA.io.muxCtrlSum := io.muxCtrlSum
  io.eleCout := Mux(io.muxCtrlC , subDPA.io.eleC , io.eleCin)

  io.vecAout := io.vecAin
  regR := io.vecBin
  io.vecBout := regR

}

// object Main extends App {
//   (new chisel3.stage.ChiselStage).emitVerilog(new PEcube)
// }
