package simple

import chisel3._
import chisel3.util._

object OBJ_BASE_ADDR {
  val BASE_ADDR_REGION1 = 0x00000.U
  val BASE_ADDR_REGION2 = 0x04000.U
}

object OBJ_DMA_STATE {
  val STOP = 0.U
  val RUN = 1.U
  val AXI_READ = 2.U
  val AXI_WRITE = 3.U
}