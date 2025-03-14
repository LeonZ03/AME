package MMAU

import chisel3._
import chisel3.util._

import common._







class Tile extends MMAUFormat {
  val io = IO(new Bundle {
    val vecAin = Input(UInt((k * 8).W))  
    val vecBin = Input(Vec(4, UInt((k * 8).W)))  
    val eleCin = Input(Vec(4, SInt(32.W)))  
    val muxCtrlC = Input(Bool())    //Tile内共用
    val muxCtrlSum = Input(Bool())    //Tile内共用    
    
    val vecAout = Output(UInt((k * 8).W))
    val vecBout = Output(Vec(4, UInt((k * 8).W))) 
    val eleCout = Output(Vec(4, SInt(32.W)))      
  })

  val pecube = Seq.fill(4)(Module(new PEcube))

  for(i <- 0 until 4){//Tile与pecubes连线
    pecube(i).io.vecBin := io.vecBin(i)
    pecube(i).io.eleCin := io.eleCin(i)
    pecube(i).io.muxCtrlC := io.muxCtrlC
    pecube(i).io.muxCtrlSum := io.muxCtrlSum
    io.vecBout(i) := pecube(i).io.vecBout
    io.eleCout(i) := pecube(i).io.eleCout
    
    if(i > 0){//pecube 之间连线
        pecube(i).io.vecAin := pecube(i-1).io.vecAout
    }
  }

  pecube(0).io.vecAin := io.vecAin

  val regR = RegInit(0.U((k * 8).W))
  regR := pecube(3).io.vecAout
  io.vecAout := regR

}


