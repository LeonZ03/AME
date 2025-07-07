package Expander

import chisel3._
import chisel3.util._

import common._
import MMAU._


/*
  Expander模块作为顶层调度器，连接IssueArbiter和各执行单元（MMAU、MLU、MSU）。
  主要功能：
  1. 实例化并连接指令仲裁、执行单元和Scoreboard等子模块。
  2. 负责各模块间信号的转发与接口对接。
  3. 提供调试信号sigDone，便于仿真时监控各执行单元的完成状态。
*/


class Expander extends Module{
    val io = IO(new Bundle {
      val Uop_io = new Uop_IO
      val ScoreboardVisit_io = new ScoreboardVisit_IO


      val FSM_MMAU_io = Flipped(new FSM_MMAU_IO)
      val FSM_MLU_io = new FSM_MLU_IO
      val FSM_MSU_io = new FSM_MSU_IO

      val sigDone = Output(Bool())    //for debug
    })

    

    val subIssueArbiter = Module(new IssueArbiter)
    val subIssueMMAU = Module(new IssueMMAU)
    val subIssueMLU = Module(new IssueMLU)
    val subIssueMSU = Module(new IssueMSU)

    /*  for debug   */
    // io.sigDone := subIssueMMAU.io.IssueMMAU_Excute_io.sigDone
    // io.sigDone := subIssueMLU.io.IssueMLU_Excute_io.sigDone
    // io.sigDone := subIssueMSU.io.IssueMSU_Excute_io.sigDone

    io.sigDone := subIssueMMAU.io.IssueMMAU_Excute_io.sigDone || subIssueMLU.io.IssueMLU_Excute_io.sigDone || subIssueMSU.io.IssueMSU_Excute_io.sigDone

//debug 在出现subIssueMMAU、subIssueMLU、subIssueMSU任意sigdone为真时，打印其值
when(subIssueMMAU.io.IssueMMAU_Excute_io.sigDone || subIssueMLU.io.IssueMLU_Excute_io.sigDone || subIssueMSU.io.IssueMSU_Excute_io.sigDone) {
  printf("[IssueArbiter] subIssueMMAU.sigDone = %d\n", subIssueMMAU.io.IssueMMAU_Excute_io.sigDone)
  printf("[IssueArbiter] subIssueMLU.sigDone = %d\n", subIssueMLU.io.IssueMLU_Excute_io.sigDone)
  printf("[IssueArbiter] subIssueMSU.sigDone = %d\n", subIssueMSU.io.IssueMSU_Excute_io.sigDone)
}
    

//debug
printf(p"[IssueArbiter] sigDone = ${io.sigDone}\n") 

    /*  between Top and IssueArbiter */
    io.ScoreboardVisit_io <> subIssueArbiter.io.ScoreboardVisit_io
    io.Uop_io <> subIssueArbiter.io.Uop_io


    /*  between Top and IssueMMAU */
    io.FSM_MMAU_io <> subIssueMMAU.io.FSM_MMAU_io


    /*  between Top and IssueMLU */
    io.FSM_MLU_io <> subIssueMLU.io.FSM_MLU_io

    /*  between Top and IssueMSU */
    io.FSM_MSU_io <> subIssueMSU.io.FSM_MSU_io
    

    /*  between IssueArbiter and IssueMMAU */
    subIssueArbiter.io.IssueMMAU_Excute_io <> subIssueMMAU.io.IssueMMAU_Excute_io

    /*  between IssueArbiter and IssueMLU */
    subIssueArbiter.io.IssueMLU_Excute_io <> subIssueMLU.io.IssueMLU_Excute_io

    /*  between IssueArbiter and IssueMSU */
    subIssueArbiter.io.IssueMSU_Excute_io <> subIssueMSU.io.IssueMSU_Excute_io


    /*  between IssueMMAU and IssueMLU  */
    //nothing

    /*  between IssueMMAU and IssueMSU  */
    //nothing

    /*  between IssueMLU and IssueMSU  */
    //nothing





}