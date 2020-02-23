package simple

import chisel3._
import chisel3.util._

/**
 * This is a very basic ALU example.
 */

class DMATransferM(DATA_WIDTH: Int = 16) extends Module{
  val io       = IO(new Bundle {
    val src    = Input(UInt(16.W))
    val dst    = Input(UInt(16.W))
    val len    = Input(UInt(16.W))
    val kick   = Input(UInt(16.W))
    val if_int = new InternaIF
  })

  var src  = io.src
  var dst  = io.dst
  var len  = io.len
  var kick = io.kick

  val r_counter = RegInit(0.U(5.W))
  val r_state   = RegInit(0.U(4.W))
  val r_rdata   = RegInit(0.U(16.W))

  val enable = Wire(UInt(1.W))
  val we     = Wire(UInt(1.W))
  val addr   = Wire(UInt(20.W))
  val wdata  = Wire(UInt(DATA_WIDTH.W))
  val wstrb  = Wire(UInt((DATA_WIDTH/8).W))

  val rdata  = Wire(UInt(DATA_WIDTH.W))
  val ready  = Wire(UInt(1.W))

  rdata := io.if_int.rdata
  ready := io.if_int.ready

  enable := 0.U
  we     := 0.U
  addr   := 0.U
  wdata  := r_rdata
  wstrb  := 0.U

  when(kick===1.U){
    r_counter := len
    r_state   := OBJ_DMA_STATE.RUN
//  }.elsewhen(r_counter != 0.U){
//    r_counter := len - 0x4.U
  }.elsewhen(r_state === OBJ_DMA_STATE.RUN){
    r_state := OBJ_DMA_STATE.AXI_READ
  }.elsewhen(r_state === OBJ_DMA_STATE.AXI_READ){
    enable := 1.U
    we     := 0.U
    addr   := src
    when(ready===1.U){
      r_rdata := rdata
      r_state := OBJ_DMA_STATE.AXI_WRITE
    }
  }.elsewhen(r_state === OBJ_DMA_STATE.AXI_WRITE){
    enable := 1.U
    we     := 1.U
    addr   := dst
    when(ready===1.U){
      r_state := OBJ_DMA_STATE.STOP
    }
  }

  io.if_int.enable := enable 
  io.if_int.we     := we    
  io.if_int.addr   := addr  
  io.if_int.wdata  := wdata 
  io.if_int.wstrb  := wstrb 

}
