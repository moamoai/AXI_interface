package simple

import chisel3._
import chisel3.util._

/**
 * This is a very basic ALU example.
 */

class DMA extends Module{
  val io     = IO(new Bundle {
    val if_slv = Flipped(new Axi4LiteIF)
//    val if_mst  = new Axi4LiteIF
  })
  val i_ifm  = Module(new Axi4LiteSlaveIFM)
  val i_regs = Module(new RegsM)
  i_ifm.io.i_int <> i_regs.io.i_int
  i_ifm.io.i_axi <> io

//  io.if_mst <> io.if_slv
}
