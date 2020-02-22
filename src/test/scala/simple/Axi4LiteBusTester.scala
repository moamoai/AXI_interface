package simple

import chisel3._
import chisel3.iotesters.PeekPokeTester
import scala.util.control.Breaks._

/**
 * Test the Axi4LiteSlave design
 */
class Axi4LiteBusTester(dut: Axi4LiteBus) extends PeekPokeTester(dut) {

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


  var rdata = f_axi_read (0x0000.U)

  step(1)
  f_axi_write(0x1000.U, 0x1110.U)
  rdata = f_axi_read (0x1000.U)
  println(f"rdata[0x$rdata%08x]");

  f_axi_write(0x2000.U, 0x2220.U)
  rdata = f_axi_read (0x2000.U)
  println(f"rdata[0x$rdata%08x]");

  f_axi_write(0x3000.U, 0x3330.U)
  rdata = f_axi_read (0x3000.U)
  println(f"rdata[0x$rdata%08x]");

  f_axi_write(0x6000.U, 0x6660.U)
  rdata = f_axi_read (0x6000.U)
  println(f"rdata[0x$rdata%08x]");

  f_axi_write(0x4000.U, 0xfff0.U)
  rdata = f_axi_read (0x4000.U)
  println(f"rdata[0x$rdata%08x]");
  step(1)

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
  iotesters.Driver.execute(Array[String]("--generate-vcd-output", "on"), () => new Axi4LiteBus()) {
    c => new Axi4LiteBusTester(c)
  }
}
