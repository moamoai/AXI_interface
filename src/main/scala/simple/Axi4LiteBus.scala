package simple

import chisel3._
import chisel3.util._

/**
 * This is a very basic ALU example.
 */

class Axi4LiteBus(N_MST: Int = 1,
                  N_SLV: Int = 2
) extends Module {
  // Wire(Vec(num_regs, UInt(1.W)))
  val io     = IO(new Bundle {
    val if_mst = Vec(N_MST, Flipped(new Axi4LiteIF))
    val if_slv = Vec(N_SLV, new Axi4LiteIF)
  })

  // Input From Master
  var AWADDR  = io.if_mst(0).i_WriteAddressChannel.AWADDR
  var AWPROT  = io.if_mst(0).i_WriteAddressChannel.AWPROT
  var AWVALID = io.if_mst(0).i_WriteAddressChannel.AWVALID
  var WDATA   = io.if_mst(0).i_WriteDataChannel.WDATA
  var WSTRB   = io.if_mst(0).i_WriteDataChannel.WSTRB
  var WVALID  = io.if_mst(0).i_WriteDataChannel.WVALID
  var BREADY  = io.if_mst(0).i_WriteResponseChannel.BREADY
  var ARADDR  = io.if_mst(0).i_ReadAddressChannel.ARADDR
  var ARPROT  = io.if_mst(0).i_ReadAddressChannel.ARPROT
  var ARVALID = io.if_mst(0).i_ReadAddressChannel.ARVALID
  var RREADY  = io.if_mst(0).i_ReadDataChannel.RREADY

  // var if_to_slv1 = io.if_to_slv1
  // var if_to_slv2 = io.if_to_slv2
  var if_mst1    = io.if_mst(0)
  val if_to_slv1 = Wire(new Axi4LiteIF)
  val if_to_slv2 = Wire(new Axi4LiteIF)

  val r_RDATA = RegInit(0.U(16.W))
  val axwvalid = Wire(Bool())
  axwvalid := ((WVALID===1.U)&&(AWVALID===1.U)) | 
               (ARVALID===1.U) 

  val region_slv1 = Wire(Bool())
  val region_slv2 = Wire(Bool())
  region_slv1 := ((AWVALID===1.U)&&((AWADDR & 0xC000.U)===OBJ_BASE_ADDR.BASE_ADDR_REGION1))|
                 ((ARVALID===1.U)&&((ARADDR & 0xC000.U)===OBJ_BASE_ADDR.BASE_ADDR_REGION1))
  region_slv2 := ((AWVALID===1.U)&&((AWADDR & 0xC000.U)===OBJ_BASE_ADDR.BASE_ADDR_REGION2)) |
                 ((ARVALID===1.U)&&((ARADDR & 0xC000.U)===OBJ_BASE_ADDR.BASE_ADDR_REGION2))

  when(axwvalid&&region_slv1){
    if_to_slv2 := if_mst1
    if_to_slv2.i_WriteAddressChannel.AWVALID := 0.U
    if_to_slv2.i_WriteDataChannel.WVALID     := 0.U

    if_to_slv1 <> if_mst1
    if_to_slv1.i_WriteAddressChannel.AWADDR  := AWADDR - OBJ_BASE_ADDR.BASE_ADDR_REGION1
    if_to_slv1.i_ReadAddressChannel.ARADDR   := ARADDR - OBJ_BASE_ADDR.BASE_ADDR_REGION1
    r_RDATA := if_to_slv1.i_ReadDataChannel.RDATA
  
  }.elsewhen(axwvalid && region_slv2){
    if_to_slv1 := if_mst1
    if_to_slv1.i_WriteAddressChannel.AWVALID := 0.U
    if_to_slv1.i_WriteDataChannel.WVALID     := 0.U
  
    if_to_slv2  <> if_mst1
    if_to_slv2.i_WriteAddressChannel.AWADDR  := AWADDR - OBJ_BASE_ADDR.BASE_ADDR_REGION2
    if_to_slv2.i_ReadAddressChannel.ARADDR   := ARADDR - OBJ_BASE_ADDR.BASE_ADDR_REGION2
    r_RDATA := if_to_slv2.i_ReadDataChannel.RDATA
  }.otherwise {
    if_to_slv1 := if_mst1
    if_to_slv1.i_WriteAddressChannel.AWVALID := 0.U
    if_to_slv1.i_WriteDataChannel.WVALID     := 0.U
  
    if_to_slv2 := if_mst1
    if_to_slv2.i_WriteAddressChannel.AWVALID := 0.U
    if_to_slv2.i_WriteDataChannel.WVALID     := 0.U

    r_RDATA                                  := 0.U
  }

  // Output to Slave
  io.if_slv(0) <> if_to_slv1
  io.if_slv(1) <> if_to_slv2

  // Outpu to Master
  io.if_mst(0).i_WriteAddressChannel.AWREADY := AWVALID
  io.if_mst(0).i_WriteDataChannel.WREADY     := WVALID
  io.if_mst(0).i_WriteResponseChannel.BRESP  := 1.U
  io.if_mst(0).i_WriteResponseChannel.BVALID := 1.U
  io.if_mst(0).i_ReadDataChannel.RRESP       := 0.U
  io.if_mst(0).i_ReadAddressChannel.ARREADY  := ARVALID
  io.if_mst(0).i_ReadDataChannel.RVALID      := 1.U
  io.if_mst(0).i_ReadDataChannel.RDATA       := r_RDATA

}

// Generate the Verilog code by invoking the Driver
object Axi4LiteBusMain extends App {
  println("Generating the Axi4LiteBus hardware")
  chisel3.Driver.execute(Array("--target-dir", "generated"), () => new Axi4LiteBus())
}

