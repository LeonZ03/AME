package MSU

import chisel3._
import chisel3.util._


import chisel3._
import chisel3.util._


import chisel3._
import chisel3.util._

class PackedInputBuffer(val numWay: Int, val width: Int, val depth: Int) extends Module {
  val io = IO(new Bundle {
    val in = Input(Vec(numWay, UInt(width.W)))
    val in_valid = Input(Bool())
    val in_ready = Output(Bool())
    val out = Decoupled(UInt((numWay * width).W))
  })

  val queue = Module(new Queue(UInt((numWay * width).W), depth))

  //拼接输入（大端）：Cat(io.in.reverse) 代表 in(0) 高位，in(numWay-1) 低位
  val packedData = Cat(io.in)  // 修复点：去掉了非法的 : _*

  // 入队逻辑
  queue.io.enq.valid := io.in_valid
  queue.io.enq.bits  := packedData
  io.in_ready := queue.io.enq.ready

  // 出队逻辑
  io.out <> queue.io.deq
}


