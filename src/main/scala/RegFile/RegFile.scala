package RegFile

import chisel3._
import chisel3.util._

import common._
import utility.sram._



/*
RegFile模块用于实现片上寄存器堆，支持Tr和Acc两类寄存器的多端口读写访问。
主要功能：
  1. 实例化多个Tr和Acc寄存器，分别支持独立读写端口。
  2. 支持Tr、Acc的分组读写和全局读写接口。
  3. 根据端口地址和使能信号，动态连接到对应寄存器的读写端口。
  4. 实现Tr和Acc的端口仲裁与数据通路管理，支持多端口并发访问。
*/

class RegFile extends RegFileFormat {
  val io = IO(new RegFileIO)
  io := DontCare

  // 实例化 numTr 个 Tr
  val trs = Seq.fill(numTr)(Module(new Tr))
  val trsVec = VecInit(trs.map(_.io)) 
  trsVec.foreach(_ := DontCare) //避免报错

  /*    Tr Ports    */
  //read
  for (portIdx <- 0 until numTrReadPort) {
  val port = io.readTr(portIdx)
  
    for ((tr, trIdx) <- trs.zipWithIndex) {
      val hit = port.addr === trIdx.U

      when(hit && port.act){
        for (bankIdx <- 0 until numTrBank) {
          port.r(bankIdx) <> tr.io.r(bankIdx)
        }
      }
    }
  }

  //write
  for (portIdx <- 0 until numTrWritePort) {
    val port = io.writeTr(portIdx)
    
    for ((tr, trIdx) <- trs.zipWithIndex) {
      val hit = port.addr === trIdx.U

      when(hit && port.act){
        for (bankIdx <- 0 until numTrBank) {
          port.w(bankIdx) <> tr.io.w(bankIdx)
        }
      }
    }
  }

  // 实例化 numAcc 个 Acc
  val accs = Seq.fill(numAcc)(Module(new Acc))
  val accsVec = VecInit(accs.map(_.io)) 
  accsVec.foreach(_ := DontCare) //避免报错

  /*    Acc Ports   */ 
  //read 
  for (portIdx <- 0 until numAccReadPort) {
  val port = io.readAcc(portIdx)
  
    for ((acc, accIdx) <- accs.zipWithIndex) {
      val hit = port.addr === accIdx.U

      when(hit && port.act){
        for (bankIdx <- 0 until numAccBank) {
          port.r(bankIdx) <> acc.io.r(bankIdx)
        }
      }
    }
  }

  //write
  for (portIdx <- 0 until numAccWritePort) {
    val port = io.writeAcc(portIdx)
    
    for ((acc, accIdx) <- accs.zipWithIndex) {
      val hit = port.addr === accIdx.U

      when(hit && port.act){
        for (bankIdx <- 0 until numAccBank) {
          port.w(bankIdx) <> acc.io.w(bankIdx)
        }
      }
    }
  }

  /*    All Ports   */
  //read
  for (portIdx <- 0 until numAllReadPort) {
  val port = io.readAll(portIdx)

    when(port.addr(2) === 0.U){// 寄存器号<4,是Tr
      
      for ((tr, trIdx) <- trs.zipWithIndex) {
      val hit = port.addr === trIdx.U

      when(hit && port.act){
        for (bankIdx <- 0 until numTrBank) {
          tr.io.r(bankIdx).req.bits.setIdx := port.r(bankIdx).req.bits.setIdx(Tr_INDEX_LEN-1 , 0)
          port.r(bankIdx).resp.data.head := tr.io.r(bankIdx).resp.data.head.asUInt.pad(All_LEN)
          tr.io.r(bankIdx).req.valid := port.r(bankIdx).req.valid
        }
      }
     }

    }.otherwise{

      for ((acc, accIdx) <- accs.zipWithIndex) {
      val hit = (port.addr - 4.U) === accIdx.U

      when(hit && port.act){
        for (bankIdx <- 0 until numAccBank) {
          acc.io.r(bankIdx).req.bits.setIdx := port.r(bankIdx).req.bits.setIdx(Acc_INDEX_LEN-1 , 0)
          port.r(bankIdx).resp.data.head := acc.io.r(bankIdx).resp.data.head.asUInt.pad(All_LEN)
          acc.io.r(bankIdx).req.valid := port.r(bankIdx).req.valid
        }
      }
     }
    }   
  }

  //write
  for (portIdx <- 0 until numAllWritePort) {
  val port = io.writeAll(portIdx)

    when(port.addr(2) === 0.U){// 寄存器号<4,是Tr
      
      for ((tr, trIdx) <- trs.zipWithIndex) {
      val hit = port.addr === trIdx.U

      when(hit && port.act){
        for (bankIdx <- 0 until numTrBank) {
          tr.io.w(bankIdx).req.bits.setIdx := port.w(bankIdx).req.bits.setIdx(Tr_INDEX_LEN-1 , 0)
          tr.io.w(bankIdx).req.bits.data.head := port.w(bankIdx).req.bits.data.head.asUInt(Tr_LEN-1 , 0)
          tr.io.w(bankIdx).req.valid := port.w(bankIdx).req.valid
        }
      }
     }

    }.otherwise{

      for ((acc, accIdx) <- accs.zipWithIndex) {
      val hit = (port.addr - 4.U) === accIdx.U

      when(hit && port.act){
        for (bankIdx <- 0 until numAccBank) {
          acc.io.w(bankIdx).req.bits.setIdx := port.w(bankIdx).req.bits.setIdx(Acc_INDEX_LEN-1 , 0)
          acc.io.w(bankIdx).req.bits.data.head := port.w(bankIdx).req.bits.data.head.asUInt(Acc_LEN-1 , 0)
          acc.io.w(bankIdx).req.valid := port.w(bankIdx).req.valid
        }
      }
     }
    }   
  }
  
}




