module DPA(
  input  [31:0] io_vecA,
  input  [31:0] io_vecB,
  output [31:0] io_eleC
);
  wire [7:0] vecA_parts_0 = io_vecA[7:0]; // @[DPA.scala 16:69]
  wire [7:0] vecA_parts_1 = io_vecA[15:8]; // @[DPA.scala 16:69]
  wire [7:0] vecA_parts_2 = io_vecA[23:16]; // @[DPA.scala 16:69]
  wire [7:0] vecA_parts_3 = io_vecA[31:24]; // @[DPA.scala 16:69]
  wire [7:0] vecB_parts_0 = io_vecB[7:0]; // @[DPA.scala 17:69]
  wire [7:0] vecB_parts_1 = io_vecB[15:8]; // @[DPA.scala 17:69]
  wire [7:0] vecB_parts_2 = io_vecB[23:16]; // @[DPA.scala 17:69]
  wire [7:0] vecB_parts_3 = io_vecB[31:24]; // @[DPA.scala 17:69]
  wire [15:0] products_0 = $signed(vecA_parts_0) * $signed(vecB_parts_0); // @[DPA.scala 20:68]
  wire [15:0] products_1 = $signed(vecA_parts_1) * $signed(vecB_parts_1); // @[DPA.scala 20:68]
  wire [15:0] products_2 = $signed(vecA_parts_2) * $signed(vecB_parts_2); // @[DPA.scala 20:68]
  wire [15:0] products_3 = $signed(vecA_parts_3) * $signed(vecB_parts_3); // @[DPA.scala 20:68]
  wire [15:0] _io_eleC_T_2 = $signed(products_0) + $signed(products_1); // @[DPA.scala 23:32]
  wire [15:0] _io_eleC_T_5 = $signed(_io_eleC_T_2) + $signed(products_2); // @[DPA.scala 23:32]
  wire [15:0] _io_eleC_T_8 = $signed(_io_eleC_T_5) + $signed(products_3); // @[DPA.scala 23:32]
  assign io_eleC = {{16{_io_eleC_T_8[15]}},_io_eleC_T_8}; // @[DPA.scala 23:11]
endmodule
module PEcube(
  input         clock,
  input         reset,
  input  [31:0] io_vecAin,
  input  [31:0] io_vecBin,
  input  [31:0] io_eleCin,
  input         io_muxCtrl,
  output [31:0] io_vecAout,
  output [31:0] io_vecBout,
  output [31:0] io_eleCout
);
`ifdef RANDOMIZE_REG_INIT
  reg [31:0] _RAND_0;
`endif // RANDOMIZE_REG_INIT
  wire [31:0] subDPA_io_vecA; // @[PE.scala 21:22]
  wire [31:0] subDPA_io_vecB; // @[PE.scala 21:22]
  wire [31:0] subDPA_io_eleC; // @[PE.scala 21:22]
  reg [31:0] regR; // @[PE.scala 22:21]
  DPA subDPA ( // @[PE.scala 21:22]
    .io_vecA(subDPA_io_vecA),
    .io_vecB(subDPA_io_vecB),
    .io_eleC(subDPA_io_eleC)
  );
  assign io_vecAout = io_vecAin; // @[PE.scala 28:14]
  assign io_vecBout = regR; // @[PE.scala 30:14]
  assign io_eleCout = io_muxCtrl ? $signed(subDPA_io_eleC) : $signed(io_eleCin); // @[PE.scala 26:20]
  assign subDPA_io_vecA = io_vecAin; // @[PE.scala 24:18]
  assign subDPA_io_vecB = io_vecBin; // @[PE.scala 25:18]
  always @(posedge clock) begin
    if (reset) begin // @[PE.scala 22:21]
      regR <= 32'h0; // @[PE.scala 22:21]
    end else begin
      regR <= io_vecBin; // @[PE.scala 29:8]
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
  regR = _RAND_0[31:0];
`endif // RANDOMIZE_REG_INIT
  `endif // RANDOMIZE
end // initial
`ifdef FIRRTL_AFTER_INITIAL
`FIRRTL_AFTER_INITIAL
`endif
`endif // SYNTHESIS
endmodule
module CUBE(
  input         clock,
  input         reset,
  input  [31:0] io_vecA_0,
  input  [31:0] io_vecA_1,
  input  [31:0] io_vecA_2,
  input  [31:0] io_vecA_3,
  input  [31:0] io_vecB_0,
  input  [31:0] io_vecB_1,
  input  [31:0] io_vecB_2,
  input  [31:0] io_vecB_3,
  input         io_muxCtrl_0,
  input         io_muxCtrl_1,
  input         io_muxCtrl_2,
  input         io_muxCtrl_3,
  input         io_muxCtrl_4,
  input         io_muxCtrl_5,
  input         io_muxCtrl_6,
  input         io_muxCtrl_7,
  input         io_muxCtrl_8,
  input         io_muxCtrl_9,
  input         io_muxCtrl_10,
  input         io_muxCtrl_11,
  input         io_muxCtrl_12,
  input         io_muxCtrl_13,
  input         io_muxCtrl_14,
  input         io_muxCtrl_15,
  output [31:0] io_eleC_0,
  output [31:0] io_eleC_1,
  output [31:0] io_eleC_2,
  output [31:0] io_eleC_3
);
  wire  PEcube_clock; // @[CUBE.scala 21:42]
  wire  PEcube_reset; // @[CUBE.scala 21:42]
  wire [31:0] PEcube_io_vecAin; // @[CUBE.scala 21:42]
  wire [31:0] PEcube_io_vecBin; // @[CUBE.scala 21:42]
  wire [31:0] PEcube_io_eleCin; // @[CUBE.scala 21:42]
  wire  PEcube_io_muxCtrl; // @[CUBE.scala 21:42]
  wire [31:0] PEcube_io_vecAout; // @[CUBE.scala 21:42]
  wire [31:0] PEcube_io_vecBout; // @[CUBE.scala 21:42]
  wire [31:0] PEcube_io_eleCout; // @[CUBE.scala 21:42]
  wire  PEcube_1_clock; // @[CUBE.scala 21:42]
  wire  PEcube_1_reset; // @[CUBE.scala 21:42]
  wire [31:0] PEcube_1_io_vecAin; // @[CUBE.scala 21:42]
  wire [31:0] PEcube_1_io_vecBin; // @[CUBE.scala 21:42]
  wire [31:0] PEcube_1_io_eleCin; // @[CUBE.scala 21:42]
  wire  PEcube_1_io_muxCtrl; // @[CUBE.scala 21:42]
  wire [31:0] PEcube_1_io_vecAout; // @[CUBE.scala 21:42]
  wire [31:0] PEcube_1_io_vecBout; // @[CUBE.scala 21:42]
  wire [31:0] PEcube_1_io_eleCout; // @[CUBE.scala 21:42]
  wire  PEcube_2_clock; // @[CUBE.scala 21:42]
  wire  PEcube_2_reset; // @[CUBE.scala 21:42]
  wire [31:0] PEcube_2_io_vecAin; // @[CUBE.scala 21:42]
  wire [31:0] PEcube_2_io_vecBin; // @[CUBE.scala 21:42]
  wire [31:0] PEcube_2_io_eleCin; // @[CUBE.scala 21:42]
  wire  PEcube_2_io_muxCtrl; // @[CUBE.scala 21:42]
  wire [31:0] PEcube_2_io_vecAout; // @[CUBE.scala 21:42]
  wire [31:0] PEcube_2_io_vecBout; // @[CUBE.scala 21:42]
  wire [31:0] PEcube_2_io_eleCout; // @[CUBE.scala 21:42]
  wire  PEcube_3_clock; // @[CUBE.scala 21:42]
  wire  PEcube_3_reset; // @[CUBE.scala 21:42]
  wire [31:0] PEcube_3_io_vecAin; // @[CUBE.scala 21:42]
  wire [31:0] PEcube_3_io_vecBin; // @[CUBE.scala 21:42]
  wire [31:0] PEcube_3_io_eleCin; // @[CUBE.scala 21:42]
  wire  PEcube_3_io_muxCtrl; // @[CUBE.scala 21:42]
  wire [31:0] PEcube_3_io_vecAout; // @[CUBE.scala 21:42]
  wire [31:0] PEcube_3_io_vecBout; // @[CUBE.scala 21:42]
  wire [31:0] PEcube_3_io_eleCout; // @[CUBE.scala 21:42]
  wire  PEcube_4_clock; // @[CUBE.scala 21:42]
  wire  PEcube_4_reset; // @[CUBE.scala 21:42]
  wire [31:0] PEcube_4_io_vecAin; // @[CUBE.scala 21:42]
  wire [31:0] PEcube_4_io_vecBin; // @[CUBE.scala 21:42]
  wire [31:0] PEcube_4_io_eleCin; // @[CUBE.scala 21:42]
  wire  PEcube_4_io_muxCtrl; // @[CUBE.scala 21:42]
  wire [31:0] PEcube_4_io_vecAout; // @[CUBE.scala 21:42]
  wire [31:0] PEcube_4_io_vecBout; // @[CUBE.scala 21:42]
  wire [31:0] PEcube_4_io_eleCout; // @[CUBE.scala 21:42]
  wire  PEcube_5_clock; // @[CUBE.scala 21:42]
  wire  PEcube_5_reset; // @[CUBE.scala 21:42]
  wire [31:0] PEcube_5_io_vecAin; // @[CUBE.scala 21:42]
  wire [31:0] PEcube_5_io_vecBin; // @[CUBE.scala 21:42]
  wire [31:0] PEcube_5_io_eleCin; // @[CUBE.scala 21:42]
  wire  PEcube_5_io_muxCtrl; // @[CUBE.scala 21:42]
  wire [31:0] PEcube_5_io_vecAout; // @[CUBE.scala 21:42]
  wire [31:0] PEcube_5_io_vecBout; // @[CUBE.scala 21:42]
  wire [31:0] PEcube_5_io_eleCout; // @[CUBE.scala 21:42]
  wire  PEcube_6_clock; // @[CUBE.scala 21:42]
  wire  PEcube_6_reset; // @[CUBE.scala 21:42]
  wire [31:0] PEcube_6_io_vecAin; // @[CUBE.scala 21:42]
  wire [31:0] PEcube_6_io_vecBin; // @[CUBE.scala 21:42]
  wire [31:0] PEcube_6_io_eleCin; // @[CUBE.scala 21:42]
  wire  PEcube_6_io_muxCtrl; // @[CUBE.scala 21:42]
  wire [31:0] PEcube_6_io_vecAout; // @[CUBE.scala 21:42]
  wire [31:0] PEcube_6_io_vecBout; // @[CUBE.scala 21:42]
  wire [31:0] PEcube_6_io_eleCout; // @[CUBE.scala 21:42]
  wire  PEcube_7_clock; // @[CUBE.scala 21:42]
  wire  PEcube_7_reset; // @[CUBE.scala 21:42]
  wire [31:0] PEcube_7_io_vecAin; // @[CUBE.scala 21:42]
  wire [31:0] PEcube_7_io_vecBin; // @[CUBE.scala 21:42]
  wire [31:0] PEcube_7_io_eleCin; // @[CUBE.scala 21:42]
  wire  PEcube_7_io_muxCtrl; // @[CUBE.scala 21:42]
  wire [31:0] PEcube_7_io_vecAout; // @[CUBE.scala 21:42]
  wire [31:0] PEcube_7_io_vecBout; // @[CUBE.scala 21:42]
  wire [31:0] PEcube_7_io_eleCout; // @[CUBE.scala 21:42]
  wire  PEcube_8_clock; // @[CUBE.scala 21:42]
  wire  PEcube_8_reset; // @[CUBE.scala 21:42]
  wire [31:0] PEcube_8_io_vecAin; // @[CUBE.scala 21:42]
  wire [31:0] PEcube_8_io_vecBin; // @[CUBE.scala 21:42]
  wire [31:0] PEcube_8_io_eleCin; // @[CUBE.scala 21:42]
  wire  PEcube_8_io_muxCtrl; // @[CUBE.scala 21:42]
  wire [31:0] PEcube_8_io_vecAout; // @[CUBE.scala 21:42]
  wire [31:0] PEcube_8_io_vecBout; // @[CUBE.scala 21:42]
  wire [31:0] PEcube_8_io_eleCout; // @[CUBE.scala 21:42]
  wire  PEcube_9_clock; // @[CUBE.scala 21:42]
  wire  PEcube_9_reset; // @[CUBE.scala 21:42]
  wire [31:0] PEcube_9_io_vecAin; // @[CUBE.scala 21:42]
  wire [31:0] PEcube_9_io_vecBin; // @[CUBE.scala 21:42]
  wire [31:0] PEcube_9_io_eleCin; // @[CUBE.scala 21:42]
  wire  PEcube_9_io_muxCtrl; // @[CUBE.scala 21:42]
  wire [31:0] PEcube_9_io_vecAout; // @[CUBE.scala 21:42]
  wire [31:0] PEcube_9_io_vecBout; // @[CUBE.scala 21:42]
  wire [31:0] PEcube_9_io_eleCout; // @[CUBE.scala 21:42]
  wire  PEcube_10_clock; // @[CUBE.scala 21:42]
  wire  PEcube_10_reset; // @[CUBE.scala 21:42]
  wire [31:0] PEcube_10_io_vecAin; // @[CUBE.scala 21:42]
  wire [31:0] PEcube_10_io_vecBin; // @[CUBE.scala 21:42]
  wire [31:0] PEcube_10_io_eleCin; // @[CUBE.scala 21:42]
  wire  PEcube_10_io_muxCtrl; // @[CUBE.scala 21:42]
  wire [31:0] PEcube_10_io_vecAout; // @[CUBE.scala 21:42]
  wire [31:0] PEcube_10_io_vecBout; // @[CUBE.scala 21:42]
  wire [31:0] PEcube_10_io_eleCout; // @[CUBE.scala 21:42]
  wire  PEcube_11_clock; // @[CUBE.scala 21:42]
  wire  PEcube_11_reset; // @[CUBE.scala 21:42]
  wire [31:0] PEcube_11_io_vecAin; // @[CUBE.scala 21:42]
  wire [31:0] PEcube_11_io_vecBin; // @[CUBE.scala 21:42]
  wire [31:0] PEcube_11_io_eleCin; // @[CUBE.scala 21:42]
  wire  PEcube_11_io_muxCtrl; // @[CUBE.scala 21:42]
  wire [31:0] PEcube_11_io_vecAout; // @[CUBE.scala 21:42]
  wire [31:0] PEcube_11_io_vecBout; // @[CUBE.scala 21:42]
  wire [31:0] PEcube_11_io_eleCout; // @[CUBE.scala 21:42]
  wire  PEcube_12_clock; // @[CUBE.scala 21:42]
  wire  PEcube_12_reset; // @[CUBE.scala 21:42]
  wire [31:0] PEcube_12_io_vecAin; // @[CUBE.scala 21:42]
  wire [31:0] PEcube_12_io_vecBin; // @[CUBE.scala 21:42]
  wire [31:0] PEcube_12_io_eleCin; // @[CUBE.scala 21:42]
  wire  PEcube_12_io_muxCtrl; // @[CUBE.scala 21:42]
  wire [31:0] PEcube_12_io_vecAout; // @[CUBE.scala 21:42]
  wire [31:0] PEcube_12_io_vecBout; // @[CUBE.scala 21:42]
  wire [31:0] PEcube_12_io_eleCout; // @[CUBE.scala 21:42]
  wire  PEcube_13_clock; // @[CUBE.scala 21:42]
  wire  PEcube_13_reset; // @[CUBE.scala 21:42]
  wire [31:0] PEcube_13_io_vecAin; // @[CUBE.scala 21:42]
  wire [31:0] PEcube_13_io_vecBin; // @[CUBE.scala 21:42]
  wire [31:0] PEcube_13_io_eleCin; // @[CUBE.scala 21:42]
  wire  PEcube_13_io_muxCtrl; // @[CUBE.scala 21:42]
  wire [31:0] PEcube_13_io_vecAout; // @[CUBE.scala 21:42]
  wire [31:0] PEcube_13_io_vecBout; // @[CUBE.scala 21:42]
  wire [31:0] PEcube_13_io_eleCout; // @[CUBE.scala 21:42]
  wire  PEcube_14_clock; // @[CUBE.scala 21:42]
  wire  PEcube_14_reset; // @[CUBE.scala 21:42]
  wire [31:0] PEcube_14_io_vecAin; // @[CUBE.scala 21:42]
  wire [31:0] PEcube_14_io_vecBin; // @[CUBE.scala 21:42]
  wire [31:0] PEcube_14_io_eleCin; // @[CUBE.scala 21:42]
  wire  PEcube_14_io_muxCtrl; // @[CUBE.scala 21:42]
  wire [31:0] PEcube_14_io_vecAout; // @[CUBE.scala 21:42]
  wire [31:0] PEcube_14_io_vecBout; // @[CUBE.scala 21:42]
  wire [31:0] PEcube_14_io_eleCout; // @[CUBE.scala 21:42]
  wire  PEcube_15_clock; // @[CUBE.scala 21:42]
  wire  PEcube_15_reset; // @[CUBE.scala 21:42]
  wire [31:0] PEcube_15_io_vecAin; // @[CUBE.scala 21:42]
  wire [31:0] PEcube_15_io_vecBin; // @[CUBE.scala 21:42]
  wire [31:0] PEcube_15_io_eleCin; // @[CUBE.scala 21:42]
  wire  PEcube_15_io_muxCtrl; // @[CUBE.scala 21:42]
  wire [31:0] PEcube_15_io_vecAout; // @[CUBE.scala 21:42]
  wire [31:0] PEcube_15_io_vecBout; // @[CUBE.scala 21:42]
  wire [31:0] PEcube_15_io_eleCout; // @[CUBE.scala 21:42]
  PEcube PEcube ( // @[CUBE.scala 21:42]
    .clock(PEcube_clock),
    .reset(PEcube_reset),
    .io_vecAin(PEcube_io_vecAin),
    .io_vecBin(PEcube_io_vecBin),
    .io_eleCin(PEcube_io_eleCin),
    .io_muxCtrl(PEcube_io_muxCtrl),
    .io_vecAout(PEcube_io_vecAout),
    .io_vecBout(PEcube_io_vecBout),
    .io_eleCout(PEcube_io_eleCout)
  );
  PEcube PEcube_1 ( // @[CUBE.scala 21:42]
    .clock(PEcube_1_clock),
    .reset(PEcube_1_reset),
    .io_vecAin(PEcube_1_io_vecAin),
    .io_vecBin(PEcube_1_io_vecBin),
    .io_eleCin(PEcube_1_io_eleCin),
    .io_muxCtrl(PEcube_1_io_muxCtrl),
    .io_vecAout(PEcube_1_io_vecAout),
    .io_vecBout(PEcube_1_io_vecBout),
    .io_eleCout(PEcube_1_io_eleCout)
  );
  PEcube PEcube_2 ( // @[CUBE.scala 21:42]
    .clock(PEcube_2_clock),
    .reset(PEcube_2_reset),
    .io_vecAin(PEcube_2_io_vecAin),
    .io_vecBin(PEcube_2_io_vecBin),
    .io_eleCin(PEcube_2_io_eleCin),
    .io_muxCtrl(PEcube_2_io_muxCtrl),
    .io_vecAout(PEcube_2_io_vecAout),
    .io_vecBout(PEcube_2_io_vecBout),
    .io_eleCout(PEcube_2_io_eleCout)
  );
  PEcube PEcube_3 ( // @[CUBE.scala 21:42]
    .clock(PEcube_3_clock),
    .reset(PEcube_3_reset),
    .io_vecAin(PEcube_3_io_vecAin),
    .io_vecBin(PEcube_3_io_vecBin),
    .io_eleCin(PEcube_3_io_eleCin),
    .io_muxCtrl(PEcube_3_io_muxCtrl),
    .io_vecAout(PEcube_3_io_vecAout),
    .io_vecBout(PEcube_3_io_vecBout),
    .io_eleCout(PEcube_3_io_eleCout)
  );
  PEcube PEcube_4 ( // @[CUBE.scala 21:42]
    .clock(PEcube_4_clock),
    .reset(PEcube_4_reset),
    .io_vecAin(PEcube_4_io_vecAin),
    .io_vecBin(PEcube_4_io_vecBin),
    .io_eleCin(PEcube_4_io_eleCin),
    .io_muxCtrl(PEcube_4_io_muxCtrl),
    .io_vecAout(PEcube_4_io_vecAout),
    .io_vecBout(PEcube_4_io_vecBout),
    .io_eleCout(PEcube_4_io_eleCout)
  );
  PEcube PEcube_5 ( // @[CUBE.scala 21:42]
    .clock(PEcube_5_clock),
    .reset(PEcube_5_reset),
    .io_vecAin(PEcube_5_io_vecAin),
    .io_vecBin(PEcube_5_io_vecBin),
    .io_eleCin(PEcube_5_io_eleCin),
    .io_muxCtrl(PEcube_5_io_muxCtrl),
    .io_vecAout(PEcube_5_io_vecAout),
    .io_vecBout(PEcube_5_io_vecBout),
    .io_eleCout(PEcube_5_io_eleCout)
  );
  PEcube PEcube_6 ( // @[CUBE.scala 21:42]
    .clock(PEcube_6_clock),
    .reset(PEcube_6_reset),
    .io_vecAin(PEcube_6_io_vecAin),
    .io_vecBin(PEcube_6_io_vecBin),
    .io_eleCin(PEcube_6_io_eleCin),
    .io_muxCtrl(PEcube_6_io_muxCtrl),
    .io_vecAout(PEcube_6_io_vecAout),
    .io_vecBout(PEcube_6_io_vecBout),
    .io_eleCout(PEcube_6_io_eleCout)
  );
  PEcube PEcube_7 ( // @[CUBE.scala 21:42]
    .clock(PEcube_7_clock),
    .reset(PEcube_7_reset),
    .io_vecAin(PEcube_7_io_vecAin),
    .io_vecBin(PEcube_7_io_vecBin),
    .io_eleCin(PEcube_7_io_eleCin),
    .io_muxCtrl(PEcube_7_io_muxCtrl),
    .io_vecAout(PEcube_7_io_vecAout),
    .io_vecBout(PEcube_7_io_vecBout),
    .io_eleCout(PEcube_7_io_eleCout)
  );
  PEcube PEcube_8 ( // @[CUBE.scala 21:42]
    .clock(PEcube_8_clock),
    .reset(PEcube_8_reset),
    .io_vecAin(PEcube_8_io_vecAin),
    .io_vecBin(PEcube_8_io_vecBin),
    .io_eleCin(PEcube_8_io_eleCin),
    .io_muxCtrl(PEcube_8_io_muxCtrl),
    .io_vecAout(PEcube_8_io_vecAout),
    .io_vecBout(PEcube_8_io_vecBout),
    .io_eleCout(PEcube_8_io_eleCout)
  );
  PEcube PEcube_9 ( // @[CUBE.scala 21:42]
    .clock(PEcube_9_clock),
    .reset(PEcube_9_reset),
    .io_vecAin(PEcube_9_io_vecAin),
    .io_vecBin(PEcube_9_io_vecBin),
    .io_eleCin(PEcube_9_io_eleCin),
    .io_muxCtrl(PEcube_9_io_muxCtrl),
    .io_vecAout(PEcube_9_io_vecAout),
    .io_vecBout(PEcube_9_io_vecBout),
    .io_eleCout(PEcube_9_io_eleCout)
  );
  PEcube PEcube_10 ( // @[CUBE.scala 21:42]
    .clock(PEcube_10_clock),
    .reset(PEcube_10_reset),
    .io_vecAin(PEcube_10_io_vecAin),
    .io_vecBin(PEcube_10_io_vecBin),
    .io_eleCin(PEcube_10_io_eleCin),
    .io_muxCtrl(PEcube_10_io_muxCtrl),
    .io_vecAout(PEcube_10_io_vecAout),
    .io_vecBout(PEcube_10_io_vecBout),
    .io_eleCout(PEcube_10_io_eleCout)
  );
  PEcube PEcube_11 ( // @[CUBE.scala 21:42]
    .clock(PEcube_11_clock),
    .reset(PEcube_11_reset),
    .io_vecAin(PEcube_11_io_vecAin),
    .io_vecBin(PEcube_11_io_vecBin),
    .io_eleCin(PEcube_11_io_eleCin),
    .io_muxCtrl(PEcube_11_io_muxCtrl),
    .io_vecAout(PEcube_11_io_vecAout),
    .io_vecBout(PEcube_11_io_vecBout),
    .io_eleCout(PEcube_11_io_eleCout)
  );
  PEcube PEcube_12 ( // @[CUBE.scala 21:42]
    .clock(PEcube_12_clock),
    .reset(PEcube_12_reset),
    .io_vecAin(PEcube_12_io_vecAin),
    .io_vecBin(PEcube_12_io_vecBin),
    .io_eleCin(PEcube_12_io_eleCin),
    .io_muxCtrl(PEcube_12_io_muxCtrl),
    .io_vecAout(PEcube_12_io_vecAout),
    .io_vecBout(PEcube_12_io_vecBout),
    .io_eleCout(PEcube_12_io_eleCout)
  );
  PEcube PEcube_13 ( // @[CUBE.scala 21:42]
    .clock(PEcube_13_clock),
    .reset(PEcube_13_reset),
    .io_vecAin(PEcube_13_io_vecAin),
    .io_vecBin(PEcube_13_io_vecBin),
    .io_eleCin(PEcube_13_io_eleCin),
    .io_muxCtrl(PEcube_13_io_muxCtrl),
    .io_vecAout(PEcube_13_io_vecAout),
    .io_vecBout(PEcube_13_io_vecBout),
    .io_eleCout(PEcube_13_io_eleCout)
  );
  PEcube PEcube_14 ( // @[CUBE.scala 21:42]
    .clock(PEcube_14_clock),
    .reset(PEcube_14_reset),
    .io_vecAin(PEcube_14_io_vecAin),
    .io_vecBin(PEcube_14_io_vecBin),
    .io_eleCin(PEcube_14_io_eleCin),
    .io_muxCtrl(PEcube_14_io_muxCtrl),
    .io_vecAout(PEcube_14_io_vecAout),
    .io_vecBout(PEcube_14_io_vecBout),
    .io_eleCout(PEcube_14_io_eleCout)
  );
  PEcube PEcube_15 ( // @[CUBE.scala 21:42]
    .clock(PEcube_15_clock),
    .reset(PEcube_15_reset),
    .io_vecAin(PEcube_15_io_vecAin),
    .io_vecBin(PEcube_15_io_vecBin),
    .io_eleCin(PEcube_15_io_eleCin),
    .io_muxCtrl(PEcube_15_io_muxCtrl),
    .io_vecAout(PEcube_15_io_vecAout),
    .io_vecBout(PEcube_15_io_vecBout),
    .io_eleCout(PEcube_15_io_eleCout)
  );
  assign io_eleC_0 = PEcube_12_io_eleCout; // @[CUBE.scala 60:16]
  assign io_eleC_1 = PEcube_13_io_eleCout; // @[CUBE.scala 60:16]
  assign io_eleC_2 = PEcube_14_io_eleCout; // @[CUBE.scala 60:16]
  assign io_eleC_3 = PEcube_15_io_eleCout; // @[CUBE.scala 60:16]
  assign PEcube_clock = clock;
  assign PEcube_reset = reset;
  assign PEcube_io_vecAin = io_vecA_0; // @[CUBE.scala 51:29]
  assign PEcube_io_vecBin = io_vecB_0; // @[CUBE.scala 55:29]
  assign PEcube_io_eleCin = 32'sh0; // @[CUBE.scala 34:22]
  assign PEcube_io_muxCtrl = io_muxCtrl_0; // @[CUBE.scala 28:21]
  assign PEcube_1_clock = clock;
  assign PEcube_1_reset = reset;
  assign PEcube_1_io_vecAin = PEcube_io_vecAout; // @[CUBE.scala 44:22]
  assign PEcube_1_io_vecBin = io_vecB_1; // @[CUBE.scala 55:29]
  assign PEcube_1_io_eleCin = 32'sh0; // @[CUBE.scala 34:22]
  assign PEcube_1_io_muxCtrl = io_muxCtrl_1; // @[CUBE.scala 28:21]
  assign PEcube_2_clock = clock;
  assign PEcube_2_reset = reset;
  assign PEcube_2_io_vecAin = PEcube_1_io_vecAout; // @[CUBE.scala 44:22]
  assign PEcube_2_io_vecBin = io_vecB_2; // @[CUBE.scala 55:29]
  assign PEcube_2_io_eleCin = 32'sh0; // @[CUBE.scala 34:22]
  assign PEcube_2_io_muxCtrl = io_muxCtrl_2; // @[CUBE.scala 28:21]
  assign PEcube_3_clock = clock;
  assign PEcube_3_reset = reset;
  assign PEcube_3_io_vecAin = PEcube_2_io_vecAout; // @[CUBE.scala 44:22]
  assign PEcube_3_io_vecBin = io_vecB_3; // @[CUBE.scala 55:29]
  assign PEcube_3_io_eleCin = 32'sh0; // @[CUBE.scala 34:22]
  assign PEcube_3_io_muxCtrl = io_muxCtrl_3; // @[CUBE.scala 28:21]
  assign PEcube_4_clock = clock;
  assign PEcube_4_reset = reset;
  assign PEcube_4_io_vecAin = io_vecA_1; // @[CUBE.scala 51:29]
  assign PEcube_4_io_vecBin = PEcube_io_vecBout; // @[CUBE.scala 39:22]
  assign PEcube_4_io_eleCin = PEcube_io_eleCout; // @[CUBE.scala 32:22]
  assign PEcube_4_io_muxCtrl = io_muxCtrl_4; // @[CUBE.scala 28:21]
  assign PEcube_5_clock = clock;
  assign PEcube_5_reset = reset;
  assign PEcube_5_io_vecAin = PEcube_4_io_vecAout; // @[CUBE.scala 44:22]
  assign PEcube_5_io_vecBin = PEcube_1_io_vecBout; // @[CUBE.scala 39:22]
  assign PEcube_5_io_eleCin = PEcube_1_io_eleCout; // @[CUBE.scala 32:22]
  assign PEcube_5_io_muxCtrl = io_muxCtrl_5; // @[CUBE.scala 28:21]
  assign PEcube_6_clock = clock;
  assign PEcube_6_reset = reset;
  assign PEcube_6_io_vecAin = PEcube_5_io_vecAout; // @[CUBE.scala 44:22]
  assign PEcube_6_io_vecBin = PEcube_2_io_vecBout; // @[CUBE.scala 39:22]
  assign PEcube_6_io_eleCin = PEcube_2_io_eleCout; // @[CUBE.scala 32:22]
  assign PEcube_6_io_muxCtrl = io_muxCtrl_6; // @[CUBE.scala 28:21]
  assign PEcube_7_clock = clock;
  assign PEcube_7_reset = reset;
  assign PEcube_7_io_vecAin = PEcube_6_io_vecAout; // @[CUBE.scala 44:22]
  assign PEcube_7_io_vecBin = PEcube_3_io_vecBout; // @[CUBE.scala 39:22]
  assign PEcube_7_io_eleCin = PEcube_3_io_eleCout; // @[CUBE.scala 32:22]
  assign PEcube_7_io_muxCtrl = io_muxCtrl_7; // @[CUBE.scala 28:21]
  assign PEcube_8_clock = clock;
  assign PEcube_8_reset = reset;
  assign PEcube_8_io_vecAin = io_vecA_2; // @[CUBE.scala 51:29]
  assign PEcube_8_io_vecBin = PEcube_4_io_vecBout; // @[CUBE.scala 39:22]
  assign PEcube_8_io_eleCin = PEcube_4_io_eleCout; // @[CUBE.scala 32:22]
  assign PEcube_8_io_muxCtrl = io_muxCtrl_8; // @[CUBE.scala 28:21]
  assign PEcube_9_clock = clock;
  assign PEcube_9_reset = reset;
  assign PEcube_9_io_vecAin = PEcube_8_io_vecAout; // @[CUBE.scala 44:22]
  assign PEcube_9_io_vecBin = PEcube_5_io_vecBout; // @[CUBE.scala 39:22]
  assign PEcube_9_io_eleCin = PEcube_5_io_eleCout; // @[CUBE.scala 32:22]
  assign PEcube_9_io_muxCtrl = io_muxCtrl_9; // @[CUBE.scala 28:21]
  assign PEcube_10_clock = clock;
  assign PEcube_10_reset = reset;
  assign PEcube_10_io_vecAin = PEcube_9_io_vecAout; // @[CUBE.scala 44:22]
  assign PEcube_10_io_vecBin = PEcube_6_io_vecBout; // @[CUBE.scala 39:22]
  assign PEcube_10_io_eleCin = PEcube_6_io_eleCout; // @[CUBE.scala 32:22]
  assign PEcube_10_io_muxCtrl = io_muxCtrl_10; // @[CUBE.scala 28:21]
  assign PEcube_11_clock = clock;
  assign PEcube_11_reset = reset;
  assign PEcube_11_io_vecAin = PEcube_10_io_vecAout; // @[CUBE.scala 44:22]
  assign PEcube_11_io_vecBin = PEcube_7_io_vecBout; // @[CUBE.scala 39:22]
  assign PEcube_11_io_eleCin = PEcube_7_io_eleCout; // @[CUBE.scala 32:22]
  assign PEcube_11_io_muxCtrl = io_muxCtrl_11; // @[CUBE.scala 28:21]
  assign PEcube_12_clock = clock;
  assign PEcube_12_reset = reset;
  assign PEcube_12_io_vecAin = io_vecA_3; // @[CUBE.scala 51:29]
  assign PEcube_12_io_vecBin = PEcube_8_io_vecBout; // @[CUBE.scala 39:22]
  assign PEcube_12_io_eleCin = PEcube_8_io_eleCout; // @[CUBE.scala 32:22]
  assign PEcube_12_io_muxCtrl = io_muxCtrl_12; // @[CUBE.scala 28:21]
  assign PEcube_13_clock = clock;
  assign PEcube_13_reset = reset;
  assign PEcube_13_io_vecAin = PEcube_12_io_vecAout; // @[CUBE.scala 44:22]
  assign PEcube_13_io_vecBin = PEcube_9_io_vecBout; // @[CUBE.scala 39:22]
  assign PEcube_13_io_eleCin = PEcube_9_io_eleCout; // @[CUBE.scala 32:22]
  assign PEcube_13_io_muxCtrl = io_muxCtrl_13; // @[CUBE.scala 28:21]
  assign PEcube_14_clock = clock;
  assign PEcube_14_reset = reset;
  assign PEcube_14_io_vecAin = PEcube_13_io_vecAout; // @[CUBE.scala 44:22]
  assign PEcube_14_io_vecBin = PEcube_10_io_vecBout; // @[CUBE.scala 39:22]
  assign PEcube_14_io_eleCin = PEcube_10_io_eleCout; // @[CUBE.scala 32:22]
  assign PEcube_14_io_muxCtrl = io_muxCtrl_14; // @[CUBE.scala 28:21]
  assign PEcube_15_clock = clock;
  assign PEcube_15_reset = reset;
  assign PEcube_15_io_vecAin = PEcube_14_io_vecAout; // @[CUBE.scala 44:22]
  assign PEcube_15_io_vecBin = PEcube_11_io_vecBout; // @[CUBE.scala 39:22]
  assign PEcube_15_io_eleCin = PEcube_11_io_eleCout; // @[CUBE.scala 32:22]
  assign PEcube_15_io_muxCtrl = io_muxCtrl_15; // @[CUBE.scala 28:21]
endmodule
