package Expander

import chisel3._
import chisel3.experimental.BundleLiterals._
import chisel3.simulator.EphemeralSimulator._
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.must.Matchers



class FSM_MLU_Test extends AnyFreeSpec with Matchers {
  
  "FSM_MLU should PASS" in {
    simulate(new FSM_MLU) { dut =>
        dut.io.rs1.poke(0.U)
        dut.io.rs2.poke(2.U)
        dut.io.md.poke(1.U)
        dut.io.TilePadding_MLU_io.nRow.poke(7.U)
        dut.io.TilePadding_MLU_io.nCol.poke(3.U)

        dut.io.sigStart.poke(true.B)
        dut.clock.step(1)
        dut.io.sigStart.poke(false.B)

        while(!dut.io.sigDone.peek().litToBoolean){
            dut.clock.step(1)
        }
    }
  }


}