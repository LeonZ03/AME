import chisel3._
import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec

import MMAU._
import common._







class SimpleTestExpect extends AnyFlatSpec with ChiselScalatestTester {
  "FSM" should "PASS" in {
    test(new FSM) { dut =>

      val numM = Consts.tileM / Consts.m
      val numN = Consts.tileN / Consts.n
      val numK = Consts.tileK / Consts.k

      for (i <- 0 until 5) { // 预跑几个cycle
        dut.clock.step(1)
      }

      dut.io.sigStart.poke(true.B)
      dut.clock.step(1)
      dut.io.sigStart.poke(false.B)

      for (i <- 0 until numM) {
        println("**********************************")
        println(s"*         mState = $i          *")
        println("**********************************")



        for (j <- 0 until numN) {
          println("**********************************")
          println(s"*         nState = $j          *")
          println("**********************************")

          for (p <- 0 until numK) {
            println("\n\n\n") // 3行空行分隔 kState
            println(s"======== kState = $p ========")

            // 打印 muxCtrlC
            println("muxCtrlC:")
            dut.io.muxCtrlC.foreach { row =>
              println(row.map(_.peek().litValue.toInt).mkString(" "))
            }

            // // 打印 muxCtrlSum
            // println("muxCtrlSum:")
            // dut.io.muxCtrlSum.foreach { row =>
            //   println(row.map(_.peek().litValue.toInt).mkString(" "))
            // }

            // 打印 addrReadA（十进制输出）
            val addrReadAStr = dut.io.addrReadA.map(_.peek().litValue.toInt).mkString(" ")
            println(s"addrReadA: $addrReadAStr")

            // 打印 addrReadB（十进制输出）
            val addrReadBStr = dut.io.addrReadB.map(_.peek().litValue.toInt).mkString(" ")
            println(s"addrReadB: $addrReadBStr")

            dut.clock.step(1)
          }
        }
      }

      dut.io.sigDone.expect(true.B)
      dut.clock.step(1)
      dut.io.sigDone.expect(false.B)
    }
  }
}



// class SimpleTestExpect extends AnyFlatSpec with ChiselScalatestTester {
    
    
//   "ADD" should "pass" in  {
//     test(new ADD) { dut =>

// /* Test Data Start */
// val eleCindata = Seq(
//     -1160117276.S,
//     -37660813.S,
//     -1091407625.S,
//     -1317925324.S,
//     2065770958.S,
//     -2129896737.S,
//     -1478719514.S,
//     1158292575.S,
//     1952666724.S,
//     1612335886.S,
//     -1003048727.S,
//     -75315731.S,
//     -1284896600.S,
//     -892336386.S,
//     -1664449315.S,
//     252779086.S,
//     -1128399999.S,
//     -449916578.S,
//     861485620.S,
//     -860595303.S,
//     -804635100.S,
//     -638504053.S,
//     51496461.S,
//     -340083908.S,
//     -1741728152.S,
//     -260008582.S,
//     -20628531.S,
//     -1846061469.S,
//     60364102.S,
//     988905400.S,
//     1943001660.S,
//     1166770147.S,
// )

// val vecCindata = Seq(
//     "h8995e16b16375a105d682804769545e8".U,
//     "h2dbff7bbbf007f5f36ac27b7696772cd".U,
//     "he780d511b75c69d221040302b3ef6346".U,
//     "h7b339f5f8f4160ad66c3147928c38633".U,
//     "h806a8de42114e22f36f9ecad0c324464".U,
//     "h9864f2ca76abf12f85fb6e15bb520c96".U,
//     "h8fbcd862fd7c300e8aba2e14a82f25d2".U,
//     "hb0ba54065891f505e68543314eaf561f".U,
// )

// val vecCoutdata = Seq(
//     "h446fe54f13f8b1831c5a98fb2807541c".U,
//     "ha8e12189400cda3ede88af9dae71972c".U,
//     "h5be429751776b4e0e53ab3ebaf722933".U,
//     "h2e9da8075a1167ab038d995637d4a081".U,
//     "h3d2889650643b38d6a5326e1d8e69ffd".U,
//     "h686f30ee509d24ba890d3422a70cc7d2".U,
//     "h27ec2ecaedfcc588897f69e13a267c35".U,
//     "hb453694c938374bd5a551d6d943ad602".U,
// )


// /* Test Data End */

//       // 设置 eleCin 输入
//       for (i <- eleCindata.indices) {
//         dut.io.eleCin(i).poke(eleCindata(i))
//       }

//       // 设置 vecCin 输入
//       for (i <- vecCindata.indices) {
//         dut.io.vecCin(i).poke(vecCindata(i))
//       }

//       // 等待一个时钟周期
//       dut.clock.step(1)

//       // 验证 vecCout 输出
//       for (i <- vecCoutdata.indices) {
//         dut.io.vecCout(i).expect(vecCoutdata(i))
//       }
//     }
//   }
// }

// class SimpleTestExpect extends AnyFlatSpec with ChiselScalatestTester {

      
//   "CUBE" should "pass" in {
//     test(new CUBE) { dut =>

// /*testdata start*/

// val vecAdata = Seq(
//     "h57285ab3fdece5eb".U,
//     "hf88da5a1cfcf7cef".U,
//     "h836d6524a51dc50f".U,
//     "hd457aeff38098d35".U
// )

// val vecBdata = Seq(
//     "h4b88ddcc10809d97".U,
//     "h390648281b167313".U,
//     "h3664e603443179b5".U,
//     "h4437f9fce76550ae".U,
//     "h24da2fa0613e7941".U,
//     "ha6e93b63275d774e".U,
//     "hf61dd033d90a4bd0".U,
//     "ha2da1d187da77ab6".U
// )

// val eleCdata = Seq(
//     Seq(9969, 4574, 3251, 5411, 7071, -17891, -9057, -9271),
//     Seq(16322, 38, 695, 1738, 15033, -4447, 7805, 14825),
//     Seq(-28764, -6078, -11399, -4102, -20544, 11563, 93, -10863),
//     Seq(-5254, -18438, -5188, -11666, -13128, -9508, -6415, -13325)
// )



// /*testdata end*/

//       val m = Consts.m
//       val n = Consts.n

//       // for(i <- 0 until m){//所有muxCtrl set 0
//       //     dut.io.muxCtrl(i).poke(false.B)
//       //   }

//       /* 0 cycle*/
//       dut.io.vecA(0).poke(vecAdata(0))//装填0 row ,以A
//       for(j <- 0 until 4){
//         dut.io.vecB(j).poke(vecBdata(j))
//       }

//       dut.clock.step(1)// 等待一个时钟周期

//       /* 1 ～ m-1 cycle*/
//       for(tick <- 1 until m+1){

//         /************   tile()(0) ************/
//         for(i <- 0 until m){//所有muxCtrl set 0
//           dut.io.muxCtrlC(i)(0).poke(false.B)
//         }
//         dut.io.muxCtrlC(tick-1)(0).poke(true.B)//对应行set 1
        
//         for(j <- 0 until 4){//检查tick-1行下来的值
//           dut.io.eleC(j).expect(eleCdata(tick-1)(j).S(32.W))
//         }



//         /************   tile()(1) ************/
//         if(tick >= 2){
//           for(i <- 0 until m){//所有muxCtrl set 0
//             dut.io.muxCtrlC(i)(1).poke(false.B)
//           }

//           dut.io.muxCtrlC(tick-2)(1).poke(true.B)//对应行set 1
        
//           for(j <- 4 until 8){//检查tick-1行下来的值
//             dut.io.eleC(j).expect(eleCdata(tick-2)(j).S(32.W))
//           }
//         }


//         if(tick < m){
//           dut.io.vecA(tick).poke(vecAdata(tick))//装填tick row ,以A
//         }

//         if(tick == 1){
//           for(j <- 4 until 8){
//             dut.io.vecB(j).poke(vecBdata(j))
//           }
//         }

//         dut.clock.step(1)// 等待一个时钟周期
//       }

//       /* m+1 cycle*/
//       for(i <- 0 until m){//所有muxCtrl set 0
//           dut.io.muxCtrlC(i)(1).poke(false.B)
//         }
//       dut.io.muxCtrlC(m-1)(1).poke(true.B)//对应行set 1

//       for(j <- 4 until 8){//检查m行下来的值
//           dut.io.eleC(j).expect(eleCdata(m-1)(j).S(32.W))
//         }

//     }
//   }
// }





// class SimpleTestExpect extends AnyFlatSpec with ChiselScalatestTester {
      
//   "PEcube" should "pass" in {
//     test(new PEcube) { dut =>

// /*testdata start*/
// val vecAdata = Seq(
//     "ha858455fc00e7b43".U,
//     "he6758fa68519b6cd".U,
//     "h4328cf277a59dd4f".U,
//     "h2b62afacfc633f8f".U,
//     "h27f353915b87332f".U,
//     "h5ef20b816b9b62b1".U,
//     "h47b2c8f5d6883607".U,
//     "h74eeb234821337cd".U,
//     "h7ac68d9a40acbe6f".U,
//     "h5ca06bfd903be4a2".U
// )

// val vecBdata = Seq(
//     "h20cad92a28aaa028".U,
//     "h4bda0b526226f352".U,
//     "hf2415ec662e2ee9a".U,
//     "he4fa1416faaf13c2".U,
//     "h12223b20d7d61444".U,
//     "hc395d28a39b3bfd2".U,
//     "hc864190fd98bd73f".U,
//     "hba1a575b0e3a4b6d".U,
//     "hd55942119f2f72c7".U,
//     "h2be3b56d25a15f80".U
// )

// val eleCdata = Seq(
//     -19161,
//         -19161 + -29343,
//             -19161 + -29343 + -3348,
//                 -19161 + -29343 + -3348 + -5052,
//                     -19161 + -29343 + -3348 + -5052 + 7172,
//                         -19161 + -29343 + -3348 + -5052 + 7172 + 21384,
//                             -19161 + -29343 + -3348 + -5052 + 7172 + 21384 + 564,
//                                 -19161 + -29343 + -3348 + -5052 + 7172 + 21384 + 564 + -12738,
//                                     -19161 + -29343 + -3348 + -5052 + 7172 + 21384 + 564 + -12738 + -43739,
//                                         -19161 + -29343 + -3348 + -5052 + 7172 + 21384 + 564 + -12738 + -43739 + -1989
// )

// /*testdata end*/


//       val num = 10
//       for(i <- 0 until num){

//         // 将转换后的向量赋值给 DUT 输入端口
//         dut.io.vecAin.poke(vecAdata(i))
//         dut.io.vecBin.poke(vecBdata(i))
//         // dut.io.vecAin.poke("hdc75a5b010f2f4df".U)
//         // dut.io.vecBin.poke("h798b5163b76c53cb".U)

//         dut.io.eleCin.poke(0.S(32.W))// 给 eleCin 赋一个测试值（可以是任意值，这里给定一个测试值）
        
//         dut.io.muxCtrlC.poke(true.B)// 控制信号设置为 true，测试计算的点积结果
//         dut.io.muxCtrlSum.poke(false.B)// 设置为 true，则不累加，否则累加

//         dut.clock.step(1)// 等待一个时钟周期

//         dut.io.eleCout.expect(eleCdata(i).S(32.W))// 手动计算点积预期值
//         // dut.io.eleCout.expect(-3322.S(32.W))// 手动计算点积预期值

//         dut.io.muxCtrlC.poke(false.B)// 切换 mux 控制信号为 false，测试是否正确输出 eleCin
//         dut.io.eleCout.expect(0.S(32.W))  // 因为传入的是 0.S(32.W) 作为 eleCin
//       }
//     }
//   }
// }




// class SimpleTestExpect extends AnyFlatSpec with ChiselScalatestTester {

      
//   "CUBE" should "pass" in {//旧版CUBE
//     test(new CUBE) { dut =>

// /*testdata start*/
// val vecAdata = Seq(
//     "hcbf7e66c545c1ddd".U,
//     "hc5a7be973860161a".U,
//     "h3f92cd35a021b007".U,
//     "h215349b6b482ac37".U,
//     "h0138a9e9a3a5b6bb".U,
//     "h0766adda5272f1f8".U,
//     "hb4b47edb373d3254".U,
//     "h018a5c5b9648f721".U
// )

// val vecBdata = Seq(
//     "habac570f55566dc2".U,
//     "h71db9cf22d5755ed".U,
//     "he876ca93e5ccf8f4".U,
//     "h45e4a61dc6e0fc75".U,
//     "h703f6200d98738b8".U,
//     "h254e165cbbd5e849".U,
//     "h2311064f94991285".U,
//     "hbaa5894fa8fa1b76".U
// )

// val eleCdata = Seq(
//     Seq(25002, 10346, -17022, -9960, -19315, -6302, -7353, 4864),
//     Seq(18976, 16944, -1069, -2050, -33123, -26859, -31007, 9946),
//     Seq(-14233, 7165, -16083, 19205, -10105, 5141, 8884, 22772),
//     Seq(-34398, -28173, 21742, 6448, 25625, 19186, 10067, -12730),
//     Seq(-32222, -10018, 22452, 6193, 10576, 7444, 25220, 1960),
//     Seq(-1319, 18334, 12566, -5285, -18180, -7891, -21405, -12146),
//     Seq(33414, -7422, -15980, -11043, -13726, -10858, -27774, 375),
//     Seq(13351, -5893, -30041, 5473, -5764, 8072, 5581, 19456)
// )

// /*testdata end*/

//       val m = Consts.m
//       val n = Consts.n

//       // for(i <- 0 until m){//所有muxCtrl set 0
//       //     dut.io.muxCtrl(i).poke(false.B)
//       //   }

//       /* 0 cycle*/
//       dut.io.vecA(0).poke(vecAdata(0))//装填0 row ,以A
//       for(j <- 0 until n){
//         dut.io.vecB(j).poke(vecBdata(j))//装填0 row ,以B
//       }

//       dut.clock.step(1)// 等待一个时钟周期

//       /* 1 ～ m-1 cycle*/
//       for(tick <- 1 until m){
//         for(i <- 0 until m){//所有muxCtrl set 0
//           dut.io.muxCtrlC(i).poke(false.B)
//         }
//         dut.io.muxCtrlC(tick-1).poke(true.B)//对应行set 1
        
//         for(j <- 0 until n){//检查tick-1行下来的值
//           dut.io.eleC(j).expect(eleCdata(tick-1)(j).S(32.W))
//         }

//         dut.io.vecA(tick).poke(vecAdata(tick))//装填tick row ,以A

//         dut.clock.step(1)// 等待一个时钟周期
//       }

//       /* m cycle*/
//       for(i <- 0 until m){//所有muxCtrl set 0
//           dut.io.muxCtrlC(i).poke(false.B)
//         }
//       dut.io.muxCtrlC(m-1).poke(true.B)//对应行set 1

//       for(j <- 0 until n){//检查m行下来的值
//           dut.io.eleC(j).expect(eleCdata(m-1)(j).S(32.W))
//         }

//     }
//   }
// }



// class SimpleTestExpect extends AnyFlatSpec with ChiselScalatestTester {

      
//   "CTRL" should "pass" in {
//     test(new CTRL) { dut =>

//       val numM = Consts.tileM/Consts.m
//       val numN = Consts.tileN/Consts.n
//       val numK = Consts.tileK/Consts.k

//       for(i <- 0 until 5){//随便跑几个cycle
//         dut.clock.step(1)
//       }

//       dut.io.sigStart.poke(true.B)
//       dut.clock.step(1)
//       dut.io.sigStart.poke(false.B)

//       for(i <- 0 until numM){
//         for(j <- 0 until numN){
//           println(s"\n    !!!!!!step: i = $i , j = $j!!!!!!\n")
//           for(kk <- 0 until numK){
//             println(s"kk = $kk :")
//             // 打印 muxCtrlC（拼接成一行）
//             val muxCtrlCStr = dut.io.muxCtrlC.map(_.peek().litToBoolean).mkString(" ")
//             println(s"muxCtrlC: $muxCtrlCStr")

//             // 打印 muxCtrlSum（拼接成一行）
//             val muxCtrlSumStr = dut.io.muxCtrlSum.map(_.peek().litToBoolean).mkString(" ")
//             println(s"muxCtrlSum: $muxCtrlSumStr")

//             // 打印 addrReadA（逐行输出）
//             println("addrReadA:")
//             dut.io.addrReadA.zipWithIndex.foreach { case (addr, i) =>
//               println(s"  [$i] = ${addr.peek().litValue.toInt.toHexString}")
//             }

//             // 打印 addrReadB（逐行输出）
//             println("addrReadB:")
//             dut.io.addrReadB.zipWithIndex.foreach { case (addr, i) =>
//               println(s"  [$i] = ${addr.peek().litValue.toInt.toHexString}")
//             }

//             dut.clock.step(1)
//           }
//         }
//       }

//       dut.io.sigDone.expect(true.B)
//       dut.clock.step(1)
//       dut.io.sigDone.expect(false.B)
//     }
//   }
// }





// class SimpleTestExpect extends AnyFlatSpec with ChiselScalatestTester {
      
//   "Tile" should "pass" in {
//     test(new Tile) { dut =>

// /*testdata start*/
// val vecAdata = Seq(
//     "ha858455fc00e7b43".U,
//     "he6758fa68519b6cd".U,
//     "h4328cf277a59dd4f".U,
//     "h2b62afacfc633f8f".U,
//     "h27f353915b87332f".U,
//     "h5ef20b816b9b62b1".U,
//     "h47b2c8f5d6883607".U,
//     "h74eeb234821337cd".U,
//     "h7ac68d9a40acbe6f".U,
//     "h5ca06bfd903be4a2".U
// )

// val vecBdata = Seq(
//     "h20cad92a28aaa028".U,
//     "h4bda0b526226f352".U,
//     "hf2415ec662e2ee9a".U,
//     "he4fa1416faaf13c2".U,
//     "h12223b20d7d61444".U,
//     "hc395d28a39b3bfd2".U,
//     "hc864190fd98bd73f".U,
//     "hba1a575b0e3a4b6d".U,
//     "hd55942119f2f72c7".U,
//     "h2be3b56d25a15f80".U
// )

// val eleCdata = Seq(
//     -19161,
//         -19161 + -29343,
//             -19161 + -29343 + -3348,
//                 -19161 + -29343 + -3348 + -5052,
//                     -19161 + -29343 + -3348 + -5052 + 7172,
//                         -19161 + -29343 + -3348 + -5052 + 7172 + 21384,
//                             -19161 + -29343 + -3348 + -5052 + 7172 + 21384 + 564,
//                                 -19161 + -29343 + -3348 + -5052 + 7172 + 21384 + 564 + -12738,
//                                     -19161 + -29343 + -3348 + -5052 + 7172 + 21384 + 564 + -12738 + -43739,
//                                         -19161 + -29343 + -3348 + -5052 + 7172 + 21384 + 564 + -12738 + -43739 + -1989
// )

// /*testdata end*/


//       val num = 10
//       for(i <- 0 until num){

//         // 将转换后的向量赋值给 DUT 输入端口
//         dut.io.vecAin.poke(vecAdata(i))
//         dut.io.vecBin(0).poke(vecBdata(i))
//         dut.io.vecBin(1).poke(vecBdata(i))
//         dut.io.vecBin(2).poke(vecBdata(i))
//         dut.io.vecBin(3).poke(vecBdata(i))
//         // dut.io.vecAin.poke("hdc75a5b010f2f4df".U)
//         // dut.io.vecBin.poke("h798b5163b76c53cb".U)

//         dut.io.eleCin(0).poke(255.S(32.W))// 给 eleCin 赋一个测试值（可以是任意值，这里给定一个测试值）
//         dut.io.eleCin(1).poke(255.S(32.W))
//         dut.io.eleCin(2).poke(255.S(32.W))
//         dut.io.eleCin(3).poke(255.S(32.W))
        
//         dut.io.muxCtrlC.poke(true.B)// 控制信号设置为 true，测试计算的点积结果
//         dut.io.muxCtrlSum.poke(false.B)// 设置为 true，则不累加，否则累加

//         dut.clock.step(1)// 等待一个时钟周期

//         dut.io.eleCout(0).expect(eleCdata(i).S(32.W))// 手动计算点积预期值
//         dut.io.eleCout(1).expect(eleCdata(i).S(32.W))//
//         dut.io.eleCout(2).expect(eleCdata(i).S(32.W))//
//         dut.io.eleCout(3).expect(eleCdata(i).S(32.W))//
//         // dut.io.eleCout.expect(-3322.S(32.W))// 手动计算点积预期值

//         dut.io.muxCtrlC.poke(false.B)// 切换 mux 控制信号为 false，测试是否正确输出 eleCin
//         dut.io.eleCout(0).expect(255.S(32.W))  // 因为传入的是 0.S(32.W) 作为 eleCin
//         dut.io.eleCout(1).expect(255.S(32.W))
//         dut.io.eleCout(2).expect(255.S(32.W))
//         dut.io.eleCout(3).expect(255.S(32.W))
//       }
//     }
//   }
// }


