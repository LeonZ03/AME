package common

import chisel3._
import chisel3.util._



//m <= tileK/k  
//n >= 4
//m=n,tileM=tileN
object Consts {

  /*    MMAU    */
  val WORD_LEN      = 64  //暂时没用


  val tileM         = 64  //RegFile尺寸，M维度
  val tileN         = 64  //RegFile尺寸，N维度
  val tileK         = 256 //RegFile尺寸，K维度

  val m             = 32  //MMAU计算单元尺寸，M维度
  val n             = 32  //MMAU计算单元尺寸，N维度
  val k             = 8   //MMAU计算单元尺寸，K维度

  val numM          = tileM / m
  val numN          = tileN / n
  val numK          = tileK / k

  /*    RegFile   */
  val sramLatency: Int = 1  //sram延迟周期数
  //Tr
  val Tr_LEN        = 8 * k//Tr单个元素位宽（向量）

  val genTr: Data = UInt(Tr_LEN.W)  // Tr单个元素位宽（向量）
  val setTr: Int = numM * numK  // Tr深度
  val wayTr: Int = 1  //bank数，由于实现时是每个bank单独实例化，所以这里是1
  val dataSplitTr: Int = k  //实例化时的一个参数，可能影响性能，填别的数对仿真没有影响（貌似）
  val setSplitTr: Int = 1   //同上
  val waySplitTr: Int = 1   //同上
  val useBitmaskTr: Boolean = false

  val numTr: Int = 4  //Tr寄存器数量
  val numTrBank: Int = m  //Tr寄存器bank数量
  val numTrReadPort: Int = 3  //Tr寄存器读端口数量
  val numTrWritePort: Int = 3  //Tr寄存器写端口数量


  val Tr_INDEX_LEN    = log2Ceil(setTr) //Tr寻址时index需要的位宽
  val Tr_ADDR_LEN     = log2Ceil(numTr) //表示Tr标号所需位宽（0～3）
  

  //Acc
  val Acc_LEN       = 32 * 4

  val genAcc: Data = UInt(Acc_LEN.W)
  val setAcc: Int = numM * numN * m
  val wayAcc: Int = 1
  val dataSplitAcc: Int = 4
  val setSplitAcc: Int = 1
  val waySplitAcc: Int = 1
  val useBitmaskAcc: Boolean = false

  val numAcc: Int = 4
  val numAccBank: Int = n / 4
  val numAccReadPort: Int = 3
  val numAccWritePort: Int = 3

  val Acc_INDEX_LEN   = log2Ceil(setAcc)
  val Acc_ADDR_LEN     = log2Ceil(numAcc) //表示Acc标号所需位宽（0～3）

  //All
  val All_LEN       = scala.math.max(Acc_LEN , Tr_LEN)

  val genAll: Data = UInt(All_LEN.W)
  val setAll: Int = scala.math.max(setAcc , setTr)
  val wayAll: Int = 1
  val dataSplitAll: Int = 4
  val setSplitAll: Int = 1
  val waySplitAll: Int = 1
  val useBitmaskAll: Boolean = false

  val numAllBank: Int = scala.math.max(numAccBank , numTrBank)
  val numAllReadPort: Int = 2
  val numAllWritePort: Int = 2

  val All_INDEX_LEN   = log2Ceil(setAll)
  val All_ADDR_LEN     = log2Ceil(numAcc + numTr) //表示所有寄存器标号所需位宽（0～7）

  // Load / Store
  val nRow_LEN = log2Ceil(16 + 1)   //这里后续还得调，先给个肯定够的
  val nCol_LEN = log2Ceil(4 + 1)
  // val rs1_LEN = 16
  val rs1_LEN = 48
  val rs2_LEN = 16
  // val L2_ADDR_LEN = 16
  val L2_ADDR_LEN = 48
  val L2_DATA_LEN = 64 * 8  //cacheline,64B
  val L2_ID_LEN = 5

  //ScoreBoard
  val RF_LEN = 8
  val Unit_LEN = 5

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

  val sramLatency = Consts.sramLatency
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

  //All
  val All_LEN       = Consts.All_LEN

  val genAll = Consts.genAll
  val setAll = Consts.setAll
  val wayAll = Consts.wayAll
  val dataSplitAll = Consts.dataSplitAll
  val setSplitAll = Consts.setSplitAll
  val waySplitAll = Consts.waySplitAll
  val useBitmaskAll = Consts.useBitmaskAll

  val numAllBank = Consts.numAllBank
  val numAllReadPort = Consts.numAllReadPort
  val numAllWritePort = Consts.numAllWritePort

  val All_INDEX_LEN   = Consts.All_INDEX_LEN

}

object Float32 {
  val LENGTH = 32
  val expWidth = 8
  val sigWidth = 23
  val SHIFTED_LENGTH = 54
}

object Float8E4M3 {
  val LENGTH = 8
  val expWidth = 4
  val sigWidth = 3
}

object FixedPoint {
  val LENGTH = 9
  val SIGN = 1
  val FRACTION = 8
  val FIRST_SHIFTED_LENGTH = 16
  val SHIFTED_LENGTH = 45
}

object Parameters {
  val WAY_SIZE = 8
  val FP8_LENGTH = 8
  val NUM_SCALE = 2
}
