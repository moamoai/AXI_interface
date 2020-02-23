package simple

import chisel3._
import chisel3.util._

/**
 * This is a very basic ALU example.
 */

class DMATransferM extends Module{
  val io       = IO(new Bundle {
    val src    = Input(UInt(16.W))
    val dst    = Input(UInt(16.W))
    val len    = Input(UInt(16.W))
    val kick   = Input(UInt(16.W))
    val if_int = new InternaIF
  })

  io.if_int.enable := 0.U
  io.if_int.we     := 0.U
  io.if_int.addr   := 0.U
  io.if_int.wdata  := 0.U
  io.if_int.wstrb  := 0.U

}
