module DPA(
  input  [127:0] io_vecA,
  input  [127:0] io_vecB,
  output [31:0]  io_eleC
);
  wire [7:0] vecA_parts_0 = io_vecA[7:0]; // @[DPA.scala 16:69]
  wire [7:0] vecA_parts_1 = io_vecA[15:8]; // @[DPA.scala 16:69]
  wire [7:0] vecA_parts_2 = io_vecA[23:16]; // @[DPA.scala 16:69]
  wire [7:0] vecA_parts_3 = io_vecA[31:24]; // @[DPA.scala 16:69]
  wire [7:0] vecA_parts_4 = io_vecA[39:32]; // @[DPA.scala 16:69]
  wire [7:0] vecA_parts_5 = io_vecA[47:40]; // @[DPA.scala 16:69]
  wire [7:0] vecA_parts_6 = io_vecA[55:48]; // @[DPA.scala 16:69]
  wire [7:0] vecA_parts_7 = io_vecA[63:56]; // @[DPA.scala 16:69]
  wire [7:0] vecA_parts_8 = io_vecA[71:64]; // @[DPA.scala 16:69]
  wire [7:0] vecA_parts_9 = io_vecA[79:72]; // @[DPA.scala 16:69]
  wire [7:0] vecA_parts_10 = io_vecA[87:80]; // @[DPA.scala 16:69]
  wire [7:0] vecA_parts_11 = io_vecA[95:88]; // @[DPA.scala 16:69]
  wire [7:0] vecA_parts_12 = io_vecA[103:96]; // @[DPA.scala 16:69]
  wire [7:0] vecA_parts_13 = io_vecA[111:104]; // @[DPA.scala 16:69]
  wire [7:0] vecA_parts_14 = io_vecA[119:112]; // @[DPA.scala 16:69]
  wire [7:0] vecA_parts_15 = io_vecA[127:120]; // @[DPA.scala 16:69]
  wire [7:0] vecB_parts_0 = io_vecB[7:0]; // @[DPA.scala 17:69]
  wire [7:0] vecB_parts_1 = io_vecB[15:8]; // @[DPA.scala 17:69]
  wire [7:0] vecB_parts_2 = io_vecB[23:16]; // @[DPA.scala 17:69]
  wire [7:0] vecB_parts_3 = io_vecB[31:24]; // @[DPA.scala 17:69]
  wire [7:0] vecB_parts_4 = io_vecB[39:32]; // @[DPA.scala 17:69]
  wire [7:0] vecB_parts_5 = io_vecB[47:40]; // @[DPA.scala 17:69]
  wire [7:0] vecB_parts_6 = io_vecB[55:48]; // @[DPA.scala 17:69]
  wire [7:0] vecB_parts_7 = io_vecB[63:56]; // @[DPA.scala 17:69]
  wire [7:0] vecB_parts_8 = io_vecB[71:64]; // @[DPA.scala 17:69]
  wire [7:0] vecB_parts_9 = io_vecB[79:72]; // @[DPA.scala 17:69]
  wire [7:0] vecB_parts_10 = io_vecB[87:80]; // @[DPA.scala 17:69]
  wire [7:0] vecB_parts_11 = io_vecB[95:88]; // @[DPA.scala 17:69]
  wire [7:0] vecB_parts_12 = io_vecB[103:96]; // @[DPA.scala 17:69]
  wire [7:0] vecB_parts_13 = io_vecB[111:104]; // @[DPA.scala 17:69]
  wire [7:0] vecB_parts_14 = io_vecB[119:112]; // @[DPA.scala 17:69]
  wire [7:0] vecB_parts_15 = io_vecB[127:120]; // @[DPA.scala 17:69]
  wire [15:0] _io_eleC_T = $signed(vecA_parts_0) * $signed(vecB_parts_0); // @[DPA.scala 20:64]
  wire [15:0] _io_eleC_T_1 = $signed(vecA_parts_1) * $signed(vecB_parts_1); // @[DPA.scala 20:64]
  wire [15:0] _io_eleC_T_2 = $signed(vecA_parts_2) * $signed(vecB_parts_2); // @[DPA.scala 20:64]
  wire [15:0] _io_eleC_T_3 = $signed(vecA_parts_3) * $signed(vecB_parts_3); // @[DPA.scala 20:64]
  wire [15:0] _io_eleC_T_4 = $signed(vecA_parts_4) * $signed(vecB_parts_4); // @[DPA.scala 20:64]
  wire [15:0] _io_eleC_T_5 = $signed(vecA_parts_5) * $signed(vecB_parts_5); // @[DPA.scala 20:64]
  wire [15:0] _io_eleC_T_6 = $signed(vecA_parts_6) * $signed(vecB_parts_6); // @[DPA.scala 20:64]
  wire [15:0] _io_eleC_T_7 = $signed(vecA_parts_7) * $signed(vecB_parts_7); // @[DPA.scala 20:64]
  wire [15:0] _io_eleC_T_8 = $signed(vecA_parts_8) * $signed(vecB_parts_8); // @[DPA.scala 20:64]
  wire [15:0] _io_eleC_T_9 = $signed(vecA_parts_9) * $signed(vecB_parts_9); // @[DPA.scala 20:64]
  wire [15:0] _io_eleC_T_10 = $signed(vecA_parts_10) * $signed(vecB_parts_10); // @[DPA.scala 20:64]
  wire [15:0] _io_eleC_T_11 = $signed(vecA_parts_11) * $signed(vecB_parts_11); // @[DPA.scala 20:64]
  wire [15:0] _io_eleC_T_12 = $signed(vecA_parts_12) * $signed(vecB_parts_12); // @[DPA.scala 20:64]
  wire [15:0] _io_eleC_T_13 = $signed(vecA_parts_13) * $signed(vecB_parts_13); // @[DPA.scala 20:64]
  wire [15:0] _io_eleC_T_14 = $signed(vecA_parts_14) * $signed(vecB_parts_14); // @[DPA.scala 20:64]
  wire [15:0] _io_eleC_T_15 = $signed(vecA_parts_15) * $signed(vecB_parts_15); // @[DPA.scala 20:64]
  wire [15:0] _io_eleC_T_18 = $signed(_io_eleC_T) + $signed(_io_eleC_T_1); // @[DPA.scala 20:79]
  wire [15:0] _io_eleC_T_21 = $signed(_io_eleC_T_18) + $signed(_io_eleC_T_2); // @[DPA.scala 20:79]
  wire [15:0] _io_eleC_T_24 = $signed(_io_eleC_T_21) + $signed(_io_eleC_T_3); // @[DPA.scala 20:79]
  wire [15:0] _io_eleC_T_27 = $signed(_io_eleC_T_24) + $signed(_io_eleC_T_4); // @[DPA.scala 20:79]
  wire [15:0] _io_eleC_T_30 = $signed(_io_eleC_T_27) + $signed(_io_eleC_T_5); // @[DPA.scala 20:79]
  wire [15:0] _io_eleC_T_33 = $signed(_io_eleC_T_30) + $signed(_io_eleC_T_6); // @[DPA.scala 20:79]
  wire [15:0] _io_eleC_T_36 = $signed(_io_eleC_T_33) + $signed(_io_eleC_T_7); // @[DPA.scala 20:79]
  wire [15:0] _io_eleC_T_39 = $signed(_io_eleC_T_36) + $signed(_io_eleC_T_8); // @[DPA.scala 20:79]
  wire [15:0] _io_eleC_T_42 = $signed(_io_eleC_T_39) + $signed(_io_eleC_T_9); // @[DPA.scala 20:79]
  wire [15:0] _io_eleC_T_45 = $signed(_io_eleC_T_42) + $signed(_io_eleC_T_10); // @[DPA.scala 20:79]
  wire [15:0] _io_eleC_T_48 = $signed(_io_eleC_T_45) + $signed(_io_eleC_T_11); // @[DPA.scala 20:79]
  wire [15:0] _io_eleC_T_51 = $signed(_io_eleC_T_48) + $signed(_io_eleC_T_12); // @[DPA.scala 20:79]
  wire [15:0] _io_eleC_T_54 = $signed(_io_eleC_T_51) + $signed(_io_eleC_T_13); // @[DPA.scala 20:79]
  wire [15:0] _io_eleC_T_57 = $signed(_io_eleC_T_54) + $signed(_io_eleC_T_14); // @[DPA.scala 20:79]
  wire [15:0] _io_eleC_T_60 = $signed(_io_eleC_T_57) + $signed(_io_eleC_T_15); // @[DPA.scala 20:79]
  assign io_eleC = {{16{_io_eleC_T_60[15]}},_io_eleC_T_60}; // @[DPA.scala 20:11]
endmodule
module PEcube(
  input          clock,
  input          reset,
  input  [127:0] io_vecAin,
  input  [127:0] io_vecBin,
  input  [31:0]  io_eleCin,
  input          io_muxCtrl,
  output [127:0] io_vecAout,
  output [127:0] io_vecBout,
  output [31:0]  io_eleCout
);
`ifdef RANDOMIZE_REG_INIT
  reg [127:0] _RAND_0;
`endif // RANDOMIZE_REG_INIT
  wire [127:0] subDPA_io_vecA; // @[PE.scala 21:22]
  wire [127:0] subDPA_io_vecB; // @[PE.scala 21:22]
  wire [31:0] subDPA_io_eleC; // @[PE.scala 21:22]
  reg [127:0] regR; // @[PE.scala 22:21]
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
      regR <= 128'h0; // @[PE.scala 22:21]
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
  _RAND_0 = {4{`RANDOM}};
  regR = _RAND_0[127:0];
`endif // RANDOMIZE_REG_INIT
  `endif // RANDOMIZE
end // initial
`ifdef FIRRTL_AFTER_INITIAL
`FIRRTL_AFTER_INITIAL
`endif
`endif // SYNTHESIS
endmodule
