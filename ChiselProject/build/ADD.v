module ADD(
  input          clock,
  input          reset,
  input  [31:0]  io_eleCin_0,
  input  [31:0]  io_eleCin_1,
  input  [31:0]  io_eleCin_2,
  input  [31:0]  io_eleCin_3,
  input  [31:0]  io_eleCin_4,
  input  [31:0]  io_eleCin_5,
  input  [31:0]  io_eleCin_6,
  input  [31:0]  io_eleCin_7,
  input  [127:0] io_vecCin_0,
  input  [127:0] io_vecCin_1,
  output [127:0] io_vecCout_0,
  output [127:0] io_vecCout_1
);
  wire [31:0] eleC0 = io_vecCin_0[127:96]; // @[ADD.scala 20:43]
  wire [31:0] eleC1 = io_vecCin_0[95:64]; // @[ADD.scala 21:42]
  wire [31:0] eleC2 = io_vecCin_0[63:32]; // @[ADD.scala 22:42]
  wire [31:0] eleC3 = io_vecCin_0[31:0]; // @[ADD.scala 23:41]
  wire [31:0] io_vecCout_0_lo_lo = $signed(io_eleCin_3) + $signed(eleC3); // @[Cat.scala 31:58]
  wire [31:0] io_vecCout_0_lo_hi = $signed(io_eleCin_2) + $signed(eleC2); // @[Cat.scala 31:58]
  wire [31:0] io_vecCout_0_hi_lo = $signed(io_eleCin_1) + $signed(eleC1); // @[Cat.scala 31:58]
  wire [31:0] io_vecCout_0_hi_hi = $signed(io_eleCin_0) + $signed(eleC0); // @[Cat.scala 31:58]
  wire [31:0] eleC0_1 = io_vecCin_1[127:96]; // @[ADD.scala 20:43]
  wire [31:0] eleC1_1 = io_vecCin_1[95:64]; // @[ADD.scala 21:42]
  wire [31:0] eleC2_1 = io_vecCin_1[63:32]; // @[ADD.scala 22:42]
  wire [31:0] eleC3_1 = io_vecCin_1[31:0]; // @[ADD.scala 23:41]
  wire [31:0] io_vecCout_1_lo_lo = $signed(io_eleCin_7) + $signed(eleC3_1); // @[Cat.scala 31:58]
  wire [31:0] io_vecCout_1_lo_hi = $signed(io_eleCin_6) + $signed(eleC2_1); // @[Cat.scala 31:58]
  wire [31:0] io_vecCout_1_hi_lo = $signed(io_eleCin_5) + $signed(eleC1_1); // @[Cat.scala 31:58]
  wire [31:0] io_vecCout_1_hi_hi = $signed(io_eleCin_4) + $signed(eleC0_1); // @[Cat.scala 31:58]
  assign io_vecCout_0 = {io_vecCout_0_hi_hi,io_vecCout_0_hi_lo,io_vecCout_0_lo_hi,io_vecCout_0_lo_lo}; // @[ADD.scala 32:54]
  assign io_vecCout_1 = {io_vecCout_1_hi_hi,io_vecCout_1_hi_lo,io_vecCout_1_lo_hi,io_vecCout_1_lo_lo}; // @[ADD.scala 32:54]
endmodule
