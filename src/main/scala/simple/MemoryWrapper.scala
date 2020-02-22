/*
 *
 * An ALU is a minimal start for a processor.
 *
 */

package simple

import chisel3._
import chisel3.util._
// import chisel3.util.experimental.loadMemoryFromFile
// import chisel3.util.experimental.MemoryLoadFileType

/**
 * This is a very basic ALU example.
 */
class MemoryWrapper extends Module {
  val io     = IO(new Bundle {
    val i_int = Flipped(new InternaIF)
  })

  val enable = io.i_int.enable
  val we     = io.i_int.we
  val addr   = io.i_int.addr
  val wdata  = io.i_int.wdata
  val wstrb  = io.i_int.wstrb


  val i_mem = Module(new Memory);
  i_mem.io.we    := enable & we
  i_mem.io.addr  := addr
  i_mem.io.wdata := wdata

  io.i_int.rdata := i_mem.io.rdata
  io.i_int.ready := 1.U

}