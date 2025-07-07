package AME

import chisel3._
import chisel3.util._

import utility.sram._
import common._
import RegFile._
import MMAU._
import Expander._
import ScoreBoard._
import MLU._
import IssueQueen._
import MSU._



//完整Expander
class AME extends Module {
  val io = IO(new Bundle {
  
    val Uop_io = new Uop_IO //译码后信号

    val writeAll = new RegFileAllWrite_IO  //通用读端口
    val readAll = new RegFileAllRead_IO  //通用写端口

    val MLU_L2_io = new MLU_L2_IO   //读L2
    val MSU_L2_io = new MSU_L2_IO   //写L2


    val sigDone = Output(Bool())    // for debug
  })

  val subMMAU = Module(new MMAU)
  val subRegFile = Module(new RegFile)
  val subExpander = Module(new Expander)
  val subScoreBoard = Module(new ScoreBoard)
  val subMLU = Module(new MLU)
  val subIssueQueen = Module(new IssueQueen)
  val subMSU = Module(new MSU)

  /*  for debug   */
  io.sigDone := subExpander.io.sigDone

  /*  between Top and MMAU  */
  // nothing

  /*  between Top and MLU  */
  io.MLU_L2_io <> subMLU.io.MLU_L2_io

  /*  between Top and RegFile  */
  subRegFile.io := DontCare

  io.writeAll <> subRegFile.io.writeAll(1)
  io.readAll <> subRegFile.io.readAll(1)

  /*  between Top and Expander  */
  // io.Uop_io <> subExpander.io.Uop_io //

  /*  between Top and ScoreBoard  */
  // nothing


  /*  between Top and IssueQueen  */
  io.Uop_io <> subIssueQueen.io.Uop_In_io

  /*  between Top and MSU  */
  io.MSU_L2_io <> subMSU.io.MSU_L2_io


  /*  between MMAU and RegFile  */
    //read A(Tr0),using subRegFile.io.readTr(0)
  connectPort.toTrReadPort(
    subRegFile.io.readTr(0),
    subMMAU.io.Ops_io.ms1,
    subMMAU.io.actPortReadA,
    subMMAU.io.addrReadA,
    subMMAU.io.vecA
  )

    //read B(Tr1),using subRegFile.io.readTr(1)
  connectPort.toTrReadPort(
    subRegFile.io.readTr(1),
    subMMAU.io.Ops_io.ms2,
    subMMAU.io.actPortReadB,
    subMMAU.io.addrReadB,
    subMMAU.io.vecB
  )

    //read Cin(Acc0),using subRegFile.io.readAcc(0)
  connectPort.toAccReadPort(
    subRegFile.io.readAcc(0),
    subMMAU.io.Ops_io.md,
    subMMAU.io.actPortReadC,
    subMMAU.io.addrReadC,
    subMMAU.io.vecCin
  )

    //write Cout(Acc0),using subRegFile.io.writeAcc(0)
  connectPort.toAccWritePort(
    subRegFile.io.writeAcc(0),
    subMMAU.io.Ops_io.md,
    subMMAU.io.actPortWriteC,
    subMMAU.io.addrWriteC,
    subMMAU.io.vecCout,
    subMMAU.io.sigEnWriteC
  )

  /*  between MMAU and Expander  */
  subMMAU.io.FSM_MMAU_io <> subExpander.io.FSM_MMAU_io

  /*  between MMAU and ScoreBoard  */
  /*  between MMAU and MLU  */
  /*  between MMAU and MSU  */
  // nothing

  /*  between RegFile and MLU  */
  subRegFile.io.writeAll(0) <> subMLU.io.RegFileAllWrite_io


  /*  between RegFile and Expander  */
  /*  between RegFile and ScoreBoard  */
  // nothing

  /*  between RegFile and MSU  */
  subRegFile.io.readAll(0) <> subMSU.io.RegFileAllRead_io

  /*  between Expander and ScoreBoard  */
  subExpander.io.ScoreboardVisit_io <> subScoreBoard.io.ScoreboardVisit_io

  /*  between Expander and MLU  */
  subMLU.io.FSM_MLU_io <> subExpander.io.FSM_MLU_io

  /*  between Expander and IssueQueen  */
  subIssueQueen.io.Uop_Out_io <> subExpander.io.Uop_io

  /*  between Expander and MSU  */
  subMSU.io.FSM_MSU_io <> subExpander.io.FSM_MSU_io

  /*  between ScoreBoard and MLU  */
  //nothing

}







