package simple

import chisel3._
import chisel3.util._

/**
 * This is a very basic ALU example.
 */

class WRREG(DATA_WIDTH: Int = 16) extends Module {
  val io     = IO(new Bundle {
    val i_if = Flipped(new RegIF(DATA_WIDTH))
    val out   = Output(UInt(DATA_WIDTH.W))
  })

  val enable = io.i_if.enable
  val we     = io.i_if.we
  val wdata  = io.i_if.wdata
  val wstrb  = io.i_if.wstrb

  // Reg
  val reg = RegInit(0.U(DATA_WIDTH.W))
  when((enable===1.U)&(we===1.U)){
    reg   := wdata
  }.otherwise {
  }

  // Wire
  val rdata = Wire(UInt(DATA_WIDTH.W))
  rdata := 0.U(DATA_WIDTH.W)
  when((enable===1.U)&(we===0.U)){
    rdata := reg
  }

  io.i_if.rdata := rdata
  io.i_if.ready := enable
  io.out        := reg

}

class RegsM extends Module {

  val io     = IO(new Bundle {
    val i_int = Flipped(new InternaIF)
  })

  val enable = io.i_int.enable
  val we     = io.i_int.we
  val addr   = io.i_int.addr
  val wdata  = io.i_int.wdata
  val wstrb  = io.i_int.wstrb

  val if_reg = Wire(new RegIF)
  if_reg.enable := enable
  if_reg.we     := we
  if_reg.wdata  := wdata
  if_reg.wstrb  := wstrb

  val i_wrreg = Module(new WRREG)
  i_wrreg.io.i_if <> if_reg

  val out = Wire(UInt(16.W))
  out := i_wrreg.io.out

  val num_regs = 4
  // val readys = Wire(VecInit(Seq.fill(4)(UInt(1.W))))
  // val readys = Wire(UInt(4.W))
  val readys = Wire(Vec(num_regs, UInt(1.W)))
  val rdatas = Wire(Vec(num_regs, UInt(16.W)))
  val outs   = Wire(Vec(num_regs, UInt(16.W)))

  val my_args  = Seq(1,2,3,4)
  val regs = for (i <- 0 until num_regs) yield {
     val i_wrreg = Module(new WRREG(16)) // args = my_args(i)
     i_wrreg.io.i_if        <> if_reg
     i_wrreg.io.i_if.enable := (addr === ((my_args(i)).U * 0x1000.U))

     readys(i) := i_wrreg.io.i_if.ready 
     rdatas(i) := i_wrreg.io.i_if.rdata
     outs(i)    := i_wrreg.io.out
  }

  io.i_int.rdata := rdatas.reduce(_ | _) // i_wrreg.io.i_if.rdata
  io.i_int.ready := readys.reduce(_ | _) // i_wrreg.io.i_if.ready

}