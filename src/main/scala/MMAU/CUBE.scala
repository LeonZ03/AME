package MMAU

import chisel3._
import chisel3.util._

import common._





class CUBE extends MMAUFormat {
  val io = IO(new Bundle {
    val vecA = Input(Vec(m, UInt((k * 8).W)))  // vecA 为 m 长度的向量，每个元素为 UInt(k*8.W)
    val vecB = Input(Vec(n, UInt((k * 8).W)))  // vecB 为 n 长度的向量，每个元素为 UInt(k*8.W)
    val muxCtrlC = Input(Vec(m, Vec(n/4, Bool())))    // m * (n/4) 个控制信号，同一Tile是共用的
    val muxCtrlSum = Input(Vec(m, Vec(n/4, Bool())))         // m * (n/4) 个,用于控制DPA内部累加寄存器更新逻辑（累加 or 归位）,同一Tile是共用的
    
    val eleC = Output(Vec(n, SInt(32.W)))      // 输出 n 长度的 vec
  })

  val subTile = Seq.fill(m , n/4)(Module(new Tile))//m行 n/4列 Tile

  //vecA connect
  for(i <- 0 until m){
    for(j <- 0 until n/4){
      if(j == 0) { subTile(i)(0).io.vecAin := io.vecA(i) }  //最左侧tile
      else { subTile(i)(j).io.vecAin := subTile(i)(j-1).io.vecAout }
    }
  }
  
  //vecB connect
  for(i <- 0 until m){
    for(j <- 0 until n/4){
      for(p <- 0 until 4){
        if(i == 0) { subTile(i)(j).io.vecBin(p) := io.vecB(j * 4 + p) } //最上方tile
        else { subTile(i)(j).io.vecBin(p) := subTile(i-1)(j).io.vecBout(p) }
      }
    }
  }

  //eleC connect
  for(i <- 0 until m){
    for(j <- 0 until n/4){
      for(p <- 0 until 4){
        if(i == 0) { subTile(i)(j).io.eleCin(p) := 0.S }  //最上方tile,赋有效值
        if(i == m-1) { io.eleC(j * 4 + p) := subTile(i)(j).io.eleCout(p) } //最下方tile
        else { subTile(i+1)(j).io.eleCin(p) := subTile(i)(j).io.eleCout(p) }
      }
    }
  }

  //muxCtrlC,muxCtrlSum connect
  for(i <- 0 until m){
    for(j <- 0 until n/4){
      subTile(i)(j).io.muxCtrlC := io.muxCtrlC(i)(j)
      subTile(i)(j).io.muxCtrlSum := io.muxCtrlSum(i)(j)
    }
  }

}
