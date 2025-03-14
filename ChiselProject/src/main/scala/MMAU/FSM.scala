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
    val sigDone = Output(Bool())    //结束信号

  })

  for(i <- 0 until n/4){//暂时给一个值
    io.addrWriteC(i) := 0.U
    io.addrReadC(i) := 0.U
  }

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

  io.sigDone := Mux(regOffM === 0.U && regOffN === 0.U && regOffK_1H(0) === 1.U && io.sigStart === false.B , true.B , false.B)//全部执行完,sigDone拉高,保持一个cycle (注意这是组合逻辑)
  


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








}






object Main extends App {
  (new chisel3.stage.ChiselStage).emitVerilog(new FSM)
}







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

