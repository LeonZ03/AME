package AME

import chisel3._
import chisel3.experimental.BundleLiterals._
import chisel3.simulator.EphemeralSimulator._
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.must.Matchers
import scala.util.Random

import common._
import L2._




object apply {

  // // 写入 Tr 数据
  // def writeTestDataToTr(testData: Seq[Seq[UInt]], trAddr: Int, dut: AME): Unit = {
  //   // 激活写端口
  //   dut.io.writeTr.act.poke(true.B)
  //   dut.io.writeTr.addr.poke(trAddr.U) // 定位到目标 Tr 地址

  //   for ((bankData, bankIdx) <- testData.zipWithIndex) { // 对每个 bank 进行写操作
  //     for ((data, setIdx) <- bankData.zipWithIndex) {
  //       // 写入 setIdx 和 data 到对应的 bank
  //       dut.io.writeTr.w(bankIdx).req.bits.setIdx.poke(setIdx.U)
  //       dut.io.writeTr.w(bankIdx).req.bits.data.head.poke(data)
  //       dut.io.writeTr.w(bankIdx).req.valid.poke(true.B)

  //       dut.clock.step() // 等待一个 cycle

  //       dut.io.writeTr.w(bankIdx).req.valid.poke(false.B)
  //     }
  //   }

  //   // 注销写端口
  //   dut.io.writeTr.act.poke(false.B)
  // }

  // // 写入 Acc 数据
  // def writeTestDataToAcc(testData: Seq[Seq[UInt]], accAddr: Int, dut: AME): Unit = {
  //   // 激活写端口
  //   dut.io.writeAcc.act.poke(true.B)
  //   dut.io.writeAcc.addr.poke(accAddr.U) // 定位到目标 Acc 地址

  //   for ((bankData, bankIdx) <- testData.zipWithIndex) { // 对每个 bank 进行写操作
  //     for ((data, setIdx) <- bankData.zipWithIndex) {
  //       dut.io.writeAcc.w(bankIdx).req.bits.setIdx.poke(setIdx.U)
  //       dut.io.writeAcc.w(bankIdx).req.bits.data.head.poke(data)
  //       dut.io.writeAcc.w(bankIdx).req.valid.poke(true.B)

  //       dut.clock.step() // 等待一个 cycle

  //       dut.io.writeAcc.w(bankIdx).req.valid.poke(false.B)
  //     }
  //   }

  //   // 注销写端口
  //   dut.io.writeAcc.act.poke(false.B)
  // }

  // 写入 All 数据
  def writeTestDataToAll(testData: Seq[Seq[UInt]], allAddr: Int, dut: AME): Unit = {
    // 激活写端口
    dut.io.writeAll.act.poke(true.B)
    dut.io.writeAll.addr.poke(allAddr.U) // 定位到目标 Reg 地址

    for ((bankData, bankIdx) <- testData.zipWithIndex) { // 对每个 bank 进行写操作
      for ((data, setIdx) <- bankData.zipWithIndex) {
        // 写入 setIdx 和 data 到对应的 bank
        dut.io.writeAll.w(bankIdx).req.bits.setIdx.poke(setIdx.U)
        dut.io.writeAll.w(bankIdx).req.bits.data.head.poke(data)
        dut.io.writeAll.w(bankIdx).req.valid.poke(true.B)

        dut.clock.step() // 等待一个 cycle

        dut.io.writeAll.w(bankIdx).req.valid.poke(false.B)
      }
    }

    // 注销写端口
    dut.io.writeAll.act.poke(false.B)
  }


  // // 读取 Tr 数据
  // def readTestDataFromTr(expectData: Seq[Seq[UInt]], trAddr: Int, dut: AME): Unit = {
  //   dut.io.readTr.act.poke(true.B) // 复用写接口作为读接口，或者你有独立读接口就改成对应的端口
  //   dut.io.readTr.addr.poke(trAddr.U) // 定位到目标 Tr 地址
  //   println(s"Reading Tr address: $trAddr")

  //   for ((bankData, bankIdx) <- expectData.zipWithIndex) {
  //     for ((data, setIdx) <- bankData.zipWithIndex) {
  //       dut.io.readTr.r(bankIdx).req.bits.setIdx.poke(setIdx.U)
  //       dut.io.readTr.r(bankIdx).req.valid.poke(true.B)

  //       dut.clock.step() // 等待一个 cycle

  //       val readValue = dut.io.readTr.r(bankIdx).resp.data.head.asUInt.peek()
  //       println(s"Bank $bankIdx, Set $setIdx - Read value: ${readValue.litValue}, Expected: ${data.litValue}")

  //       dut.io.readTr.r(bankIdx).resp.data.head.asUInt.expect(data)

  //       dut.io.readTr.r(bankIdx).req.valid.poke(false.B)
  //     }
  //   }

  //   dut.io.readTr.act.poke(false.B)
  // }

  // // 读取 Acc 数据
  // def readTestDataFromAcc(expectData: Seq[Seq[UInt]], accAddr: Int, dut: AME): Unit = {
  //   dut.io.readAcc.act.poke(true.B)
  //   dut.io.readAcc.addr.poke(accAddr.U)
  //   println(s"Reading Acc address: $accAddr")

  //   for ((bankData, bankIdx) <- expectData.zipWithIndex) {
  //     for ((data, setIdx) <- bankData.zipWithIndex) {
  //       dut.io.readAcc.r(bankIdx).req.bits.setIdx.poke(setIdx.U)
  //       dut.io.readAcc.r(bankIdx).req.valid.poke(true.B)

  //       dut.clock.step() // 等待一个 cycle

  //       val readValue = dut.io.readAcc.r(bankIdx).resp.data.head.asUInt.peek()
  //       println(s"Bank $bankIdx, Set $setIdx - Read value: ${readValue.litValue}, Expected: ${data.litValue}")

  //       dut.io.readAcc.r(bankIdx).resp.data.head.asUInt.expect(data)

  //       dut.io.readAcc.r(bankIdx).req.valid.poke(false.B)
  //     }
  //   }

  //   dut.io.readAcc.act.poke(false.B)
  // }

  


  // 读取 All 数据
  def readTestDataFromAll(expectData: Seq[Seq[UInt]], allAddr: Int, dut: AME): Unit = {
    dut.io.readAll.act.poke(true.B) // 复用写接口作为读接口，或者你有独立读接口就改成对应的端口
    dut.io.readAll.addr.poke(allAddr.U) // 定位到目标 Tr 地址
    println(s"Reading \"all\" address: $allAddr")

    for ((bankData, bankIdx) <- expectData.zipWithIndex) {
      for ((data, setIdx) <- bankData.zipWithIndex) {
        dut.io.readAll.r(bankIdx).req.bits.setIdx.poke(setIdx.U)
        dut.io.readAll.r(bankIdx).req.valid.poke(true.B)

        dut.clock.step() // 等待一个 cycle

        val readValue = dut.io.readAll.r(bankIdx).resp.data.head.asUInt.peek()
        val status = if (readValue.litValue == data.litValue) "PASS" else "FAILED"
        // println(s"Bank $bankIdx, Set $setIdx - Read value: ${readValue.litValue}, Expected: ${data.litValue} [$status]") //十进制
        println(f"Bank $bankIdx, Set $setIdx - Read value: 0x${readValue.litValue.toString(16)}%s, Expected: 0x${data.litValue.toString(16)}%s [$status]") //十六进制

        // dut.io.readAll.r(bankIdx).resp.data.head.asUInt.expect(data)

        dut.io.readAll.r(bankIdx).req.valid.poke(false.B)
      }
    }

    dut.io.readAll.act.poke(false.B)
  }








  /*  完整Expander版本   */
  def AMEStart(
    dut: AME,
    mtilem: Int = 0,
    mtilen: Int = 0,
    mtilek: Int = 0,
    ms1: Int = 0,
    ms2: Int = 0,
    md: Int = 0,
    rs1: Int = 0,
    rs2: Int = 0,
    valid: Bool = false.B,
    is_mmacc: Bool = false.B,
    is_mlbe8: Bool = false.B,
    is_mlae8: Bool = false.B,
    is_mlce32: Bool = false.B,
    is_msce32: Bool = false.B
  ): Unit = {  //启动AME，配置矩阵形状，确定操作数矩阵标号（ABC标号范围均是0～7)

    // dut.io.sigStart.poke(true.B)  //启动信号

    // 输入用户配置尺寸
    dut.io.Uop_io.mtileConfig_io.mtilem.poke(mtilem.U)
    dut.io.Uop_io.mtileConfig_io.mtilen.poke(mtilen.U)
    dut.io.Uop_io.mtileConfig_io.mtilek.poke(mtilek.U)

    // 确定操作数矩阵
    dut.io.Uop_io.Operands_io.ms1.poke(ms1.U)
    dut.io.Uop_io.Operands_io.ms2.poke(ms2.U)
    dut.io.Uop_io.Operands_io.md.poke(md.U)
    dut.io.Uop_io.Operands_io.rs1.poke(rs1.U)
    dut.io.Uop_io.Operands_io.rs2.poke(rs2.U)

    //valid信号
    dut.io.Uop_io.ShakeHands_io.valid.poke(valid)

    //InsType_io信号
    dut.io.Uop_io.InsType_io.is_mmacc.poke(is_mmacc)
    dut.io.Uop_io.InsType_io.is_mlbe8.poke(is_mlbe8)
    dut.io.Uop_io.InsType_io.is_mlae8.poke(is_mlae8)
    dut.io.Uop_io.InsType_io.is_mlce32.poke(is_mlce32)
    dut.io.Uop_io.InsType_io.is_msce32.poke(is_msce32)

    // dut.clock.step(1)

    // dut.io.sigStart.poke(false.B)
  }

  def AMEStop(dut: AME): Unit = {
    AMEStart(dut, 0, 0, 0, 0, 0, 0, 0, 0, false.B, false.B, false.B , false.B)
  }


  // /*  无Expander版本   */
  // def AMEStart(dut: AME , mtilem: Int , mtilen: Int , mtilek: Int , A: Int , B: Int , C: Int): Unit = {  //启动AME，配置矩阵形状，确定操作数矩阵标号（ABC标号范围均是0～7)
  //   dut.io.sigStart.poke(true.B)  //启动信号

  //   // 输入用户配置尺寸
  //   dut.io.mtileConfig_io.mtilem.poke(mtilem.U)
  //   dut.io.mtileConfig_io.mtilen.poke(mtilen.U)
  //   dut.io.mtileConfig_io.mtilek.poke(mtilek.U)

  //   // 确定操作数矩阵
  //   dut.io.Operands_io.ms1.poke(A.U)
  //   dut.io.Operands_io.ms2.poke(B.U)
  //   dut.io.Operands_io.md.poke(C.U)

  //   dut.clock.step(1)

  //   dut.io.sigStart.poke(false.B)
  // }




  /*  使用IssueQueen push   */
  //push进IssueQueen,同时step 1
  def IssueQueen_Push_Step(
    dut: AME,
    mtilem: Int = 0,
    mtilen: Int = 0,
    mtilek: Int = 0,
    ms1: Int = 0,
    ms2: Int = 0,
    md: Int = 0,
    rs1: Int = 0,
    rs2: Int = 0,
    valid: Bool = false.B,
    is_mmacc: Bool = false.B,
    is_mlbe8: Bool = false.B,
    is_mlae8: Bool = false.B,
    is_mlce32: Bool = false.B,
    is_msce32: Bool = false.B
  ): Unit = {  
    // dut.io.sigStart.poke(true.B)  //启动信号

    // 输入用户配置尺寸
    dut.io.Uop_io.mtileConfig_io.mtilem.poke(mtilem.U)
    dut.io.Uop_io.mtileConfig_io.mtilen.poke(mtilen.U)
    dut.io.Uop_io.mtileConfig_io.mtilek.poke(mtilek.U)

    // 确定操作数矩阵
    dut.io.Uop_io.Operands_io.ms1.poke(ms1.U)
    dut.io.Uop_io.Operands_io.ms2.poke(ms2.U)
    dut.io.Uop_io.Operands_io.md.poke(md.U)
    dut.io.Uop_io.Operands_io.rs1.poke(rs1.U)
    dut.io.Uop_io.Operands_io.rs2.poke(rs2.U)

    //valid信号
    dut.io.Uop_io.ShakeHands_io.valid.poke(valid)

    //InsType_io信号
    dut.io.Uop_io.InsType_io.is_mmacc.poke(is_mmacc)
    dut.io.Uop_io.InsType_io.is_mlbe8.poke(is_mlbe8)
    dut.io.Uop_io.InsType_io.is_mlae8.poke(is_mlae8)
    dut.io.Uop_io.InsType_io.is_mlce32.poke(is_mlce32)
    dut.io.Uop_io.InsType_io.is_msce32.poke(is_msce32)

    dut.clock.step(1) //前进一个时钟,IssueQueen更新

    // dut.io.sigStart.poke(false.B)
  }

  //push进IssueQueen,不step 1
  def IssueQueen_Push_noStep(
    dut: AME,
    mtilem: Int = 0,
    mtilen: Int = 0,
    mtilek: Int = 0,
    ms1: Int = 0,
    ms2: Int = 0,
    md: Int = 0,
    rs1: Int = 0,
    rs2: Int = 0,
    valid: Bool = false.B,
    is_mmacc: Bool = false.B,
    is_mlbe8: Bool = false.B,
    is_mlae8: Bool = false.B,
    is_mlce32: Bool = false.B,
    is_msce32: Bool = false.B
  ): Unit = {  
    
    // 输入用户配置尺寸
    dut.io.Uop_io.mtileConfig_io.mtilem.poke(mtilem.U)
    dut.io.Uop_io.mtileConfig_io.mtilen.poke(mtilen.U)
    dut.io.Uop_io.mtileConfig_io.mtilek.poke(mtilek.U)

    // 确定操作数矩阵
    dut.io.Uop_io.Operands_io.ms1.poke(ms1.U)
    dut.io.Uop_io.Operands_io.ms2.poke(ms2.U)
    dut.io.Uop_io.Operands_io.md.poke(md.U)
    dut.io.Uop_io.Operands_io.rs1.poke(rs1.U)
    dut.io.Uop_io.Operands_io.rs2.poke(rs2.U)

    //valid信号
    dut.io.Uop_io.ShakeHands_io.valid.poke(valid)

    //InsType_io信号
    dut.io.Uop_io.InsType_io.is_mmacc.poke(is_mmacc)
    dut.io.Uop_io.InsType_io.is_mlbe8.poke(is_mlbe8)
    dut.io.Uop_io.InsType_io.is_mlae8.poke(is_mlae8)
    dut.io.Uop_io.InsType_io.is_mlce32.poke(is_mlce32)
    dut.io.Uop_io.InsType_io.is_msce32.poke(is_msce32)

  }


  /*  MLU   */
  //手动从L2读进AME，同时前进一个时钟
  def load_ins_step(dut: AME): Unit = {
    for(i <- 0 until 8){//8条cacheline
          dut.io.MLU_L2_io.Cacheline_Read_io(i).ready.poke(true.B)  //L2始终就绪

          if(dut.io.MLU_L2_io.Cacheline_Read_io(i).valid.peek().litToBoolean){//对L2读请求有意义
            val addr_req = dut.io.MLU_L2_io.Cacheline_Read_io(i).addr.peek().litValue.toInt
            val id_req = dut.io.MLU_L2_io.Cacheline_Read_io(i).id.peek().litValue.toInt

// println(s"addr_req = ${addr_req} , id_req = ${id_req}")

            val (readData, id) = L2Sim.readLine(addr_req, id_req)
// println(f"readData = 0x${readData.litValue}%0128X, id = $id")
            dut.io.MLU_L2_io.Cacheline_ReadBack_io(i).data.poke(readData)
            dut.io.MLU_L2_io.Cacheline_ReadBack_io(i).id.poke(id)
            dut.io.MLU_L2_io.Cacheline_ReadBack_io(i).valid.poke(true.B)
          }else{
            dut.io.MLU_L2_io.Cacheline_ReadBack_io(i).valid.poke(false.B)
          }
        }
        
        dut.clock.step(1)


        for(i <- 0 until 8){  //最后validq确保false,防止误读L2数据
        dut.io.MLU_L2_io.Cacheline_ReadBack_io(i).valid.poke(false.B)
      }
  }



  def L2_store_check_step(dut: AME ): Unit = {//检查AME写进L2的数据是否正确

    for(i <- 0 until 2){//两条cacheline
      dut.io.MSU_L2_io.Cacheline_Write_io(i).ready.poke(true.B)  //L2始终就绪

      if(dut.io.MSU_L2_io.Cacheline_Write_io(i).valid.peek().litToBoolean){//写L2数据有效
        val addr_w = dut.io.MSU_L2_io.Cacheline_Write_io(i).addr.peek().litValue.toInt //尝试写入的地址
        val data_w = dut.io.MSU_L2_io.Cacheline_Write_io(i).data.peek().litValue  //尝试写入的数据

        val (l2DataUInt, id) = L2Sim.readLine(addr_w, 0)
        val l2Data = l2DataUInt.litValue  // 从 UInt(512.W) 取 BigInt

        val status = if (l2Data == data_w) "PASS" else "FAILED"

        
        println(s"[L2 WRITE CHECK] addr = $addr_w, data_w = 0x${data_w.toString(16)}, l2Data = 0x${l2Data.toString(16)} [$status]")
        
        dut.io.MSU_L2_io.Cacheline_WriteBack_io(i).valid.poke(true.B)

      }else{
        dut.io.MSU_L2_io.Cacheline_WriteBack_io(i).valid.poke(false.B)
      }
    }

    dut.clock.step(1)
    
  }

  def LS_check_step(dut: AME ): Unit = {//包含load指令和store指令,同时会检查store的正确性

    //Load
    for(i <- 0 until 8){//8条cacheline
          dut.io.MLU_L2_io.Cacheline_Read_io(i).ready.poke(true.B)  //L2始终就绪

          if(dut.io.MLU_L2_io.Cacheline_Read_io(i).valid.peek().litToBoolean){//对L2读请求有意义
            val addr_req = dut.io.MLU_L2_io.Cacheline_Read_io(i).addr.peek().litValue.toInt
            val id_req = dut.io.MLU_L2_io.Cacheline_Read_io(i).id.peek().litValue.toInt

// println(s"addr_req = ${addr_req} , id_req = ${id_req}")

            val (readData, id) = L2Sim.readLine(addr_req, id_req)
// println(f"readData = 0x${readData.litValue}%0128X, id = $id")
            dut.io.MLU_L2_io.Cacheline_ReadBack_io(i).data.poke(readData)
            dut.io.MLU_L2_io.Cacheline_ReadBack_io(i).id.poke(id)
            dut.io.MLU_L2_io.Cacheline_ReadBack_io(i).valid.poke(true.B)
          }else{
            dut.io.MLU_L2_io.Cacheline_ReadBack_io(i).valid.poke(false.B)
          }
        }

    //Store
    for(i <- 0 until 2){//两条cacheline
      dut.io.MSU_L2_io.Cacheline_Write_io(i).ready.poke(true.B)  //L2始终就绪

      if(dut.io.MSU_L2_io.Cacheline_Write_io(i).valid.peek().litToBoolean){//写L2数据有效
        val addr_w = dut.io.MSU_L2_io.Cacheline_Write_io(i).addr.peek().litValue.toInt //尝试写入的地址
        val data_w = dut.io.MSU_L2_io.Cacheline_Write_io(i).data.peek().litValue  //尝试写入的数据

        val (l2DataUInt, id) = L2Sim.readLine(addr_w, 0)
        val l2Data = l2DataUInt.litValue  // 从 UInt(512.W) 取 BigInt

        val status = if (l2Data == data_w) "PASS" else "FAILED"

        
        println(s"[L2 WRITE CHECK] addr = $addr_w, data_w = 0x${data_w.toString(16)}, l2Data = 0x${l2Data.toString(16)} [$status]")
        
        dut.io.MSU_L2_io.Cacheline_WriteBack_io(i).valid.poke(true.B)

      }else{
        dut.io.MSU_L2_io.Cacheline_WriteBack_io(i).valid.poke(false.B)
      }
    }


    dut.clock.step(1)



    for(i <- 0 until 8){  //最后validq确保false,防止误读L2数据
        dut.io.MLU_L2_io.Cacheline_ReadBack_io(i).valid.poke(false.B)
    }
  }

  def LS_check_steps(dut: AME , steps: Int): Unit = { //前进多个时钟，包含load指令和store指令,同时会检查store的正确性
    for(_ <- 0 until steps){
      LS_check_step(dut)
    }
  }

}



