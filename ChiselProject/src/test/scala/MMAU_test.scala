import chisel3._
import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec

import MMAU._
import common._





class MMAUTestExpect extends AnyFlatSpec with ChiselScalatestTester {


  def checkOutput(dut: MMAU): Unit = {
    // 装填 vecA
    for (i <- 0 until Consts.m) {
      val addrReadA = dut.io.addrReadA(i).peek().litValue.toInt  // 获取地址值
      if(addrReadA < TestData.A.head.length){
        dut.io.vecA(i).poke(TestData.A(i)(addrReadA))  // 根据地址装填数据
      }
      
    }

    // 装填 vecB
    for (i <- 0 until Consts.n) {
      val addrReadB = dut.io.addrReadB(i).peek().litValue.toInt  // 获取地址值
      if(addrReadB < TestData.B.head.length){
        dut.io.vecB(i).poke(TestData.B(i)(addrReadB))  // 根据地址装填数据
      }
      
    }

    // 装填 vecCin
    for (i <- 0 until Consts.n / 4) {
      val addrReadC = dut.io.addrReadC(i).peek().litValue.toInt  // 获取地址值
      if(addrReadC < TestData.Ctmp.head.length){
        // dut.io.vecCin(i).poke(TestData.Ctmp(i)(addrReadC))  // 根据地址装填数据
        dut.io.vecCin(i).poke(0.U)
      }
      
    }

    // 推进时钟
    dut.clock.step(1)

    // 检查 vecCout
    for (i <- 0 until Consts.n / 4) {
      val addrWriteC = dut.io.addrWriteC(i).peek().litValue.toInt  // 获取地址值
      if (dut.io.sigEnWriteC(i).peek().litToBoolean) {  // 检查写使能信号
        dut.io.vecCout(i).expect(TestData.C(i)(addrWriteC))  // 验证输出
        // val vecCout = dut.io.vecCout(i).peek().litValue.toString(16)
        // println(s"i = $i , addrWriteC = $addrWriteC , sigEnWriteC = ${dut.io.sigEnWriteC(i).peek().litToBoolean} vecCout = $vecCout")
      }
    }
  }

  "MMAU" should "PASS" in {
    test(new MMAU) { dut =>

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
        for (j <- 0 until numN) {
          for (p <- 0 until numK) {
            // println(s"mState = $i , nState = $j , kState = $p")
            checkOutput(dut)
            // println("\n")
          }
        }
      }


      for (p <- 0 until numK) {
            // println(s"mState = $i , nState = $j , kState = $p")
            checkOutput(dut)
            println(s"sigDone = ${dut.io.sigDone.peek().litToBoolean}") // 打印sigDone
            // println("\n")
      }

      for (p <- 0 until numK) {
            // println(s"mState = $i , nState = $j , kState = $p")
            checkOutput(dut)
            println(s"sigDone = ${dut.io.sigDone.peek().litToBoolean}") // 打印sigDone
            // println("\n")
      }
      

      
    }
  }
}
























class MMAUPrintTestExpect extends AnyFlatSpec with ChiselScalatestTester {
  def printIO(dut: MMAU): Unit = {
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


  "MMAU" should "PASS" in {
    test(new MMAU) { dut =>

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