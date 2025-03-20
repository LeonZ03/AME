package common

import chisel3._

//m <= tileK/k  
//n >= 4
object Consts {
  val WORD_LEN      = 64
  val ADDR_LEN      = 16
  val tileM         = 8
  val tileN         = 16
  val tileK         = 32

  val m             = 4   
  val n             = 8   
  val k             = 4

  val numM          = tileM / m
  val numN          = tileN / n
  val numK          = tileK / k
}

class MMAUFormat extends Module{
  val WORD_LEN      = Consts.WORD_LEN
  val ADDR_LEN      = Consts.ADDR_LEN
  val tileM         = Consts.tileM
  val tileN         = Consts.tileN
  val tileK         = Consts.tileK

  val m             = Consts.m
  val n             = Consts.n
  val k             = Consts.k

  val numM          = Consts.numM
  val numN          = Consts.numN
  val numK          = Consts.numK
}