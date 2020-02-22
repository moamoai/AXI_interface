package simple

import chisel3._
import chisel3.util._

/**
 * This is a very basic ALU example.
 */

class Axi4LiteSlaveIFM extends Module {
  val io     = IO(new Bundle {
    val i_axi = Flipped(new Axi4LiteIF)
    val i_int = new InternaIF
  })

  var AWADDR  = io.i_axi.i_WriteAddressChannel.AWADDR
  var AWPROT  = io.i_axi.i_WriteAddressChannel.AWPROT
  var AWVALID = io.i_axi.i_WriteAddressChannel.AWVALID
  var WDATA   = io.i_axi.i_WriteDataChannel.WDATA
  var WSTRB   = io.i_axi.i_WriteDataChannel.WSTRB
  var WVALID  = io.i_axi.i_WriteDataChannel.WVALID
  var BREADY  = io.i_axi.i_WriteResponseChannel.BREADY
  var ARADDR  = io.i_axi.i_ReadAddressChannel.ARADDR
  var ARPROT  = io.i_axi.i_ReadAddressChannel.ARPROT
  var ARVALID = io.i_axi.i_ReadAddressChannel.ARVALID
  var RREADY  = io.i_axi.i_ReadDataChannel.RREADY

//  val wrreg_4000 = RegInit(0.U(16.W))
//  when((WVALID===1.U)&(AWVALID===1.U)&(AWADDR===0x2000.U)){
//    wrreg_4000 := WDATA
//  }.otherwise {
//  }

  io.i_axi.i_WriteAddressChannel.AWREADY := AWVALID
  io.i_axi.i_WriteDataChannel.WREADY     := WVALID
  io.i_axi.i_WriteResponseChannel.BRESP  := 1.U
  io.i_axi.i_WriteResponseChannel.BVALID := 1.U
  io.i_axi.i_ReadAddressChannel.ARREADY  := 1.U
  io.i_axi.i_ReadDataChannel.RDATA       := io.i_int.rdata
  io.i_axi.i_ReadDataChannel.RRESP       := 0.U
  io.i_axi.i_ReadDataChannel.RVALID      := io.i_int.ready
  val addr = Wire(UInt(16.W))
  when (AWVALID===1.U){
    addr := AWADDR
  }.otherwise{
    addr := ARADDR
  }

  io.i_int.enable := (AWVALID & WVALID)
  io.i_int.we     := WVALID
  io.i_int.addr   := addr
  io.i_int.wdata  := WDATA
  io.i_int.wstrb  := WSTRB

}

// Generate the Verilog code by invoking the Driver
object Axi4LiteSlaveIFMMain extends App {
  println("Generating the Axi4LiteSlaveIFM hardware")
  chisel3.Driver.execute(Array("--target-dir", "generated"), () => new Axi4LiteSlaveIFM())
}

