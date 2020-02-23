package simple

import chisel3._
import chisel3.util._

/**
 * This is a very basic ALU example.
 */

class BusMasterSelector(N_MST: Int = 1) extends Module{
  val io     = IO(new Bundle {
    val if_msts = Vec(N_MST, Flipped(new Axi4LiteIF))
    val if_mst  = new Axi4LiteIF
  })

  //io.if_mst <> io.if_msts(0)
  val w_if_mst_sel = Wire(Vec(N_MST, new Axi4LiteIF))
  io.if_mst <> w_if_mst_sel(0)
  // w_if_mst_sel(N_MST-1) <> io.if_msts(0)
  w_if_mst_sel(N_MST-1) <> io.if_msts(N_MST-1)

  val r_mstsel = RegInit(VecInit(Seq.fill(N_MST)(0.U(1.W))))

  val arbtrator = for (i <- 0 until N_MST-1) yield {
    var AWVALID = io.if_msts(i).i_WriteAddressChannel.AWVALID
    var ARVALID = io.if_msts(i).i_ReadAddressChannel.ARVALID
    var RVALID  = io.if_msts(i).i_ReadDataChannel.RVALID
    // val r_mstsel = RegInit(0.U(1.W))
    when(ARVALID===1.U){
      r_mstsel(i) := 1.U
    }.elsewhen((r_mstsel(i)===1.U)&&(RVALID===1.U)){
      r_mstsel(i) := 0.U
    }

    // w_if_mst_sel(i) <> w_if_mst_sel(i+1)
    // when((AWVALID===1.U) || (ARVALID===1.U)){
    when((AWVALID===1.U) || (ARVALID===1.U) || (r_mstsel(i)===1.U)){
      w_if_mst_sel(i).i_WriteAddressChannel.AWADDR  := io.if_msts(i).i_WriteAddressChannel.AWADDR
      w_if_mst_sel(i).i_WriteAddressChannel.AWPROT  := io.if_msts(i).i_WriteAddressChannel.AWPROT
      w_if_mst_sel(i).i_WriteAddressChannel.AWVALID := io.if_msts(i).i_WriteAddressChannel.AWVALID
      w_if_mst_sel(i).i_WriteDataChannel.WDATA      := io.if_msts(i).i_WriteDataChannel.WDATA
      w_if_mst_sel(i).i_WriteDataChannel.WSTRB      := io.if_msts(i).i_WriteDataChannel.WSTRB
      w_if_mst_sel(i).i_WriteDataChannel.WVALID     := io.if_msts(i).i_WriteDataChannel.WVALID
      w_if_mst_sel(i).i_WriteResponseChannel.BREADY := io.if_msts(i).i_WriteResponseChannel.BREADY
      w_if_mst_sel(i).i_ReadAddressChannel.ARADDR   := io.if_msts(i).i_ReadAddressChannel.ARADDR
      w_if_mst_sel(i).i_ReadAddressChannel.ARPROT   := io.if_msts(i).i_ReadAddressChannel.ARPROT
      w_if_mst_sel(i).i_ReadAddressChannel.ARVALID  := io.if_msts(i).i_ReadAddressChannel.ARVALID
      w_if_mst_sel(i).i_ReadDataChannel.RREADY      := io.if_msts(i).i_ReadDataChannel.RREADY

      io.if_msts(i).i_WriteAddressChannel.AWREADY := w_if_mst_sel(i).i_WriteAddressChannel.AWREADY
      io.if_msts(i).i_WriteDataChannel.WREADY     := w_if_mst_sel(i).i_WriteDataChannel.WREADY    
      io.if_msts(i).i_WriteResponseChannel.BRESP  := w_if_mst_sel(i).i_WriteResponseChannel.BRESP 
      io.if_msts(i).i_WriteResponseChannel.BVALID := w_if_mst_sel(i).i_WriteResponseChannel.BVALID
      io.if_msts(i).i_ReadDataChannel.RRESP       := w_if_mst_sel(i).i_ReadDataChannel.RRESP      
      io.if_msts(i).i_ReadAddressChannel.ARREADY  := w_if_mst_sel(i).i_ReadAddressChannel.ARREADY 
      io.if_msts(i).i_ReadDataChannel.RVALID      := w_if_mst_sel(i).i_ReadDataChannel.RVALID     
      io.if_msts(i).i_ReadDataChannel.RDATA       := w_if_mst_sel(i).i_ReadDataChannel.RDATA      

      w_if_mst_sel(i+1).i_WriteAddressChannel.AWREADY := 0.U
      w_if_mst_sel(i+1).i_WriteDataChannel.WREADY     := 0.U
      w_if_mst_sel(i+1).i_WriteResponseChannel.BRESP  := 0.U
      w_if_mst_sel(i+1).i_WriteResponseChannel.BVALID := 0.U
      w_if_mst_sel(i+1).i_ReadDataChannel.RRESP       := 0.U
      w_if_mst_sel(i+1).i_ReadAddressChannel.ARREADY  := 0.U
      w_if_mst_sel(i+1).i_ReadDataChannel.RVALID      := 0.U
      w_if_mst_sel(i+1).i_ReadDataChannel.RDATA       := 0.U
    }.otherwise {
      w_if_mst_sel(i).i_WriteAddressChannel.AWADDR  := w_if_mst_sel(i+1).i_WriteAddressChannel.AWADDR
      w_if_mst_sel(i).i_WriteAddressChannel.AWPROT  := w_if_mst_sel(i+1).i_WriteAddressChannel.AWPROT
      w_if_mst_sel(i).i_WriteAddressChannel.AWVALID := w_if_mst_sel(i+1).i_WriteAddressChannel.AWVALID
      w_if_mst_sel(i).i_WriteDataChannel.WDATA      := w_if_mst_sel(i+1).i_WriteDataChannel.WDATA
      w_if_mst_sel(i).i_WriteDataChannel.WSTRB      := w_if_mst_sel(i+1).i_WriteDataChannel.WSTRB
      w_if_mst_sel(i).i_WriteDataChannel.WVALID     := w_if_mst_sel(i+1).i_WriteDataChannel.WVALID
      w_if_mst_sel(i).i_WriteResponseChannel.BREADY := w_if_mst_sel(i+1).i_WriteResponseChannel.BREADY
      w_if_mst_sel(i).i_ReadAddressChannel.ARADDR   := w_if_mst_sel(i+1).i_ReadAddressChannel.ARADDR
      w_if_mst_sel(i).i_ReadAddressChannel.ARPROT   := w_if_mst_sel(i+1).i_ReadAddressChannel.ARPROT
      w_if_mst_sel(i).i_ReadAddressChannel.ARVALID  := w_if_mst_sel(i+1).i_ReadAddressChannel.ARVALID
      w_if_mst_sel(i).i_ReadDataChannel.RREADY      := w_if_mst_sel(i+1).i_ReadDataChannel.RREADY

      w_if_mst_sel(i+1).i_WriteAddressChannel.AWREADY := w_if_mst_sel(i).i_WriteAddressChannel.AWREADY
      w_if_mst_sel(i+1).i_WriteDataChannel.WREADY     := w_if_mst_sel(i).i_WriteDataChannel.WREADY    
      w_if_mst_sel(i+1).i_WriteResponseChannel.BRESP  := w_if_mst_sel(i).i_WriteResponseChannel.BRESP 
      w_if_mst_sel(i+1).i_WriteResponseChannel.BVALID := w_if_mst_sel(i).i_WriteResponseChannel.BVALID
      w_if_mst_sel(i+1).i_ReadDataChannel.RRESP       := w_if_mst_sel(i).i_ReadDataChannel.RRESP      
      w_if_mst_sel(i+1).i_ReadAddressChannel.ARREADY  := w_if_mst_sel(i).i_ReadAddressChannel.ARREADY 
      w_if_mst_sel(i+1).i_ReadDataChannel.RVALID      := w_if_mst_sel(i).i_ReadDataChannel.RVALID     
      w_if_mst_sel(i+1).i_ReadDataChannel.RDATA       := w_if_mst_sel(i).i_ReadDataChannel.RDATA      

      io.if_msts(i).i_WriteAddressChannel.AWREADY := 0.U
      io.if_msts(i).i_WriteDataChannel.WREADY     := 0.U
      io.if_msts(i).i_WriteResponseChannel.BRESP  := 0.U
      io.if_msts(i).i_WriteResponseChannel.BVALID := 0.U
      io.if_msts(i).i_ReadDataChannel.RRESP       := 0.U
      io.if_msts(i).i_ReadAddressChannel.ARREADY  := 0.U
      io.if_msts(i).i_ReadDataChannel.RVALID      := 0.U
      io.if_msts(i).i_ReadDataChannel.RDATA       := 0xFFFF.U

    }
  }
}

class Axi4LiteBus(N_MST: Int = 1,
                  N_SLV: Int = 2
) extends Module {
  // Wire(Vec(num_regs, UInt(1.W)))
  val io     = IO(new Bundle {
    val if_mst = Vec(N_MST, Flipped(new Axi4LiteIF))
    val if_slv = Vec(N_SLV, new Axi4LiteIF)
  })

  val r_RDATA = RegInit(0.U(16.W))

  val i_massel = Module(new BusMasterSelector(N_MST))
  i_massel.io.if_msts <> io.if_mst
  var if_mstselected = i_massel.io.if_mst 

  // Input From Master
  var AWADDR  = if_mstselected.i_WriteAddressChannel.AWADDR
  var AWPROT  = if_mstselected.i_WriteAddressChannel.AWPROT
  var AWVALID = if_mstselected.i_WriteAddressChannel.AWVALID
  var WDATA   = if_mstselected.i_WriteDataChannel.WDATA
  var WSTRB   = if_mstselected.i_WriteDataChannel.WSTRB
  var WVALID  = if_mstselected.i_WriteDataChannel.WVALID
  var BREADY  = if_mstselected.i_WriteResponseChannel.BREADY
  var ARADDR  = if_mstselected.i_ReadAddressChannel.ARADDR
  var ARPROT  = if_mstselected.i_ReadAddressChannel.ARPROT
  var ARVALID = if_mstselected.i_ReadAddressChannel.ARVALID
  var RREADY  = if_mstselected.i_ReadDataChannel.RREADY

  // Outpu to Master
  if_mstselected.i_WriteAddressChannel.AWREADY := AWVALID
  if_mstselected.i_WriteDataChannel.WREADY     := WVALID
  if_mstselected.i_WriteResponseChannel.BRESP  := 1.U
  if_mstselected.i_WriteResponseChannel.BVALID := 1.U
  if_mstselected.i_ReadDataChannel.RRESP       := 0.U
  if_mstselected.i_ReadAddressChannel.ARREADY  := ARVALID
  if_mstselected.i_ReadDataChannel.RVALID      := 1.U
  if_mstselected.i_ReadDataChannel.RDATA       := r_RDATA

  // var if_to_slv1 = io.if_to_slv1
  // var if_to_slv2 = io.if_to_slv2
  //var if_mstselected    = if_mstselected

  val if_to_slv  = Wire(Vec(N_SLV, new Axi4LiteIF))
  val region_slv = Wire(Vec(N_SLV, Bool()))
  // val if_to_slv1 = Wire(new Axi4LiteIF)
  // val if_to_slv2 = Wire(new Axi4LiteIF)

  val axwvalid = Wire(Bool())
  axwvalid := ((WVALID===1.U)&&(AWVALID===1.U)) | 
               (ARVALID===1.U) 

  val switcher = for (i <- 0 until N_SLV) yield {
    region_slv(i) := ((AWVALID===1.U)&&((AWADDR & 0xFC000.U)===(0x4000.U * i.U))) |
                     ((ARVALID===1.U)&&((ARADDR & 0xFC000.U)===(0x4000.U * i.U)))
    when(axwvalid&&region_slv(i)){
      if_to_slv(i) <> if_mstselected
      if_to_slv(i).i_WriteAddressChannel.AWADDR  := AWADDR - (0x4000.U * i.U)
      if_to_slv(i).i_ReadAddressChannel.ARADDR   := ARADDR - (0x4000.U * i.U)
      r_RDATA := if_to_slv(i).i_ReadDataChannel.RDATA
    }.otherwise {
      if_to_slv(i) := if_mstselected
      if_to_slv(i).i_WriteAddressChannel.AWVALID := 0.U
      if_to_slv(i).i_WriteDataChannel.WVALID     := 0.U
    }
    // Output to Slave
    io.if_slv(i) <> if_to_slv(i)
  }
}

// Generate the Verilog code by invoking the Driver
object Axi4LiteBusMain extends App {
  println("Generating the Axi4LiteBus hardware")
  chisel3.Driver.execute(Array("--target-dir", "generated"), () => new Axi4LiteBus())
}