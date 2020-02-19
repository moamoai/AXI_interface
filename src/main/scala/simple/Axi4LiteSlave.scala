package simple

import chisel3._
import chisel3.util._

/**
 * This is a very basic ALU example.
 */

class Axi4LiteSlave extends Module {
  val io = IO(Flipped((new Axi4LiteIF)))

  val i_ifm  = Module(new Axi4LiteSlaveIFM)
  val i_regs = Module(new RegsM)
  i_ifm.io.i_int <> i_regs.io.i_int
  i_ifm.io.i_axi <> io
}

// Generate the Verilog code by invoking the Driver
object Axi4LiteSlaveMain extends App {
  println("Generating the Axi4LiteSlave hardware")
  chisel3.Driver.execute(Array("--target-dir", "generated"), () => new Axi4LiteSlave())
}

