module FSM(
  input         clock,
  input         reset,
  input         io_sigStart,
  output        io_muxCtrlC_0_0,
  output        io_muxCtrlC_0_1,
  output        io_muxCtrlC_1_0,
  output        io_muxCtrlC_1_1,
  output        io_muxCtrlC_2_0,
  output        io_muxCtrlC_2_1,
  output        io_muxCtrlC_3_0,
  output        io_muxCtrlC_3_1,
  output        io_muxCtrlC_4_0,
  output        io_muxCtrlC_4_1,
  output        io_muxCtrlC_5_0,
  output        io_muxCtrlC_5_1,
  output        io_muxCtrlC_6_0,
  output        io_muxCtrlC_6_1,
  output        io_muxCtrlC_7_0,
  output        io_muxCtrlC_7_1,
  output        io_muxCtrlSum_0_0,
  output        io_muxCtrlSum_0_1,
  output        io_muxCtrlSum_1_0,
  output        io_muxCtrlSum_1_1,
  output        io_muxCtrlSum_2_0,
  output        io_muxCtrlSum_2_1,
  output        io_muxCtrlSum_3_0,
  output        io_muxCtrlSum_3_1,
  output        io_muxCtrlSum_4_0,
  output        io_muxCtrlSum_4_1,
  output        io_muxCtrlSum_5_0,
  output        io_muxCtrlSum_5_1,
  output        io_muxCtrlSum_6_0,
  output        io_muxCtrlSum_6_1,
  output        io_muxCtrlSum_7_0,
  output        io_muxCtrlSum_7_1,
  output [31:0] io_addrReadA_0,
  output [31:0] io_addrReadA_1,
  output [31:0] io_addrReadA_2,
  output [31:0] io_addrReadA_3,
  output [31:0] io_addrReadA_4,
  output [31:0] io_addrReadA_5,
  output [31:0] io_addrReadA_6,
  output [31:0] io_addrReadA_7,
  output [31:0] io_addrReadB_0,
  output [31:0] io_addrReadB_1,
  output [31:0] io_addrReadC_0,
  output [31:0] io_addrReadC_1,
  output [31:0] io_addrWriteC_0,
  output [31:0] io_addrWriteC_1,
  output        io_sigDone
);
`ifdef RANDOMIZE_REG_INIT
  reg [31:0] _RAND_0;
  reg [31:0] _RAND_1;
  reg [31:0] _RAND_2;
  reg [31:0] _RAND_3;
  reg [31:0] _RAND_4;
  reg [31:0] _RAND_5;
  reg [31:0] _RAND_6;
  reg [31:0] _RAND_7;
  reg [31:0] _RAND_8;
  reg [31:0] _RAND_9;
  reg [31:0] _RAND_10;
  reg [31:0] _RAND_11;
  reg [31:0] _RAND_12;
`endif // RANDOMIZE_REG_INIT
  reg  regOffM; // @[FSM.scala 30:24]
  reg  regOffN; // @[FSM.scala 31:24]
  reg [3:0] regOffK; // @[FSM.scala 32:24]
  reg [15:0] regOffK_1H; // @[FSM.scala 33:27]
  wire [15:0] _regOffK_1H_T_3 = {regOffK_1H[14:0],regOffK_1H[15]}; // @[Cat.scala 31:58]
  wire [3:0] _regOffK_T_2 = regOffK + 4'h1; // @[FSM.scala 36:78]
  reg [7:0] regMuxCrtl_0; // @[FSM.scala 54:25]
  wire [5:0] _wireAddrReadA_T = regOffM * 5'h10; // @[FSM.scala 71:28]
  wire [5:0] _GEN_0 = {{2'd0}, regOffK}; // @[FSM.scala 71:42]
  wire [5:0] _wireAddrReadA_T_2 = _wireAddrReadA_T + _GEN_0; // @[FSM.scala 71:42]
  reg [31:0] regDelayA_0; // @[FSM.scala 73:22]
  reg [31:0] regDelayA_1; // @[FSM.scala 73:22]
  reg [31:0] regDelayA_2; // @[FSM.scala 73:22]
  reg [31:0] regDelayA_3; // @[FSM.scala 73:22]
  reg [31:0] regDelayA_4; // @[FSM.scala 73:22]
  reg [31:0] regDelayA_5; // @[FSM.scala 73:22]
  reg [31:0] regDelayA_6; // @[FSM.scala 73:22]
  wire [5:0] _wireAddrReadB_T = regOffN * 5'h10; // @[FSM.scala 89:28]
  wire [5:0] _wireAddrReadB_T_2 = _wireAddrReadB_T + _GEN_0; // @[FSM.scala 89:42]
  reg [31:0] regDelayB_0; // @[FSM.scala 94:24]
  assign io_muxCtrlC_0_0 = regOffK_1H[0]; // @[FSM.scala 49:36]
  assign io_muxCtrlC_0_1 = regMuxCrtl_0[0]; // @[FSM.scala 61:45]
  assign io_muxCtrlC_1_0 = regOffK_1H[1]; // @[FSM.scala 49:36]
  assign io_muxCtrlC_1_1 = regMuxCrtl_0[1]; // @[FSM.scala 61:45]
  assign io_muxCtrlC_2_0 = regOffK_1H[2]; // @[FSM.scala 49:36]
  assign io_muxCtrlC_2_1 = regMuxCrtl_0[2]; // @[FSM.scala 61:45]
  assign io_muxCtrlC_3_0 = regOffK_1H[3]; // @[FSM.scala 49:36]
  assign io_muxCtrlC_3_1 = regMuxCrtl_0[3]; // @[FSM.scala 61:45]
  assign io_muxCtrlC_4_0 = regOffK_1H[4]; // @[FSM.scala 49:36]
  assign io_muxCtrlC_4_1 = regMuxCrtl_0[4]; // @[FSM.scala 61:45]
  assign io_muxCtrlC_5_0 = regOffK_1H[5]; // @[FSM.scala 49:36]
  assign io_muxCtrlC_5_1 = regMuxCrtl_0[5]; // @[FSM.scala 61:45]
  assign io_muxCtrlC_6_0 = regOffK_1H[6]; // @[FSM.scala 49:36]
  assign io_muxCtrlC_6_1 = regMuxCrtl_0[6]; // @[FSM.scala 61:45]
  assign io_muxCtrlC_7_0 = regOffK_1H[7]; // @[FSM.scala 49:36]
  assign io_muxCtrlC_7_1 = regMuxCrtl_0[7]; // @[FSM.scala 61:45]
  assign io_muxCtrlSum_0_0 = regOffK_1H[0]; // @[FSM.scala 50:38]
  assign io_muxCtrlSum_0_1 = regMuxCrtl_0[0]; // @[FSM.scala 62:47]
  assign io_muxCtrlSum_1_0 = regOffK_1H[1]; // @[FSM.scala 50:38]
  assign io_muxCtrlSum_1_1 = regMuxCrtl_0[1]; // @[FSM.scala 62:47]
  assign io_muxCtrlSum_2_0 = regOffK_1H[2]; // @[FSM.scala 50:38]
  assign io_muxCtrlSum_2_1 = regMuxCrtl_0[2]; // @[FSM.scala 62:47]
  assign io_muxCtrlSum_3_0 = regOffK_1H[3]; // @[FSM.scala 50:38]
  assign io_muxCtrlSum_3_1 = regMuxCrtl_0[3]; // @[FSM.scala 62:47]
  assign io_muxCtrlSum_4_0 = regOffK_1H[4]; // @[FSM.scala 50:38]
  assign io_muxCtrlSum_4_1 = regMuxCrtl_0[4]; // @[FSM.scala 62:47]
  assign io_muxCtrlSum_5_0 = regOffK_1H[5]; // @[FSM.scala 50:38]
  assign io_muxCtrlSum_5_1 = regMuxCrtl_0[5]; // @[FSM.scala 62:47]
  assign io_muxCtrlSum_6_0 = regOffK_1H[6]; // @[FSM.scala 50:38]
  assign io_muxCtrlSum_6_1 = regMuxCrtl_0[6]; // @[FSM.scala 62:47]
  assign io_muxCtrlSum_7_0 = regOffK_1H[7]; // @[FSM.scala 50:38]
  assign io_muxCtrlSum_7_1 = regMuxCrtl_0[7]; // @[FSM.scala 62:47]
  assign io_addrReadA_0 = {{26'd0}, _wireAddrReadA_T_2}; // @[FSM.scala 70:27 71:17]
  assign io_addrReadA_1 = regDelayA_0; // @[FSM.scala 83:21]
  assign io_addrReadA_2 = regDelayA_1; // @[FSM.scala 83:21]
  assign io_addrReadA_3 = regDelayA_2; // @[FSM.scala 83:21]
  assign io_addrReadA_4 = regDelayA_3; // @[FSM.scala 83:21]
  assign io_addrReadA_5 = regDelayA_4; // @[FSM.scala 83:21]
  assign io_addrReadA_6 = regDelayA_5; // @[FSM.scala 83:21]
  assign io_addrReadA_7 = regDelayA_6; // @[FSM.scala 83:21]
  assign io_addrReadB_0 = {{26'd0}, _wireAddrReadB_T_2}; // @[FSM.scala 88:27 89:17]
  assign io_addrReadB_1 = regDelayB_0; // @[FSM.scala 103:23]
  assign io_addrReadC_0 = 32'h0; // @[FSM.scala 27:21]
  assign io_addrReadC_1 = 32'h0; // @[FSM.scala 27:21]
  assign io_addrWriteC_0 = 32'h0; // @[FSM.scala 26:22]
  assign io_addrWriteC_1 = 32'h0; // @[FSM.scala 26:22]
  assign io_sigDone = ~regOffM & ~regOffN & regOffK_1H[0] & ~io_sigStart; // @[FSM.scala 42:81]
  always @(posedge clock) begin
    if (reset) begin // @[FSM.scala 30:24]
      regOffM <= 1'h0; // @[FSM.scala 30:24]
    end else if (io_sigStart) begin // @[FSM.scala 39:17]
      regOffM <= 1'h0;
    end else if (regOffN & regOffK_1H[15]) begin // @[FSM.scala 40:20]
      regOffM <= regOffM + 1'h1;
    end
    if (reset) begin // @[FSM.scala 31:24]
      regOffN <= 1'h0; // @[FSM.scala 31:24]
    end else if (io_sigStart) begin // @[FSM.scala 37:17]
      regOffN <= 1'h0;
    end else if (regOffK_1H[15]) begin // @[FSM.scala 38:20]
      regOffN <= regOffN + 1'h1;
    end
    if (reset) begin // @[FSM.scala 32:24]
      regOffK <= 4'h0; // @[FSM.scala 32:24]
    end else if (io_sigStart) begin // @[FSM.scala 36:17]
      regOffK <= 4'h0;
    end else begin
      regOffK <= _regOffK_T_2;
    end
    if (reset) begin // @[FSM.scala 33:27]
      regOffK_1H <= 16'h1; // @[FSM.scala 33:27]
    end else if (io_sigStart) begin // @[FSM.scala 35:20]
      regOffK_1H <= 16'h1;
    end else begin
      regOffK_1H <= _regOffK_1H_T_3;
    end
    regMuxCrtl_0 <= regOffK_1H[7:0]; // @[FSM.scala 57:32]
    regDelayA_0 <= {{26'd0}, _wireAddrReadA_T_2}; // @[FSM.scala 70:27 71:17]
    regDelayA_1 <= regDelayA_0; // @[FSM.scala 78:18]
    regDelayA_2 <= regDelayA_1; // @[FSM.scala 78:18]
    regDelayA_3 <= regDelayA_2; // @[FSM.scala 78:18]
    regDelayA_4 <= regDelayA_3; // @[FSM.scala 78:18]
    regDelayA_5 <= regDelayA_4; // @[FSM.scala 78:18]
    regDelayA_6 <= regDelayA_5; // @[FSM.scala 78:18]
    regDelayB_0 <= {{26'd0}, _wireAddrReadB_T_2}; // @[FSM.scala 88:27 89:17]
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
  regOffM = _RAND_0[0:0];
  _RAND_1 = {1{`RANDOM}};
  regOffN = _RAND_1[0:0];
  _RAND_2 = {1{`RANDOM}};
  regOffK = _RAND_2[3:0];
  _RAND_3 = {1{`RANDOM}};
  regOffK_1H = _RAND_3[15:0];
  _RAND_4 = {1{`RANDOM}};
  regMuxCrtl_0 = _RAND_4[7:0];
  _RAND_5 = {1{`RANDOM}};
  regDelayA_0 = _RAND_5[31:0];
  _RAND_6 = {1{`RANDOM}};
  regDelayA_1 = _RAND_6[31:0];
  _RAND_7 = {1{`RANDOM}};
  regDelayA_2 = _RAND_7[31:0];
  _RAND_8 = {1{`RANDOM}};
  regDelayA_3 = _RAND_8[31:0];
  _RAND_9 = {1{`RANDOM}};
  regDelayA_4 = _RAND_9[31:0];
  _RAND_10 = {1{`RANDOM}};
  regDelayA_5 = _RAND_10[31:0];
  _RAND_11 = {1{`RANDOM}};
  regDelayA_6 = _RAND_11[31:0];
  _RAND_12 = {1{`RANDOM}};
  regDelayB_0 = _RAND_12[31:0];
`endif // RANDOMIZE_REG_INIT
  `endif // RANDOMIZE
end // initial
`ifdef FIRRTL_AFTER_INITIAL
`FIRRTL_AFTER_INITIAL
`endif
`endif // SYNTHESIS
endmodule
