package Expander

import chisel3._
import chisel3.util._

import common._
import MMAU._



class Expander extends Module{
    val io = IO(new Bundle {
      val Uop_io = new Uop_IO
      val ScoreboardVisit_io = new ScoreboardVisit_IO


      val FSM_MMAU_io = Flipped(new FSM_MMAU_IO)
      val FSM_MLU_io = new FSM_MLU_IO
      val FSM_MSU_io = new FSM_MSU_IO

      val sigDone = Output(Bool())    //for debug
    })

    

    val subExcuteHandler = Module(new ExcuteHandler)
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
  printf("[ExcuteHandler] subIssueMMAU.sigDone = %d\n", subIssueMMAU.io.IssueMMAU_Excute_io.sigDone)
  printf("[ExcuteHandler] subIssueMLU.sigDone = %d\n", subIssueMLU.io.IssueMLU_Excute_io.sigDone)
  printf("[ExcuteHandler] subIssueMSU.sigDone = %d\n", subIssueMSU.io.IssueMSU_Excute_io.sigDone)
}
    

//debug
printf(p"[ExcuteHandler] sigDone = ${io.sigDone}\n") 

    /*  between Top and ExcuteHandler */
    io.ScoreboardVisit_io <> subExcuteHandler.io.ScoreboardVisit_io
    io.Uop_io <> subExcuteHandler.io.Uop_io


    /*  between Top and IssueMMAU */
    io.FSM_MMAU_io <> subIssueMMAU.io.FSM_MMAU_io


    /*  between Top and IssueMLU */
    io.FSM_MLU_io <> subIssueMLU.io.FSM_MLU_io

    /*  between Top and IssueMSU */
    io.FSM_MSU_io <> subIssueMSU.io.FSM_MSU_io
    

    /*  between ExcuteHandler and IssueMMAU */
    subExcuteHandler.io.IssueMMAU_Excute_io <> subIssueMMAU.io.IssueMMAU_Excute_io

    /*  between ExcuteHandler and IssueMLU */
    subExcuteHandler.io.IssueMLU_Excute_io <> subIssueMLU.io.IssueMLU_Excute_io

    /*  between ExcuteHandler and IssueMSU */
    subExcuteHandler.io.IssueMSU_Excute_io <> subIssueMSU.io.IssueMSU_Excute_io


    /*  between IssueMMAU and IssueMLU  */
    //nothing

    /*  between IssueMMAU and IssueMSU  */
    //nothing

    /*  between IssueMLU and IssueMSU  */
    //nothing





}