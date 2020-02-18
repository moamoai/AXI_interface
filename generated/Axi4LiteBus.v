module Axi4LiteSlave(
  input         clock,
  input         reset,
  input  [15:0] io_i_WriteAddressChannel_AWADDR,
  input         io_i_WriteAddressChannel_AWVALID,
  input  [15:0] io_i_WriteDataChannel_WDATA,
  input         io_i_WriteDataChannel_WVALID,
  output [15:0] io_i_ReadDataChannel_RDATA
);
  reg [15:0] wrreg_4000; // @[Axi4LiteSlave.scala 31:27]
  reg [31:0] _RAND_0;
  wire  _T_2; // @[Axi4LiteSlave.scala 32:22]
  wire  _T_3; // @[Axi4LiteSlave.scala 32:46]
  wire  _T_4; // @[Axi4LiteSlave.scala 32:38]
  assign _T_2 = io_i_WriteDataChannel_WVALID & io_i_WriteAddressChannel_AWVALID; // @[Axi4LiteSlave.scala 32:22]
  assign _T_3 = io_i_WriteAddressChannel_AWADDR == 16'h4000; // @[Axi4LiteSlave.scala 32:46]
  assign _T_4 = _T_2 & _T_3; // @[Axi4LiteSlave.scala 32:38]
  assign io_i_ReadDataChannel_RDATA = wrreg_4000; // @[Axi4LiteSlave.scala 42:36]
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
module Axi4LiteBus(
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
  wire  i_slv1_clock; // @[Axi4LiteBus.scala 28:22]
  wire  i_slv1_reset; // @[Axi4LiteBus.scala 28:22]
  wire [15:0] i_slv1_io_i_WriteAddressChannel_AWADDR; // @[Axi4LiteBus.scala 28:22]
  wire  i_slv1_io_i_WriteAddressChannel_AWVALID; // @[Axi4LiteBus.scala 28:22]
  wire [15:0] i_slv1_io_i_WriteDataChannel_WDATA; // @[Axi4LiteBus.scala 28:22]
  wire  i_slv1_io_i_WriteDataChannel_WVALID; // @[Axi4LiteBus.scala 28:22]
  wire [15:0] i_slv1_io_i_ReadDataChannel_RDATA; // @[Axi4LiteBus.scala 28:22]
  wire  i_slv2_clock; // @[Axi4LiteBus.scala 33:22]
  wire  i_slv2_reset; // @[Axi4LiteBus.scala 33:22]
  wire [15:0] i_slv2_io_i_WriteAddressChannel_AWADDR; // @[Axi4LiteBus.scala 33:22]
  wire  i_slv2_io_i_WriteAddressChannel_AWVALID; // @[Axi4LiteBus.scala 33:22]
  wire [15:0] i_slv2_io_i_WriteDataChannel_WDATA; // @[Axi4LiteBus.scala 33:22]
  wire  i_slv2_io_i_WriteDataChannel_WVALID; // @[Axi4LiteBus.scala 33:22]
  wire [15:0] i_slv2_io_i_ReadDataChannel_RDATA; // @[Axi4LiteBus.scala 33:22]
  reg [15:0] r_RDATA; // @[Axi4LiteBus.scala 37:24]
  reg [31:0] _RAND_0;
  wire  _T_2; // @[Axi4LiteBus.scala 39:22]
  wire  _T_3; // @[Axi4LiteBus.scala 39:46]
  wire  _T_4; // @[Axi4LiteBus.scala 39:38]
  wire  _T_8; // @[Axi4LiteBus.scala 59:52]
  wire  _T_9; // @[Axi4LiteBus.scala 59:44]
  wire [15:0] _T_11; // @[Axi4LiteBus.scala 73:56]
  wire  _GEN_18; // @[Axi4LiteBus.scala 59:65]
  wire [15:0] _GEN_19; // @[Axi4LiteBus.scala 59:65]
  wire  _GEN_20; // @[Axi4LiteBus.scala 59:65]
  Axi4LiteSlave i_slv1 ( // @[Axi4LiteBus.scala 28:22]
    .clock(i_slv1_clock),
    .reset(i_slv1_reset),
    .io_i_WriteAddressChannel_AWADDR(i_slv1_io_i_WriteAddressChannel_AWADDR),
    .io_i_WriteAddressChannel_AWVALID(i_slv1_io_i_WriteAddressChannel_AWVALID),
    .io_i_WriteDataChannel_WDATA(i_slv1_io_i_WriteDataChannel_WDATA),
    .io_i_WriteDataChannel_WVALID(i_slv1_io_i_WriteDataChannel_WVALID),
    .io_i_ReadDataChannel_RDATA(i_slv1_io_i_ReadDataChannel_RDATA)
  );
  Axi4LiteSlave i_slv2 ( // @[Axi4LiteBus.scala 33:22]
    .clock(i_slv2_clock),
    .reset(i_slv2_reset),
    .io_i_WriteAddressChannel_AWADDR(i_slv2_io_i_WriteAddressChannel_AWADDR),
    .io_i_WriteAddressChannel_AWVALID(i_slv2_io_i_WriteAddressChannel_AWVALID),
    .io_i_WriteDataChannel_WDATA(i_slv2_io_i_WriteDataChannel_WDATA),
    .io_i_WriteDataChannel_WVALID(i_slv2_io_i_WriteDataChannel_WVALID),
    .io_i_ReadDataChannel_RDATA(i_slv2_io_i_ReadDataChannel_RDATA)
  );
  assign _T_2 = io_i_WriteDataChannel_WVALID & io_i_WriteAddressChannel_AWVALID; // @[Axi4LiteBus.scala 39:22]
  assign _T_3 = io_i_WriteAddressChannel_AWADDR == 16'h4000; // @[Axi4LiteBus.scala 39:46]
  assign _T_4 = _T_2 & _T_3; // @[Axi4LiteBus.scala 39:38]
  assign _T_8 = io_i_WriteAddressChannel_AWADDR == 16'hc000; // @[Axi4LiteBus.scala 59:52]
  assign _T_9 = _T_2 & _T_8; // @[Axi4LiteBus.scala 59:44]
  assign _T_11 = io_i_WriteAddressChannel_AWADDR - 16'h8000; // @[Axi4LiteBus.scala 73:56]
  assign _GEN_18 = _T_9 & io_i_WriteAddressChannel_AWVALID; // @[Axi4LiteBus.scala 59:65]
  assign _GEN_19 = _T_9 ? _T_11 : io_i_WriteAddressChannel_AWADDR; // @[Axi4LiteBus.scala 59:65]
  assign _GEN_20 = _T_9 & io_i_WriteDataChannel_WVALID; // @[Axi4LiteBus.scala 59:65]
  assign io_i_WriteAddressChannel_AWREADY = io_i_WriteAddressChannel_AWVALID; // @[Axi4LiteBus.scala 50:16 Axi4LiteBus.scala 68:39 Axi4LiteBus.scala 95:36]
  assign io_i_WriteDataChannel_WREADY = io_i_WriteDataChannel_WVALID; // @[Axi4LiteBus.scala 50:16 Axi4LiteBus.scala 69:39 Axi4LiteBus.scala 96:36]
  assign io_i_WriteResponseChannel_BRESP = 1'h1; // @[Axi4LiteBus.scala 50:16 Axi4LiteBus.scala 70:39 Axi4LiteBus.scala 97:36]
  assign io_i_WriteResponseChannel_BVALID = 1'h1; // @[Axi4LiteBus.scala 50:16 Axi4LiteBus.scala 70:39 Axi4LiteBus.scala 98:36]
  assign io_i_ReadAddressChannel_ARREADY = 1'h1; // @[Axi4LiteBus.scala 50:16 Axi4LiteBus.scala 71:39 Axi4LiteBus.scala 99:36]
  assign io_i_ReadDataChannel_RDATA = r_RDATA; // @[Axi4LiteBus.scala 50:16 Axi4LiteBus.scala 72:39 Axi4LiteBus.scala 102:36]
  assign io_i_ReadDataChannel_RRESP = 1'h0; // @[Axi4LiteBus.scala 50:16 Axi4LiteBus.scala 72:39 Axi4LiteBus.scala 100:36]
  assign io_i_ReadDataChannel_RVALID = 1'h0; // @[Axi4LiteBus.scala 50:16 Axi4LiteBus.scala 72:39 Axi4LiteBus.scala 101:36]
  assign i_slv1_clock = clock;
  assign i_slv1_reset = reset;
  assign i_slv1_io_i_WriteAddressChannel_AWADDR = io_i_WriteAddressChannel_AWADDR; // @[Axi4LiteBus.scala 29:13]
  assign i_slv1_io_i_WriteAddressChannel_AWVALID = _T_4 & io_i_WriteAddressChannel_AWVALID; // @[Axi4LiteBus.scala 29:13]
  assign i_slv1_io_i_WriteDataChannel_WDATA = io_i_WriteDataChannel_WDATA; // @[Axi4LiteBus.scala 29:13]
  assign i_slv1_io_i_WriteDataChannel_WVALID = _T_4 & io_i_WriteDataChannel_WVALID; // @[Axi4LiteBus.scala 29:13]
  assign i_slv2_clock = clock;
  assign i_slv2_reset = reset;
  assign i_slv2_io_i_WriteAddressChannel_AWADDR = _T_4 ? io_i_WriteAddressChannel_AWADDR : _GEN_19; // @[Axi4LiteBus.scala 34:13]
  assign i_slv2_io_i_WriteAddressChannel_AWVALID = _T_4 ? 1'h0 : _GEN_18; // @[Axi4LiteBus.scala 34:13]
  assign i_slv2_io_i_WriteDataChannel_WDATA = io_i_WriteDataChannel_WDATA; // @[Axi4LiteBus.scala 34:13]
  assign i_slv2_io_i_WriteDataChannel_WVALID = _T_4 ? 1'h0 : _GEN_20; // @[Axi4LiteBus.scala 34:13]
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
  r_RDATA = _RAND_0[15:0];
  `endif // RANDOMIZE_REG_INIT
  `endif // RANDOMIZE
end // initial
`endif // SYNTHESIS
  always @(posedge clock) begin
    if (reset) begin
      r_RDATA <= 16'h0;
    end else if (_T_4) begin
      if (_T_4) begin
        r_RDATA <= i_slv1_io_i_ReadDataChannel_RDATA;
      end else begin
        r_RDATA <= io_i_ReadDataChannel_RDATA;
      end
    end else if (_T_9) begin
      if (_T_4) begin
        r_RDATA <= io_i_ReadDataChannel_RDATA;
      end else if (_T_9) begin
        r_RDATA <= i_slv2_io_i_ReadDataChannel_RDATA;
      end else begin
        r_RDATA <= io_i_ReadDataChannel_RDATA;
      end
    end else begin
      r_RDATA <= 16'h0;
    end
  end
endmodule
