import random

def generate_test_data(k, m, n, output_file):
    # 打开文件准备写入
    with open(output_file, 'w') as f:
        # 生成 vecA
        f.write("val vecAdata = Seq(\n")
        vecA_list = []
        vecA_values = []  # 存储 vecA 的实际值（用于计算点积）
        for _ in range(m):
            vecA = [random.randint(-128, 127) for _ in range(k)]  # 生成 k 个 SInt8
            vecA_hex = ''.join([f"{(x + 256) % 256:02x}" for x in vecA])  # 转换为 16 进制
            vecA_list.append(f'    "h{vecA_hex}".U')
            vecA_values.append(vecA)  # 存储实际值
        f.write(",\n".join(vecA_list) + "\n)\n\n")

        # 生成 vecB
        f.write("val vecBdata = Seq(\n")
        vecB_list = []
        vecB_values = []  # 存储 vecB 的实际值（用于计算点积）
        for _ in range(n):
            vecB = [random.randint(-128, 127) for _ in range(k)]  # 生成 k 个 SInt8
            vecB_hex = ''.join([f"{(x + 256) % 256:02x}" for x in vecB])  # 转换为 16 进制
            vecB_list.append(f'    "h{vecB_hex}".U')
            vecB_values.append(vecB)  # 存储实际值
        f.write(",\n".join(vecB_list) + "\n)\n\n")

        # 生成 eleC（二维数组）
        f.write("val eleCdata = Seq(\n")
        eleC_list = []
        for i in range(m):
            row = []
            for j in range(n):
                # 使用 vecA_values[i] 和 vecB_values[j] 计算点积
                dot_product = sum(vecA_values[i][idx] * vecB_values[j][idx] for idx in range(k))
                row.append(str(dot_product))
            eleC_list.append("    Seq(" + ", ".join(row) + ")")
        f.write(",\n".join(eleC_list) + "\n)\n")

# 调用函数，生成测试数据
generate_test_data(k=4, m=4, n=4, output_file="CUBEtestGen.txt")