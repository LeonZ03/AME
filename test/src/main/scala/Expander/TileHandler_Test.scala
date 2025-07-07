package Expander

import chisel3._
import chisel3.experimental.BundleLiterals._
import chisel3.simulator.EphemeralSimulator._
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.must.Matchers


import common._




class TilePadding_MLU_Test extends AnyFreeSpec with Matchers {

  "TilePadding_MLU should PASS" in {
    simulate(new TilePadding_MLU) { dut =>
    
      // 输入用户配置尺寸
      dut.io.is_mlbe8.poke(true.B)
      dut.io.is_mlae8.poke(false.B)

      dut.io.mtileConfig_io.mtilem.poke(1.U)
      dut.io.mtileConfig_io.mtilen.poke(0.U)
      dut.io.mtileConfig_io.mtilek.poke(1.U)

      // 打印输出，不前进时钟(纯组合逻辑)
      dut.clock.step(1)

      println(s"nRow  = ${dut.io.TilePadding_MLU_io.nRow.peek().litValue}")
      println(s"nCol  = ${dut.io.TilePadding_MLU_io.nCol.peek().litValue}")
      
    }
  }
}




class TilePadding_MMAU_Test extends AnyFreeSpec with Matchers {

  "TilePadding_MMAU should PASS" in {
    simulate(new TilePadding_MMAU) { dut =>
    
      // 输入用户配置尺寸
      dut.io.mtileConfig_io.mtilem.poke(32.U)
      dut.io.mtileConfig_io.mtilen.poke(32.U)
      dut.io.mtileConfig_io.mtilek.poke(64.U)

      // 打印输出，不前进时钟
      // println(s"tilem = ${dut.io.TilePadding_MMAU_io.tilem.peek().litValue}")
      // println(s"tilen = ${dut.io.TilePadding_MMAU_io.tilen.peek().litValue}")
      // println(s"tilek = ${dut.io.TilePadding_MMAU_io.tilek.peek().litValue}")
      println(s"numm  = ${dut.io.TilePadding_MMAU_io.numm.peek().litValue}")
      println(s"numn  = ${dut.io.TilePadding_MMAU_io.numn.peek().litValue}")
      println(s"numk  = ${dut.io.TilePadding_MMAU_io.numk.peek().litValue}")
      
    }
  }
}
