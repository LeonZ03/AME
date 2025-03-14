module CTRL(
  input         clock,
  input         reset,
  output        io_muxCtrlC_0,
  output        io_muxCtrlC_1,
  output        io_muxCtrlC_2,
  output        io_muxCtrlC_3,
  output        io_muxCtrlSum_0,
  output        io_muxCtrlSum_1,
  output        io_muxCtrlSum_2,
  output        io_muxCtrlSum_3,
  output [31:0] io_addrReadA_0,
  output [31:0] io_addrReadA_1,
  output [31:0] io_addrReadA_2,
  output [31:0] io_addrReadA_3,
  output [31:0] io_addrReadB_0,
  output [31:0] io_addrReadB_1,
  output [31:0] io_addrReadB_2,
  output [31:0] io_addrReadB_3,
  output [31:0] io_addrReadC_0,
  output [31:0] io_addrReadC_1,
  output [31:0] io_addrReadC_2,
  output [31:0] io_addrReadC_3,
  output [31:0] io_addrWriteC_0,
  output [31:0] io_addrWriteC_1,
  output [31:0] io_addrWriteC_2,
  output [31:0] io_addrWriteC_3,
  output        io_writeEnC_0,
  output        io_writeEnC_1,
  output        io_writeEnC_2,
  output        io_writeEnC_3
);
`ifdef RANDOMIZE_REG_INIT
  reg [31:0] _RAND_0;
  reg [31:0] _RAND_1;
  reg [31:0] _RAND_2;
  reg [31:0] _RAND_3;
`endif // RANDOMIZE_REG_INIT
  reg [1:0] regOffM; // @[CTRL.scala 25:24]
  reg [1:0] regOffN; // @[CTRL.scala 26:24]
  reg [2:0] regOffK; // @[CTRL.scala 27:24]
  reg [7:0] regOffK_1H; // @[CTRL.scala 28:27]
  wire [7:0] _regOffK_1H_T_2 = {regOffK_1H[6:0],regOffK_1H[7]}; // @[Cat.scala 31:58]
  wire [2:0] _regOffK_T_1 = regOffK + 3'h1; // @[CTRL.scala 31:22]
  wire [1:0] _regOffN_T_3 = regOffN + 2'h1; // @[CTRL.scala 32:60]
  wire [1:0] _regOffM_T_2 = regOffM + 2'h1; // @[CTRL.scala 33:58]
  wire [5:0] _wireAddrReadA_T = regOffM * 4'h8; // @[CTRL.scala 46:28]
  wire [5:0] _GEN_0 = {{3'd0}, regOffK}; // @[CTRL.scala 46:42]
  wire [5:0] _wireAddrReadA_T_2 = _wireAddrReadA_T + _GEN_0; // @[CTRL.scala 46:42]
  wire [5:0] _wireAddrReadB_T = regOffN * 4'h8; // @[CTRL.scala 60:28]
  wire [5:0] _wireAddrReadB_T_2 = _wireAddrReadB_T + _GEN_0; // @[CTRL.scala 60:42]
  assign io_muxCtrlC_0 = regOffK_1H[0]; // @[CTRL.scala 40:33]
  assign io_muxCtrlC_1 = regOffK_1H[1]; // @[CTRL.scala 40:33]
  assign io_muxCtrlC_2 = regOffK_1H[2]; // @[CTRL.scala 40:33]
  assign io_muxCtrlC_3 = regOffK_1H[3]; // @[CTRL.scala 40:33]
  assign io_muxCtrlSum_0 = regOffK_1H[0]; // @[CTRL.scala 41:35]
  assign io_muxCtrlSum_1 = regOffK_1H[1]; // @[CTRL.scala 41:35]
  assign io_muxCtrlSum_2 = regOffK_1H[2]; // @[CTRL.scala 41:35]
  assign io_muxCtrlSum_3 = regOffK_1H[3]; // @[CTRL.scala 41:35]
  assign io_addrReadA_0 = {{26'd0}, _wireAddrReadA_T_2}; // @[CTRL.scala 45:27 46:17]
  assign io_addrReadA_1 = 32'h0; // @[CTRL.scala 48:{26,26}]
  assign io_addrReadA_2 = 32'h0; // @[CTRL.scala 48:{26,26}]
  assign io_addrReadA_3 = 32'h0; // @[CTRL.scala 48:{26,26}]
  assign io_addrReadB_0 = {{26'd0}, _wireAddrReadB_T_2}; // @[CTRL.scala 59:27 60:17]
  assign io_addrReadB_1 = {{26'd0}, _wireAddrReadB_T_2}; // @[CTRL.scala 59:27 60:17]
  assign io_addrReadB_2 = {{26'd0}, _wireAddrReadB_T_2}; // @[CTRL.scala 59:27 60:17]
  assign io_addrReadB_3 = {{26'd0}, _wireAddrReadB_T_2}; // @[CTRL.scala 59:27 60:17]
  assign io_addrReadC_0 = 32'h0; // @[CTRL.scala 22:21]
  assign io_addrReadC_1 = 32'h0; // @[CTRL.scala 22:21]
  assign io_addrReadC_2 = 32'h0; // @[CTRL.scala 22:21]
  assign io_addrReadC_3 = 32'h0; // @[CTRL.scala 22:21]
  assign io_addrWriteC_0 = 32'h0; // @[CTRL.scala 20:22]
  assign io_addrWriteC_1 = 32'h0; // @[CTRL.scala 20:22]
  assign io_addrWriteC_2 = 32'h0; // @[CTRL.scala 20:22]
  assign io_addrWriteC_3 = 32'h0; // @[CTRL.scala 20:22]
  assign io_writeEnC_0 = 1'h0; // @[CTRL.scala 21:20]
  assign io_writeEnC_1 = 1'h0; // @[CTRL.scala 21:20]
  assign io_writeEnC_2 = 1'h0; // @[CTRL.scala 21:20]
  assign io_writeEnC_3 = 1'h0; // @[CTRL.scala 21:20]
  always @(posedge clock) begin
    if (reset) begin // @[CTRL.scala 25:24]
      regOffM <= 2'h0; // @[CTRL.scala 25:24]
    end else if (regOffN == 2'h3) begin // @[CTRL.scala 33:17]
      regOffM <= _regOffM_T_2;
    end
    if (reset) begin // @[CTRL.scala 26:24]
      regOffN <= 2'h0; // @[CTRL.scala 26:24]
    end else if (regOffK_1H[7]) begin // @[CTRL.scala 32:17]
      regOffN <= _regOffN_T_3;
    end
    if (reset) begin // @[CTRL.scala 27:24]
      regOffK <= 3'h0; // @[CTRL.scala 27:24]
    end else begin
      regOffK <= _regOffK_T_1; // @[CTRL.scala 31:11]
    end
    if (reset) begin // @[CTRL.scala 28:27]
      regOffK_1H <= 8'h1; // @[CTRL.scala 28:27]
    end else begin
      regOffK_1H <= _regOffK_1H_T_2; // @[CTRL.scala 30:14]
    end
  end
// Register and memory initialization
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
`ifdef FIRRTL_BEFORE_INITIAL
`FIRRTL_BEFORE_INITIAL
`endif
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
  regOffM = _RAND_0[1:0];
  _RAND_1 = {1{`RANDOM}};
  regOffN = _RAND_1[1:0];
  _RAND_2 = {1{`RANDOM}};
  regOffK = _RAND_2[2:0];
  _RAND_3 = {1{`RANDOM}};
  regOffK_1H = _RAND_3[7:0];
`endif // RANDOMIZE_REG_INIT
  `endif // RANDOMIZE
end // initial
`ifdef FIRRTL_AFTER_INITIAL
`FIRRTL_AFTER_INITIAL
`endif
`endif // SYNTHESIS
endmodule
