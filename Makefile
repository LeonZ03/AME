BUILD_DIR = ./build
TEST_NAME ?= MMAU.MMAUTestExpect
SV_NAME ?= PEcube_sv
PYTHON_NAME ?= MMAUtestGen.py

init:
	git submodule update --init --recursive --jobs 4

compile:
	mill -i AME.compile

testAll:
	mill -i AME.test.test

testOnly:
	mill -i AME.test.testOnly ${TEST_NAME}

sv:
	mkdir -p $(BUILD_DIR)
	mill -i AME.runMain ${SV_NAME} --target-dir $(BUILD_DIR)

pythonGen:
	cd ./python_Gen && python3 ${PYTHON_NAME}


clean:
	rm -rf ./out
