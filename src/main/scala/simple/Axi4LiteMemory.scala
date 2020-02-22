package simple

import chisel3._
import chisel3.util._

/**
 * This is a very basic ALU example.
 */

class Axi4LiteMemory extends Module {
  val io = IO(Flipped((new Axi4LiteIF)))

  val i_ifm = Module(new Axi4LiteSlaveIFM)
  val i_mem = Module(new MemoryWrapper)
  i_ifm.io.i_int <> i_mem.io.i_int
  i_ifm.io.i_axi <> io
}