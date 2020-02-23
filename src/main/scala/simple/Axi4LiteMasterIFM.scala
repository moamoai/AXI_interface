package simple

import chisel3._
import chisel3.util._

/**
 * This is a very basic ALU example.
 */

class Axi4LiteMasterIFM extends Module {
  val io     = IO(new Bundle {
    val if_int = Flipped(new InternaIF )
    val if_axi = new Axi4LiteIF
  })

  var enable = io.if_int.enable
  var we     = io.if_int.we
  var addr   = io.if_int.addr
  var wdata  = io.if_int.wdata
  var wstrb  = io.if_int.wstrb

  io.if_axi.i_WriteAddressChannel.AWADDR  := addr
  io.if_axi.i_WriteAddressChannel.AWPROT  := 0.U
  io.if_axi.i_WriteAddressChannel.AWVALID := enable & (we === 1.U) 
  io.if_axi.i_WriteDataChannel.WDATA      := wdata
  io.if_axi.i_WriteDataChannel.WSTRB      := wstrb
  io.if_axi.i_WriteDataChannel.WVALID     := enable & (we === 1.U) 
  io.if_axi.i_WriteResponseChannel.BREADY := 1.U
  io.if_axi.i_ReadAddressChannel.ARADDR   := addr
  io.if_axi.i_ReadAddressChannel.ARPROT   := 0.U
  io.if_axi.i_ReadAddressChannel.ARVALID  := enable & (we === 0.U) 
  io.if_axi.i_ReadDataChannel.RREADY      := 1.U

  var AWREADY = io.if_axi.i_WriteAddressChannel.AWREADY // := AWVALID
  var WREADY  = io.if_axi.i_WriteDataChannel.WREADY     // := WVALID
  var BRESP   = io.if_axi.i_WriteResponseChannel.BRESP  // := 1.U
  var BVALID  = io.if_axi.i_WriteResponseChannel.BVALID // := 1.U
  var ARREADY = io.if_axi.i_ReadAddressChannel.ARREADY  // := 1.U
  var RDATA   = io.if_axi.i_ReadDataChannel.RDATA       // := io.i_int.rdata
  var BREADY  = io.if_axi.i_ReadDataChannel.RRESP       // := 0.U
  var RVALID  = io.if_axi.i_ReadDataChannel.RVALID      // := io.i_int.ready

  // Output
  io.if_int.rdata := RDATA
  io.if_int.ready := AWREADY | WREADY | BREADY

  io.if_axi.i_WriteAddressChannel.AWADDR  := 0.U
  io.if_axi.i_WriteAddressChannel.AWPROT  := 0.U
  io.if_axi.i_WriteAddressChannel.AWVALID := 0.U
  io.if_axi.i_WriteDataChannel.WDATA      := 0.U
  io.if_axi.i_WriteDataChannel.WSTRB      := 0.U
  io.if_axi.i_WriteDataChannel.WVALID     := 0.U
  io.if_axi.i_WriteResponseChannel.BREADY := 0.U
  io.if_axi.i_ReadAddressChannel.ARADDR   := 0.U
  io.if_axi.i_ReadAddressChannel.ARPROT   := 0.U
  io.if_axi.i_ReadAddressChannel.ARVALID  := 0.U
  io.if_axi.i_ReadDataChannel.RREADY      := 0.U

//  val addr = Wire(UInt(16.W))
//  when (AWVALID===1.U){
//    addr := AWADDR
//  }.otherwise{
//    addr := ARADDR
//  }

}