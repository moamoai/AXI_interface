package simple

import chisel3._
import chisel3.iotesters.PeekPokeTester
import scala.util.control.Breaks._

/**
 * Test the Axi4LiteSlave design
 */
class Axi4LiteSlaveTester(dut: Axi4LiteSlave) extends PeekPokeTester(dut) {

  var i_WriteAddressChannel = dut.io.i_WriteAddressChannel
  var i_WriteDataChannel    = dut.io.i_WriteDataChannel

  def f_axi_write(
    WADDR: UInt, 
    WDATA: UInt
  ) : Int = {
    poke(i_WriteAddressChannel.AWVALID, 1.U)
    poke(i_WriteAddressChannel.AWADDR , WADDR)
    poke(i_WriteDataChannel.WVALID    , 1.U)
    poke(i_WriteDataChannel.WDATA     , WDATA)
    step(1)
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

  step(1)
  f_axi_write(0x1000.U, 0x1110.U)
  f_axi_write(0x4000.U, 0x4440.U)
  f_axi_write(0x2000.U, 0x2220.U)
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

object Axi4LiteSlaveTester extends App {
  println("Testing the dut")
  iotesters.Driver.execute(Array[String]("--generate-vcd-output", "on"), () => new Axi4LiteSlave()) {
    c => new Axi4LiteSlaveTester(c)
  }
}
