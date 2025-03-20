import chisel3._
import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec

import MMAU._
import common._



class PEcubeTestExpect extends AnyFlatSpec with ChiselScalatestTester {
      
  "PEcube" should "pass" in {
    test(new PEcube) { dut =>

/*testdata start*/
val vecAdata = Seq(
    "ha858455fc00e7b43".U,
    "he6758fa68519b6cd".U,
    "h4328cf277a59dd4f".U,
    "h2b62afacfc633f8f".U,
    "h27f353915b87332f".U,
    "h5ef20b816b9b62b1".U,
    "h47b2c8f5d6883607".U,
    "h74eeb234821337cd".U,
    "h7ac68d9a40acbe6f".U,
    "h5ca06bfd903be4a2".U
)

val vecBdata = Seq(
    "h20cad92a28aaa028".U,
    "h4bda0b526226f352".U,
    "hf2415ec662e2ee9a".U,
    "he4fa1416faaf13c2".U,
    "h12223b20d7d61444".U,
    "hc395d28a39b3bfd2".U,
    "hc864190fd98bd73f".U,
    "hba1a575b0e3a4b6d".U,
    "hd55942119f2f72c7".U,
    "h2be3b56d25a15f80".U
)

val eleCdata = Seq(
    -19161,
        -19161 + -29343,
            -19161 + -29343 + -3348,
                -19161 + -29343 + -3348 + -5052,
                    -19161 + -29343 + -3348 + -5052 + 7172,
                        -19161 + -29343 + -3348 + -5052 + 7172 + 21384,
                            -19161 + -29343 + -3348 + -5052 + 7172 + 21384 + 564,
                                -19161 + -29343 + -3348 + -5052 + 7172 + 21384 + 564 + -12738,
                                    -19161 + -29343 + -3348 + -5052 + 7172 + 21384 + 564 + -12738 + -43739,
                                        -19161 + -29343 + -3348 + -5052 + 7172 + 21384 + 564 + -12738 + -43739 + -1989
)

/*testdata end*/


      val num = 10
      for(i <- 0 until num){

        // 将转换后的向量赋值给 DUT 输入端口
        dut.io.vecAin.poke(vecAdata(i))
        dut.io.vecBin.poke(vecBdata(i))
        // dut.io.vecAin.poke("hdc75a5b010f2f4df".U)
        // dut.io.vecBin.poke("h798b5163b76c53cb".U)

        dut.io.eleCin.poke(0.S(32.W))// 给 eleCin 赋一个测试值（可以是任意值，这里给定一个测试值）
        
        dut.io.muxCtrlC.poke(true.B)// 控制信号设置为 true，测试计算的点积结果
        dut.io.muxCtrlSum.poke(false.B)// 设置为 true，则不累加，否则累加

        dut.clock.step(1)// 等待一个时钟周期

        dut.io.eleCout.expect(eleCdata(i).S(32.W))// 手动计算点积预期值
        // dut.io.eleCout.expect(-3322.S(32.W))// 手动计算点积预期值

        dut.io.muxCtrlC.poke(false.B)// 切换 mux 控制信号为 false，测试是否正确输出 eleCin
        dut.io.eleCout.expect(0.S(32.W))  // 因为传入的是 0.S(32.W) 作为 eleCin
      }
    }
  }
}

