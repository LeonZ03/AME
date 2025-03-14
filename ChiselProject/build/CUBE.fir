circuit CUBE :
  module DPA :
    input clock : Clock
    input reset : Reset
    output io : { flip vecA : UInt<32>, flip vecB : UInt<32>, eleC : SInt<32>}

    node _vecA_parts_T = bits(io.vecA, 7, 0) @[DPA.scala 16:49]
    node vecA_parts_0 = asSInt(_vecA_parts_T) @[DPA.scala 16:69]
    node _vecA_parts_T_1 = bits(io.vecA, 15, 8) @[DPA.scala 16:49]
    node vecA_parts_1 = asSInt(_vecA_parts_T_1) @[DPA.scala 16:69]
    node _vecA_parts_T_2 = bits(io.vecA, 23, 16) @[DPA.scala 16:49]
    node vecA_parts_2 = asSInt(_vecA_parts_T_2) @[DPA.scala 16:69]
    node _vecA_parts_T_3 = bits(io.vecA, 31, 24) @[DPA.scala 16:49]
    node vecA_parts_3 = asSInt(_vecA_parts_T_3) @[DPA.scala 16:69]
    node _vecB_parts_T = bits(io.vecB, 7, 0) @[DPA.scala 17:49]
    node vecB_parts_0 = asSInt(_vecB_parts_T) @[DPA.scala 17:69]
    node _vecB_parts_T_1 = bits(io.vecB, 15, 8) @[DPA.scala 17:49]
    node vecB_parts_1 = asSInt(_vecB_parts_T_1) @[DPA.scala 17:69]
    node _vecB_parts_T_2 = bits(io.vecB, 23, 16) @[DPA.scala 17:49]
    node vecB_parts_2 = asSInt(_vecB_parts_T_2) @[DPA.scala 17:69]
    node _vecB_parts_T_3 = bits(io.vecB, 31, 24) @[DPA.scala 17:49]
    node vecB_parts_3 = asSInt(_vecB_parts_T_3) @[DPA.scala 17:69]
    node products_0 = mul(vecA_parts_0, vecB_parts_0) @[DPA.scala 20:68]
    node products_1 = mul(vecA_parts_1, vecB_parts_1) @[DPA.scala 20:68]
    node products_2 = mul(vecA_parts_2, vecB_parts_2) @[DPA.scala 20:68]
    node products_3 = mul(vecA_parts_3, vecB_parts_3) @[DPA.scala 20:68]
    node _io_eleC_T = add(products_0, products_1) @[DPA.scala 23:32]
    node _io_eleC_T_1 = tail(_io_eleC_T, 1) @[DPA.scala 23:32]
    node _io_eleC_T_2 = asSInt(_io_eleC_T_1) @[DPA.scala 23:32]
    node _io_eleC_T_3 = add(_io_eleC_T_2, products_2) @[DPA.scala 23:32]
    node _io_eleC_T_4 = tail(_io_eleC_T_3, 1) @[DPA.scala 23:32]
    node _io_eleC_T_5 = asSInt(_io_eleC_T_4) @[DPA.scala 23:32]
    node _io_eleC_T_6 = add(_io_eleC_T_5, products_3) @[DPA.scala 23:32]
    node _io_eleC_T_7 = tail(_io_eleC_T_6, 1) @[DPA.scala 23:32]
    node _io_eleC_T_8 = asSInt(_io_eleC_T_7) @[DPA.scala 23:32]
    io.eleC <= _io_eleC_T_8 @[DPA.scala 23:11]

  module PEcube :
    input clock : Clock
    input reset : Reset
    output io : { flip vecAin : UInt<32>, flip vecBin : UInt<32>, flip eleCin : SInt<32>, flip muxCtrl : UInt<1>, vecAout : UInt<32>, vecBout : UInt<32>, eleCout : SInt<32>}

    inst subDPA of DPA @[PE.scala 21:22]
    subDPA.clock <= clock
    subDPA.reset <= reset
    reg regR : UInt<32>, clock with :
      reset => (reset, UInt<32>("h0")) @[PE.scala 22:21]
    subDPA.io.vecA <= io.vecAin @[PE.scala 24:18]
    subDPA.io.vecB <= io.vecBin @[PE.scala 25:18]
    node _io_eleCout_T = mux(io.muxCtrl, subDPA.io.eleC, io.eleCin) @[PE.scala 26:20]
    io.eleCout <= _io_eleCout_T @[PE.scala 26:14]
    io.vecAout <= io.vecAin @[PE.scala 28:14]
    regR <= io.vecBin @[PE.scala 29:8]
    io.vecBout <= regR @[PE.scala 30:14]

  module DPA_1 :
    input clock : Clock
    input reset : Reset
    output io : { flip vecA : UInt<32>, flip vecB : UInt<32>, eleC : SInt<32>}

    node _vecA_parts_T = bits(io.vecA, 7, 0) @[DPA.scala 16:49]
    node vecA_parts_0 = asSInt(_vecA_parts_T) @[DPA.scala 16:69]
    node _vecA_parts_T_1 = bits(io.vecA, 15, 8) @[DPA.scala 16:49]
    node vecA_parts_1 = asSInt(_vecA_parts_T_1) @[DPA.scala 16:69]
    node _vecA_parts_T_2 = bits(io.vecA, 23, 16) @[DPA.scala 16:49]
    node vecA_parts_2 = asSInt(_vecA_parts_T_2) @[DPA.scala 16:69]
    node _vecA_parts_T_3 = bits(io.vecA, 31, 24) @[DPA.scala 16:49]
    node vecA_parts_3 = asSInt(_vecA_parts_T_3) @[DPA.scala 16:69]
    node _vecB_parts_T = bits(io.vecB, 7, 0) @[DPA.scala 17:49]
    node vecB_parts_0 = asSInt(_vecB_parts_T) @[DPA.scala 17:69]
    node _vecB_parts_T_1 = bits(io.vecB, 15, 8) @[DPA.scala 17:49]
    node vecB_parts_1 = asSInt(_vecB_parts_T_1) @[DPA.scala 17:69]
    node _vecB_parts_T_2 = bits(io.vecB, 23, 16) @[DPA.scala 17:49]
    node vecB_parts_2 = asSInt(_vecB_parts_T_2) @[DPA.scala 17:69]
    node _vecB_parts_T_3 = bits(io.vecB, 31, 24) @[DPA.scala 17:49]
    node vecB_parts_3 = asSInt(_vecB_parts_T_3) @[DPA.scala 17:69]
    node products_0 = mul(vecA_parts_0, vecB_parts_0) @[DPA.scala 20:68]
    node products_1 = mul(vecA_parts_1, vecB_parts_1) @[DPA.scala 20:68]
    node products_2 = mul(vecA_parts_2, vecB_parts_2) @[DPA.scala 20:68]
    node products_3 = mul(vecA_parts_3, vecB_parts_3) @[DPA.scala 20:68]
    node _io_eleC_T = add(products_0, products_1) @[DPA.scala 23:32]
    node _io_eleC_T_1 = tail(_io_eleC_T, 1) @[DPA.scala 23:32]
    node _io_eleC_T_2 = asSInt(_io_eleC_T_1) @[DPA.scala 23:32]
    node _io_eleC_T_3 = add(_io_eleC_T_2, products_2) @[DPA.scala 23:32]
    node _io_eleC_T_4 = tail(_io_eleC_T_3, 1) @[DPA.scala 23:32]
    node _io_eleC_T_5 = asSInt(_io_eleC_T_4) @[DPA.scala 23:32]
    node _io_eleC_T_6 = add(_io_eleC_T_5, products_3) @[DPA.scala 23:32]
    node _io_eleC_T_7 = tail(_io_eleC_T_6, 1) @[DPA.scala 23:32]
    node _io_eleC_T_8 = asSInt(_io_eleC_T_7) @[DPA.scala 23:32]
    io.eleC <= _io_eleC_T_8 @[DPA.scala 23:11]

  module PEcube_1 :
    input clock : Clock
    input reset : Reset
    output io : { flip vecAin : UInt<32>, flip vecBin : UInt<32>, flip eleCin : SInt<32>, flip muxCtrl : UInt<1>, vecAout : UInt<32>, vecBout : UInt<32>, eleCout : SInt<32>}

    inst subDPA of DPA_1 @[PE.scala 21:22]
    subDPA.clock <= clock
    subDPA.reset <= reset
    reg regR : UInt<32>, clock with :
      reset => (reset, UInt<32>("h0")) @[PE.scala 22:21]
    subDPA.io.vecA <= io.vecAin @[PE.scala 24:18]
    subDPA.io.vecB <= io.vecBin @[PE.scala 25:18]
    node _io_eleCout_T = mux(io.muxCtrl, subDPA.io.eleC, io.eleCin) @[PE.scala 26:20]
    io.eleCout <= _io_eleCout_T @[PE.scala 26:14]
    io.vecAout <= io.vecAin @[PE.scala 28:14]
    regR <= io.vecBin @[PE.scala 29:8]
    io.vecBout <= regR @[PE.scala 30:14]

  module DPA_2 :
    input clock : Clock
    input reset : Reset
    output io : { flip vecA : UInt<32>, flip vecB : UInt<32>, eleC : SInt<32>}

    node _vecA_parts_T = bits(io.vecA, 7, 0) @[DPA.scala 16:49]
    node vecA_parts_0 = asSInt(_vecA_parts_T) @[DPA.scala 16:69]
    node _vecA_parts_T_1 = bits(io.vecA, 15, 8) @[DPA.scala 16:49]
    node vecA_parts_1 = asSInt(_vecA_parts_T_1) @[DPA.scala 16:69]
    node _vecA_parts_T_2 = bits(io.vecA, 23, 16) @[DPA.scala 16:49]
    node vecA_parts_2 = asSInt(_vecA_parts_T_2) @[DPA.scala 16:69]
    node _vecA_parts_T_3 = bits(io.vecA, 31, 24) @[DPA.scala 16:49]
    node vecA_parts_3 = asSInt(_vecA_parts_T_3) @[DPA.scala 16:69]
    node _vecB_parts_T = bits(io.vecB, 7, 0) @[DPA.scala 17:49]
    node vecB_parts_0 = asSInt(_vecB_parts_T) @[DPA.scala 17:69]
    node _vecB_parts_T_1 = bits(io.vecB, 15, 8) @[DPA.scala 17:49]
    node vecB_parts_1 = asSInt(_vecB_parts_T_1) @[DPA.scala 17:69]
    node _vecB_parts_T_2 = bits(io.vecB, 23, 16) @[DPA.scala 17:49]
    node vecB_parts_2 = asSInt(_vecB_parts_T_2) @[DPA.scala 17:69]
    node _vecB_parts_T_3 = bits(io.vecB, 31, 24) @[DPA.scala 17:49]
    node vecB_parts_3 = asSInt(_vecB_parts_T_3) @[DPA.scala 17:69]
    node products_0 = mul(vecA_parts_0, vecB_parts_0) @[DPA.scala 20:68]
    node products_1 = mul(vecA_parts_1, vecB_parts_1) @[DPA.scala 20:68]
    node products_2 = mul(vecA_parts_2, vecB_parts_2) @[DPA.scala 20:68]
    node products_3 = mul(vecA_parts_3, vecB_parts_3) @[DPA.scala 20:68]
    node _io_eleC_T = add(products_0, products_1) @[DPA.scala 23:32]
    node _io_eleC_T_1 = tail(_io_eleC_T, 1) @[DPA.scala 23:32]
    node _io_eleC_T_2 = asSInt(_io_eleC_T_1) @[DPA.scala 23:32]
    node _io_eleC_T_3 = add(_io_eleC_T_2, products_2) @[DPA.scala 23:32]
    node _io_eleC_T_4 = tail(_io_eleC_T_3, 1) @[DPA.scala 23:32]
    node _io_eleC_T_5 = asSInt(_io_eleC_T_4) @[DPA.scala 23:32]
    node _io_eleC_T_6 = add(_io_eleC_T_5, products_3) @[DPA.scala 23:32]
    node _io_eleC_T_7 = tail(_io_eleC_T_6, 1) @[DPA.scala 23:32]
    node _io_eleC_T_8 = asSInt(_io_eleC_T_7) @[DPA.scala 23:32]
    io.eleC <= _io_eleC_T_8 @[DPA.scala 23:11]

  module PEcube_2 :
    input clock : Clock
    input reset : Reset
    output io : { flip vecAin : UInt<32>, flip vecBin : UInt<32>, flip eleCin : SInt<32>, flip muxCtrl : UInt<1>, vecAout : UInt<32>, vecBout : UInt<32>, eleCout : SInt<32>}

    inst subDPA of DPA_2 @[PE.scala 21:22]
    subDPA.clock <= clock
    subDPA.reset <= reset
    reg regR : UInt<32>, clock with :
      reset => (reset, UInt<32>("h0")) @[PE.scala 22:21]
    subDPA.io.vecA <= io.vecAin @[PE.scala 24:18]
    subDPA.io.vecB <= io.vecBin @[PE.scala 25:18]
    node _io_eleCout_T = mux(io.muxCtrl, subDPA.io.eleC, io.eleCin) @[PE.scala 26:20]
    io.eleCout <= _io_eleCout_T @[PE.scala 26:14]
    io.vecAout <= io.vecAin @[PE.scala 28:14]
    regR <= io.vecBin @[PE.scala 29:8]
    io.vecBout <= regR @[PE.scala 30:14]

  module DPA_3 :
    input clock : Clock
    input reset : Reset
    output io : { flip vecA : UInt<32>, flip vecB : UInt<32>, eleC : SInt<32>}

    node _vecA_parts_T = bits(io.vecA, 7, 0) @[DPA.scala 16:49]
    node vecA_parts_0 = asSInt(_vecA_parts_T) @[DPA.scala 16:69]
    node _vecA_parts_T_1 = bits(io.vecA, 15, 8) @[DPA.scala 16:49]
    node vecA_parts_1 = asSInt(_vecA_parts_T_1) @[DPA.scala 16:69]
    node _vecA_parts_T_2 = bits(io.vecA, 23, 16) @[DPA.scala 16:49]
    node vecA_parts_2 = asSInt(_vecA_parts_T_2) @[DPA.scala 16:69]
    node _vecA_parts_T_3 = bits(io.vecA, 31, 24) @[DPA.scala 16:49]
    node vecA_parts_3 = asSInt(_vecA_parts_T_3) @[DPA.scala 16:69]
    node _vecB_parts_T = bits(io.vecB, 7, 0) @[DPA.scala 17:49]
    node vecB_parts_0 = asSInt(_vecB_parts_T) @[DPA.scala 17:69]
    node _vecB_parts_T_1 = bits(io.vecB, 15, 8) @[DPA.scala 17:49]
    node vecB_parts_1 = asSInt(_vecB_parts_T_1) @[DPA.scala 17:69]
    node _vecB_parts_T_2 = bits(io.vecB, 23, 16) @[DPA.scala 17:49]
    node vecB_parts_2 = asSInt(_vecB_parts_T_2) @[DPA.scala 17:69]
    node _vecB_parts_T_3 = bits(io.vecB, 31, 24) @[DPA.scala 17:49]
    node vecB_parts_3 = asSInt(_vecB_parts_T_3) @[DPA.scala 17:69]
    node products_0 = mul(vecA_parts_0, vecB_parts_0) @[DPA.scala 20:68]
    node products_1 = mul(vecA_parts_1, vecB_parts_1) @[DPA.scala 20:68]
    node products_2 = mul(vecA_parts_2, vecB_parts_2) @[DPA.scala 20:68]
    node products_3 = mul(vecA_parts_3, vecB_parts_3) @[DPA.scala 20:68]
    node _io_eleC_T = add(products_0, products_1) @[DPA.scala 23:32]
    node _io_eleC_T_1 = tail(_io_eleC_T, 1) @[DPA.scala 23:32]
    node _io_eleC_T_2 = asSInt(_io_eleC_T_1) @[DPA.scala 23:32]
    node _io_eleC_T_3 = add(_io_eleC_T_2, products_2) @[DPA.scala 23:32]
    node _io_eleC_T_4 = tail(_io_eleC_T_3, 1) @[DPA.scala 23:32]
    node _io_eleC_T_5 = asSInt(_io_eleC_T_4) @[DPA.scala 23:32]
    node _io_eleC_T_6 = add(_io_eleC_T_5, products_3) @[DPA.scala 23:32]
    node _io_eleC_T_7 = tail(_io_eleC_T_6, 1) @[DPA.scala 23:32]
    node _io_eleC_T_8 = asSInt(_io_eleC_T_7) @[DPA.scala 23:32]
    io.eleC <= _io_eleC_T_8 @[DPA.scala 23:11]

  module PEcube_3 :
    input clock : Clock
    input reset : Reset
    output io : { flip vecAin : UInt<32>, flip vecBin : UInt<32>, flip eleCin : SInt<32>, flip muxCtrl : UInt<1>, vecAout : UInt<32>, vecBout : UInt<32>, eleCout : SInt<32>}

    inst subDPA of DPA_3 @[PE.scala 21:22]
    subDPA.clock <= clock
    subDPA.reset <= reset
    reg regR : UInt<32>, clock with :
      reset => (reset, UInt<32>("h0")) @[PE.scala 22:21]
    subDPA.io.vecA <= io.vecAin @[PE.scala 24:18]
    subDPA.io.vecB <= io.vecBin @[PE.scala 25:18]
    node _io_eleCout_T = mux(io.muxCtrl, subDPA.io.eleC, io.eleCin) @[PE.scala 26:20]
    io.eleCout <= _io_eleCout_T @[PE.scala 26:14]
    io.vecAout <= io.vecAin @[PE.scala 28:14]
    regR <= io.vecBin @[PE.scala 29:8]
    io.vecBout <= regR @[PE.scala 30:14]

  module DPA_4 :
    input clock : Clock
    input reset : Reset
    output io : { flip vecA : UInt<32>, flip vecB : UInt<32>, eleC : SInt<32>}

    node _vecA_parts_T = bits(io.vecA, 7, 0) @[DPA.scala 16:49]
    node vecA_parts_0 = asSInt(_vecA_parts_T) @[DPA.scala 16:69]
    node _vecA_parts_T_1 = bits(io.vecA, 15, 8) @[DPA.scala 16:49]
    node vecA_parts_1 = asSInt(_vecA_parts_T_1) @[DPA.scala 16:69]
    node _vecA_parts_T_2 = bits(io.vecA, 23, 16) @[DPA.scala 16:49]
    node vecA_parts_2 = asSInt(_vecA_parts_T_2) @[DPA.scala 16:69]
    node _vecA_parts_T_3 = bits(io.vecA, 31, 24) @[DPA.scala 16:49]
    node vecA_parts_3 = asSInt(_vecA_parts_T_3) @[DPA.scala 16:69]
    node _vecB_parts_T = bits(io.vecB, 7, 0) @[DPA.scala 17:49]
    node vecB_parts_0 = asSInt(_vecB_parts_T) @[DPA.scala 17:69]
    node _vecB_parts_T_1 = bits(io.vecB, 15, 8) @[DPA.scala 17:49]
    node vecB_parts_1 = asSInt(_vecB_parts_T_1) @[DPA.scala 17:69]
    node _vecB_parts_T_2 = bits(io.vecB, 23, 16) @[DPA.scala 17:49]
    node vecB_parts_2 = asSInt(_vecB_parts_T_2) @[DPA.scala 17:69]
    node _vecB_parts_T_3 = bits(io.vecB, 31, 24) @[DPA.scala 17:49]
    node vecB_parts_3 = asSInt(_vecB_parts_T_3) @[DPA.scala 17:69]
    node products_0 = mul(vecA_parts_0, vecB_parts_0) @[DPA.scala 20:68]
    node products_1 = mul(vecA_parts_1, vecB_parts_1) @[DPA.scala 20:68]
    node products_2 = mul(vecA_parts_2, vecB_parts_2) @[DPA.scala 20:68]
    node products_3 = mul(vecA_parts_3, vecB_parts_3) @[DPA.scala 20:68]
    node _io_eleC_T = add(products_0, products_1) @[DPA.scala 23:32]
    node _io_eleC_T_1 = tail(_io_eleC_T, 1) @[DPA.scala 23:32]
    node _io_eleC_T_2 = asSInt(_io_eleC_T_1) @[DPA.scala 23:32]
    node _io_eleC_T_3 = add(_io_eleC_T_2, products_2) @[DPA.scala 23:32]
    node _io_eleC_T_4 = tail(_io_eleC_T_3, 1) @[DPA.scala 23:32]
    node _io_eleC_T_5 = asSInt(_io_eleC_T_4) @[DPA.scala 23:32]
    node _io_eleC_T_6 = add(_io_eleC_T_5, products_3) @[DPA.scala 23:32]
    node _io_eleC_T_7 = tail(_io_eleC_T_6, 1) @[DPA.scala 23:32]
    node _io_eleC_T_8 = asSInt(_io_eleC_T_7) @[DPA.scala 23:32]
    io.eleC <= _io_eleC_T_8 @[DPA.scala 23:11]

  module PEcube_4 :
    input clock : Clock
    input reset : Reset
    output io : { flip vecAin : UInt<32>, flip vecBin : UInt<32>, flip eleCin : SInt<32>, flip muxCtrl : UInt<1>, vecAout : UInt<32>, vecBout : UInt<32>, eleCout : SInt<32>}

    inst subDPA of DPA_4 @[PE.scala 21:22]
    subDPA.clock <= clock
    subDPA.reset <= reset
    reg regR : UInt<32>, clock with :
      reset => (reset, UInt<32>("h0")) @[PE.scala 22:21]
    subDPA.io.vecA <= io.vecAin @[PE.scala 24:18]
    subDPA.io.vecB <= io.vecBin @[PE.scala 25:18]
    node _io_eleCout_T = mux(io.muxCtrl, subDPA.io.eleC, io.eleCin) @[PE.scala 26:20]
    io.eleCout <= _io_eleCout_T @[PE.scala 26:14]
    io.vecAout <= io.vecAin @[PE.scala 28:14]
    regR <= io.vecBin @[PE.scala 29:8]
    io.vecBout <= regR @[PE.scala 30:14]

  module DPA_5 :
    input clock : Clock
    input reset : Reset
    output io : { flip vecA : UInt<32>, flip vecB : UInt<32>, eleC : SInt<32>}

    node _vecA_parts_T = bits(io.vecA, 7, 0) @[DPA.scala 16:49]
    node vecA_parts_0 = asSInt(_vecA_parts_T) @[DPA.scala 16:69]
    node _vecA_parts_T_1 = bits(io.vecA, 15, 8) @[DPA.scala 16:49]
    node vecA_parts_1 = asSInt(_vecA_parts_T_1) @[DPA.scala 16:69]
    node _vecA_parts_T_2 = bits(io.vecA, 23, 16) @[DPA.scala 16:49]
    node vecA_parts_2 = asSInt(_vecA_parts_T_2) @[DPA.scala 16:69]
    node _vecA_parts_T_3 = bits(io.vecA, 31, 24) @[DPA.scala 16:49]
    node vecA_parts_3 = asSInt(_vecA_parts_T_3) @[DPA.scala 16:69]
    node _vecB_parts_T = bits(io.vecB, 7, 0) @[DPA.scala 17:49]
    node vecB_parts_0 = asSInt(_vecB_parts_T) @[DPA.scala 17:69]
    node _vecB_parts_T_1 = bits(io.vecB, 15, 8) @[DPA.scala 17:49]
    node vecB_parts_1 = asSInt(_vecB_parts_T_1) @[DPA.scala 17:69]
    node _vecB_parts_T_2 = bits(io.vecB, 23, 16) @[DPA.scala 17:49]
    node vecB_parts_2 = asSInt(_vecB_parts_T_2) @[DPA.scala 17:69]
    node _vecB_parts_T_3 = bits(io.vecB, 31, 24) @[DPA.scala 17:49]
    node vecB_parts_3 = asSInt(_vecB_parts_T_3) @[DPA.scala 17:69]
    node products_0 = mul(vecA_parts_0, vecB_parts_0) @[DPA.scala 20:68]
    node products_1 = mul(vecA_parts_1, vecB_parts_1) @[DPA.scala 20:68]
    node products_2 = mul(vecA_parts_2, vecB_parts_2) @[DPA.scala 20:68]
    node products_3 = mul(vecA_parts_3, vecB_parts_3) @[DPA.scala 20:68]
    node _io_eleC_T = add(products_0, products_1) @[DPA.scala 23:32]
    node _io_eleC_T_1 = tail(_io_eleC_T, 1) @[DPA.scala 23:32]
    node _io_eleC_T_2 = asSInt(_io_eleC_T_1) @[DPA.scala 23:32]
    node _io_eleC_T_3 = add(_io_eleC_T_2, products_2) @[DPA.scala 23:32]
    node _io_eleC_T_4 = tail(_io_eleC_T_3, 1) @[DPA.scala 23:32]
    node _io_eleC_T_5 = asSInt(_io_eleC_T_4) @[DPA.scala 23:32]
    node _io_eleC_T_6 = add(_io_eleC_T_5, products_3) @[DPA.scala 23:32]
    node _io_eleC_T_7 = tail(_io_eleC_T_6, 1) @[DPA.scala 23:32]
    node _io_eleC_T_8 = asSInt(_io_eleC_T_7) @[DPA.scala 23:32]
    io.eleC <= _io_eleC_T_8 @[DPA.scala 23:11]

  module PEcube_5 :
    input clock : Clock
    input reset : Reset
    output io : { flip vecAin : UInt<32>, flip vecBin : UInt<32>, flip eleCin : SInt<32>, flip muxCtrl : UInt<1>, vecAout : UInt<32>, vecBout : UInt<32>, eleCout : SInt<32>}

    inst subDPA of DPA_5 @[PE.scala 21:22]
    subDPA.clock <= clock
    subDPA.reset <= reset
    reg regR : UInt<32>, clock with :
      reset => (reset, UInt<32>("h0")) @[PE.scala 22:21]
    subDPA.io.vecA <= io.vecAin @[PE.scala 24:18]
    subDPA.io.vecB <= io.vecBin @[PE.scala 25:18]
    node _io_eleCout_T = mux(io.muxCtrl, subDPA.io.eleC, io.eleCin) @[PE.scala 26:20]
    io.eleCout <= _io_eleCout_T @[PE.scala 26:14]
    io.vecAout <= io.vecAin @[PE.scala 28:14]
    regR <= io.vecBin @[PE.scala 29:8]
    io.vecBout <= regR @[PE.scala 30:14]

  module DPA_6 :
    input clock : Clock
    input reset : Reset
    output io : { flip vecA : UInt<32>, flip vecB : UInt<32>, eleC : SInt<32>}

    node _vecA_parts_T = bits(io.vecA, 7, 0) @[DPA.scala 16:49]
    node vecA_parts_0 = asSInt(_vecA_parts_T) @[DPA.scala 16:69]
    node _vecA_parts_T_1 = bits(io.vecA, 15, 8) @[DPA.scala 16:49]
    node vecA_parts_1 = asSInt(_vecA_parts_T_1) @[DPA.scala 16:69]
    node _vecA_parts_T_2 = bits(io.vecA, 23, 16) @[DPA.scala 16:49]
    node vecA_parts_2 = asSInt(_vecA_parts_T_2) @[DPA.scala 16:69]
    node _vecA_parts_T_3 = bits(io.vecA, 31, 24) @[DPA.scala 16:49]
    node vecA_parts_3 = asSInt(_vecA_parts_T_3) @[DPA.scala 16:69]
    node _vecB_parts_T = bits(io.vecB, 7, 0) @[DPA.scala 17:49]
    node vecB_parts_0 = asSInt(_vecB_parts_T) @[DPA.scala 17:69]
    node _vecB_parts_T_1 = bits(io.vecB, 15, 8) @[DPA.scala 17:49]
    node vecB_parts_1 = asSInt(_vecB_parts_T_1) @[DPA.scala 17:69]
    node _vecB_parts_T_2 = bits(io.vecB, 23, 16) @[DPA.scala 17:49]
    node vecB_parts_2 = asSInt(_vecB_parts_T_2) @[DPA.scala 17:69]
    node _vecB_parts_T_3 = bits(io.vecB, 31, 24) @[DPA.scala 17:49]
    node vecB_parts_3 = asSInt(_vecB_parts_T_3) @[DPA.scala 17:69]
    node products_0 = mul(vecA_parts_0, vecB_parts_0) @[DPA.scala 20:68]
    node products_1 = mul(vecA_parts_1, vecB_parts_1) @[DPA.scala 20:68]
    node products_2 = mul(vecA_parts_2, vecB_parts_2) @[DPA.scala 20:68]
    node products_3 = mul(vecA_parts_3, vecB_parts_3) @[DPA.scala 20:68]
    node _io_eleC_T = add(products_0, products_1) @[DPA.scala 23:32]
    node _io_eleC_T_1 = tail(_io_eleC_T, 1) @[DPA.scala 23:32]
    node _io_eleC_T_2 = asSInt(_io_eleC_T_1) @[DPA.scala 23:32]
    node _io_eleC_T_3 = add(_io_eleC_T_2, products_2) @[DPA.scala 23:32]
    node _io_eleC_T_4 = tail(_io_eleC_T_3, 1) @[DPA.scala 23:32]
    node _io_eleC_T_5 = asSInt(_io_eleC_T_4) @[DPA.scala 23:32]
    node _io_eleC_T_6 = add(_io_eleC_T_5, products_3) @[DPA.scala 23:32]
    node _io_eleC_T_7 = tail(_io_eleC_T_6, 1) @[DPA.scala 23:32]
    node _io_eleC_T_8 = asSInt(_io_eleC_T_7) @[DPA.scala 23:32]
    io.eleC <= _io_eleC_T_8 @[DPA.scala 23:11]

  module PEcube_6 :
    input clock : Clock
    input reset : Reset
    output io : { flip vecAin : UInt<32>, flip vecBin : UInt<32>, flip eleCin : SInt<32>, flip muxCtrl : UInt<1>, vecAout : UInt<32>, vecBout : UInt<32>, eleCout : SInt<32>}

    inst subDPA of DPA_6 @[PE.scala 21:22]
    subDPA.clock <= clock
    subDPA.reset <= reset
    reg regR : UInt<32>, clock with :
      reset => (reset, UInt<32>("h0")) @[PE.scala 22:21]
    subDPA.io.vecA <= io.vecAin @[PE.scala 24:18]
    subDPA.io.vecB <= io.vecBin @[PE.scala 25:18]
    node _io_eleCout_T = mux(io.muxCtrl, subDPA.io.eleC, io.eleCin) @[PE.scala 26:20]
    io.eleCout <= _io_eleCout_T @[PE.scala 26:14]
    io.vecAout <= io.vecAin @[PE.scala 28:14]
    regR <= io.vecBin @[PE.scala 29:8]
    io.vecBout <= regR @[PE.scala 30:14]

  module DPA_7 :
    input clock : Clock
    input reset : Reset
    output io : { flip vecA : UInt<32>, flip vecB : UInt<32>, eleC : SInt<32>}

    node _vecA_parts_T = bits(io.vecA, 7, 0) @[DPA.scala 16:49]
    node vecA_parts_0 = asSInt(_vecA_parts_T) @[DPA.scala 16:69]
    node _vecA_parts_T_1 = bits(io.vecA, 15, 8) @[DPA.scala 16:49]
    node vecA_parts_1 = asSInt(_vecA_parts_T_1) @[DPA.scala 16:69]
    node _vecA_parts_T_2 = bits(io.vecA, 23, 16) @[DPA.scala 16:49]
    node vecA_parts_2 = asSInt(_vecA_parts_T_2) @[DPA.scala 16:69]
    node _vecA_parts_T_3 = bits(io.vecA, 31, 24) @[DPA.scala 16:49]
    node vecA_parts_3 = asSInt(_vecA_parts_T_3) @[DPA.scala 16:69]
    node _vecB_parts_T = bits(io.vecB, 7, 0) @[DPA.scala 17:49]
    node vecB_parts_0 = asSInt(_vecB_parts_T) @[DPA.scala 17:69]
    node _vecB_parts_T_1 = bits(io.vecB, 15, 8) @[DPA.scala 17:49]
    node vecB_parts_1 = asSInt(_vecB_parts_T_1) @[DPA.scala 17:69]
    node _vecB_parts_T_2 = bits(io.vecB, 23, 16) @[DPA.scala 17:49]
    node vecB_parts_2 = asSInt(_vecB_parts_T_2) @[DPA.scala 17:69]
    node _vecB_parts_T_3 = bits(io.vecB, 31, 24) @[DPA.scala 17:49]
    node vecB_parts_3 = asSInt(_vecB_parts_T_3) @[DPA.scala 17:69]
    node products_0 = mul(vecA_parts_0, vecB_parts_0) @[DPA.scala 20:68]
    node products_1 = mul(vecA_parts_1, vecB_parts_1) @[DPA.scala 20:68]
    node products_2 = mul(vecA_parts_2, vecB_parts_2) @[DPA.scala 20:68]
    node products_3 = mul(vecA_parts_3, vecB_parts_3) @[DPA.scala 20:68]
    node _io_eleC_T = add(products_0, products_1) @[DPA.scala 23:32]
    node _io_eleC_T_1 = tail(_io_eleC_T, 1) @[DPA.scala 23:32]
    node _io_eleC_T_2 = asSInt(_io_eleC_T_1) @[DPA.scala 23:32]
    node _io_eleC_T_3 = add(_io_eleC_T_2, products_2) @[DPA.scala 23:32]
    node _io_eleC_T_4 = tail(_io_eleC_T_3, 1) @[DPA.scala 23:32]
    node _io_eleC_T_5 = asSInt(_io_eleC_T_4) @[DPA.scala 23:32]
    node _io_eleC_T_6 = add(_io_eleC_T_5, products_3) @[DPA.scala 23:32]
    node _io_eleC_T_7 = tail(_io_eleC_T_6, 1) @[DPA.scala 23:32]
    node _io_eleC_T_8 = asSInt(_io_eleC_T_7) @[DPA.scala 23:32]
    io.eleC <= _io_eleC_T_8 @[DPA.scala 23:11]

  module PEcube_7 :
    input clock : Clock
    input reset : Reset
    output io : { flip vecAin : UInt<32>, flip vecBin : UInt<32>, flip eleCin : SInt<32>, flip muxCtrl : UInt<1>, vecAout : UInt<32>, vecBout : UInt<32>, eleCout : SInt<32>}

    inst subDPA of DPA_7 @[PE.scala 21:22]
    subDPA.clock <= clock
    subDPA.reset <= reset
    reg regR : UInt<32>, clock with :
      reset => (reset, UInt<32>("h0")) @[PE.scala 22:21]
    subDPA.io.vecA <= io.vecAin @[PE.scala 24:18]
    subDPA.io.vecB <= io.vecBin @[PE.scala 25:18]
    node _io_eleCout_T = mux(io.muxCtrl, subDPA.io.eleC, io.eleCin) @[PE.scala 26:20]
    io.eleCout <= _io_eleCout_T @[PE.scala 26:14]
    io.vecAout <= io.vecAin @[PE.scala 28:14]
    regR <= io.vecBin @[PE.scala 29:8]
    io.vecBout <= regR @[PE.scala 30:14]

  module DPA_8 :
    input clock : Clock
    input reset : Reset
    output io : { flip vecA : UInt<32>, flip vecB : UInt<32>, eleC : SInt<32>}

    node _vecA_parts_T = bits(io.vecA, 7, 0) @[DPA.scala 16:49]
    node vecA_parts_0 = asSInt(_vecA_parts_T) @[DPA.scala 16:69]
    node _vecA_parts_T_1 = bits(io.vecA, 15, 8) @[DPA.scala 16:49]
    node vecA_parts_1 = asSInt(_vecA_parts_T_1) @[DPA.scala 16:69]
    node _vecA_parts_T_2 = bits(io.vecA, 23, 16) @[DPA.scala 16:49]
    node vecA_parts_2 = asSInt(_vecA_parts_T_2) @[DPA.scala 16:69]
    node _vecA_parts_T_3 = bits(io.vecA, 31, 24) @[DPA.scala 16:49]
    node vecA_parts_3 = asSInt(_vecA_parts_T_3) @[DPA.scala 16:69]
    node _vecB_parts_T = bits(io.vecB, 7, 0) @[DPA.scala 17:49]
    node vecB_parts_0 = asSInt(_vecB_parts_T) @[DPA.scala 17:69]
    node _vecB_parts_T_1 = bits(io.vecB, 15, 8) @[DPA.scala 17:49]
    node vecB_parts_1 = asSInt(_vecB_parts_T_1) @[DPA.scala 17:69]
    node _vecB_parts_T_2 = bits(io.vecB, 23, 16) @[DPA.scala 17:49]
    node vecB_parts_2 = asSInt(_vecB_parts_T_2) @[DPA.scala 17:69]
    node _vecB_parts_T_3 = bits(io.vecB, 31, 24) @[DPA.scala 17:49]
    node vecB_parts_3 = asSInt(_vecB_parts_T_3) @[DPA.scala 17:69]
    node products_0 = mul(vecA_parts_0, vecB_parts_0) @[DPA.scala 20:68]
    node products_1 = mul(vecA_parts_1, vecB_parts_1) @[DPA.scala 20:68]
    node products_2 = mul(vecA_parts_2, vecB_parts_2) @[DPA.scala 20:68]
    node products_3 = mul(vecA_parts_3, vecB_parts_3) @[DPA.scala 20:68]
    node _io_eleC_T = add(products_0, products_1) @[DPA.scala 23:32]
    node _io_eleC_T_1 = tail(_io_eleC_T, 1) @[DPA.scala 23:32]
    node _io_eleC_T_2 = asSInt(_io_eleC_T_1) @[DPA.scala 23:32]
    node _io_eleC_T_3 = add(_io_eleC_T_2, products_2) @[DPA.scala 23:32]
    node _io_eleC_T_4 = tail(_io_eleC_T_3, 1) @[DPA.scala 23:32]
    node _io_eleC_T_5 = asSInt(_io_eleC_T_4) @[DPA.scala 23:32]
    node _io_eleC_T_6 = add(_io_eleC_T_5, products_3) @[DPA.scala 23:32]
    node _io_eleC_T_7 = tail(_io_eleC_T_6, 1) @[DPA.scala 23:32]
    node _io_eleC_T_8 = asSInt(_io_eleC_T_7) @[DPA.scala 23:32]
    io.eleC <= _io_eleC_T_8 @[DPA.scala 23:11]

  module PEcube_8 :
    input clock : Clock
    input reset : Reset
    output io : { flip vecAin : UInt<32>, flip vecBin : UInt<32>, flip eleCin : SInt<32>, flip muxCtrl : UInt<1>, vecAout : UInt<32>, vecBout : UInt<32>, eleCout : SInt<32>}

    inst subDPA of DPA_8 @[PE.scala 21:22]
    subDPA.clock <= clock
    subDPA.reset <= reset
    reg regR : UInt<32>, clock with :
      reset => (reset, UInt<32>("h0")) @[PE.scala 22:21]
    subDPA.io.vecA <= io.vecAin @[PE.scala 24:18]
    subDPA.io.vecB <= io.vecBin @[PE.scala 25:18]
    node _io_eleCout_T = mux(io.muxCtrl, subDPA.io.eleC, io.eleCin) @[PE.scala 26:20]
    io.eleCout <= _io_eleCout_T @[PE.scala 26:14]
    io.vecAout <= io.vecAin @[PE.scala 28:14]
    regR <= io.vecBin @[PE.scala 29:8]
    io.vecBout <= regR @[PE.scala 30:14]

  module DPA_9 :
    input clock : Clock
    input reset : Reset
    output io : { flip vecA : UInt<32>, flip vecB : UInt<32>, eleC : SInt<32>}

    node _vecA_parts_T = bits(io.vecA, 7, 0) @[DPA.scala 16:49]
    node vecA_parts_0 = asSInt(_vecA_parts_T) @[DPA.scala 16:69]
    node _vecA_parts_T_1 = bits(io.vecA, 15, 8) @[DPA.scala 16:49]
    node vecA_parts_1 = asSInt(_vecA_parts_T_1) @[DPA.scala 16:69]
    node _vecA_parts_T_2 = bits(io.vecA, 23, 16) @[DPA.scala 16:49]
    node vecA_parts_2 = asSInt(_vecA_parts_T_2) @[DPA.scala 16:69]
    node _vecA_parts_T_3 = bits(io.vecA, 31, 24) @[DPA.scala 16:49]
    node vecA_parts_3 = asSInt(_vecA_parts_T_3) @[DPA.scala 16:69]
    node _vecB_parts_T = bits(io.vecB, 7, 0) @[DPA.scala 17:49]
    node vecB_parts_0 = asSInt(_vecB_parts_T) @[DPA.scala 17:69]
    node _vecB_parts_T_1 = bits(io.vecB, 15, 8) @[DPA.scala 17:49]
    node vecB_parts_1 = asSInt(_vecB_parts_T_1) @[DPA.scala 17:69]
    node _vecB_parts_T_2 = bits(io.vecB, 23, 16) @[DPA.scala 17:49]
    node vecB_parts_2 = asSInt(_vecB_parts_T_2) @[DPA.scala 17:69]
    node _vecB_parts_T_3 = bits(io.vecB, 31, 24) @[DPA.scala 17:49]
    node vecB_parts_3 = asSInt(_vecB_parts_T_3) @[DPA.scala 17:69]
    node products_0 = mul(vecA_parts_0, vecB_parts_0) @[DPA.scala 20:68]
    node products_1 = mul(vecA_parts_1, vecB_parts_1) @[DPA.scala 20:68]
    node products_2 = mul(vecA_parts_2, vecB_parts_2) @[DPA.scala 20:68]
    node products_3 = mul(vecA_parts_3, vecB_parts_3) @[DPA.scala 20:68]
    node _io_eleC_T = add(products_0, products_1) @[DPA.scala 23:32]
    node _io_eleC_T_1 = tail(_io_eleC_T, 1) @[DPA.scala 23:32]
    node _io_eleC_T_2 = asSInt(_io_eleC_T_1) @[DPA.scala 23:32]
    node _io_eleC_T_3 = add(_io_eleC_T_2, products_2) @[DPA.scala 23:32]
    node _io_eleC_T_4 = tail(_io_eleC_T_3, 1) @[DPA.scala 23:32]
    node _io_eleC_T_5 = asSInt(_io_eleC_T_4) @[DPA.scala 23:32]
    node _io_eleC_T_6 = add(_io_eleC_T_5, products_3) @[DPA.scala 23:32]
    node _io_eleC_T_7 = tail(_io_eleC_T_6, 1) @[DPA.scala 23:32]
    node _io_eleC_T_8 = asSInt(_io_eleC_T_7) @[DPA.scala 23:32]
    io.eleC <= _io_eleC_T_8 @[DPA.scala 23:11]

  module PEcube_9 :
    input clock : Clock
    input reset : Reset
    output io : { flip vecAin : UInt<32>, flip vecBin : UInt<32>, flip eleCin : SInt<32>, flip muxCtrl : UInt<1>, vecAout : UInt<32>, vecBout : UInt<32>, eleCout : SInt<32>}

    inst subDPA of DPA_9 @[PE.scala 21:22]
    subDPA.clock <= clock
    subDPA.reset <= reset
    reg regR : UInt<32>, clock with :
      reset => (reset, UInt<32>("h0")) @[PE.scala 22:21]
    subDPA.io.vecA <= io.vecAin @[PE.scala 24:18]
    subDPA.io.vecB <= io.vecBin @[PE.scala 25:18]
    node _io_eleCout_T = mux(io.muxCtrl, subDPA.io.eleC, io.eleCin) @[PE.scala 26:20]
    io.eleCout <= _io_eleCout_T @[PE.scala 26:14]
    io.vecAout <= io.vecAin @[PE.scala 28:14]
    regR <= io.vecBin @[PE.scala 29:8]
    io.vecBout <= regR @[PE.scala 30:14]

  module DPA_10 :
    input clock : Clock
    input reset : Reset
    output io : { flip vecA : UInt<32>, flip vecB : UInt<32>, eleC : SInt<32>}

    node _vecA_parts_T = bits(io.vecA, 7, 0) @[DPA.scala 16:49]
    node vecA_parts_0 = asSInt(_vecA_parts_T) @[DPA.scala 16:69]
    node _vecA_parts_T_1 = bits(io.vecA, 15, 8) @[DPA.scala 16:49]
    node vecA_parts_1 = asSInt(_vecA_parts_T_1) @[DPA.scala 16:69]
    node _vecA_parts_T_2 = bits(io.vecA, 23, 16) @[DPA.scala 16:49]
    node vecA_parts_2 = asSInt(_vecA_parts_T_2) @[DPA.scala 16:69]
    node _vecA_parts_T_3 = bits(io.vecA, 31, 24) @[DPA.scala 16:49]
    node vecA_parts_3 = asSInt(_vecA_parts_T_3) @[DPA.scala 16:69]
    node _vecB_parts_T = bits(io.vecB, 7, 0) @[DPA.scala 17:49]
    node vecB_parts_0 = asSInt(_vecB_parts_T) @[DPA.scala 17:69]
    node _vecB_parts_T_1 = bits(io.vecB, 15, 8) @[DPA.scala 17:49]
    node vecB_parts_1 = asSInt(_vecB_parts_T_1) @[DPA.scala 17:69]
    node _vecB_parts_T_2 = bits(io.vecB, 23, 16) @[DPA.scala 17:49]
    node vecB_parts_2 = asSInt(_vecB_parts_T_2) @[DPA.scala 17:69]
    node _vecB_parts_T_3 = bits(io.vecB, 31, 24) @[DPA.scala 17:49]
    node vecB_parts_3 = asSInt(_vecB_parts_T_3) @[DPA.scala 17:69]
    node products_0 = mul(vecA_parts_0, vecB_parts_0) @[DPA.scala 20:68]
    node products_1 = mul(vecA_parts_1, vecB_parts_1) @[DPA.scala 20:68]
    node products_2 = mul(vecA_parts_2, vecB_parts_2) @[DPA.scala 20:68]
    node products_3 = mul(vecA_parts_3, vecB_parts_3) @[DPA.scala 20:68]
    node _io_eleC_T = add(products_0, products_1) @[DPA.scala 23:32]
    node _io_eleC_T_1 = tail(_io_eleC_T, 1) @[DPA.scala 23:32]
    node _io_eleC_T_2 = asSInt(_io_eleC_T_1) @[DPA.scala 23:32]
    node _io_eleC_T_3 = add(_io_eleC_T_2, products_2) @[DPA.scala 23:32]
    node _io_eleC_T_4 = tail(_io_eleC_T_3, 1) @[DPA.scala 23:32]
    node _io_eleC_T_5 = asSInt(_io_eleC_T_4) @[DPA.scala 23:32]
    node _io_eleC_T_6 = add(_io_eleC_T_5, products_3) @[DPA.scala 23:32]
    node _io_eleC_T_7 = tail(_io_eleC_T_6, 1) @[DPA.scala 23:32]
    node _io_eleC_T_8 = asSInt(_io_eleC_T_7) @[DPA.scala 23:32]
    io.eleC <= _io_eleC_T_8 @[DPA.scala 23:11]

  module PEcube_10 :
    input clock : Clock
    input reset : Reset
    output io : { flip vecAin : UInt<32>, flip vecBin : UInt<32>, flip eleCin : SInt<32>, flip muxCtrl : UInt<1>, vecAout : UInt<32>, vecBout : UInt<32>, eleCout : SInt<32>}

    inst subDPA of DPA_10 @[PE.scala 21:22]
    subDPA.clock <= clock
    subDPA.reset <= reset
    reg regR : UInt<32>, clock with :
      reset => (reset, UInt<32>("h0")) @[PE.scala 22:21]
    subDPA.io.vecA <= io.vecAin @[PE.scala 24:18]
    subDPA.io.vecB <= io.vecBin @[PE.scala 25:18]
    node _io_eleCout_T = mux(io.muxCtrl, subDPA.io.eleC, io.eleCin) @[PE.scala 26:20]
    io.eleCout <= _io_eleCout_T @[PE.scala 26:14]
    io.vecAout <= io.vecAin @[PE.scala 28:14]
    regR <= io.vecBin @[PE.scala 29:8]
    io.vecBout <= regR @[PE.scala 30:14]

  module DPA_11 :
    input clock : Clock
    input reset : Reset
    output io : { flip vecA : UInt<32>, flip vecB : UInt<32>, eleC : SInt<32>}

    node _vecA_parts_T = bits(io.vecA, 7, 0) @[DPA.scala 16:49]
    node vecA_parts_0 = asSInt(_vecA_parts_T) @[DPA.scala 16:69]
    node _vecA_parts_T_1 = bits(io.vecA, 15, 8) @[DPA.scala 16:49]
    node vecA_parts_1 = asSInt(_vecA_parts_T_1) @[DPA.scala 16:69]
    node _vecA_parts_T_2 = bits(io.vecA, 23, 16) @[DPA.scala 16:49]
    node vecA_parts_2 = asSInt(_vecA_parts_T_2) @[DPA.scala 16:69]
    node _vecA_parts_T_3 = bits(io.vecA, 31, 24) @[DPA.scala 16:49]
    node vecA_parts_3 = asSInt(_vecA_parts_T_3) @[DPA.scala 16:69]
    node _vecB_parts_T = bits(io.vecB, 7, 0) @[DPA.scala 17:49]
    node vecB_parts_0 = asSInt(_vecB_parts_T) @[DPA.scala 17:69]
    node _vecB_parts_T_1 = bits(io.vecB, 15, 8) @[DPA.scala 17:49]
    node vecB_parts_1 = asSInt(_vecB_parts_T_1) @[DPA.scala 17:69]
    node _vecB_parts_T_2 = bits(io.vecB, 23, 16) @[DPA.scala 17:49]
    node vecB_parts_2 = asSInt(_vecB_parts_T_2) @[DPA.scala 17:69]
    node _vecB_parts_T_3 = bits(io.vecB, 31, 24) @[DPA.scala 17:49]
    node vecB_parts_3 = asSInt(_vecB_parts_T_3) @[DPA.scala 17:69]
    node products_0 = mul(vecA_parts_0, vecB_parts_0) @[DPA.scala 20:68]
    node products_1 = mul(vecA_parts_1, vecB_parts_1) @[DPA.scala 20:68]
    node products_2 = mul(vecA_parts_2, vecB_parts_2) @[DPA.scala 20:68]
    node products_3 = mul(vecA_parts_3, vecB_parts_3) @[DPA.scala 20:68]
    node _io_eleC_T = add(products_0, products_1) @[DPA.scala 23:32]
    node _io_eleC_T_1 = tail(_io_eleC_T, 1) @[DPA.scala 23:32]
    node _io_eleC_T_2 = asSInt(_io_eleC_T_1) @[DPA.scala 23:32]
    node _io_eleC_T_3 = add(_io_eleC_T_2, products_2) @[DPA.scala 23:32]
    node _io_eleC_T_4 = tail(_io_eleC_T_3, 1) @[DPA.scala 23:32]
    node _io_eleC_T_5 = asSInt(_io_eleC_T_4) @[DPA.scala 23:32]
    node _io_eleC_T_6 = add(_io_eleC_T_5, products_3) @[DPA.scala 23:32]
    node _io_eleC_T_7 = tail(_io_eleC_T_6, 1) @[DPA.scala 23:32]
    node _io_eleC_T_8 = asSInt(_io_eleC_T_7) @[DPA.scala 23:32]
    io.eleC <= _io_eleC_T_8 @[DPA.scala 23:11]

  module PEcube_11 :
    input clock : Clock
    input reset : Reset
    output io : { flip vecAin : UInt<32>, flip vecBin : UInt<32>, flip eleCin : SInt<32>, flip muxCtrl : UInt<1>, vecAout : UInt<32>, vecBout : UInt<32>, eleCout : SInt<32>}

    inst subDPA of DPA_11 @[PE.scala 21:22]
    subDPA.clock <= clock
    subDPA.reset <= reset
    reg regR : UInt<32>, clock with :
      reset => (reset, UInt<32>("h0")) @[PE.scala 22:21]
    subDPA.io.vecA <= io.vecAin @[PE.scala 24:18]
    subDPA.io.vecB <= io.vecBin @[PE.scala 25:18]
    node _io_eleCout_T = mux(io.muxCtrl, subDPA.io.eleC, io.eleCin) @[PE.scala 26:20]
    io.eleCout <= _io_eleCout_T @[PE.scala 26:14]
    io.vecAout <= io.vecAin @[PE.scala 28:14]
    regR <= io.vecBin @[PE.scala 29:8]
    io.vecBout <= regR @[PE.scala 30:14]

  module DPA_12 :
    input clock : Clock
    input reset : Reset
    output io : { flip vecA : UInt<32>, flip vecB : UInt<32>, eleC : SInt<32>}

    node _vecA_parts_T = bits(io.vecA, 7, 0) @[DPA.scala 16:49]
    node vecA_parts_0 = asSInt(_vecA_parts_T) @[DPA.scala 16:69]
    node _vecA_parts_T_1 = bits(io.vecA, 15, 8) @[DPA.scala 16:49]
    node vecA_parts_1 = asSInt(_vecA_parts_T_1) @[DPA.scala 16:69]
    node _vecA_parts_T_2 = bits(io.vecA, 23, 16) @[DPA.scala 16:49]
    node vecA_parts_2 = asSInt(_vecA_parts_T_2) @[DPA.scala 16:69]
    node _vecA_parts_T_3 = bits(io.vecA, 31, 24) @[DPA.scala 16:49]
    node vecA_parts_3 = asSInt(_vecA_parts_T_3) @[DPA.scala 16:69]
    node _vecB_parts_T = bits(io.vecB, 7, 0) @[DPA.scala 17:49]
    node vecB_parts_0 = asSInt(_vecB_parts_T) @[DPA.scala 17:69]
    node _vecB_parts_T_1 = bits(io.vecB, 15, 8) @[DPA.scala 17:49]
    node vecB_parts_1 = asSInt(_vecB_parts_T_1) @[DPA.scala 17:69]
    node _vecB_parts_T_2 = bits(io.vecB, 23, 16) @[DPA.scala 17:49]
    node vecB_parts_2 = asSInt(_vecB_parts_T_2) @[DPA.scala 17:69]
    node _vecB_parts_T_3 = bits(io.vecB, 31, 24) @[DPA.scala 17:49]
    node vecB_parts_3 = asSInt(_vecB_parts_T_3) @[DPA.scala 17:69]
    node products_0 = mul(vecA_parts_0, vecB_parts_0) @[DPA.scala 20:68]
    node products_1 = mul(vecA_parts_1, vecB_parts_1) @[DPA.scala 20:68]
    node products_2 = mul(vecA_parts_2, vecB_parts_2) @[DPA.scala 20:68]
    node products_3 = mul(vecA_parts_3, vecB_parts_3) @[DPA.scala 20:68]
    node _io_eleC_T = add(products_0, products_1) @[DPA.scala 23:32]
    node _io_eleC_T_1 = tail(_io_eleC_T, 1) @[DPA.scala 23:32]
    node _io_eleC_T_2 = asSInt(_io_eleC_T_1) @[DPA.scala 23:32]
    node _io_eleC_T_3 = add(_io_eleC_T_2, products_2) @[DPA.scala 23:32]
    node _io_eleC_T_4 = tail(_io_eleC_T_3, 1) @[DPA.scala 23:32]
    node _io_eleC_T_5 = asSInt(_io_eleC_T_4) @[DPA.scala 23:32]
    node _io_eleC_T_6 = add(_io_eleC_T_5, products_3) @[DPA.scala 23:32]
    node _io_eleC_T_7 = tail(_io_eleC_T_6, 1) @[DPA.scala 23:32]
    node _io_eleC_T_8 = asSInt(_io_eleC_T_7) @[DPA.scala 23:32]
    io.eleC <= _io_eleC_T_8 @[DPA.scala 23:11]

  module PEcube_12 :
    input clock : Clock
    input reset : Reset
    output io : { flip vecAin : UInt<32>, flip vecBin : UInt<32>, flip eleCin : SInt<32>, flip muxCtrl : UInt<1>, vecAout : UInt<32>, vecBout : UInt<32>, eleCout : SInt<32>}

    inst subDPA of DPA_12 @[PE.scala 21:22]
    subDPA.clock <= clock
    subDPA.reset <= reset
    reg regR : UInt<32>, clock with :
      reset => (reset, UInt<32>("h0")) @[PE.scala 22:21]
    subDPA.io.vecA <= io.vecAin @[PE.scala 24:18]
    subDPA.io.vecB <= io.vecBin @[PE.scala 25:18]
    node _io_eleCout_T = mux(io.muxCtrl, subDPA.io.eleC, io.eleCin) @[PE.scala 26:20]
    io.eleCout <= _io_eleCout_T @[PE.scala 26:14]
    io.vecAout <= io.vecAin @[PE.scala 28:14]
    regR <= io.vecBin @[PE.scala 29:8]
    io.vecBout <= regR @[PE.scala 30:14]

  module DPA_13 :
    input clock : Clock
    input reset : Reset
    output io : { flip vecA : UInt<32>, flip vecB : UInt<32>, eleC : SInt<32>}

    node _vecA_parts_T = bits(io.vecA, 7, 0) @[DPA.scala 16:49]
    node vecA_parts_0 = asSInt(_vecA_parts_T) @[DPA.scala 16:69]
    node _vecA_parts_T_1 = bits(io.vecA, 15, 8) @[DPA.scala 16:49]
    node vecA_parts_1 = asSInt(_vecA_parts_T_1) @[DPA.scala 16:69]
    node _vecA_parts_T_2 = bits(io.vecA, 23, 16) @[DPA.scala 16:49]
    node vecA_parts_2 = asSInt(_vecA_parts_T_2) @[DPA.scala 16:69]
    node _vecA_parts_T_3 = bits(io.vecA, 31, 24) @[DPA.scala 16:49]
    node vecA_parts_3 = asSInt(_vecA_parts_T_3) @[DPA.scala 16:69]
    node _vecB_parts_T = bits(io.vecB, 7, 0) @[DPA.scala 17:49]
    node vecB_parts_0 = asSInt(_vecB_parts_T) @[DPA.scala 17:69]
    node _vecB_parts_T_1 = bits(io.vecB, 15, 8) @[DPA.scala 17:49]
    node vecB_parts_1 = asSInt(_vecB_parts_T_1) @[DPA.scala 17:69]
    node _vecB_parts_T_2 = bits(io.vecB, 23, 16) @[DPA.scala 17:49]
    node vecB_parts_2 = asSInt(_vecB_parts_T_2) @[DPA.scala 17:69]
    node _vecB_parts_T_3 = bits(io.vecB, 31, 24) @[DPA.scala 17:49]
    node vecB_parts_3 = asSInt(_vecB_parts_T_3) @[DPA.scala 17:69]
    node products_0 = mul(vecA_parts_0, vecB_parts_0) @[DPA.scala 20:68]
    node products_1 = mul(vecA_parts_1, vecB_parts_1) @[DPA.scala 20:68]
    node products_2 = mul(vecA_parts_2, vecB_parts_2) @[DPA.scala 20:68]
    node products_3 = mul(vecA_parts_3, vecB_parts_3) @[DPA.scala 20:68]
    node _io_eleC_T = add(products_0, products_1) @[DPA.scala 23:32]
    node _io_eleC_T_1 = tail(_io_eleC_T, 1) @[DPA.scala 23:32]
    node _io_eleC_T_2 = asSInt(_io_eleC_T_1) @[DPA.scala 23:32]
    node _io_eleC_T_3 = add(_io_eleC_T_2, products_2) @[DPA.scala 23:32]
    node _io_eleC_T_4 = tail(_io_eleC_T_3, 1) @[DPA.scala 23:32]
    node _io_eleC_T_5 = asSInt(_io_eleC_T_4) @[DPA.scala 23:32]
    node _io_eleC_T_6 = add(_io_eleC_T_5, products_3) @[DPA.scala 23:32]
    node _io_eleC_T_7 = tail(_io_eleC_T_6, 1) @[DPA.scala 23:32]
    node _io_eleC_T_8 = asSInt(_io_eleC_T_7) @[DPA.scala 23:32]
    io.eleC <= _io_eleC_T_8 @[DPA.scala 23:11]

  module PEcube_13 :
    input clock : Clock
    input reset : Reset
    output io : { flip vecAin : UInt<32>, flip vecBin : UInt<32>, flip eleCin : SInt<32>, flip muxCtrl : UInt<1>, vecAout : UInt<32>, vecBout : UInt<32>, eleCout : SInt<32>}

    inst subDPA of DPA_13 @[PE.scala 21:22]
    subDPA.clock <= clock
    subDPA.reset <= reset
    reg regR : UInt<32>, clock with :
      reset => (reset, UInt<32>("h0")) @[PE.scala 22:21]
    subDPA.io.vecA <= io.vecAin @[PE.scala 24:18]
    subDPA.io.vecB <= io.vecBin @[PE.scala 25:18]
    node _io_eleCout_T = mux(io.muxCtrl, subDPA.io.eleC, io.eleCin) @[PE.scala 26:20]
    io.eleCout <= _io_eleCout_T @[PE.scala 26:14]
    io.vecAout <= io.vecAin @[PE.scala 28:14]
    regR <= io.vecBin @[PE.scala 29:8]
    io.vecBout <= regR @[PE.scala 30:14]

  module DPA_14 :
    input clock : Clock
    input reset : Reset
    output io : { flip vecA : UInt<32>, flip vecB : UInt<32>, eleC : SInt<32>}

    node _vecA_parts_T = bits(io.vecA, 7, 0) @[DPA.scala 16:49]
    node vecA_parts_0 = asSInt(_vecA_parts_T) @[DPA.scala 16:69]
    node _vecA_parts_T_1 = bits(io.vecA, 15, 8) @[DPA.scala 16:49]
    node vecA_parts_1 = asSInt(_vecA_parts_T_1) @[DPA.scala 16:69]
    node _vecA_parts_T_2 = bits(io.vecA, 23, 16) @[DPA.scala 16:49]
    node vecA_parts_2 = asSInt(_vecA_parts_T_2) @[DPA.scala 16:69]
    node _vecA_parts_T_3 = bits(io.vecA, 31, 24) @[DPA.scala 16:49]
    node vecA_parts_3 = asSInt(_vecA_parts_T_3) @[DPA.scala 16:69]
    node _vecB_parts_T = bits(io.vecB, 7, 0) @[DPA.scala 17:49]
    node vecB_parts_0 = asSInt(_vecB_parts_T) @[DPA.scala 17:69]
    node _vecB_parts_T_1 = bits(io.vecB, 15, 8) @[DPA.scala 17:49]
    node vecB_parts_1 = asSInt(_vecB_parts_T_1) @[DPA.scala 17:69]
    node _vecB_parts_T_2 = bits(io.vecB, 23, 16) @[DPA.scala 17:49]
    node vecB_parts_2 = asSInt(_vecB_parts_T_2) @[DPA.scala 17:69]
    node _vecB_parts_T_3 = bits(io.vecB, 31, 24) @[DPA.scala 17:49]
    node vecB_parts_3 = asSInt(_vecB_parts_T_3) @[DPA.scala 17:69]
    node products_0 = mul(vecA_parts_0, vecB_parts_0) @[DPA.scala 20:68]
    node products_1 = mul(vecA_parts_1, vecB_parts_1) @[DPA.scala 20:68]
    node products_2 = mul(vecA_parts_2, vecB_parts_2) @[DPA.scala 20:68]
    node products_3 = mul(vecA_parts_3, vecB_parts_3) @[DPA.scala 20:68]
    node _io_eleC_T = add(products_0, products_1) @[DPA.scala 23:32]
    node _io_eleC_T_1 = tail(_io_eleC_T, 1) @[DPA.scala 23:32]
    node _io_eleC_T_2 = asSInt(_io_eleC_T_1) @[DPA.scala 23:32]
    node _io_eleC_T_3 = add(_io_eleC_T_2, products_2) @[DPA.scala 23:32]
    node _io_eleC_T_4 = tail(_io_eleC_T_3, 1) @[DPA.scala 23:32]
    node _io_eleC_T_5 = asSInt(_io_eleC_T_4) @[DPA.scala 23:32]
    node _io_eleC_T_6 = add(_io_eleC_T_5, products_3) @[DPA.scala 23:32]
    node _io_eleC_T_7 = tail(_io_eleC_T_6, 1) @[DPA.scala 23:32]
    node _io_eleC_T_8 = asSInt(_io_eleC_T_7) @[DPA.scala 23:32]
    io.eleC <= _io_eleC_T_8 @[DPA.scala 23:11]

  module PEcube_14 :
    input clock : Clock
    input reset : Reset
    output io : { flip vecAin : UInt<32>, flip vecBin : UInt<32>, flip eleCin : SInt<32>, flip muxCtrl : UInt<1>, vecAout : UInt<32>, vecBout : UInt<32>, eleCout : SInt<32>}

    inst subDPA of DPA_14 @[PE.scala 21:22]
    subDPA.clock <= clock
    subDPA.reset <= reset
    reg regR : UInt<32>, clock with :
      reset => (reset, UInt<32>("h0")) @[PE.scala 22:21]
    subDPA.io.vecA <= io.vecAin @[PE.scala 24:18]
    subDPA.io.vecB <= io.vecBin @[PE.scala 25:18]
    node _io_eleCout_T = mux(io.muxCtrl, subDPA.io.eleC, io.eleCin) @[PE.scala 26:20]
    io.eleCout <= _io_eleCout_T @[PE.scala 26:14]
    io.vecAout <= io.vecAin @[PE.scala 28:14]
    regR <= io.vecBin @[PE.scala 29:8]
    io.vecBout <= regR @[PE.scala 30:14]

  module DPA_15 :
    input clock : Clock
    input reset : Reset
    output io : { flip vecA : UInt<32>, flip vecB : UInt<32>, eleC : SInt<32>}

    node _vecA_parts_T = bits(io.vecA, 7, 0) @[DPA.scala 16:49]
    node vecA_parts_0 = asSInt(_vecA_parts_T) @[DPA.scala 16:69]
    node _vecA_parts_T_1 = bits(io.vecA, 15, 8) @[DPA.scala 16:49]
    node vecA_parts_1 = asSInt(_vecA_parts_T_1) @[DPA.scala 16:69]
    node _vecA_parts_T_2 = bits(io.vecA, 23, 16) @[DPA.scala 16:49]
    node vecA_parts_2 = asSInt(_vecA_parts_T_2) @[DPA.scala 16:69]
    node _vecA_parts_T_3 = bits(io.vecA, 31, 24) @[DPA.scala 16:49]
    node vecA_parts_3 = asSInt(_vecA_parts_T_3) @[DPA.scala 16:69]
    node _vecB_parts_T = bits(io.vecB, 7, 0) @[DPA.scala 17:49]
    node vecB_parts_0 = asSInt(_vecB_parts_T) @[DPA.scala 17:69]
    node _vecB_parts_T_1 = bits(io.vecB, 15, 8) @[DPA.scala 17:49]
    node vecB_parts_1 = asSInt(_vecB_parts_T_1) @[DPA.scala 17:69]
    node _vecB_parts_T_2 = bits(io.vecB, 23, 16) @[DPA.scala 17:49]
    node vecB_parts_2 = asSInt(_vecB_parts_T_2) @[DPA.scala 17:69]
    node _vecB_parts_T_3 = bits(io.vecB, 31, 24) @[DPA.scala 17:49]
    node vecB_parts_3 = asSInt(_vecB_parts_T_3) @[DPA.scala 17:69]
    node products_0 = mul(vecA_parts_0, vecB_parts_0) @[DPA.scala 20:68]
    node products_1 = mul(vecA_parts_1, vecB_parts_1) @[DPA.scala 20:68]
    node products_2 = mul(vecA_parts_2, vecB_parts_2) @[DPA.scala 20:68]
    node products_3 = mul(vecA_parts_3, vecB_parts_3) @[DPA.scala 20:68]
    node _io_eleC_T = add(products_0, products_1) @[DPA.scala 23:32]
    node _io_eleC_T_1 = tail(_io_eleC_T, 1) @[DPA.scala 23:32]
    node _io_eleC_T_2 = asSInt(_io_eleC_T_1) @[DPA.scala 23:32]
    node _io_eleC_T_3 = add(_io_eleC_T_2, products_2) @[DPA.scala 23:32]
    node _io_eleC_T_4 = tail(_io_eleC_T_3, 1) @[DPA.scala 23:32]
    node _io_eleC_T_5 = asSInt(_io_eleC_T_4) @[DPA.scala 23:32]
    node _io_eleC_T_6 = add(_io_eleC_T_5, products_3) @[DPA.scala 23:32]
    node _io_eleC_T_7 = tail(_io_eleC_T_6, 1) @[DPA.scala 23:32]
    node _io_eleC_T_8 = asSInt(_io_eleC_T_7) @[DPA.scala 23:32]
    io.eleC <= _io_eleC_T_8 @[DPA.scala 23:11]

  module PEcube_15 :
    input clock : Clock
    input reset : Reset
    output io : { flip vecAin : UInt<32>, flip vecBin : UInt<32>, flip eleCin : SInt<32>, flip muxCtrl : UInt<1>, vecAout : UInt<32>, vecBout : UInt<32>, eleCout : SInt<32>}

    inst subDPA of DPA_15 @[PE.scala 21:22]
    subDPA.clock <= clock
    subDPA.reset <= reset
    reg regR : UInt<32>, clock with :
      reset => (reset, UInt<32>("h0")) @[PE.scala 22:21]
    subDPA.io.vecA <= io.vecAin @[PE.scala 24:18]
    subDPA.io.vecB <= io.vecBin @[PE.scala 25:18]
    node _io_eleCout_T = mux(io.muxCtrl, subDPA.io.eleC, io.eleCin) @[PE.scala 26:20]
    io.eleCout <= _io_eleCout_T @[PE.scala 26:14]
    io.vecAout <= io.vecAin @[PE.scala 28:14]
    regR <= io.vecBin @[PE.scala 29:8]
    io.vecBout <= regR @[PE.scala 30:14]

  module CUBE :
    input clock : Clock
    input reset : UInt<1>
    output io : { flip vecA : UInt<32>[4], flip vecB : UInt<32>[4], flip muxCtrl : UInt<1>[16], eleC : SInt<32>[4]}

    inst PEcube of PEcube @[CUBE.scala 21:42]
    PEcube.clock <= clock
    PEcube.reset <= reset
    inst PEcube_1 of PEcube_1 @[CUBE.scala 21:42]
    PEcube_1.clock <= clock
    PEcube_1.reset <= reset
    inst PEcube_2 of PEcube_2 @[CUBE.scala 21:42]
    PEcube_2.clock <= clock
    PEcube_2.reset <= reset
    inst PEcube_3 of PEcube_3 @[CUBE.scala 21:42]
    PEcube_3.clock <= clock
    PEcube_3.reset <= reset
    inst PEcube_4 of PEcube_4 @[CUBE.scala 21:42]
    PEcube_4.clock <= clock
    PEcube_4.reset <= reset
    inst PEcube_5 of PEcube_5 @[CUBE.scala 21:42]
    PEcube_5.clock <= clock
    PEcube_5.reset <= reset
    inst PEcube_6 of PEcube_6 @[CUBE.scala 21:42]
    PEcube_6.clock <= clock
    PEcube_6.reset <= reset
    inst PEcube_7 of PEcube_7 @[CUBE.scala 21:42]
    PEcube_7.clock <= clock
    PEcube_7.reset <= reset
    inst PEcube_8 of PEcube_8 @[CUBE.scala 21:42]
    PEcube_8.clock <= clock
    PEcube_8.reset <= reset
    inst PEcube_9 of PEcube_9 @[CUBE.scala 21:42]
    PEcube_9.clock <= clock
    PEcube_9.reset <= reset
    inst PEcube_10 of PEcube_10 @[CUBE.scala 21:42]
    PEcube_10.clock <= clock
    PEcube_10.reset <= reset
    inst PEcube_11 of PEcube_11 @[CUBE.scala 21:42]
    PEcube_11.clock <= clock
    PEcube_11.reset <= reset
    inst PEcube_12 of PEcube_12 @[CUBE.scala 21:42]
    PEcube_12.clock <= clock
    PEcube_12.reset <= reset
    inst PEcube_13 of PEcube_13 @[CUBE.scala 21:42]
    PEcube_13.clock <= clock
    PEcube_13.reset <= reset
    inst PEcube_14 of PEcube_14 @[CUBE.scala 21:42]
    PEcube_14.clock <= clock
    PEcube_14.reset <= reset
    inst PEcube_15 of PEcube_15 @[CUBE.scala 21:42]
    PEcube_15.clock <= clock
    PEcube_15.reset <= reset
    PEcube.io.muxCtrl <= io.muxCtrl[0] @[CUBE.scala 28:21]
    PEcube.io.eleCin <= asSInt(UInt<1>("h0")) @[CUBE.scala 34:22]
    PEcube_1.io.muxCtrl <= io.muxCtrl[1] @[CUBE.scala 28:21]
    PEcube_1.io.eleCin <= asSInt(UInt<1>("h0")) @[CUBE.scala 34:22]
    PEcube_1.io.vecAin <= PEcube.io.vecAout @[CUBE.scala 44:22]
    PEcube_2.io.muxCtrl <= io.muxCtrl[2] @[CUBE.scala 28:21]
    PEcube_2.io.eleCin <= asSInt(UInt<1>("h0")) @[CUBE.scala 34:22]
    PEcube_2.io.vecAin <= PEcube_1.io.vecAout @[CUBE.scala 44:22]
    PEcube_3.io.muxCtrl <= io.muxCtrl[3] @[CUBE.scala 28:21]
    PEcube_3.io.eleCin <= asSInt(UInt<1>("h0")) @[CUBE.scala 34:22]
    PEcube_3.io.vecAin <= PEcube_2.io.vecAout @[CUBE.scala 44:22]
    PEcube_4.io.muxCtrl <= io.muxCtrl[4] @[CUBE.scala 28:21]
    PEcube_4.io.eleCin <= PEcube.io.eleCout @[CUBE.scala 32:22]
    PEcube_4.io.vecBin <= PEcube.io.vecBout @[CUBE.scala 39:22]
    PEcube_5.io.muxCtrl <= io.muxCtrl[5] @[CUBE.scala 28:21]
    PEcube_5.io.eleCin <= PEcube_1.io.eleCout @[CUBE.scala 32:22]
    PEcube_5.io.vecBin <= PEcube_1.io.vecBout @[CUBE.scala 39:22]
    PEcube_5.io.vecAin <= PEcube_4.io.vecAout @[CUBE.scala 44:22]
    PEcube_6.io.muxCtrl <= io.muxCtrl[6] @[CUBE.scala 28:21]
    PEcube_6.io.eleCin <= PEcube_2.io.eleCout @[CUBE.scala 32:22]
    PEcube_6.io.vecBin <= PEcube_2.io.vecBout @[CUBE.scala 39:22]
    PEcube_6.io.vecAin <= PEcube_5.io.vecAout @[CUBE.scala 44:22]
    PEcube_7.io.muxCtrl <= io.muxCtrl[7] @[CUBE.scala 28:21]
    PEcube_7.io.eleCin <= PEcube_3.io.eleCout @[CUBE.scala 32:22]
    PEcube_7.io.vecBin <= PEcube_3.io.vecBout @[CUBE.scala 39:22]
    PEcube_7.io.vecAin <= PEcube_6.io.vecAout @[CUBE.scala 44:22]
    PEcube_8.io.muxCtrl <= io.muxCtrl[8] @[CUBE.scala 28:21]
    PEcube_8.io.eleCin <= PEcube_4.io.eleCout @[CUBE.scala 32:22]
    PEcube_8.io.vecBin <= PEcube_4.io.vecBout @[CUBE.scala 39:22]
    PEcube_9.io.muxCtrl <= io.muxCtrl[9] @[CUBE.scala 28:21]
    PEcube_9.io.eleCin <= PEcube_5.io.eleCout @[CUBE.scala 32:22]
    PEcube_9.io.vecBin <= PEcube_5.io.vecBout @[CUBE.scala 39:22]
    PEcube_9.io.vecAin <= PEcube_8.io.vecAout @[CUBE.scala 44:22]
    PEcube_10.io.muxCtrl <= io.muxCtrl[10] @[CUBE.scala 28:21]
    PEcube_10.io.eleCin <= PEcube_6.io.eleCout @[CUBE.scala 32:22]
    PEcube_10.io.vecBin <= PEcube_6.io.vecBout @[CUBE.scala 39:22]
    PEcube_10.io.vecAin <= PEcube_9.io.vecAout @[CUBE.scala 44:22]
    PEcube_11.io.muxCtrl <= io.muxCtrl[11] @[CUBE.scala 28:21]
    PEcube_11.io.eleCin <= PEcube_7.io.eleCout @[CUBE.scala 32:22]
    PEcube_11.io.vecBin <= PEcube_7.io.vecBout @[CUBE.scala 39:22]
    PEcube_11.io.vecAin <= PEcube_10.io.vecAout @[CUBE.scala 44:22]
    PEcube_12.io.muxCtrl <= io.muxCtrl[12] @[CUBE.scala 28:21]
    PEcube_12.io.eleCin <= PEcube_8.io.eleCout @[CUBE.scala 32:22]
    PEcube_12.io.vecBin <= PEcube_8.io.vecBout @[CUBE.scala 39:22]
    PEcube_13.io.muxCtrl <= io.muxCtrl[13] @[CUBE.scala 28:21]
    PEcube_13.io.eleCin <= PEcube_9.io.eleCout @[CUBE.scala 32:22]
    PEcube_13.io.vecBin <= PEcube_9.io.vecBout @[CUBE.scala 39:22]
    PEcube_13.io.vecAin <= PEcube_12.io.vecAout @[CUBE.scala 44:22]
    PEcube_14.io.muxCtrl <= io.muxCtrl[14] @[CUBE.scala 28:21]
    PEcube_14.io.eleCin <= PEcube_10.io.eleCout @[CUBE.scala 32:22]
    PEcube_14.io.vecBin <= PEcube_10.io.vecBout @[CUBE.scala 39:22]
    PEcube_14.io.vecAin <= PEcube_13.io.vecAout @[CUBE.scala 44:22]
    PEcube_15.io.muxCtrl <= io.muxCtrl[15] @[CUBE.scala 28:21]
    PEcube_15.io.eleCin <= PEcube_11.io.eleCout @[CUBE.scala 32:22]
    PEcube_15.io.vecBin <= PEcube_11.io.vecBout @[CUBE.scala 39:22]
    PEcube_15.io.vecAin <= PEcube_14.io.vecAout @[CUBE.scala 44:22]
    PEcube.io.vecAin <= io.vecA[0] @[CUBE.scala 51:29]
    PEcube_4.io.vecAin <= io.vecA[1] @[CUBE.scala 51:29]
    PEcube_8.io.vecAin <= io.vecA[2] @[CUBE.scala 51:29]
    PEcube_12.io.vecAin <= io.vecA[3] @[CUBE.scala 51:29]
    PEcube.io.vecBin <= io.vecB[0] @[CUBE.scala 55:29]
    PEcube_1.io.vecBin <= io.vecB[1] @[CUBE.scala 55:29]
    PEcube_2.io.vecBin <= io.vecB[2] @[CUBE.scala 55:29]
    PEcube_3.io.vecBin <= io.vecB[3] @[CUBE.scala 55:29]
    io.eleC[0] <= PEcube_12.io.eleCout @[CUBE.scala 60:16]
    io.eleC[1] <= PEcube_13.io.eleCout @[CUBE.scala 60:16]
    io.eleC[2] <= PEcube_14.io.eleCout @[CUBE.scala 60:16]
    io.eleC[3] <= PEcube_15.io.eleCout @[CUBE.scala 60:16]

