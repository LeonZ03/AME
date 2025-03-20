import chisel3._
import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec

import MMAU._
import common._



// object TestData {

//   val eleCindata = Seq(
//       1394819561.S,
//       -1875873300.S,
//       320327154.S,
//       664469964.S,
//   )

//   val vecCindata = Seq(
//       "h262e315b658df8e7a30e2191114e3c8b".U,
//   )

//   val vecCoutdata = Seq(
//       "h79517344f5be6ad3b625ef8338e93e57".U,
//   )

// }


// class ADDTestExpect extends AnyFlatSpec with ChiselScalatestTester {
    
    
//   "ADD" should "pass" in  {
//     test(new ADD) { dut =>

// /* Test Data Start */

// /* Test Data End */

//       // 设置 eleCin 输入
//       for (i <- TestData.eleCindata.indices) {
//         dut.io.eleCin(i).poke(TestData.eleCindata(i))
//       }

//       // 设置 vecCin 输入
//       for (i <- TestData.vecCindata.indices) {
//         dut.io.vecCin(i).poke(TestData.vecCindata(i))
//       }

//       // 等待一个时钟周期
//       dut.clock.step(1)

//       // 验证 vecCout 输出
//       for (i <- TestData.vecCoutdata.indices) {
//         dut.io.vecCout(i).expect(TestData.vecCoutdata(i))
//       }
//     }
//   }
// }
