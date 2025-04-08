package common

import chisel3._

//m <= tileK/k  
//n >= 4
object Consts {

  /*    MMAU    */
  val WORD_LEN      = 64
  // val ADDR_LEN      = 16  


  val tileM         = 16
  val tileN         = 16
  val tileK         = 32

  val m             = 4   
  val n             = 4  
  val k             = 8
  // val tileM         = 64
  // val tileN         = 64
  // val tileK         = 256

  // val m             = 32   
  // val n             = 32  
  // val k             = 8

  val numM          = tileM / m
  val numN          = tileN / n
  val numK          = tileK / k

  /*    RegFile   */
  //Tr
  val Tr_LEN        = 8 * k//Tr单个元素位宽（向量）
  val Tr_INDEX_LEN    = 6 //Tr寻址时index需要的位宽
  
  val genTr: Data = UInt(Tr_LEN.W)
  val setTr: Int = numM * numK
  val wayTr: Int = 1
  val dataSplitTr: Int = 1
  val setSplitTr: Int = 1
  val waySplitTr: Int = 1
  val useBitmaskTr: Boolean = false

  val numTr: Int = 4
  val numTrBank: Int = m
  val numTrReadPort: Int = 2
  val numTrWritePort: Int = 2

  //Acc
  val Acc_LEN       = 32 * 4
  val Acc_INDEX_LEN   = 7

  val genAcc: Data = UInt(Acc_LEN.W)
  val setAcc: Int = numM * numN * m
  val wayAcc: Int = 1
  val dataSplitAcc: Int = 1
  val setSplitAcc: Int = 1
  val waySplitAcc: Int = 1
  val useBitmaskAcc: Boolean = false

  val numAcc: Int = 4
  val numAccBank: Int = n / 4
  val numAccReadPort: Int = 2
  val numAccWritePort: Int = 2
}

class MMAUFormat extends Module{
  val WORD_LEN      = Consts.WORD_LEN
  // val ADDR_LEN      = Consts.ADDR_LEN
  val Tr_INDEX_LEN    = Consts.Tr_INDEX_LEN
  val Acc_INDEX_LEN   = Consts.Acc_INDEX_LEN

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

class RegFileFormat extends Module{
  //Tr
  val Tr_LEN        = Consts.Tr_LEN //Tr单个元素位宽（向量）
  val Tr_INDEX_LEN    = Consts.Tr_INDEX_LEN //Tr寻址时index需要的位宽
  val genTr = Consts.genTr
  val setTr = Consts.setTr
  val wayTr = Consts.wayTr
  val dataSplitTr = Consts.dataSplitTr
  val setSplitTr = Consts.setSplitTr
  val waySplitTr = Consts.waySplitTr
  val useBitmaskTr = Consts.useBitmaskTr
  val numTr = Consts.numTr
  val numTrBank = Consts.numTrBank
  val numTrReadPort = Consts.numTrReadPort
  val numTrWritePort = Consts.numTrWritePort

  //Acc
  val Acc_LEN        = Consts.Acc_LEN //Acc单个元素位宽（向量）
  val Acc_INDEX_LEN    = Consts.Acc_INDEX_LEN //Acc寻址时index需要的位宽
  val genAcc = Consts.genAcc
  val setAcc = Consts.setAcc
  val wayAcc = Consts.wayAcc
  val dataSplitAcc = Consts.dataSplitAcc
  val setSplitAcc = Consts.setSplitAcc
  val waySplitAcc = Consts.waySplitAcc
  val useBitmaskAcc = Consts.useBitmaskAcc
  val numAcc = Consts.numAcc
  val numAccBank = Consts.numAccBank
  val numAccReadPort = Consts.numAccReadPort
  val numAccWritePort = Consts.numAccWritePort
}
