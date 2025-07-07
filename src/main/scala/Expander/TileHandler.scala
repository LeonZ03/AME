package Expander

import chisel3._
import chisel3.util._

import common._
import MMAU._




//用于处理MSU Tile各维度相关参数
class TileHandler_MSU extends Module {
  val io = IO(new Bundle {
    val is_msce32 = Input(Bool())
    
    val mtileConfig_io = new mtileConfig_IO
    val TileHandler_MSU_io = new TileHandler_MSU_IO
  })

  // 默认输出为 0

  val nRow = Wire(UInt(7.W))
  val nCol = Wire(UInt(Consts.nCol_LEN.W))

  nRow := 0.U
  nCol := 0.U

  when(io.is_msce32) {

    nRow := io.mtileConfig_io.mtilem
    nCol := (io.mtileConfig_io.mtilen + 31.U) >> 5

  }

//debug
printf(p"[TileHandler_MSU] io.is_msce32 = ${io.is_msce32} \n") 
printf(p"[TileHandler_MSU] nRow = ${nRow} , nCol = ${nCol}  \n") 

  io.TileHandler_MSU_io.nRow := nRow
  io.TileHandler_MSU_io.nCol := nCol
}








//用于处理MLU Tile各维度相关参数
class TileHandler_MLU extends Module {
  val io = IO(new Bundle {
    val is_mlbe8 = Input(Bool())
    val is_mlae8 = Input(Bool())
    val is_mlce32 = Input(Bool())
    
    val mtileConfig_io = new mtileConfig_IO
    val TileHandler_MLU_io = new TileHandler_MLU_IO
  })


  val nRow = Wire(UInt(Consts.nRow_LEN.W))
  val nCol = Wire(UInt(Consts.nCol_LEN.W))

  nRow := 0.U
  nCol := 0.U

  when(io.is_mlbe8) {

    nRow := (io.mtileConfig_io.mtilen + 7.U) >> 3
    nCol := (io.mtileConfig_io.mtilek + 63.U) >> 6

  }.elsewhen(io.is_mlae8) {

    nRow := (io.mtileConfig_io.mtilem + 7.U) >> 3
    nCol := (io.mtileConfig_io.mtilek + 63.U) >> 6

  }.elsewhen(io.is_mlce32) {

    nRow := (io.mtileConfig_io.mtilem + 3.U) >> 2
    nCol := (io.mtileConfig_io.mtilen + 31.U) >> 5

  }

//debug
// printf(p"[TileHandler] io.is_mlbe8 = ${io.is_mlbe8} , io.is_mlae8 = ${io.is_mlae8} , io.is_mlce32 = ${io.is_mlce32} \n") 
// printf(p"[TileHandler] nRow = ${nRow} , nCol = ${nCol}  \n") 

  io.TileHandler_MLU_io.nRow := nRow
  io.TileHandler_MLU_io.nCol := nCol
}




//用于处理MMAU Tile各维度相关参数
class TileHandler_MMAU extends MMAUFormat {
  val io = IO(new Bundle {
    val mtileConfig_io = new mtileConfig_IO   //用户配置尺寸
    val TileHandler_MMAU_io = new TileHandler_MMAU_IO   //padding后的numm,numn,numk
  })

  // 计算 log2 常量（m/n/k 是 2 的幂次）
  val log2m = log2Ceil(m)
  val log2n = log2Ceil(n)
  val log2k = log2Ceil(k)

  // padding 向上补齐，避免使用除法
  val tilem = applyTileHandler.ceilAlignPow2(io.mtileConfig_io.mtilem, log2m)
  val tilen = applyTileHandler.ceilAlignPow2(io.mtileConfig_io.mtilen, log2n)

  val tilek = Wire(UInt(log2Ceil(tileK + 1).W))
  when(io.mtileConfig_io.mtilek < (m * k).U) {
    tilek := (m * k).U
  }.otherwise {
    tilek := applyTileHandler.ceilAlignPow2(io.mtileConfig_io.mtilek, log2k)
  }


  // 替代除法为右移
  io.TileHandler_MMAU_io.numm := tilem >> log2m.U
  io.TileHandler_MMAU_io.numn := tilen >> log2n.U
  io.TileHandler_MMAU_io.numk := tilek >> log2k.U
}
