package MMAU

import chisel3._
import chisel3.util._

import common._

// import fpu.core._

// class DPA(k: Int) extends Module {
//   val io = IO(new Bundle {
//     val vecA = Input(UInt((k * 8).W))   // 输入向量 A，宽度为 k*8 的 UInt 类型
//     val vecB = Input(UInt((k * 8).W))   // 输入向量 B，宽度为 k*8 的 UInt 类型
//     val eleC = Output(SInt(32.W))     // 点积结果，SInt(32.W)
//   })

//   // 将 vecA 和 vecB 拆分成 k 个 SInt(8.W) 类型的元素
//   val vecA_parts = (0 until k).map(i => (io.vecA(i * 8 + 7, i * 8)).asSInt)
//   val vecB_parts = (0 until k).map(i => (io.vecB(i * 8 + 7, i * 8)).asSInt)

//   // 计算每一对元素的乘积，结果是 SInt(16.W)
//   val products = vecA_parts.zip(vecB_parts).map { case (a, b) => a * b }

//   // 对所有的乘积求和，最终结果是 SInt(32.W)
//   io.eleC := products.reduce(_ + _)  // 对乘积进行累加，得到最终的 SInt(32.W)
// }



class DPA extends MMAUFormat {
  val io = IO(new Bundle {
    val vecA = Input(UInt((k * 8).W))   // 输入向量 A，宽度为 k*8 的 UInt 类型
    val vecB = Input(UInt((k * 8).W))   // 输入向量 B，宽度为 k*8 的 UInt 类型
    val muxCtrlSum = Input(Bool())         // 用于控制DPA内部累加寄存器更新逻辑（累加 or 归位）

    val eleC = Output(SInt(32.W))       // 点积结果，SInt(32.W)
  })

  // 拆解 vecA 和 vecB 为 k 个 SInt(8.W) 元素
  val vecA_parts = Wire(Vec(k, SInt(8.W)))
  val vecB_parts = Wire(Vec(k, SInt(8.W)))

  for (i <- 0 until k) {
    vecA_parts(i) := io.vecA(i * 8 + 7, i * 8).asSInt  // 将 vecA 的每 8 位拆解为 SInt(8.W)
    vecB_parts(i) := io.vecB(i * 8 + 7, i * 8).asSInt  // 将 vecB 的每 8 位拆解为 SInt(8.W)
  }

  // 计算每一对元素的乘积，结果是 SInt(32.W)    这里原本是打算16.W,但是会出问题
  val products = Wire(Vec(k, SInt(32.W)))
  for (i <- 0 until k) {
    products(i) := vecA_parts(i) * vecB_parts(i)  // 计算每对元素的乘积
  }

  // 对所有的乘积求和，最终结果是 SInt(32.W)
  val sum = Wire(SInt(32.W))
  sum := AdderTree.reduceAddTree(products)

  // 累加寄存器，通过muxCtrlSum归位，注意是归位不是清零，因为下一set计算是紧接着的
  val regAcc = RegInit(0.S(32.W))
  regAcc := Mux(io.muxCtrlSum , sum , regAcc + sum)


  // 输出点积结果
  io.eleC := regAcc

}





