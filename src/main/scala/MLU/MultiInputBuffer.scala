package MLU

import chisel3._
import chisel3.util._



//每路输入有独立的invalid
class MultiInputBuffer(val numWay: Int, val width: Int, val depth: Int) extends Module {
  require(depth > 0, "Depth must be positive")
  require(numWay > 0, "numWay must be positive")

  val io = IO(new Bundle {
    val in = Input(Vec(numWay, UInt(width.W)))
    val in_valid = Input(Vec(numWay, Bool()))
    val out = Decoupled(UInt(width.W))
  })

  val buffer = Reg(Vec(depth, Vec(numWay, UInt(width.W))))
  val valids = Reg(Vec(depth, Vec(numWay, Bool())))

  val wptr = RegInit(0.U(log2Ceil(depth).W))
  val rptr = RegInit(0.U(log2Ceil(depth).W))
  val outCnt = RegInit(0.U(log2Ceil(numWay).W))

  // 是否写爆（buffer满）：写指针+1 == 读指针，且该行有效（未完全清空）
  val nextWptr = Mux(wptr === (depth - 1).U, 0.U, wptr + 1.U)
  val bufferFull = (nextWptr === rptr) && valids(rptr).reduce(_ || _)

  val doWrite = io.in_valid.reduce(_ || _) && !bufferFull

  when(doWrite) {
    buffer(wptr) := io.in
    valids(wptr) := io.in_valid
    wptr := nextWptr
  }

  val currentLine = buffer(rptr)
  val currentValid = valids(rptr)

  val activeValid = VecInit((0 until numWay).map { i =>
    currentValid(i) && (i.U >= outCnt)
  })

  val nextValidIdx = PriorityEncoder(activeValid)
  val found = activeValid.asUInt.orR

  io.out.valid := found
  io.out.bits := currentLine(nextValidIdx)

  when(io.out.fire) {
    valids(rptr)(nextValidIdx) := false.B

    val remaining = valids(rptr).zipWithIndex.map {
      case (v, i) => v && (i.U > nextValidIdx)
    }.reduce(_ || _)

    when(remaining) {
      outCnt := nextValidIdx + 1.U
    }.otherwise {
      outCnt := 0.U
      rptr := Mux(rptr === (depth - 1).U, 0.U, rptr + 1.U)
    }
  }
}

