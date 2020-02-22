package simple

import chisel3._
import chisel3.util._

/**
 * This is a very basic ALU example.
 */

class Axi4LiteTop extends Module {
  val io = IO(Flipped((new Axi4LiteIF)))

  // Axi4LiteSlave Modules
  val i_slv1 = Module(new Axi4LiteSlave)
  val i_slv2 = Module(new Axi4LiteMemory)

  // Bus
  //val i_bus = Module(new Axi4LiteBus(N_MST=1, N_SLV=3))
  val i_bus = Module(new Axi4LiteBus(N_MST=2, N_SLV=3))
  io <> i_bus.io.if_mst(0)
  i_slv1.io <> i_bus.io.if_slv(0)
  i_slv2.io <> i_bus.io.if_slv(1)

  // DMA
  val i_dma = Module(new DMA)
  i_dma.io.if_slv <>i_bus.io.if_slv(2)
  i_dma.io.if_mst <>i_bus.io.if_mst(1)
}

// Generate the Verilog code by invoking the Driver
object Axi4LiteTopMain extends App {
  println("Generating the Axi4LiteBus hardware")
  chisel3.Driver.execute(Array("--target-dir", "generated"), () => new Axi4LiteTop())
}

