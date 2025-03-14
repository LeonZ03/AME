# 首次需要修改DUT name
DUT ?= MMAU

SBT = sbt
TEST = testOnly SimpleTestExpect
WAVE_TEST = testOnly SimpleTestExpect -- -DwriteVcd=1
WAVE_FILE = ./test_run_dir/DUT_should_pass/$(DUT).vcd
VERILOG_DIR = ./generated
BUILD_DIR := ./build


# 目标：默认运行sbt run,生成verilog文件并复制到指定目录
.PHONY: all
all:
	$(SBT) run

# 创建目标目录（如果不存在）
	mkdir -p $(BUILD_DIR)
    
# 搬运 .json 文件
	mv *.json $(BUILD_DIR)
    
# 搬运 .fir 文件
	mv *.fir $(BUILD_DIR)
    
# 搬运 .v 文件
	mv *.v $(BUILD_DIR)
    
	@echo "Files moved to $(BUILD_DIR)"

# 目标：运行sbt "testOnly SimpleTestExpect -- -DwriteVcd=1"，生成波形
.PHONY: test
test:
	$(SBT) "$(WAVE_TEST)"

# 目标：运行sbt "testOnly SimpleTestExpect"，不生成波形
.PHONY: test_no_wave
test_no_wave:
	$(SBT) "$(TEST)"

# 目标：打开波形文件
.PHONY: open-wave
open-wave:
	@echo "Opening waveform file: $(WAVE_FILE)"
	open $(WAVE_FILE) || (echo "Waveform file not found, make sure it exists.")

# 目标：清理生成文件
.PHONY: clean
clean:
	$(SBT) clean
	rm -rf ./test_run_dir




