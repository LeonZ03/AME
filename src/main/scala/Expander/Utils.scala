package Expander

import chisel3._
import chisel3.util._

import common._
import MMAU._








class ShakeHands_IO extends Bundle{//握手
  val valid = Input(Bool())
  val ready = Output(Bool())
}

class Operands_IO extends Bundle{//操作数信息
  val ms1 = Input(UInt(Consts.All_ADDR_LEN.W))
  val ms2 = Input(UInt(Consts.All_ADDR_LEN.W))
  val md = Input(UInt(Consts.All_ADDR_LEN.W))
  val rs1 = Input(UInt(Consts.rs1_LEN.W)) //base addr
  val rs2 = Input(UInt(Consts.rs2_LEN.W)) //stride
}

class InsType_IO extends Bundle{//指令类型
  val is_mmacc = Input(Bool())      //整型矩阵乘,signed 8bit, output quad-widen

  val is_mlbe8 = Input(Bool())      //load指令,8-bit right tile load(A)
  val is_mlae8 = Input(Bool())      //load指令,8-bit left tile load(B)
  val is_mlce32 = Input(Bool())      //load指令,32-bit Acc load(C)

  val is_msce32 = Input(Bool())      //store指令,32-bit Acc store(C)
}




class mtileConfig_IO extends Bundle{
  val mtilem = Input(UInt(log2Ceil(Consts.tileM+1).W))    //用户配置m维度长度
  val mtilen = Input(UInt(log2Ceil(Consts.tileN+1).W))    //用户配置n维度长度
  val mtilek = Input(UInt(log2Ceil(Consts.tileK+1).W))    //用户配置k维度长度
}


class Uop_IO extends Bundle{  //微操作序列
  val ShakeHands_io = new ShakeHands_IO
  val Operands_io = new Operands_IO
  val InsType_io = new InsType_IO
  val mtileConfig_io = new mtileConfig_IO
}


/*    计分板    */

class ScoreboardVisit_IO extends Bundle{//与计分板交互
  val read_RF = Input(UInt(Consts.RF_LEN.W))    //读取RF资源使用情况
  val read_Unit = Input(UInt(Consts.Unit_LEN.W))  //读取功能单元使用情况
  val writeMaskAlloc_RF = Output(UInt(Consts.RF_LEN.W))   //分配RF，掩码形式  
  val writeMaskAlloc_Unit = Output(UInt(Consts.Unit_LEN.W)) //分配功能单元，掩码形式
  val writeMaskFree_RF = Output(UInt(Consts.RF_LEN.W))   //释放RF，掩码形式  
  val writeMaskFree_Unit = Output(UInt(Consts.Unit_LEN.W)) //释放功能单元，掩码形式
}



/*    TileHandler   */


object applyTileHandler {
  // 向上补齐到 2^log2align 的倍数（不使用除法）
  def ceilAlignPow2(x: UInt, log2align: Int): UInt = {
    val mask = ((1 << log2align) - 1).U(x.getWidth.W)
    (x + mask) & (~mask)
  }
}




class TileHandler_MMAU_IO extends Bundle{
    val numm = Output(UInt(log2Ceil(Consts.numM+1).W))     
    val numn = Output(UInt(log2Ceil(Consts.numN+1).W))
    val numk = Output(UInt(log2Ceil(Consts.numK+1).W))
}

class TileHandler_MLU_IO extends Bundle{
  val nRow = Output(UInt(Consts.nRow_LEN.W))
  val nCol = Output(UInt(Consts.nCol_LEN.W))
}


class TileHandler_MSU_IO extends Bundle{
  val nRow = Output(UInt(7.W))
  val nCol = Output(UInt(Consts.nCol_LEN.W))
}






/*    MMAU   */

//CTRL、MMAU、FSM公共
class FSM_MMAU_IO extends Bundle {   
  val firstMuxCtrl = Input(UInt(Consts.m.W)) //muxCtrlC和muxCtrlSum第一个值
  val firstAddrReadA = Input(UInt(Consts.Tr_INDEX_LEN.W))  //AddrReadA第一个值
  val firstAddrReadB = Input(UInt(Consts.Tr_INDEX_LEN.W))  //AddrReadB第一个值
  val firstAddrPublicC = Input(UInt(Consts.Acc_INDEX_LEN.W)) //AddrC第一个读写公共地址
  val firstEnWriteC = Input(Bool()) //C第一个写使能
  val actPortReadA = Input(Bool())  //A读端口激活
  val actPortReadB = Input(Bool())  //B读端口激活
  val actPortReadC = Input(Bool())  //C读端口激活
  val actPortWriteC = Input(Bool())  //C写端口激活
  val Ops_io = new Ops_IO
}

class Ops_IO extends Bundle{
  val ms1 = Input(UInt(Consts.Tr_ADDR_LEN.W))
  val ms2 = Input(UInt(Consts.Tr_ADDR_LEN.W))
  val md = Input(UInt(Consts.Acc_ADDR_LEN.W))
}



//IssueMMAU

class IssueMMAU_Excute_IO extends Bundle{//连接ExcuteHandler
  val sigStart = Input(Bool())    //启动信号
  val in_ms1 = Input(UInt(Consts.All_ADDR_LEN.W))  //初始值
  val in_ms2 = Input(UInt(Consts.All_ADDR_LEN.W))
  val in_md = Input(UInt(Consts.All_ADDR_LEN.W))
  val mtilem = Input(UInt(log2Ceil(Consts.tileM+1).W))    //用户配置m维度长度
  val mtilen = Input(UInt(log2Ceil(Consts.tileN+1).W))    //用户配置n维度长度
  val mtilek = Input(UInt(log2Ceil(Consts.tileK+1).W))    //用户配置k维度长度

  val sigDone = Output(Bool())    //结束信号
  val out_ms1 = Output(UInt(Consts.All_ADDR_LEN.W)) //结束时用于回收
  val out_ms2 = Output(UInt(Consts.All_ADDR_LEN.W))
  val out_md = Output(UInt(Consts.All_ADDR_LEN.W))
}

/*    MLU   */

class Cacheline_Read_IO extends Bundle{ //向L2发送读请求（单条cacheline）
  val ready = Input(Bool()) //标志L2是否就绪,即反压信号
  
  val addr = Output(UInt(Consts.L2_ADDR_LEN.W))
  val id = Output(UInt(Consts.L2_ID_LEN.W))
  val valid = Output(Bool())  //指示当前产生的读请求是否有意义
}

class Cacheline_ReadBack_IO extends Bundle{ //从L2读回的数据（单条cacheline）
  val data = Input(UInt((64 * 8).W))  //64B
  val id = Input(UInt(Consts.L2_ID_LEN.W))
  val valid = Input(Bool()) //标志L2过来的数据是否有效
}

class FSM_MLU_IO extends Bundle{  //MLU的FSM，连接下层MLU接口
  val sigLineDone = Input(Vec(8, Bool()))  //每条Cacheline对应的写入RF，每成功写入一次数据，则上报一次

  val Cacheline_Read_io = Vec(8 , new Cacheline_Read_IO)  //对应8条cacheline
  val md = Output(UInt(Consts.All_ADDR_LEN.W))  //告知MLU该写入哪个寄存器
  val sigPortState = Output(Bool()) //告知MLU是否处于工作状态，用于Port的激活或注销
  val is_loadAB = Output(Bool()) //是load A/B指令
  val is_loadC = Output(Bool()) //是load C指令

}

class MLU_L2_IO extends Bundle{ //MLU访问L2
  val Cacheline_Read_io = Vec(8 , new Cacheline_Read_IO)
  val Cacheline_ReadBack_io = Vec(8 , new Cacheline_ReadBack_IO)
}

//IssueMLU

class IssueMLU_Excute_IO extends Bundle{//连接ExcuteHandler
  val sigStart = Input(Bool())    //启动信号
  val is_mlbe8 = Input(Bool())
  val is_mlae8 = Input(Bool())
  val is_mlce32 = Input(Bool())
  val rs1 = Input(UInt(Consts.rs1_LEN.W))  //baseaddr
  val rs2 = Input(UInt(Consts.rs2_LEN.W))   //stride
  val in_md = Input(UInt(Consts.All_ADDR_LEN.W))
  val mtilem = Input(UInt(log2Ceil(Consts.tileM+1).W))    //用户配置m维度长度
  val mtilen = Input(UInt(log2Ceil(Consts.tileN+1).W))    //用户配置n维度长度
  val mtilek = Input(UInt(log2Ceil(Consts.tileK+1).W))    //用户配置k维度长度

  val sigDone = Output(Bool())    //结束信号
  val out_md = Output(UInt(Consts.All_ADDR_LEN.W))  //结束时用于回收
}





/*    MSU   */
class Cacheline_Write_IO extends Bundle { //向L2发送写请求（单条cacheline）
  val ready = Input(Bool())   //标志L2是否就绪,即反压信号

  val addr = Output(UInt(Consts.L2_ADDR_LEN.W))
  val data = Output(UInt(Consts.L2_DATA_LEN.W))
  val valid = Output(Bool())  //指示当前产生的写请求是否有意义

}

class Cacheline_WriteBack_IO extends Bundle{ //写L2的Ack（单条cacheline）
  val valid = Input(Bool()) //即"ReleaseACK",若为true,说明成功写入一次
}

class MSU_L2_IO extends Bundle{ //MSU访问L2
  val Cacheline_Write_io = Vec(2 , new Cacheline_Write_IO)
  val Cacheline_WriteBack_io = Vec(2 , new Cacheline_WriteBack_IO)
}

class FSM_MSU_Output_IO extends Bundle {  //MSU状态机直接输出信号
  val addr = Output(UInt(Consts.L2_ADDR_LEN.W))
  val valid = Output(Bool())  //指示当前产生的写请求是否有意义
  val index = Output(UInt(Consts.All_INDEX_LEN.W))  //寄存器寻址深度
}


class FSM_MSU_IO extends Bundle{  //MSU的FSM，连接下层MSU接口
  val sigLineDone = Input(Vec(2, Bool()))  //每条Cacheline对应的写入RF，每成功写入一次数据，则上报一次

  val FSM_MSU_Output_io = Vec(2 , new FSM_MSU_Output_IO)  //状态机输出信号
  val md = Output(UInt(Consts.All_ADDR_LEN.W))  //告知MSU该写入哪个寄存器
  val sigPortState = Output(Bool()) //告知MSU是否处于工作状态，用于Port的激活或注销
  val is_storeC = Output(Bool()) //是store C指令

}

//IssueMSU

class IssueMSU_Excute_IO extends Bundle {
  val sigStart = Input(Bool())    //启动信号
  val is_msce32 = Input(Bool())

  val rs1 = Input(UInt(Consts.rs1_LEN.W))  //baseaddr
  val rs2 = Input(UInt(Consts.rs2_LEN.W))   //stride
  val in_md = Input(UInt(Consts.All_ADDR_LEN.W))  //告知寄存器编号
  val mtilem = Input(UInt(log2Ceil(Consts.tileM+1).W))    //用户配置m维度长度
  val mtilen = Input(UInt(log2Ceil(Consts.tileN+1).W))    //用户配置n维度长度
  val mtilek = Input(UInt(log2Ceil(Consts.tileK+1).W))    //用户配置k维度长度

  val sigDone = Output(Bool())    //结束信号
  val out_md = Output(UInt(Consts.All_ADDR_LEN.W))  //结束时用于回收
}