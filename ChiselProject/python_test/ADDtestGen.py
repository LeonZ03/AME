
import random
import struct

def generate_test_data(n):
    # 生成随机 eleCin 数组，大小为 n，每个元素为 32 位有符号整数
    eleCin = [random.randint(-2**31, 2**31 - 1) for _ in range(n)]
    
    # 生成随机 vecCin 数组，大小为 n/4，每个元素为 4 个 32 位有符号整数拼接成的 128 位无符号整数
    vecCin = []
    for i in range(n // 4):
        # 生成 4 个 32 位有符号整数
        eleC0 = random.randint(-2**31, 2**31 - 1)
        eleC1 = random.randint(-2**31, 2**31 - 1)
        eleC2 = random.randint(-2**31, 2**31 - 1)
        eleC3 = random.randint(-2**31, 2**31 - 1)
        
        # 将 4 个 32 位整数拼接成一个 128 位无符号整数
        vecCin_value = (eleC0 & 0xFFFFFFFF) << 96 | (eleC1 & 0xFFFFFFFF) << 64 | (eleC2 & 0xFFFFFFFF) << 32 | (eleC3 & 0xFFFFFFFF)
        vecCin.append(vecCin_value)
    
    # 计算 vecCout，大小为 n/4
    vecCout = []
    for i in range(n // 4):
        # 从 vecCin 中提取 4 个 32 位有符号整数
        eleC0 = (vecCin[i] >> 96) & 0xFFFFFFFF
        eleC1 = (vecCin[i] >> 64) & 0xFFFFFFFF
        eleC2 = (vecCin[i] >> 32) & 0xFFFFFFFF
        eleC3 = vecCin[i] & 0xFFFFFFFF
        
        # 将提取的整数转换为有符号整数
        eleC0 = eleC0 if eleC0 < 2**31 else eleC0 - 2**32
        eleC1 = eleC1 if eleC1 < 2**31 else eleC1 - 2**32
        eleC2 = eleC2 if eleC2 < 2**31 else eleC2 - 2**32
        eleC3 = eleC3 if eleC3 < 2**31 else eleC3 - 2**32
        
        # 与 eleCin 对应位置的元素相加
        sum0 = eleCin[i * 4] + eleC0
        sum1 = eleCin[i * 4 + 1] + eleC1
        sum2 = eleCin[i * 4 + 2] + eleC2
        sum3 = eleCin[i * 4 + 3] + eleC3
        
        # 将结果拼接成一个 128 位有符号整数
        vecCout_value = (sum0 & 0xFFFFFFFF) << 96 | (sum1 & 0xFFFFFFFF) << 64 | (sum2 & 0xFFFFFFFF) << 32 | (sum3 & 0xFFFFFFFF)
        vecCout.append(vecCout_value)
    
    return eleCin, vecCin, vecCout

def write_to_file(eleCin, vecCin, vecCout, filename="ADDtestGen.txt"):
    with open(filename, "w") as f:
        # 写入 eleCin
        f.write("val eleCindata = Seq(\n")
        for value in eleCin:
            f.write(f"    {value}.S,\n")
        f.write(")\n\n")
        
        # 写入 vecCin
        f.write("val vecCindata = Seq(\n")
        for value in vecCin:
            f.write(f"    \"h{value:032x}\".U,\n")
        f.write(")\n\n")
        
        # 写入 vecCout
        f.write("val vecCoutdata = Seq(\n")
        for value in vecCout:
            f.write(f"    \"h{value:032x}\".U,\n")
        f.write(")\n")

if __name__ == "__main__":
    # 配置参数 n
    n = 4  # 可以修改为其他值，但必须是 4 的倍数
    
    # 生成测试数据
    eleCin, vecCin, vecCout = generate_test_data(n)
    
    # 将测试数据写入文件
    write_to_file(eleCin, vecCin, vecCout)
