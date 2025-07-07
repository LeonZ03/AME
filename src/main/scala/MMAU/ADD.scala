package MMAU

import chisel3._
import chisel3.util._

import common._



class ADD extends MMAUFormat {
    val io = IO(new Bundle {
        val eleCin = Input(Vec(n, SInt(32.W)))   //来自CUBE
        val vecCin = Input(Vec(n/4 , UInt((32 * 4).W)))
        val vecCout = Output(Vec(n/4 , UInt((32 * 4).W)))
    })

    // 内部逻辑
    for (i <- 0 until n/4) {
        // 将 vecCin(i) 拆分为 4 个 SInt(32.W)
        val eleC0 = io.vecCin(i)(127, 96).asSInt  // 第1个元素
        val eleC1 = io.vecCin(i)(95, 64).asSInt // 第2个元素
        val eleC2 = io.vecCin(i)(63, 32).asSInt // 第3个元素
        val eleC3 = io.vecCin(i)(31, 0).asSInt // 第4个元素

        // 将拆分的元素与 eleCin 对应位置的元素相加
        val sum0 = io.eleCin(i * 4) + eleC0         //eleCin(0)对应vecC高位段
        val sum1 = io.eleCin(i * 4 + 1) + eleC1
        val sum2 = io.eleCin(i * 4 + 2) + eleC2
        val sum3 = io.eleCin(i * 4 + 3) + eleC3

        // 将相加的结果拼接成一个 vecCout
        io.vecCout(i) := Cat(sum0, sum1, sum2, sum3).asUInt
    }
}




