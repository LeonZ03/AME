package common

import chisel3._

//m <= tileK/k  
object Consts {
  val WORD_LEN      = 64
  val ADDR_LEN      = 32
  val tileM         = 16
  val tileN         = 16
  val tileK         = 64




  val m             = 8   
  val n             = 8   
  val k             = 4   
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
}