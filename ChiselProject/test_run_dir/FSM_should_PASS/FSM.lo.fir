circuit FSM :
  module FSM :
    input clock : Clock
    input reset : UInt<1>
    input io_sigStart : UInt<1>
    output io_muxCtrlC_0_0 : UInt<1>
    output io_muxCtrlC_0_1 : UInt<1>
    output io_muxCtrlC_1_0 : UInt<1>
    output io_muxCtrlC_1_1 : UInt<1>
    output io_muxCtrlC_2_0 : UInt<1>
    output io_muxCtrlC_2_1 : UInt<1>
    output io_muxCtrlC_3_0 : UInt<1>
    output io_muxCtrlC_3_1 : UInt<1>
    output io_muxCtrlC_4_0 : UInt<1>
    output io_muxCtrlC_4_1 : UInt<1>
    output io_muxCtrlC_5_0 : UInt<1>
    output io_muxCtrlC_5_1 : UInt<1>
    output io_muxCtrlC_6_0 : UInt<1>
    output io_muxCtrlC_6_1 : UInt<1>
    output io_muxCtrlC_7_0 : UInt<1>
    output io_muxCtrlC_7_1 : UInt<1>
    output io_muxCtrlSum_0_0 : UInt<1>
    output io_muxCtrlSum_0_1 : UInt<1>
    output io_muxCtrlSum_1_0 : UInt<1>
    output io_muxCtrlSum_1_1 : UInt<1>
    output io_muxCtrlSum_2_0 : UInt<1>
    output io_muxCtrlSum_2_1 : UInt<1>
    output io_muxCtrlSum_3_0 : UInt<1>
    output io_muxCtrlSum_3_1 : UInt<1>
    output io_muxCtrlSum_4_0 : UInt<1>
    output io_muxCtrlSum_4_1 : UInt<1>
    output io_muxCtrlSum_5_0 : UInt<1>
    output io_muxCtrlSum_5_1 : UInt<1>
    output io_muxCtrlSum_6_0 : UInt<1>
    output io_muxCtrlSum_6_1 : UInt<1>
    output io_muxCtrlSum_7_0 : UInt<1>
    output io_muxCtrlSum_7_1 : UInt<1>
    output io_addrReadA_0 : UInt<32>
    output io_addrReadA_1 : UInt<32>
    output io_addrReadA_2 : UInt<32>
    output io_addrReadA_3 : UInt<32>
    output io_addrReadA_4 : UInt<32>
    output io_addrReadA_5 : UInt<32>
    output io_addrReadA_6 : UInt<32>
    output io_addrReadA_7 : UInt<32>
    output io_addrReadB_0 : UInt<32>
    output io_addrReadB_1 : UInt<32>
    output io_addrReadC_0 : UInt<32>
    output io_addrReadC_1 : UInt<32>
    output io_addrWriteC_0 : UInt<32>
    output io_addrWriteC_1 : UInt<32>
    output io_sigDone : UInt<1>

    reg regOffM : UInt<1>, clock with :
      reset => (UInt<1>("h0"), regOffM) @[FSM.scala 30:24]
    reg regOffN : UInt<1>, clock with :
      reset => (UInt<1>("h0"), regOffN) @[FSM.scala 31:24]
    reg regOffK : UInt<4>, clock with :
      reset => (UInt<1>("h0"), regOffK) @[FSM.scala 32:24]
    reg regOffK_1H : UInt<16>, clock with :
      reset => (UInt<1>("h0"), regOffK_1H) @[FSM.scala 33:27]
    node _regOffK_1H_T = eq(io_sigStart, UInt<1>("h1")) @[FSM.scala 35:33]
    node _regOffK_1H_T_1 = bits(regOffK_1H, 14, 0) @[FSM.scala 35:79]
    node _regOffK_1H_T_2 = bits(regOffK_1H, 15, 15) @[FSM.scala 35:109]
    node _regOffK_1H_T_3 = cat(_regOffK_1H_T_1, _regOffK_1H_T_2) @[Cat.scala 31:58]
    node _regOffK_1H_T_4 = mux(_regOffK_1H_T, UInt<16>("h1"), _regOffK_1H_T_3) @[FSM.scala 35:20]
    node _regOffK_T = eq(io_sigStart, UInt<1>("h1")) @[FSM.scala 36:30]
    node _regOffK_T_1 = add(regOffK, UInt<1>("h1")) @[FSM.scala 36:78]
    node _regOffK_T_2 = tail(_regOffK_T_1, 1) @[FSM.scala 36:78]
    node _regOffK_T_3 = mux(_regOffK_T, UInt<4>("h0"), _regOffK_T_2) @[FSM.scala 36:17]
    node _regOffN_T = eq(io_sigStart, UInt<1>("h1")) @[FSM.scala 37:30]
    node _regOffN_T_1 = bits(regOffK_1H, 15, 15) @[FSM.scala 38:31]
    node _regOffN_T_2 = eq(_regOffN_T_1, UInt<1>("h1")) @[FSM.scala 38:46]
    node _regOffN_T_3 = add(regOffN, UInt<1>("h1")) @[FSM.scala 38:63]
    node _regOffN_T_4 = tail(_regOffN_T_3, 1) @[FSM.scala 38:63]
    node _regOffN_T_5 = mux(_regOffN_T_2, _regOffN_T_4, regOffN) @[FSM.scala 38:20]
    node _regOffN_T_6 = mux(_regOffN_T, UInt<1>("h0"), _regOffN_T_5) @[FSM.scala 37:17]
    node _regOffM_T = eq(io_sigStart, UInt<1>("h1")) @[FSM.scala 39:30]
    node _regOffM_T_1 = eq(regOffN, UInt<1>("h1")) @[FSM.scala 40:29]
    node _regOffM_T_2 = bits(regOffK_1H, 15, 15) @[FSM.scala 40:64]
    node _regOffM_T_3 = eq(_regOffM_T_2, UInt<1>("h1")) @[FSM.scala 40:80]
    node _regOffM_T_4 = and(_regOffM_T_1, _regOffM_T_3) @[FSM.scala 40:51]
    node _regOffM_T_5 = add(regOffM, UInt<1>("h1")) @[FSM.scala 40:98]
    node _regOffM_T_6 = tail(_regOffM_T_5, 1) @[FSM.scala 40:98]
    node _regOffM_T_7 = mux(_regOffM_T_4, _regOffM_T_6, regOffM) @[FSM.scala 40:20]
    node _regOffM_T_8 = mux(_regOffM_T, UInt<1>("h0"), _regOffM_T_7) @[FSM.scala 39:17]
    node _io_sigDone_T = eq(regOffM, UInt<1>("h0")) @[FSM.scala 42:29]
    node _io_sigDone_T_1 = eq(regOffN, UInt<1>("h0")) @[FSM.scala 42:48]
    node _io_sigDone_T_2 = and(_io_sigDone_T, _io_sigDone_T_1) @[FSM.scala 42:37]
    node _io_sigDone_T_3 = bits(regOffK_1H, 0, 0) @[FSM.scala 42:69]
    node _io_sigDone_T_4 = eq(_io_sigDone_T_3, UInt<1>("h1")) @[FSM.scala 42:73]
    node _io_sigDone_T_5 = and(_io_sigDone_T_2, _io_sigDone_T_4) @[FSM.scala 42:56]
    node _io_sigDone_T_6 = eq(io_sigStart, UInt<1>("h0")) @[FSM.scala 42:96]
    node _io_sigDone_T_7 = and(_io_sigDone_T_5, _io_sigDone_T_6) @[FSM.scala 42:81]
    node _io_sigDone_T_8 = mux(_io_sigDone_T_7, UInt<1>("h1"), UInt<1>("h0")) @[FSM.scala 42:20]
    node _io_muxCtrlC_0_0_T = bits(regOffK_1H, 0, 0) @[FSM.scala 49:36]
    node _io_muxCtrlSum_0_0_T = bits(regOffK_1H, 0, 0) @[FSM.scala 50:38]
    node _io_muxCtrlC_1_0_T = bits(regOffK_1H, 1, 1) @[FSM.scala 49:36]
    node _io_muxCtrlSum_1_0_T = bits(regOffK_1H, 1, 1) @[FSM.scala 50:38]
    node _io_muxCtrlC_2_0_T = bits(regOffK_1H, 2, 2) @[FSM.scala 49:36]
    node _io_muxCtrlSum_2_0_T = bits(regOffK_1H, 2, 2) @[FSM.scala 50:38]
    node _io_muxCtrlC_3_0_T = bits(regOffK_1H, 3, 3) @[FSM.scala 49:36]
    node _io_muxCtrlSum_3_0_T = bits(regOffK_1H, 3, 3) @[FSM.scala 50:38]
    node _io_muxCtrlC_4_0_T = bits(regOffK_1H, 4, 4) @[FSM.scala 49:36]
    node _io_muxCtrlSum_4_0_T = bits(regOffK_1H, 4, 4) @[FSM.scala 50:38]
    node _io_muxCtrlC_5_0_T = bits(regOffK_1H, 5, 5) @[FSM.scala 49:36]
    node _io_muxCtrlSum_5_0_T = bits(regOffK_1H, 5, 5) @[FSM.scala 50:38]
    node _io_muxCtrlC_6_0_T = bits(regOffK_1H, 6, 6) @[FSM.scala 49:36]
    node _io_muxCtrlSum_6_0_T = bits(regOffK_1H, 6, 6) @[FSM.scala 50:38]
    node _io_muxCtrlC_7_0_T = bits(regOffK_1H, 7, 7) @[FSM.scala 49:36]
    node _io_muxCtrlSum_7_0_T = bits(regOffK_1H, 7, 7) @[FSM.scala 50:38]
    reg regMuxCrtl_0 : UInt<8>, clock with :
      reset => (UInt<1>("h0"), regMuxCrtl_0) @[FSM.scala 54:25]
    node _regMuxCrtl_0_T = bits(regOffK_1H, 7, 0) @[FSM.scala 57:32]
    node _io_muxCtrlC_0_1_T = bits(regMuxCrtl_0, 0, 0) @[FSM.scala 61:45]
    node _io_muxCtrlSum_0_1_T = bits(regMuxCrtl_0, 0, 0) @[FSM.scala 62:47]
    node _io_muxCtrlC_1_1_T = bits(regMuxCrtl_0, 1, 1) @[FSM.scala 61:45]
    node _io_muxCtrlSum_1_1_T = bits(regMuxCrtl_0, 1, 1) @[FSM.scala 62:47]
    node _io_muxCtrlC_2_1_T = bits(regMuxCrtl_0, 2, 2) @[FSM.scala 61:45]
    node _io_muxCtrlSum_2_1_T = bits(regMuxCrtl_0, 2, 2) @[FSM.scala 62:47]
    node _io_muxCtrlC_3_1_T = bits(regMuxCrtl_0, 3, 3) @[FSM.scala 61:45]
    node _io_muxCtrlSum_3_1_T = bits(regMuxCrtl_0, 3, 3) @[FSM.scala 62:47]
    node _io_muxCtrlC_4_1_T = bits(regMuxCrtl_0, 4, 4) @[FSM.scala 61:45]
    node _io_muxCtrlSum_4_1_T = bits(regMuxCrtl_0, 4, 4) @[FSM.scala 62:47]
    node _io_muxCtrlC_5_1_T = bits(regMuxCrtl_0, 5, 5) @[FSM.scala 61:45]
    node _io_muxCtrlSum_5_1_T = bits(regMuxCrtl_0, 5, 5) @[FSM.scala 62:47]
    node _io_muxCtrlC_6_1_T = bits(regMuxCrtl_0, 6, 6) @[FSM.scala 61:45]
    node _io_muxCtrlSum_6_1_T = bits(regMuxCrtl_0, 6, 6) @[FSM.scala 62:47]
    node _io_muxCtrlC_7_1_T = bits(regMuxCrtl_0, 7, 7) @[FSM.scala 61:45]
    node _io_muxCtrlSum_7_1_T = bits(regMuxCrtl_0, 7, 7) @[FSM.scala 62:47]
    node _wireAddrReadA_T = mul(regOffM, UInt<5>("h10")) @[FSM.scala 71:28]
    node _wireAddrReadA_T_1 = add(_wireAddrReadA_T, regOffK) @[FSM.scala 71:42]
    node _wireAddrReadA_T_2 = tail(_wireAddrReadA_T_1, 1) @[FSM.scala 71:42]
    reg regDelayA_0 : UInt<32>, clock with :
      reset => (UInt<1>("h0"), regDelayA_0) @[FSM.scala 73:22]
    reg regDelayA_1 : UInt<32>, clock with :
      reset => (UInt<1>("h0"), regDelayA_1) @[FSM.scala 73:22]
    reg regDelayA_2 : UInt<32>, clock with :
      reset => (UInt<1>("h0"), regDelayA_2) @[FSM.scala 73:22]
    reg regDelayA_3 : UInt<32>, clock with :
      reset => (UInt<1>("h0"), regDelayA_3) @[FSM.scala 73:22]
    reg regDelayA_4 : UInt<32>, clock with :
      reset => (UInt<1>("h0"), regDelayA_4) @[FSM.scala 73:22]
    reg regDelayA_5 : UInt<32>, clock with :
      reset => (UInt<1>("h0"), regDelayA_5) @[FSM.scala 73:22]
    reg regDelayA_6 : UInt<32>, clock with :
      reset => (UInt<1>("h0"), regDelayA_6) @[FSM.scala 73:22]
    node _wireAddrReadB_T = mul(regOffN, UInt<5>("h10")) @[FSM.scala 89:28]
    node _wireAddrReadB_T_1 = add(_wireAddrReadB_T, regOffK) @[FSM.scala 89:42]
    node _wireAddrReadB_T_2 = tail(_wireAddrReadB_T_1, 1) @[FSM.scala 89:42]
    reg regDelayB_0 : UInt<32>, clock with :
      reset => (UInt<1>("h0"), regDelayB_0) @[FSM.scala 94:24]
    node wireAddrReadA = pad(_wireAddrReadA_T_2, 32) @[FSM.scala 70:27 71:17]
    node wireAddrReadB = pad(_wireAddrReadB_T_2, 32) @[FSM.scala 88:27 89:17]
    io_muxCtrlC_0_0 <= _io_muxCtrlC_0_0_T @[FSM.scala 49:23]
    io_muxCtrlC_0_1 <= _io_muxCtrlC_0_1_T @[FSM.scala 61:27]
    io_muxCtrlC_1_0 <= _io_muxCtrlC_1_0_T @[FSM.scala 49:23]
    io_muxCtrlC_1_1 <= _io_muxCtrlC_1_1_T @[FSM.scala 61:27]
    io_muxCtrlC_2_0 <= _io_muxCtrlC_2_0_T @[FSM.scala 49:23]
    io_muxCtrlC_2_1 <= _io_muxCtrlC_2_1_T @[FSM.scala 61:27]
    io_muxCtrlC_3_0 <= _io_muxCtrlC_3_0_T @[FSM.scala 49:23]
    io_muxCtrlC_3_1 <= _io_muxCtrlC_3_1_T @[FSM.scala 61:27]
    io_muxCtrlC_4_0 <= _io_muxCtrlC_4_0_T @[FSM.scala 49:23]
    io_muxCtrlC_4_1 <= _io_muxCtrlC_4_1_T @[FSM.scala 61:27]
    io_muxCtrlC_5_0 <= _io_muxCtrlC_5_0_T @[FSM.scala 49:23]
    io_muxCtrlC_5_1 <= _io_muxCtrlC_5_1_T @[FSM.scala 61:27]
    io_muxCtrlC_6_0 <= _io_muxCtrlC_6_0_T @[FSM.scala 49:23]
    io_muxCtrlC_6_1 <= _io_muxCtrlC_6_1_T @[FSM.scala 61:27]
    io_muxCtrlC_7_0 <= _io_muxCtrlC_7_0_T @[FSM.scala 49:23]
    io_muxCtrlC_7_1 <= _io_muxCtrlC_7_1_T @[FSM.scala 61:27]
    io_muxCtrlSum_0_0 <= _io_muxCtrlSum_0_0_T @[FSM.scala 50:25]
    io_muxCtrlSum_0_1 <= _io_muxCtrlSum_0_1_T @[FSM.scala 62:29]
    io_muxCtrlSum_1_0 <= _io_muxCtrlSum_1_0_T @[FSM.scala 50:25]
    io_muxCtrlSum_1_1 <= _io_muxCtrlSum_1_1_T @[FSM.scala 62:29]
    io_muxCtrlSum_2_0 <= _io_muxCtrlSum_2_0_T @[FSM.scala 50:25]
    io_muxCtrlSum_2_1 <= _io_muxCtrlSum_2_1_T @[FSM.scala 62:29]
    io_muxCtrlSum_3_0 <= _io_muxCtrlSum_3_0_T @[FSM.scala 50:25]
    io_muxCtrlSum_3_1 <= _io_muxCtrlSum_3_1_T @[FSM.scala 62:29]
    io_muxCtrlSum_4_0 <= _io_muxCtrlSum_4_0_T @[FSM.scala 50:25]
    io_muxCtrlSum_4_1 <= _io_muxCtrlSum_4_1_T @[FSM.scala 62:29]
    io_muxCtrlSum_5_0 <= _io_muxCtrlSum_5_0_T @[FSM.scala 50:25]
    io_muxCtrlSum_5_1 <= _io_muxCtrlSum_5_1_T @[FSM.scala 62:29]
    io_muxCtrlSum_6_0 <= _io_muxCtrlSum_6_0_T @[FSM.scala 50:25]
    io_muxCtrlSum_6_1 <= _io_muxCtrlSum_6_1_T @[FSM.scala 62:29]
    io_muxCtrlSum_7_0 <= _io_muxCtrlSum_7_0_T @[FSM.scala 50:25]
    io_muxCtrlSum_7_1 <= _io_muxCtrlSum_7_1_T @[FSM.scala 62:29]
    io_addrReadA_0 <= wireAddrReadA @[FSM.scala 81:19]
    io_addrReadA_1 <= regDelayA_0 @[FSM.scala 83:21]
    io_addrReadA_2 <= regDelayA_1 @[FSM.scala 83:21]
    io_addrReadA_3 <= regDelayA_2 @[FSM.scala 83:21]
    io_addrReadA_4 <= regDelayA_3 @[FSM.scala 83:21]
    io_addrReadA_5 <= regDelayA_4 @[FSM.scala 83:21]
    io_addrReadA_6 <= regDelayA_5 @[FSM.scala 83:21]
    io_addrReadA_7 <= regDelayA_6 @[FSM.scala 83:21]
    io_addrReadB_0 <= wireAddrReadB @[FSM.scala 91:19]
    io_addrReadB_1 <= regDelayB_0 @[FSM.scala 103:23]
    io_addrReadC_0 <= UInt<32>("h0") @[FSM.scala 27:21]
    io_addrReadC_1 <= UInt<32>("h0") @[FSM.scala 27:21]
    io_addrWriteC_0 <= UInt<32>("h0") @[FSM.scala 26:22]
    io_addrWriteC_1 <= UInt<32>("h0") @[FSM.scala 26:22]
    io_sigDone <= _io_sigDone_T_8 @[FSM.scala 42:14]
    regOffM <= mux(reset, UInt<1>("h0"), _regOffM_T_8) @[FSM.scala 30:{24,24} 39:11]
    regOffN <= mux(reset, UInt<1>("h0"), _regOffN_T_6) @[FSM.scala 31:{24,24} 37:11]
    regOffK <= mux(reset, UInt<4>("h0"), _regOffK_T_3) @[FSM.scala 32:{24,24} 36:11]
    regOffK_1H <= mux(reset, UInt<16>("h1"), _regOffK_1H_T_4) @[FSM.scala 33:{27,27} 35:14]
    regMuxCrtl_0 <= _regMuxCrtl_0_T @[FSM.scala 57:19]
    regDelayA_0 <= wireAddrReadA @[FSM.scala 76:16]
    regDelayA_1 <= regDelayA_0 @[FSM.scala 78:18]
    regDelayA_2 <= regDelayA_1 @[FSM.scala 78:18]
    regDelayA_3 <= regDelayA_2 @[FSM.scala 78:18]
    regDelayA_4 <= regDelayA_3 @[FSM.scala 78:18]
    regDelayA_5 <= regDelayA_4 @[FSM.scala 78:18]
    regDelayA_6 <= regDelayA_5 @[FSM.scala 78:18]
    regDelayB_0 <= wireAddrReadB @[FSM.scala 97:18]
