package MMAU

import chisel3._
import chisel3.util._

import common._




class FSM extends MMAUFormat{
  val io = IO(new Bundle {
    val sigStart = Input(Bool())    //启动信号

    val muxCtrlC = Output(Vec(m , Vec(n/4 , Bool())))     
    val muxCtrlSum = Output(Vec(m , Vec(n/4 , Bool())))          
    val addrReadA = Output(Vec(m , UInt(ADDR_LEN.W)))
    val addrReadB = Output(Vec(n/4 , UInt(ADDR_LEN.W)))
    val addrReadC = Output(Vec(n/4 , UInt(ADDR_LEN.W)))
    val addrWriteC = Output(Vec(n/4 , UInt(ADDR_LEN.W)))
    val sigEnWriteC = Output(Vec(n/4 , Bool()))    //C写使能
    val sigDone = Output(Bool())    //结束信号

  })


  val regOffM = RegInit(0.U(log2Ceil(tileM/m).W))   //log2,向上取整
  val regOffN = RegInit(0.U(log2Ceil(tileN/n).W))
  val regOffK = RegInit(0.U(log2Ceil(tileK/k).W))   //表示nState内部的numK个kState状态
  val regOffK_1H = RegInit(1.U((tileK/k).W))    //独热编码，表示nState内部的numK个kState状态

  regOffK_1H := Mux(io.sigStart === true.B , 1.U((tileK/k).W) , Cat(regOffK_1H((tileK/k)-2 , 0) , regOffK_1H((tileK/k)-1)))   //循环左移
  regOffK := Mux(io.sigStart === true.B , 0.U(log2Ceil(tileK/k).W) , regOffK + 1.U)  //递增，循环
  regOffN := Mux(io.sigStart === true.B , 0.U(log2Ceil(tileN/n).W) , 
                Mux(regOffK_1H((tileK/k) - 1)===1.U , regOffN + 1.U , regOffN))    //regOffK_1H最高位为1时更新
  regOffM := Mux(io.sigStart === true.B , 0.U(log2Ceil(tileM/m).W) , 
                Mux(regOffN === ((tileN/n) - 1).U && regOffK_1H((tileK/k) - 1) === 1.U , regOffM + 1.U , regOffM)) //可能会有一定开销，可以加一个regOffN_1H优化

  // val delayDone := Mux(regOffM === 0.U && regOffN === 0.U && regOffK_1H(m-1) === 1.U && io.sigStart === false.B , true.B , false.B)//全部执行完,sigDone拉高,保持一个cycle (注意这是组合逻辑)



  /*    muxCtrlC muxCtrlSum    */

  for(i <- 0 until m){//这很巧妙，muxCtrl直接复用regOffK
    io.muxCtrlC(i)(0) := regOffK_1H(i)
    io.muxCtrlSum(i)(0) := regOffK_1H(i)
  }

  if(n > 4){  //此时不止一列tile,需要寄存器打拍
    val regMuxCrtl = Reg(Vec(n/4 - 1 , UInt(m.W)))
    regMuxCrtl.foreach(_ := 0.U)
    for(i <- 1 until n/4 -1) { regMuxCrtl(i) := regMuxCrtl(i-1)}
    regMuxCrtl(0) := regOffK_1H(m-1 , 0)

    for(i <- 0 until m){
      for(j <- 1 until n/4){
        io.muxCtrlC(i)(j) := regMuxCrtl(j-1)(i)
        io.muxCtrlSum(i)(j) := regMuxCrtl(j-1)(i)
      }
    }
  }


  /*    read matrixA    */
  
  val wireAddrReadA = Wire(UInt(ADDR_LEN.W))
  wireAddrReadA := regOffM * (tileK/k).U + regOffK

  val regDelayA = Reg(Vec(m-1, UInt(ADDR_LEN.W)))  // 定义寄存器组
  regDelayA.foreach(_ := 0.U)  // 初始化所有寄存器为 0
  
  regDelayA(0) := wireAddrReadA
  for(i <- 1 until m-1){
    regDelayA(i) := regDelayA(i-1)   //reg组内部连接,实现"数据流动"的效果
  }

  io.addrReadA(0) := wireAddrReadA
  for(i <- 1 until m){
    io.addrReadA(i) := regDelayA(i-1)
  }

  /*    read matrixB    */
  
  val wireAddrReadB = Wire(UInt(ADDR_LEN.W))
  wireAddrReadB := regOffN * (tileK/k).U + regOffK

  io.addrReadB(0) := wireAddrReadB  

  if(n > 4){
    val regDelayB = Reg(Vec(n/4 - 1, UInt(ADDR_LEN.W)))  // 定义寄存器组
    regDelayB.foreach(_ := 0.U)  // 初始化所有寄存器为 0

    regDelayB(0) := wireAddrReadB
    for(i <- 1 until n/4 - 1){
      regDelayB(i) := regDelayB(i-1)   //reg组内部连接,实现"数据流动"的效果
    }

    for(i <- 1 until n/4){
      io.addrReadB(i) := regDelayB(i-1)
    }
  }


  /*    read & write matrixC    */

  val wireAddrPublicC = Wire(UInt(ADDR_LEN.W))  //C读写地址是可以复用的,只是时间上有前后(目前认为没有先后,因为是瞬间出来)
  // wireAddrPublicC := ( numN.U * regOffM + regOffN - 1.U) * m.U + regOffK  //只有kState(0) ~ kState(m-1) 的数据是有意义的
  val wireNumStep = Wire(UInt(log2Ceil(numM * numN).W))
  wireNumStep := numN.U * regOffM + regOffN - 1.U
  wireAddrPublicC := wireNumStep * m.U + regOffK  //只有kState(0) ~ kState(m-1) 的数据是有意义的

  val regDelayC = Reg(Vec(n/4 , UInt(ADDR_LEN.W)))
  regDelayC.foreach(_ := 0.U)

  regDelayC(0) := wireAddrPublicC
  for(i <- 1 until n/4){
    regDelayC(i) := regDelayC(i-1)
  }

  io.addrReadC(0) := wireAddrPublicC
  for(i <- 1 until n/4){
    io.addrReadC(i) := regDelayC(i-1)
  } 

  // for(i <- 0 until n/4){
  //   io.addrWriteC(i) := regDelayC(i)
  // }
  io.addrWriteC(0) := wireAddrPublicC  //和read同时
  for(i <- 1 until n/4){
    io.addrWriteC(i) := regDelayC(i-1)
  } 


  //write C enable

  val regCntZero = RegInit(0.U(2.W))  //记录kState为 numK-1 的次数,1次之后不再更新
  when(io.sigStart === true.B){
    regCntZero := 0.U
  }.elsewhen(regCntZero === 1.U){
    regCntZero := regCntZero
  }.elsewhen(regOffK_1H(numK-1) === 1.U){
    regCntZero := regCntZero + 1.U
  }.otherwise{
    regCntZero := regCntZero
  }

  val wireEnWriteC = Wire(Bool())

  if(m < numK){
    wireEnWriteC := Mux(regCntZero === 1.U && regOffK >= 0.U && regOffK <= (m-1).U , true.B , false.B)
  }else{
    wireEnWriteC := Mux(regCntZero === 1.U , true.B , false.B)
  }

  io.sigEnWriteC(0) := wireEnWriteC

  if(n/4 > 1){
    val regDelayEnWriteC = Reg(Vec(n/4 - 1 , Bool()))
    regDelayEnWriteC(0) := Mux(io.sigStart === true.B , false.B , wireEnWriteC)
    for(i <- 1 until n/4 - 1){
      regDelayEnWriteC(i) := Mux(io.sigStart === true.B , false.B , regDelayEnWriteC(i-1))
    }

    for(i <- 1 until n/4){
      io.sigEnWriteC(i) := regDelayEnWriteC(i-1)
    }
  }
  

  /*    signal done    */
  val regCntDone = RegInit(0.U(log2Ceil(n/4 + m).W)) //C的第一个bank的index0结束后,还需等待n/4 + m 个周期,所有bank结束
  val wireDone = Wire(Bool()) 

  if(m < numK){
    wireDone := Mux(regOffM === 0.U && regOffN === 0.U && regOffK === 0.U && regCntZero === 1.U , true.B , false.B)
  }else{
    wireDone := Mux(regOffM === 0.U && regOffN === 0.U && regOffK === 0.U && regCntZero === 1.U , true.B , false.B)
  }

  when(io.sigStart === true.B){
    regCntDone := 0.U
  }.elsewhen(regCntDone === (n/4 + m - 1).U){ //满了则不再变
    regCntDone := regCntDone
  }.elsewhen(wireDone === true.B || regCntDone > 0.U){
    regCntDone := regCntDone + 1.U
  }.otherwise{
    regCntDone := 0.U
  }

  io.sigDone := Mux(regCntDone === (n/4 + m - 1).U , true.B , false.B)

}






// object Main extends App {
//   (new chisel3.stage.ChiselStage).emitVerilog(new FSM)
// }







//旧版
// class CTRL extends MMAUFormat{
//   val io = IO(new Bundle {
//     val sigStart = Input(Bool())    //启动信号

//     val muxCtrlC = Output(Vec(m, Bool()))    // m 个控制信号，同一行是共用的
//     val muxCtrlSum = Output(Vec(m, Bool()))         // m 个,用于控制DPA内部累加寄存器更新逻辑（累加 or 归位）,同一行是共用的
//     val addrReadA = Output(Vec(m,UInt(ADDR_LEN.W)))
//     val addrReadB = Output(Vec(n,UInt(ADDR_LEN.W)))
//     val addrReadC = Output(Vec(n,UInt(ADDR_LEN.W)))
//     val addrWriteC = Output(Vec(n,UInt(ADDR_LEN.W)))
//     val writeEnC = Output(Vec(n, Bool()))
//     val sigDone = Output(Bool())    //结束信号

//     // val testOut = Output(UInt(8.W))  //test usage
//   })

//   for(i <- 0 until n){//暂时给一个值
//     io.addrWriteC(i) := 0.U
//     io.writeEnC(i) := false.B
//     io.addrReadC(i) := 0.U
//   }

//   val regOffM = RegInit(0.U(log2Ceil(tileM/m).W))   //log2,向上取整
//   val regOffN = RegInit(0.U(log2Ceil(tileN/n).W))
//   val regOffK = RegInit(0.U(log2Ceil(tileK/k).W))   //表示State内部的numK个subState状态
//   val regOffK_1H = RegInit(1.U((tileK/k).W))    //独热编码，表示State内部的numK个subState状态

//   regOffK_1H := Mux(io.sigStart === true.B , 1.U((tileK/k).W) , Cat(regOffK_1H((tileK/k)-2 , 0) , regOffK_1H((tileK/k)-1)))   //循环左移
//   regOffK := Mux(io.sigStart === true.B , 0.U(log2Ceil(tileK/k).W) , regOffK + 1.U)  //递增，循环
//   regOffN := Mux(io.sigStart === true.B , 0.U(log2Ceil(tileN/n).W) , 
//                 Mux(regOffK_1H((tileK/k) - 1)===1.U , regOffN + 1.U , regOffN))    //regOffK_1H最高位为1时更新
//   regOffM := Mux(io.sigStart === true.B , 0.U(log2Ceil(tileM/m).W) , 
//                 Mux(regOffN === ((tileN/n) - 1).U && regOffK_1H((tileK/k) - 1) === 1.U , regOffM + 1.U , regOffM)) //可能会有一定开销，可以加一个regOffN_1H优化

//   io.sigDone := Mux(regOffM === 0.U && regOffN === 0.U && regOffK_1H(0) === 1.U && io.sigStart === false.B , true.B , false.B)//全部执行完,sigDone拉高,保持一个cycle (注意这是组合逻辑)
  
//   for(i <- 0 until m){//这很巧妙，muxCtrl直接复用regOffK
//     io.muxCtrlC(i) := regOffK_1H(i)
//     io.muxCtrlSum(i) := regOffK_1H(i)
//   }

// //  test usage
// //   val regTest0 = RegInit(0.U(8.W))
// //   val regTest1 = RegInit(0.U(8.W))
// //   val regTest2 = RegInit(0.U(8.W))
// //   val regTest3 = RegInit(0.U(8.W))

// //   regTest0 := 1.U
// //   regTest1 := regTest0
// //   regTest2 := regTest1
// //   regTest3 := regTest2
// //   io.testOut := regTest3

//   /*    read matrixA    */
//   val wireAddrReadA = Wire(UInt(ADDR_LEN.W))
//   wireAddrReadA := regOffM * (tileK/k).U + regOffK

// //   val regDelayA = VecInit(Seq.fill(m-1)(RegInit(0.U(ADDR_LEN.W))))   //这种实例化方式好像有问题,无法通过简单连接寄存器的方式实现"数据流动"的效果
//   val regDelayA = Reg(Vec(m-1, UInt(ADDR_LEN.W)))  // 定义寄存器组
//   regDelayA.foreach(_ := 0.U)  // 初始化所有寄存器为 0
  
//   regDelayA(0) := wireAddrReadA
//   for(i <- 1 until m-1){
//     regDelayA(i) := regDelayA(i-1)   //reg组内部连接,实现"数据流动"的效果
//   }

//   io.addrReadA(0) := wireAddrReadA
//   for(i <- 1 until m){
//     io.addrReadA(i) := regDelayA(i-1)
//   }

//   /*    read matrixB    */
//   val wireAddrReadB = Wire(UInt(ADDR_LEN.W))
//   wireAddrReadB := regOffN * (tileK/k).U + regOffK
  
//   for(i <- 0 until n){
//     io.addrReadB(i) := wireAddrReadB
//   }
// }

