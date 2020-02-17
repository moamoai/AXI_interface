package simple

import chisel3._
import chisel3.util._

/**
 * This is a very basic ALU example.
 */

class Axi4LiteBus extends Module {
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

  val if_to_slv1_WriteAddressChannel  = Wire(new WriteAddressChannel  )
  val if_to_slv1_WriteDataChannel     = Wire(new WriteDataChannel     )
  val if_to_slv1_WriteResponseChannel = Wire(new WriteResponseChannel )
  val if_to_slv1_ReadAddressChannel   = Wire(new ReadAddressChannel   )
  val if_to_slv1_ReadDataChannel      = Wire(new ReadDataChannel      )

  // Axi4LiteSlave
  val i_slv1 = Module(new Axi4LiteSlave)
  i_slv1.io.i_WriteAddressChannel  <> if_to_slv1_WriteAddressChannel 
  i_slv1.io.i_WriteDataChannel     <> if_to_slv1_WriteDataChannel    
  i_slv1.io.i_WriteResponseChannel <> if_to_slv1_WriteResponseChannel
  i_slv1.io.i_ReadAddressChannel   <> if_to_slv1_ReadAddressChannel  
  i_slv1.io.i_ReadDataChannel      <> if_to_slv1_ReadDataChannel     

  val if_to_slv2_WriteAddressChannel  = Wire(new WriteAddressChannel  )
  val if_to_slv2_WriteDataChannel     = Wire(new WriteDataChannel     )
  val if_to_slv2_WriteResponseChannel = Wire(new WriteResponseChannel )
  val if_to_slv2_ReadAddressChannel   = Wire(new ReadAddressChannel   )
  val if_to_slv2_ReadDataChannel      = Wire(new ReadDataChannel      )

  val i_slv2 = Module(new Axi4LiteSlave)
  i_slv2.io.i_WriteAddressChannel  <> if_to_slv2_WriteAddressChannel 
  i_slv2.io.i_WriteDataChannel     <> if_to_slv2_WriteDataChannel    
  i_slv2.io.i_WriteResponseChannel <> if_to_slv2_WriteResponseChannel
  i_slv2.io.i_ReadAddressChannel   <> if_to_slv2_ReadAddressChannel  
  i_slv2.io.i_ReadDataChannel      <> if_to_slv2_ReadDataChannel     

  // init
  val r_RDATA = RegInit(0.U(16.W))

  when((WVALID===1.U)&(AWVALID===1.U)&(AWADDR===0x4000.U)){
  
    if_to_slv2_WriteAddressChannel         := io.i_WriteAddressChannel  
    if_to_slv2_WriteDataChannel            := io.i_WriteDataChannel    
    if_to_slv2_WriteResponseChannel        := io.i_WriteResponseChannel
    if_to_slv2_ReadAddressChannel          := io.i_ReadAddressChannel  
    if_to_slv2_ReadDataChannel             := io.i_ReadDataChannel     
    if_to_slv2_WriteAddressChannel.AWVALID := 0.U
    if_to_slv2_WriteDataChannel.WVALID     := 0.U

    if_to_slv1_WriteAddressChannel         <> io.i_WriteAddressChannel  
    if_to_slv1_WriteDataChannel            <> io.i_WriteDataChannel    
    if_to_slv1_WriteResponseChannel        <> io.i_WriteResponseChannel
    if_to_slv1_ReadAddressChannel          <> io.i_ReadAddressChannel  
    if_to_slv1_ReadDataChannel             <> io.i_ReadDataChannel     

    r_RDATA := if_to_slv1_ReadDataChannel.RDATA
  
  }.elsewhen((WVALID===1.U)&(AWVALID===1.U)&(AWADDR===0xC000.U)){
    if_to_slv1_WriteAddressChannel         := io.i_WriteAddressChannel  
    if_to_slv1_WriteDataChannel            := io.i_WriteDataChannel    
    if_to_slv1_WriteResponseChannel        := io.i_WriteResponseChannel
    if_to_slv1_ReadAddressChannel          := io.i_ReadAddressChannel  
    if_to_slv1_ReadDataChannel             := io.i_ReadDataChannel     
    if_to_slv1_WriteAddressChannel.AWVALID := 0.U
    if_to_slv1_WriteDataChannel.WVALID     := 0.U
  
    if_to_slv2_WriteAddressChannel  <> io.i_WriteAddressChannel  
    if_to_slv2_WriteDataChannel     <> io.i_WriteDataChannel    
    if_to_slv2_WriteResponseChannel <> io.i_WriteResponseChannel
    if_to_slv2_ReadAddressChannel   <> io.i_ReadAddressChannel  
    if_to_slv2_ReadDataChannel      <> io.i_ReadDataChannel     
    if_to_slv2_WriteAddressChannel.AWADDR  := AWADDR - 0x8000.U

    r_RDATA := if_to_slv2_ReadDataChannel.RDATA
  }.otherwise {
    if_to_slv1_WriteAddressChannel         := io.i_WriteAddressChannel  
    if_to_slv1_WriteDataChannel            := io.i_WriteDataChannel    
    if_to_slv1_WriteResponseChannel        := io.i_WriteResponseChannel
    if_to_slv1_ReadAddressChannel          := io.i_ReadAddressChannel  
    if_to_slv1_ReadDataChannel             := io.i_ReadDataChannel     
    if_to_slv1_WriteAddressChannel.AWVALID := 0.U
    if_to_slv1_WriteDataChannel.WVALID     := 0.U
  
    if_to_slv2_WriteAddressChannel         := io.i_WriteAddressChannel  
    if_to_slv2_WriteDataChannel            := io.i_WriteDataChannel    
    if_to_slv2_WriteResponseChannel        := io.i_WriteResponseChannel
    if_to_slv2_ReadAddressChannel          := io.i_ReadAddressChannel  
    if_to_slv2_ReadDataChannel             := io.i_ReadDataChannel     
    if_to_slv2_WriteAddressChannel.AWVALID := 0.U
    if_to_slv2_WriteDataChannel.WVALID     := 0.U
    r_RDATA                                := 0.U
  }

  io.i_WriteAddressChannel.AWREADY := AWVALID
  io.i_WriteDataChannel.WREADY     := WVALID
  io.i_WriteResponseChannel.BRESP  := 1.U
  io.i_WriteResponseChannel.BVALID := 1.U
  io.i_ReadAddressChannel.ARREADY  := 1.U
  io.i_ReadDataChannel.RRESP       := 0.U
  io.i_ReadDataChannel.RVALID      := 0.U
  io.i_ReadDataChannel.RDATA       := r_RDATA

}

// Generate the Verilog code by invoking the Driver
object Axi4LiteBusMain extends App {
  println("Generating the Axi4LiteBus hardware")
  chisel3.Driver.execute(Array("--target-dir", "generated"), () => new Axi4LiteBus())
}

