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

  var AWADDR = io.i_WriteAddressChannel.AWADDR
  var AWPROT = io.i_WriteAddressChannel.AWPROT
  var AWVALID = io.i_WriteAddressChannel.AWVALID
  var WDATA = io.i_WriteDataChannel.WDATA
  var WSTRB = io.i_WriteDataChannel.WSTRB
  var WVALID = io.i_WriteDataChannel.WVALID
  var BREADY = io.i_WriteResponseChannel.BREADY
  var ARADDR = io.i_ReadAddressChannel.ARADDR
  var ARPROT = io.i_ReadAddressChannel.ARPROT
  var ARVALID = io.i_ReadAddressChannel.ARVALID
  var RREADY = io.i_ReadDataChannel.RREADY

  val wrreg_4000 = RegInit(0.U(16.W))
  when((WVALID===1.U)&(AWVALID===1.U)&(AWADDR===0x4000.U)){
    wrreg_4000 := WDATA
  }.otherwise {
  }

  io.i_WriteAddressChannel.AWREADY := AWVALID
  io.i_WriteDataChannel.WREADY     := WVALID
  io.i_WriteResponseChannel.BRESP  := 1.U
  io.i_WriteResponseChannel.BVALID := 1.U
  io.i_ReadAddressChannel.ARREADY  := 1.U
  io.i_ReadDataChannel.RDATA       := wrreg_4000
  io.i_ReadDataChannel.RRESP       := 0.U
  io.i_ReadDataChannel.RVALID      := 0.U

}

// Generate the Verilog code by invoking the Driver
object Axi4LiteSlaveMain extends App {
  println("Generating the Axi4LiteSlave hardware")
  chisel3.Driver.execute(Array("--target-dir", "generated"), () => new Axi4LiteSlave())
}

