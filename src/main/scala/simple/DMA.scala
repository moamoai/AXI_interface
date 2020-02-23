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
  val i_regs = Module(new RegsM)
  i_ifm.io.i_axi <> io.if_slv
  i_ifm.io.i_int <> i_regs.io.i_int

//  val r_ff  = RegInit(0.U.asTypeOf(new Axi4LiteIF))
//  r_ff  := io.if_slv
//  io.if_mst := r_ff

  // io.if_mst <> io.if_slv
  io.if_slv.i_WriteAddressChannel.AWREADY := 0.U
  io.if_slv.i_WriteDataChannel.WREADY     := 0.U
  io.if_slv.i_WriteResponseChannel.BRESP  := 0.U
  io.if_slv.i_WriteResponseChannel.BVALID := 0.U
  io.if_slv.i_ReadDataChannel.RRESP       := 0.U
  io.if_slv.i_ReadAddressChannel.ARREADY  := 0.U
  io.if_slv.i_ReadDataChannel.RVALID      := 0.U
  io.if_slv.i_ReadDataChannel.RDATA       := 0.U

  io.if_mst.i_WriteAddressChannel.AWADDR  := 0.U
  io.if_mst.i_WriteAddressChannel.AWPROT  := 0.U
  io.if_mst.i_WriteAddressChannel.AWVALID := 0.U
  io.if_mst.i_WriteDataChannel.WDATA      := 0.U
  io.if_mst.i_WriteDataChannel.WSTRB      := 0.U
  io.if_mst.i_WriteDataChannel.WVALID     := 0.U
  io.if_mst.i_WriteResponseChannel.BREADY := 0.U
  io.if_mst.i_ReadAddressChannel.ARADDR   := 0.U
  io.if_mst.i_ReadAddressChannel.ARPROT   := 0.U
  io.if_mst.i_ReadAddressChannel.ARVALID  := 0.U
  io.if_mst.i_ReadDataChannel.RREADY      := 0.U

}
