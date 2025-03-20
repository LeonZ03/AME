import chisel3._
import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec

import MMAU._
import common._









class FSMTestExpect extends AnyFlatSpec with ChiselScalatestTester {
  def printIO(dut: FSM): Unit = {
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

    // 打印 addrReadC（十进制输出）
    val addrReadCStr = dut.io.addrReadC.map(_.peek().litValue.toInt).mkString(" ")
    println(s"addrReadC: $addrReadCStr")

    // 打印 addrWriteC（十进制输出）
    val addrWriteCStr = dut.io.addrWriteC.map(_.peek().litValue.toInt).mkString(" ")
    println(s"addrWriteC: $addrWriteCStr")

    // 打印sigEnWriteC
    // val sigEnWriteCStr = dut.io.sigEnWriteC.map(_.peek().litValue.toBoolean).mkString(" ")
    val sigEnWriteCStr = dut.io.sigEnWriteC.map(_.peek().litToBoolean).mkString(" ")
    println(s"sigEnWriteC = $sigEnWriteCStr")

    // 打印sigDone
    println(s"sigDone = ${dut.io.sigDone.peek().litToBoolean}")
  }


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

    
            printIO(dut)  //打印所有相关信号

            dut.clock.step(1)
          }
        }
      }

      /*    再来一轮    */

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

    
            printIO(dut)  //打印所有相关信号

            dut.clock.step(1)
          }
        }
      }

      
    }
  }
}



















// class CTRLTestExpect extends AnyFlatSpec with ChiselScalatestTester {

      
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

