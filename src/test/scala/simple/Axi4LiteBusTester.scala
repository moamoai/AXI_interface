package simple

import chisel3._
import chisel3.iotesters.PeekPokeTester
import scala.util.control.Breaks._

/**
 * Test the Axi4LiteSlave design
 */
class Axi4LiteBusTester(dut: Axi4LiteTop) extends PeekPokeTester(dut) {

  var i_WriteAddressChannel = dut.io.i_WriteAddressChannel
  var i_WriteDataChannel    = dut.io.i_WriteDataChannel

  var i_ReadAddressChannel = dut.io.i_ReadAddressChannel
  var i_ReadDataChannel    = dut.io.i_ReadDataChannel

  def f_axi_read(
    RADDR: UInt
  ) : BigInt = {
    poke(i_ReadAddressChannel.ARVALID, 1.U)
    poke(i_ReadAddressChannel.ARADDR , RADDR)
    step(1)
    var arready = peek(i_ReadAddressChannel.ARREADY)
    while ((arready == 0)){
      step(1)
      arready = peek(i_ReadAddressChannel.ARREADY)
    }
    poke(i_ReadAddressChannel.ARVALID, 0.U)

    var rvalid = peek(i_ReadDataChannel.RVALID)
    while ((rvalid == 0)){
      step(1)
      rvalid = peek(i_ReadDataChannel.RVALID)
    }
    var rdata = peek(i_ReadDataChannel.RDATA)
    step(1)
    poke(i_ReadAddressChannel.ARADDR , 0.U)
    return rdata;
  }
  def f_axi_write(
    WADDR: UInt, 
    WDATA: UInt
  ) : Int = {
    poke(i_WriteAddressChannel.AWVALID, 1.U)
    poke(i_WriteAddressChannel.AWADDR , WADDR)
    poke(i_WriteDataChannel.WVALID    , 1.U)
    poke(i_WriteDataChannel.WDATA     , WDATA)
    step(2)
    var awready = peek(i_WriteAddressChannel.AWREADY)
    var wready  = peek(i_WriteDataChannel.WREADY)
    while ((awready == 0)|(wready == 0)){
      step(1)
      awready = peek(i_WriteAddressChannel.AWREADY)
      wready  = peek(i_WriteDataChannel.WREADY)
    }
    poke(i_WriteAddressChannel.AWVALID, 0.U)
    poke(i_WriteAddressChannel.AWADDR , 0.U)
    poke(i_WriteDataChannel.WVALID    , 0.U)
    poke(i_WriteDataChannel.WDATA     , 0.U)
    step(1)
    return 0;
  }

  def f_write_read_test(
    addr : Int, 
    len  : Int
  ) : Int = {
    var test_addr  = 0
    var wdata = 0
    var rdata = f_axi_read (0.U) 
    step(1)
    for (i <- 0 to (len-1)){
      test_addr = addr   + 4 * i
      wdata     = test_addr
      f_axi_write(test_addr.U, wdata.U)
      rdata = f_axi_read (test_addr.U)
      if(rdata != wdata){
       println(f"[NG] test_addr[0x$test_addr%08x] wdata[0x$wdata%08x] rdata[0x$rdata%08x]");
      }
    }
    return 0
  }
  def test_0101(): Int ={
    // slv0
    f_write_read_test(0x0, 0x4)
    //  f_write_read_test(0x10, 0x4)
    // slv1
    f_write_read_test(0x4000, 0x4)
    f_write_read_test(0x6000, 0x4)
    // slv2
    f_write_read_test(0x8000, 0x4)
    //f_write_read_test(0x8010, 0x4)
    return 0;
  }
  def dma_transfer(
    src : Int, 
    dst : Int,
    len : Int
  ): Int ={
    var DMA_BASE = 0x8000
    f_axi_write((DMA_BASE + 0x00).U, src.U)
    f_axi_write((DMA_BASE + 0x04).U, dst.U)
    f_axi_write((DMA_BASE + 0x08).U, len.U)
    f_axi_write((DMA_BASE + 0x0C).U, 1.U)
    f_axi_write((DMA_BASE + 0x0C).U, 0.U)
    return 0
  }
  // Read Write Test
  // test_0101()

  f_axi_write(0x4000.U, 0x1234.U)

  // DMA Transfer
  dma_transfer(0x4000, 0x5000, 0x4)
  step(10)
  var rdata = f_axi_read(0x4000.U)
  println(f"DMA rdata[0x$rdata%08x]");
      rdata = f_axi_read(0x5000.U)
  println(f"DMA rdata[0x$rdata%08x]");


//  for (i  <- 0 to 1 by 1) {
//    for (j <- 1 to 1) {
//      var addr  = rnd.nextInt((1<<16)) // i
//      var wdata = 0
//      f_writeMem(addr, wdata)
//      var rdata = f_readMem (addr)
//      println(f"Init Test [0x$i%03d] addr[0x$addr%08x]" 
//            + f"wdata[0x$wdata%08x] rdata[0x$rdata%08x]");
//      expect(dut.io.rdata, 0)
//      // if(rdata != 0){
//      //  println("[NG] init test")
//      // }
//
//      wdata = rnd.nextInt((1<<16)) // j
//      f_writeMem(addr, wdata)
//      rdata = f_readMem (addr)
//      println(f"Write Test[0x$i%03d] addr[0x$addr%08x]" 
//            + f"wdata[0x$wdata%08x] rdata[0x$rdata%08x]");
//      expect(dut.io.rdata, wdata)
//      // if(rdata != wdata){
//      //  println("[NG] wdata test")
//      // }
//    }
//  }
}

object Axi4LiteBusTester extends App {
  println("Testing the dut")
  iotesters.Driver.execute(Array[String]("--generate-vcd-output", "on"), () => new Axi4LiteTop()) {
    c => new Axi4LiteBusTester(c)
  }
}
