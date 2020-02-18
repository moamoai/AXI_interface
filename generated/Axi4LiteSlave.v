module Axi4LiteSlave(
  input         clock,
  input         reset,
  input  [15:0] io_i_WriteAddressChannel_AWADDR,
  input         io_i_WriteAddressChannel_AWPROT,
  input         io_i_WriteAddressChannel_AWVALID,
  output        io_i_WriteAddressChannel_AWREADY,
  input  [15:0] io_i_WriteDataChannel_WDATA,
  input  [7:0]  io_i_WriteDataChannel_WSTRB,
  input         io_i_WriteDataChannel_WVALID,
  output        io_i_WriteDataChannel_WREADY,
  output        io_i_WriteResponseChannel_BRESP,
  output        io_i_WriteResponseChannel_BVALID,
  input         io_i_WriteResponseChannel_BREADY,
  input  [15:0] io_i_ReadAddressChannel_ARADDR,
  input         io_i_ReadAddressChannel_ARPROT,
  input         io_i_ReadAddressChannel_ARVALID,
  output        io_i_ReadAddressChannel_ARREADY,
  output [15:0] io_i_ReadDataChannel_RDATA,
  output        io_i_ReadDataChannel_RRESP,
  output        io_i_ReadDataChannel_RVALID,
  input         io_i_ReadDataChannel_RREADY
);
  reg [15:0] wrreg_4000; // @[Axi4LiteSlave.scala 31:27]
  reg [31:0] _RAND_0;
  wire  _T_2; // @[Axi4LiteSlave.scala 32:22]
  wire  _T_3; // @[Axi4LiteSlave.scala 32:46]
  wire  _T_4; // @[Axi4LiteSlave.scala 32:38]
  assign _T_2 = io_i_WriteDataChannel_WVALID & io_i_WriteAddressChannel_AWVALID; // @[Axi4LiteSlave.scala 32:22]
  assign _T_3 = io_i_WriteAddressChannel_AWADDR == 16'h4000; // @[Axi4LiteSlave.scala 32:46]
  assign _T_4 = _T_2 & _T_3; // @[Axi4LiteSlave.scala 32:38]
  assign io_i_WriteAddressChannel_AWREADY = io_i_WriteAddressChannel_AWVALID; // @[Axi4LiteSlave.scala 37:36]
  assign io_i_WriteDataChannel_WREADY = io_i_WriteDataChannel_WVALID; // @[Axi4LiteSlave.scala 38:36]
  assign io_i_WriteResponseChannel_BRESP = 1'h1; // @[Axi4LiteSlave.scala 39:36]
  assign io_i_WriteResponseChannel_BVALID = 1'h1; // @[Axi4LiteSlave.scala 40:36]
  assign io_i_ReadAddressChannel_ARREADY = 1'h1; // @[Axi4LiteSlave.scala 41:36]
  assign io_i_ReadDataChannel_RDATA = wrreg_4000; // @[Axi4LiteSlave.scala 42:36]
  assign io_i_ReadDataChannel_RRESP = 1'h0; // @[Axi4LiteSlave.scala 43:36]
  assign io_i_ReadDataChannel_RVALID = 1'h0; // @[Axi4LiteSlave.scala 44:36]
`ifdef RANDOMIZE_GARBAGE_ASSIGN
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_INVALID_ASSIGN
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_REG_INIT
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_MEM_INIT
`define RANDOMIZE
`endif
`ifndef RANDOM
`define RANDOM $random
`endif
`ifdef RANDOMIZE_MEM_INIT
  integer initvar;
`endif
`ifndef SYNTHESIS
initial begin
  `ifdef RANDOMIZE
    `ifdef INIT_RANDOM
      `INIT_RANDOM
    `endif
    `ifndef VERILATOR
      `ifdef RANDOMIZE_DELAY
        #`RANDOMIZE_DELAY begin end
      `else
        #0.002 begin end
      `endif
    `endif
  `ifdef RANDOMIZE_REG_INIT
  _RAND_0 = {1{`RANDOM}};
  wrreg_4000 = _RAND_0[15:0];
  `endif // RANDOMIZE_REG_INIT
  `endif // RANDOMIZE
end // initial
`endif // SYNTHESIS
  always @(posedge clock) begin
    if (reset) begin
      wrreg_4000 <= 16'h0;
    end else if (_T_4) begin
      wrreg_4000 <= io_i_WriteDataChannel_WDATA;
    end
  end
endmodule
