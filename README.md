# AME

This project is used to accelerate matrix operations , using "riscv_matrix_extension"

**Currently supported instructions are as follows:**

```
mlae8
mlbe8
mlce32
msce32
mmacc.w.b
```

## 开发环境说明

本项目基于 Ubuntu 系统开发与测试，推荐使用 Ubuntu 22.04


| 工具      | 推荐版本         |
| --------- | ---------------- |
| Java      | OpenJDK 17       |
| Mill      | 0.11.1           |
| verilator | 默认即可         |
| Python    | ≥ 3.10          |
| pip (可选)  | ≥ 22.0          |
| git/make  | 默认即可         |
| pv        | 用于终端可视化   |
| cloc      | 用于统计代码行数 |

可使用make命令 `make install_tools` 一键安装所有工具

在配置开发环境过程中，可能需要访问github，建议使用vpn全局代理，并通过ssh密钥访问

## Quick start

```
make init  #初次clone需更新git子模块
make compile  #编译工程（可选，可用于验证是否成功配置开发环境）
```

## Generate SystemVerilog

```
make sv SV_NAME=xxx_sv  #specific target , eg: SV_NAME=PEcube_sv
```

## ChiselTest

```
make testAll  #run all tests (一般不使用)
make testOnly TEST_NAME=packageName.testName  #specific target , eg: TEST_NAME=AME.AMETest
```

## Generate ramdom testdata by Python

```
make pythonGen PYTHON_NAME=xxx.py  #specific target , eg: PYTHON_NAME=MMAUtestGen.py
```

## Clean mill cache

```
make clean
```
