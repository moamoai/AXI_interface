package simple

import chisel3._
import chisel3.util._

/**
 * This is a very basic ALU example.
 */

class Axi4LiteSlave extends Module {
  val io = IO(new Bundle {
    val i_WriteAddressChannel  = Flipped(new WriteAddressChannel)
    val i_WriteDataChannel     = Flipped(new WriteDataChannel)
    val i_WriteResponseChannel = Flipped(new WriteResponseChannel)
    val i_ReadAddressChannel   = Flipped(new ReadAddressChannel)
    val i_ReadDataChannel      = Flipped(new ReadDataChannel)
  })
  io.i_WriteAddressChannel.AWREADY := 0.U
  io.i_WriteDataChannel.WREADY := 0.U
  io.i_WriteResponseChannel.BRESP := 0.U
  io.i_WriteResponseChannel.BVALID := 0.U
  io.i_ReadAddressChannel.ARREADY := 0.U
  io.i_ReadDataChannel.RDATA := 0.U
  io.i_ReadDataChannel.RRESP := 0.U
  io.i_ReadDataChannel.RVALID := 0.U

}

// Generate the Verilog code by invoking the Driver
object Axi4LiteSlaveMain extends App {
  println("Generating the Axi4LiteSlave hardware")
  chisel3.Driver.execute(Array("--target-dir", "generated"), () => new Axi4LiteSlave())
}

