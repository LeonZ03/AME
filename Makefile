BUILD_DIR = ./build
TEST_NAME ?= AME.AMETest
SV_NAME ?= PEcube_sv
PYTHON_NAME ?= AMEtestGen.py

init:
	git submodule update --init --recursive --jobs 4

compile:
	mill -i AME.compile | pv -t

testAll:
	mill -i AME.test.test | pv -t

testOnly:
	mill -i AME.test.testOnly ${TEST_NAME} | pv -t

sv:
	mkdir -p $(BUILD_DIR)
	mill -i AME.runMain ${SV_NAME} --target-dir $(BUILD_DIR) | pv -t

pythonGen:
	cd ./python_Gen && python3 ${PYTHON_NAME}


clean:
	rm -rf ./out

install_tools:
	sudo apt update && sudo apt install -y \
		openjdk-17-jdk \
		python3 \
		python3-pip \
		make \
		git \
		curl \
		pv \
		cloc \
		verilator

	# 安装 mill 0.11.1
	curl -L https://github.com/com-lihaoyi/mill/releases/download/0.11.1/0.11.1 > mill && \
	chmod +x mill && \
	sudo mv mill /usr/local/bin/
	@echo "✅ 当前mill版本已为0.11.1"

	# 可选：安装常用 Python 库（用于测试、绘图、数据处理）
	# pip3 install --user -U matplotlib numpy wheel

	@echo "✅ All tools have been installed!"
