package simple

import chisel3._
import chisel3.util._

/**
 * This is a very basic ALU example.
 */

class DMA extends Module{
  val io     = IO(new Bundle {
    val if_slv = Flipped(new Axi4LiteIF)
    val if_mst  = new Axi4LiteIF
  })
  val i_ifm  = Module(new Axi4LiteSlaveIFM)
  val i_regs = Module(new RegsM(4))
  i_ifm.io.i_axi <> io.if_slv
  i_ifm.io.i_int <> i_regs.io.i_int

  val src  = Wire(UInt(16.W)) // i_regs.io.outs(0)
  val dst  = Wire(UInt(16.W)) // i_regs.io.outs(1)
  val len  = Wire(UInt(16.W)) // i_regs.io.outs(2)
  val kick = Wire(UInt(16.W)) // i_regs.io.outs(3)
  src  := i_regs.io.outs(0)
  dst  := i_regs.io.outs(1)
  len  := i_regs.io.outs(2)
  kick := i_regs.io.outs(3)

  val i_mifm  = Module(new Axi4LiteMasterIFM)
  io.if_mst        <> i_mifm.io.if_axi

  val i_trans  = Module(new DMATransferM)
  i_trans.io.src  := src
  i_trans.io.dst  := dst
  i_trans.io.len  := len
  i_trans.io.kick := kick
  i_trans.io.if_int <> i_mifm.io.if_int

}
