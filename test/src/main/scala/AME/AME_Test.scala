package AME

import chisel3._
import chisel3.experimental.BundleLiterals._
import chisel3.simulator.EphemeralSimulator._
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.must.Matchers
import scala.util.Random



import utility.sram._
import common._
import RegFile._
import MMAU._
import L2._


//AME完整测试，load A ,load B, mma指令混合,使用IssueQueen
class AMETest_allIns_usingQueen extends AnyFreeSpec with Matchers {

  "AMETest_allIns_usingQueen should PASS" in {
    simulate(new AME) { dut =>

      L2Sim.loadDataFrom(L2TestData.L2_Data, 0)//初始化L2

      
      
      dut.clock.step(1) //等待上电稳定


      // (AME , mtilem , mtilen , mtilek , ms1 , ms2 , md , rs1 , rs2 , valid , is_mmacc , is_mlbe8 , is_mlae8)


      AME.apply.IssueQueen_Push_noStep(dut = dut, mtilem = 64, mtilen = 64, mtilek = 256, md = 0, rs1 = 0, rs2 = 512, valid = true.B, is_mlbe8 = true.B) //load B
      AME.apply.load_ins_step(dut)  //step 1(load指令专用),由于这里L2是手动装填,所以不能用普通的step,否则会丢数据

      AME.apply.IssueQueen_Push_noStep(dut = dut, mtilem = 64, mtilen = 64, mtilek = 256, md = 1, rs1 = 0, rs2 = 512, valid = true.B, is_mlae8 = true.B) //load A
      AME.apply.load_ins_step(dut) 

      AME.apply.IssueQueen_Push_noStep(dut = dut, mtilem = 64, mtilen = 64, mtilek = 256, md = 4, rs1 = 0, rs2 = 512, valid = true.B, is_mlce32 = true.B)//load c
      AME.apply.load_ins_step(dut) 

      dut.io.Uop_io.ShakeHands_io.valid.poke(false.B)//结束
 
      var cycleCountMLU = 0


      while(!dut.io.sigDone.peek().litToBoolean){ //等到执行完毕,这里的sigDone是MMAU/MLU的
        AME.apply.load_ins_step(dut)
        cycleCountMLU += 1
      }

      AME.apply.load_ins_step(dut)
      cycleCountMLU += 1


      while(!dut.io.sigDone.peek().litToBoolean){ //等到执行完毕,这里的sigDone是MMAU/MLU的
        AME.apply.load_ins_step(dut)
        cycleCountMLU += 1
      }

      AME.apply.load_ins_step(dut)
      cycleCountMLU += 1


      while(!dut.io.sigDone.peek().litToBoolean){ //等到执行完毕,这里的sigDone是MMAU/MLU的
        AME.apply.load_ins_step(dut)
        cycleCountMLU += 1
      }

      // println(s"cycleCountReady = $cycleCountReady , cycleCountMLU = $cycleCountMLU")
      AME.apply.readTestDataFromAll(RFTestData.B, 0, dut)  //验证结果是否正确
      AME.apply.readTestDataFromAll(RFTestData.A, 1, dut)
      AME.apply.readTestDataFromAll(RFTestData.C, 4, dut)


    }
  }
}



//AME完整测试，仅测store C指令，使用IssueQueen
class AMETest_storeC_usingQueen extends AnyFreeSpec with Matchers {

  "AMETest_storeC_usingQueen should PASS" in {
    simulate(new AME) { dut =>

      L2Sim.loadDataFrom(L2TestData.L2_Data, 0)//初始化L2

      dut.clock.step(1) //等待上电稳定

      // AME.apply.writeTestDataToAll(RFTestData.C, 4, dut)  //外部手动写入C

      // AME.apply.readTestDataFromAll(RFTestData.C, 4, dut)


      // (AME , mtilem , mtilen , mtilek , ms1 , ms2 , md , rs1 , rs2 , valid , is_mmacc , is_mlbe8 , is_mlae8)
      AME.apply.IssueQueen_Push_noStep(dut = dut, mtilem = 30, mtilen = 30, mtilek = 200, md = 4, rs1 = 0, rs2 = 512, valid = true.B, is_mlce32 = true.B)  //全尺寸
      AME.apply.LS_check_step(dut)  //step 1(load指令专用),由于这里L2是手动装填,所以不能用普通的step,否则会丢数据

      AME.apply.IssueQueen_Push_noStep(dut = dut, mtilem = 30, mtilen = 30, mtilek = 200, md = 4, rs1 = 0, rs2 = 512, valid = true.B, is_msce32 = true.B)  //全尺寸
      AME.apply.LS_check_step(dut)  //step 1(load指令专用),由于这里L2是手动装填,所以不能用普通的step,否则会丢数据

      


      dut.io.Uop_io.ShakeHands_io.valid.poke(false.B)//结束
      
 
      var cycleCountMLU = 0


// println(s"ins 1 excuting")

      while(!dut.io.sigDone.peek().litToBoolean){ //等到执行完毕，第一次是MLU
        AME.apply.LS_check_step(dut)
        cycleCountMLU += 1
      }

      AME.apply.L2_store_check_step(dut)
      cycleCountMLU += 1

      while(!dut.io.sigDone.peek().litToBoolean){ //等到执行完毕，第二次是MSU
        AME.apply.LS_check_step(dut)
        cycleCountMLU += 1
      }

      


    }
  }
}


//AME完整测试，仅测load C指令，使用IssueQueen
class AMETest_loadC_usingQueen extends AnyFreeSpec with Matchers {

  "AMETest_loadC_usingQueen should PASS" in {
    simulate(new AME) { dut =>

      L2Sim.loadDataFrom(L2TestData.L2_Data, 0)//初始化L2

      dut.clock.step(1) //等待上电稳定


      // (AME , mtilem , mtilen , mtilek , ms1 , ms2 , md , rs1 , rs2 , valid , is_mmacc , is_mlbe8 , is_mlae8)
      AME.apply.IssueQueen_Push_noStep(dut = dut, mtilem = 64, mtilen = 64, mtilek = 256, md = 4, rs1 = 0, rs2 = 512, valid = true.B, is_mlce32 = true.B)  //全尺寸
      AME.apply.load_ins_step(dut)  //step 1(load指令专用),由于这里L2是手动装填,所以不能用普通的step,否则会丢数据

      AME.apply.IssueQueen_Push_noStep(dut = dut, mtilem = 64, mtilen = 64, mtilek = 256, md = 5, rs1 = 0, rs2 = 512, valid = true.B, is_mlce32 = true.B)  //全尺寸
      AME.apply.load_ins_step(dut)  //step 1(load指令专用),由于这里L2是手动装填,所以不能用普通的step,否则会丢数据

      AME.apply.IssueQueen_Push_noStep(dut = dut, mtilem = 64, mtilen = 64, mtilek = 256, md = 6, rs1 = 0, rs2 = 512, valid = true.B, is_mlce32 = true.B)  //全尺寸
      AME.apply.load_ins_step(dut)  //step 1(load指令专用),由于这里L2是手动装填,所以不能用普通的step,否则会丢数据


      dut.io.Uop_io.ShakeHands_io.valid.poke(false.B)//结束
      
 
      var cycleCountMLU = 0


// println(s"ins 1 excuting")

      while(!dut.io.sigDone.peek().litToBoolean){ //等到执行完毕
        AME.apply.load_ins_step(dut)
        cycleCountMLU += 1
      }

      AME.apply.load_ins_step(dut)
      cycleCountMLU += 1




      while(!dut.io.sigDone.peek().litToBoolean){ //等到执行完毕
        AME.apply.load_ins_step(dut)
        cycleCountMLU += 1
      }

      AME.apply.load_ins_step(dut)
      cycleCountMLU += 1




      while(!dut.io.sigDone.peek().litToBoolean){ //等到执行完毕
        AME.apply.load_ins_step(dut)
        cycleCountMLU += 1
      }


      AME.apply.readTestDataFromAll(RFTestData.C, 4, dut) //验证结果是否正确
      AME.apply.readTestDataFromAll(RFTestData.C, 5, dut) //验证结果是否正确
      AME.apply.readTestDataFromAll(RFTestData.C, 6, dut) //验证结果是否正确

    }
  }
}




// //AME完整测试，仅测load B指令
// class AMETest_loadB extends AnyFreeSpec with Matchers {

//   "AME_loadB should PASS" in {
//     simulate(new AME) { dut =>

//       L2Sim.loadDataFrom(L2TestData.L2_Data, 0)//初始化L2

//       // (AME , mtilem , mtilen , mtilek , ms1 , ms2 , md , rs1 , rs2 , valid , is_mmacc , is_mlbe8 , is_mlae8)
//       // AME.apply.AMEStart(dut, 64, 64, 256, 0, 0, 1, 0, 256, true.B, false.B, true.B) //全尺寸
//       // AME.apply.AMEStart(dut, 40, 40, 100, 0, 0, 1, 0, 256, true.B, false.B, true.B) //非全尺寸
//       AME.apply.AMEStart(dut, 35, 35, 100, 0, 0, 1, 0, 256, true.B, false.B, true.B , false.B) //非全尺寸
 
//       var cycleCountMLU = 0
//       var cycleCountReady = 0

//       while(!dut.io.Uop_io.ShakeHands_io.ready.peek().litToBoolean){ //等到ready
//         dut.clock.step(1)
//         cycleCountReady += 1
//       }

//       dut.clock.step(1) //ready后需要主动前进一个时钟周期

// println(s"ins 1 excuting")

//       // while(!dut.io.sigDone.peek().litToBoolean && cycleCountMLU < 500){ //等到执行完毕
//       // while(cycleCountMLU < 100){ //等到执行完毕
//       while(!dut.io.sigDone.peek().litToBoolean){ //等到执行完毕
//         for(i <- 0 until 8){//8条cacheline
//           if(dut.io.MLU_L2_io.Cacheline_Read_io(i).valid.peek().litToBoolean){//对L2读请求有意义
//             val addr_req = dut.io.MLU_L2_io.Cacheline_Read_io(i).addr.peek().litValue.toInt
//             val id_req = dut.io.MLU_L2_io.Cacheline_Read_io(i).id.peek().litValue.toInt

// println(s"addr_req = ${addr_req} , id_req = ${id_req}")

//             val (readData, id) = L2Sim.readLine(addr_req, id_req)
// println(f"readData = 0x${readData.litValue}%0128X, id = $id")
//             dut.io.MLU_L2_io.Cacheline_ReadBack_io(i).data.poke(readData)
//             dut.io.MLU_L2_io.Cacheline_ReadBack_io(i).id.poke(id)
//             dut.io.MLU_L2_io.Cacheline_ReadBack_io(i).valid.poke(true.B)
//           }else{
//             dut.io.MLU_L2_io.Cacheline_ReadBack_io(i).valid.poke(false.B)
//           }
//         }
        
//         dut.clock.step(1)
//         cycleCountMLU += 1
//       }

//       for(i <- 0 until 8){
//         dut.io.MLU_L2_io.Cacheline_ReadBack_io(i).valid.poke(false.B)
//       }


//       // println(s"cycleCountReady = $cycleCountReady , cycleCountMLU = $cycleCountMLU")
//       AME.apply.readTestDataFromAll(RFTestData.A, 1, dut) //验证结果是否正确


//     }
//   }
// }




//AME完整测试，仅测load B指令，使用IssueQueen
class AMETest_loadB_usingQueen extends AnyFreeSpec with Matchers {

  "AMETest_loadB_usingQueen should PASS" in {
    simulate(new AME) { dut =>

      L2Sim.loadDataFrom(L2TestData.L2_Data, 0)//初始化L2

      
      
      dut.clock.step(1) //等待上电稳定


      // (AME , mtilem , mtilen , mtilek , ms1 , ms2 , md , rs1 , rs2 , valid , is_mmacc , is_mlbe8 , is_mlae8)
      AME.apply.IssueQueen_Push_noStep(dut = dut, mtilem = 30, mtilen = 64, mtilek = 256, md = 0, rs1 = 0, rs2 = 512, valid = true.B, is_mlbe8 = true.B)  //全尺寸
      AME.apply.load_ins_step(dut)  //step 1(load指令专用),由于这里L2是手动装填,所以不能用普通的step,否则会丢数据


      // AME.apply.IssueQueen_Push_noStep(dut, 35, 35, 100, 0, 0, 1, 0, 256, true.B, false.B, true.B) //非全尺寸
      // AME.apply.load_ins_step(dut)  //step 1(load指令专用),由于这里L2是手动装填,所以不能用普通的step,否则会丢数据

      // AME.apply.IssueQueen_Push_noStep(dut, 35, 35, 100, 0, 0, 2, 0, 256, true.B, false.B, true.B) //非全尺寸
      // AME.apply.load_ins_step(dut)  //step 1(load指令专用),由于这里L2是手动装填,所以不能用普通的step,否则会丢数据

      // AME.apply.IssueQueen_Push_noStep(dut, 35, 35, 100, 0, 0, 3, 0, 256, true.B, false.B, true.B) //非全尺寸
      // AME.apply.load_ins_step(dut)  //step 1(load指令专用),由于这里L2是手动装填,所以不能用普通的step,否则会丢数据

      // AME.apply.IssueQueen_Push_noStep(dut, 35, 35, 100, 0, 0, 0, 0, 256, true.B, false.B, true.B) //非全尺寸
      // AME.apply.load_ins_step(dut)  //step 1(load指令专用),由于这里L2是手动装填,所以不能用普通的step,否则会丢数据

      // AME.apply.IssueQueen_Push_Step(dut, 35, 35, 100, 0, 0, 1, 0, 256, true.B, false.B, true.B)

      dut.io.Uop_io.ShakeHands_io.valid.poke(false.B)
      // AME.apply.IssueQueen_Push_Step(dut, 35, 35, 100, 0, 0, 2, 0, 256, false.B, false.B, false.B) //结束
 
      var cycleCountMLU = 0


println(s"ins 1 excuting")

      while(!dut.io.sigDone.peek().litToBoolean){ //等到执行完毕
        AME.apply.load_ins_step(dut)
        cycleCountMLU += 1
      }

//       AME.apply.load_ins_step(dut)

// println(s"ins 2 excuting")

//       while(!dut.io.sigDone.peek().litToBoolean){ //等到执行完毕
//         AME.apply.load_ins_step(dut)
//         cycleCountMLU += 1
//       }

//       AME.apply.load_ins_step(dut)

// println(s"ins 3 excuting")

//       while(!dut.io.sigDone.peek().litToBoolean){ //等到执行完毕
//         AME.apply.load_ins_step(dut)
//         cycleCountMLU += 1
//       }


//       AME.apply.load_ins_step(dut)

// println(s"ins 4 excuting")

//       while(!dut.io.sigDone.peek().litToBoolean){ //等到执行完毕
//         AME.apply.load_ins_step(dut)
//         cycleCountMLU += 1
//       }

      // println(s"cycleCountReady = $cycleCountReady , cycleCountMLU = $cycleCountMLU")
      AME.apply.readTestDataFromAll(RFTestData.B, 0, dut) //验证结果是否正确
      // AME.apply.readTestDataFromAll(RFTestData.A, 2, dut) //验证结果是否正确
      // AME.apply.readTestDataFromAll(RFTestData.A, 3, dut) //验证结果是否正确
      // AME.apply.readTestDataFromAll(RFTestData.A, 0, dut) //验证结果是否正确


    }
  }
}



//AME完整测试，仅测load A指令，使用IssueQueen
class AMETest_loadA_usingQueen extends AnyFreeSpec with Matchers {

  "AMETest_loadA_usingQueen should PASS" in {
    simulate(new AME) { dut =>

      L2Sim.loadDataFrom(L2TestData.L2_Data, 0)//初始化L2

      
      
      dut.clock.step(1) //等待上电稳定


      // (AME , mtilem , mtilen , mtilek , ms1 , ms2 , md , rs1 , rs2 , valid , is_mmacc , is_mlbe8 , is_mlae8)

      AME.apply.IssueQueen_Push_noStep(dut = dut, mtilem = 30, mtilen = 64, mtilek = 256, md = 0, rs1 = 0, rs2 = 512, valid = true.B, is_mlae8 = true.B)
      AME.apply.load_ins_step(dut)  //step 1(load指令专用),由于这里L2是手动装填,所以不能用普通的step,否则会丢数据

      // AME.apply.IssueQueen_Push_noStep(dut, 64, 30, 256, 0, 0, 2, 0, 256, true.B, false.B, false.B , true.B  ) //全尺寸
      // AME.apply.load_ins_step(dut)  //step 1(load指令专用),由于这里L2是手动装填,所以不能用普通的step,否则会丢数据

      // AME.apply.IssueQueen_Push_noStep(dut, 64, 30, 256, 0, 0, 3, 0, 256, true.B, false.B, false.B , true.B  ) //全尺寸
      // AME.apply.load_ins_step(dut)  //step 1(load指令专用),由于这里L2是手动装填,所以不能用普通的step,否则会丢数据


      // AME.apply.IssueQueen_Push_noStep(dut, 35, 35, 100, 0, 0, 1, 0, 256, true.B, false.B, true.B) //非全尺寸
      // AME.apply.load_ins_step(dut)  //step 1(load指令专用),由于这里L2是手动装填,所以不能用普通的step,否则会丢数据

      // AME.apply.IssueQueen_Push_noStep(dut, 35, 35, 100, 0, 0, 2, 0, 256, true.B, false.B, true.B) //非全尺寸
      // AME.apply.load_ins_step(dut)  //step 1(load指令专用),由于这里L2是手动装填,所以不能用普通的step,否则会丢数据

      // AME.apply.IssueQueen_Push_noStep(dut, 35, 35, 100, 0, 0, 3, 0, 256, true.B, false.B, true.B) //非全尺寸
      // AME.apply.load_ins_step(dut)  //step 1(load指令专用),由于这里L2是手动装填,所以不能用普通的step,否则会丢数据

      // AME.apply.IssueQueen_Push_noStep(dut, 35, 35, 100, 0, 0, 0, 0, 256, true.B, false.B, true.B) //非全尺寸
      // AME.apply.load_ins_step(dut)  //step 1(load指令专用),由于这里L2是手动装填,所以不能用普通的step,否则会丢数据

      // AME.apply.IssueQueen_Push_Step(dut, 35, 35, 100, 0, 0, 1, 0, 256, true.B, false.B, true.B)

      dut.io.Uop_io.ShakeHands_io.valid.poke(false.B)
      // AME.apply.IssueQueen_Push_Step(dut, 35, 35, 100, 0, 0, 2, 0, 256, false.B, false.B, false.B) //结束
 
      var cycleCountMLU = 0


println(s"ins 1 excuting")

      while(!dut.io.sigDone.peek().litToBoolean){ //等到执行完毕
        AME.apply.load_ins_step(dut)
        cycleCountMLU += 1
      }

//       AME.apply.load_ins_step(dut)

// println(s"ins 2 excuting")

//       while(!dut.io.sigDone.peek().litToBoolean){ //等到执行完毕
//         AME.apply.load_ins_step(dut)
//         cycleCountMLU += 1
//       }

//       AME.apply.load_ins_step(dut)

// println(s"ins 3 excuting")

//       while(!dut.io.sigDone.peek().litToBoolean){ //等到执行完毕
//         AME.apply.load_ins_step(dut)
//         cycleCountMLU += 1
//       }


//       AME.apply.load_ins_step(dut)

// println(s"ins 4 excuting")

//       while(!dut.io.sigDone.peek().litToBoolean){ //等到执行完毕
//         AME.apply.load_ins_step(dut)
//         cycleCountMLU += 1
//       }

      // println(s"cycleCountReady = $cycleCountReady , cycleCountMLU = $cycleCountMLU")
      AME.apply.readTestDataFromAll(RFTestData.A, 0, dut) //验证结果是否正确
      // AME.apply.readTestDataFromAll(RFTestData.A, 2, dut) //验证结果是否正确
      // AME.apply.readTestDataFromAll(RFTestData.A, 3, dut) //验证结果是否正确
      // AME.apply.readTestDataFromAll(RFTestData.A, 0, dut) //验证结果是否正确


    }
  }
}




//AME完整测试，仅测mma指令
class AMETest_mma extends AnyFreeSpec with Matchers {

  "AME_mma should PASS" in {
    simulate(new AME) { dut =>
    
      
      /*  提前手动写入A、B、C*/
      AME.apply.writeTestDataToAll(AMETestData.A, 0, dut)
      AME.apply.writeTestDataToAll(AMETestData.B, 1, dut)
      AME.apply.writeTestDataToAll(AMETestData.Ctmp, 4, dut)
      AME.apply.writeTestDataToAll(AMETestData.Ctmp, 5, dut)
      AME.apply.writeTestDataToAll(AMETestData.Ctmp, 6, dut)
      AME.apply.writeTestDataToAll(AMETestData.Ctmp, 7, dut)

      dut.clock.step(1000) //随便跑几个cycle

      



      // // ins1
      var cycleCountMMAU = 0
      var cycleCountReady = 0

      AME.apply.AMEStart(dut, 32, 32, 64, 0, 1, 4, 0, 0, true.B, true.B, false.B , false.B)  //启动AME，配置矩阵形状，确定操作数矩阵标号（ABC标号范围均是0～7)

      while(!dut.io.Uop_io.ShakeHands_io.ready.peek().litToBoolean){ //等到ready
        dut.clock.step(1)
        cycleCountReady += 1
      }

      dut.clock.step(1) //ready后需要主动前进一个时钟周期

println(s"ins 1 excuting")


      AME.apply.AMEStart(dut, 32, 32, 64, 0, 1, 5, 0, 0, true.B, true.B, false.B , false.B)  //下一条指令ins2，应该没有响应


      while(!dut.io.sigDone.peek().litToBoolean){ //等到执行完毕
        dut.clock.step(1)
        cycleCountMMAU += 1
      }


      println(s"cycleCountReady = $cycleCountReady , cycleCountMMAU = $cycleCountMMAU")


      // ins2

      cycleCountReady = 0
      cycleCountMMAU = 0  //计数清零


      // AME.apply.AMEStart(dut, 32, 32, 64, 0, 1, 5, true.B, true.B)  //下一条指令，换一个C

      while(!dut.io.Uop_io.ShakeHands_io.ready.peek().litToBoolean){ //等到ready
        dut.clock.step(1)
        cycleCountReady += 1
      }

      dut.clock.step(1) //ready后需要主动前进一个时钟周期

println(s"ins 2 excuting")

      AME.apply.AMEStart(dut, 32, 32, 64, 0, 1, 6, 0, 0, true.B, true.B, false.B , false.B)  //下一条指令ins3，应该没有响应

      while(!dut.io.sigDone.peek().litToBoolean){ //等到执行完毕
        dut.clock.step(1)
        cycleCountMMAU += 1
      }

      println(s"cycleCountReady = $cycleCountReady , cycleCountMMAU = $cycleCountMMAU")


      // ins3

      cycleCountReady = 0
      cycleCountMMAU = 0  //计数清零


      // AME.apply.AMEStart(dut, 32, 32, 64, 0, 1, 6, true.B, true.B)  //下一条指令，换一个C

      while(!dut.io.Uop_io.ShakeHands_io.ready.peek().litToBoolean){ //等到ready
        dut.clock.step(1)
        cycleCountReady += 1
      }

      dut.clock.step(1) //ready后需要主动前进一个时钟周期

println(s"ins 3 excuting")

      while(!dut.io.sigDone.peek().litToBoolean){ //等到执行完毕
        dut.clock.step(1)
        cycleCountMMAU += 1
      }

      println(s"cycleCountReady = $cycleCountReady , cycleCountMMAU = $cycleCountMMAU")



      

      AME.apply.AMEStop(dut)  //停止，无效指令，valid = false

      AME.apply.readTestDataFromAll(AMETestData.C, 4, dut) //验证结果是否正确
      AME.apply.readTestDataFromAll(AMETestData.C, 5, dut) //验证结果是否正确
      AME.apply.readTestDataFromAll(AMETestData.C, 6, dut) //验证结果是否正确
      // AME.apply.readTestDataFromAll(AMETestData.C, 7, dut) //验证结果是否正确


 


    }
  }
}




//AME完整测试，仅测mma指令
class AMETest_mma_usingQueen extends AnyFreeSpec with Matchers {

  "AME_mma should PASS" in {
    simulate(new AME) { dut =>
    
      
      /*  提前手动写入A、B、C*/
      AME.apply.writeTestDataToAll(AMETestData.A, 0, dut)
      AME.apply.writeTestDataToAll(AMETestData.B, 1, dut)
      AME.apply.writeTestDataToAll(AMETestData.Ctmp, 4, dut)
      AME.apply.writeTestDataToAll(AMETestData.Ctmp, 5, dut)
      AME.apply.writeTestDataToAll(AMETestData.Ctmp, 6, dut)
      AME.apply.writeTestDataToAll(AMETestData.Ctmp, 7, dut)

      dut.clock.step(1000) //随便跑几个cycle

      



      // // ins1
      var cycleCountMMAU = 0
      var cycleCountReady = 0

      AME.apply.IssueQueen_Push_Step(dut, 32, 32, 64, 0, 1, 4, 0, 0, true.B, true.B, false.B , false.B)  //启动AME，配置矩阵形状，确定操作数矩阵标号（ABC标号范围均是0～7)
      // AME.apply.IssueQueen_Push_Step(dut, 32, 32, 64, 0, 1, 5, 0, 0, true.B, true.B, false.B)  //下一条指令
      // AME.apply.IssueQueen_Push_Step(dut, 32, 32, 64, 0, 1, 6, 0, 0, true.B, true.B, false.B)  //下一条指令

      // AME.apply.IssueQueen_Push_Step(dut, 32, 32, 64, 0, 1, 6, 0, 0, false.B, false.B, false.B)  //结束
      dut.io.Uop_io.ShakeHands_io.valid.poke(false.B)   //结束


      while(!dut.io.sigDone.peek().litToBoolean){ //等到执行完毕，此时只有第1条指令执行完毕
        dut.clock.step(1)
        cycleCountMMAU += 1
      }

      println(s"cycleCountMMAU = $cycleCountMMAU")

      // dut.clock.step(1)
      // cycleCountMMAU += 1


      // while(!dut.io.sigDone.peek().litToBoolean){ //等到执行完毕，此时只有第2条指令执行完毕
      //   dut.clock.step(1)
      //   cycleCountMMAU += 1

      //   // println(s"111")
      // }


      // dut.clock.step(1)
      // cycleCountMMAU += 1


      // while(!dut.io.sigDone.peek().litToBoolean){ //等到执行完毕，此时只有第3条指令执行完毕
      //   dut.clock.step(1)
      //   cycleCountMMAU += 1

      //   // println(s"222")
      // }




      AME.apply.readTestDataFromAll(AMETestData.C, 4, dut) //验证结果是否正确
      AME.apply.readTestDataFromAll(AMETestData.C, 5, dut) //验证结果是否正确
      AME.apply.readTestDataFromAll(AMETestData.C, 6, dut) //验证结果是否正确
      // AME.apply.readTestDataFromAll(AMETestData.C, 7, dut) //验证结果是否正确


 


    }
  }
}

