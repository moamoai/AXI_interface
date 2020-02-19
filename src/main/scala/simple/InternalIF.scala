/*
 *
 * An ALU is a minimal start for a processor.
 *
 */

package simple

import chisel3._
import chisel3.util._

class InternaIF extends Bundle {
  val enable = Output(UInt(1.W))
  val we = Output(UInt(1.W))
  val addr = Output(UInt(20.W))
  val wdata = Output(UInt(16.W))
  val wstrb = Output(UInt(2.W))
  val rdata = Input(UInt(16.W))
  val ready = Input(UInt(1.W))
}

class RegIF(DATA_WIDTH: Int = 16) extends Bundle {
  val enable = Output(UInt(1.W))
  val we = Output(UInt(1.W))
  val wdata = Output(UInt(DATA_WIDTH.W))
  val wstrb = Output(UInt((DATA_WIDTH/8).W))
  val rdata = Input(UInt(DATA_WIDTH.W))
  val ready = Input(UInt(1.W))
  override def cloneType: this.type =
    new RegIF(DATA_WIDTH).asInstanceOf[this.type]
}