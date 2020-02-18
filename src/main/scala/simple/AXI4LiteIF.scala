/*
 *
 * An ALU is a minimal start for a processor.
 *
 */

package simple

import chisel3._
import chisel3.util._

class WriteAddressChannel(ADDR_WIDTH: Int = 20) extends Bundle {
  val AWADDR = Output(UInt(ADDR_WIDTH.W))
  val AWPROT = Output(UInt(1.W))
  val AWVALID = Output(UInt(1.W))
  val AWREADY = Input (UInt(1.W))
}
class WriteDataChannel extends Bundle {
  val WDATA = Output(UInt(16.W))
  val WSTRB = Output(UInt(8.W))
  val WVALID = Output(UInt(1.W))
  val WREADY = Input (UInt(1.W))
}
class WriteResponseChannel extends Bundle {
  val BRESP = Input (UInt(1.W))
  val BVALID = Input (UInt(1.W))
  val BREADY = Output(UInt(1.W))
}
class ReadAddressChannel(ADDR_WIDTH: Int = 20) extends Bundle {
  val ARADDR = Output(UInt(ADDR_WIDTH.W))
  val ARPROT = Output(UInt(1.W))
  val ARVALID = Output(UInt(1.W))
  val ARREADY = Input (UInt(1.W))
}
class ReadDataChannel extends Bundle {
  val RDATA = Input (UInt(16.W))
  val RRESP = Input (UInt(1.W))
  val RVALID = Input (UInt(1.W))
  val RREADY = Output(UInt(1.W))
}

class Axi4LiteIF (ADDR_WIDTH: Int = 20) extends Bundle {
  val i_WriteAddressChannel  = new WriteAddressChannel(ADDR_WIDTH)
  val i_WriteDataChannel     = new WriteDataChannel
  val i_WriteResponseChannel = new WriteResponseChannel
  val i_ReadAddressChannel   = new ReadAddressChannel  (ADDR_WIDTH)
  val i_ReadDataChannel      = new ReadDataChannel

  override def cloneType: this.type =
    new Axi4LiteIF(ADDR_WIDTH).asInstanceOf[this.type]
}