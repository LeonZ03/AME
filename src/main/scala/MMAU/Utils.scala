package MMAU

import chisel3._
import chisel3.util._


object AdderTree {
  def reduceAddTree(products: Seq[SInt]): SInt = {
    if (products.size == 1) {
      products.head
    } else {
      val pairedProducts = products.grouped(2).collect {
        case Seq(a, b) => a + b
      }.toSeq
      reduceAddTree(pairedProducts)
    }
  }
}