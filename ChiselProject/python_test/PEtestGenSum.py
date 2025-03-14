#考虑累加,模拟1个完整Set
import random

def generate_test_data(k, num, output_file):
    # 打开文件准备写入
    with open(output_file, 'w') as f:
        # 初始化存储列表
        vecA_list = []
        vecB_list = []
        eleC_list = []

        # 生成 num 组测试数据
        for i in range(num):
            # 生成 vecA 和 vecB
            vecA = [random.randint(-128, 127) for _ in range(k)]
            vecB = [random.randint(-128, 127) for _ in range(k)]

            # 计算点积结果
            dot_product = sum(vecA[j] * vecB[j] for j in range(k))

            # 将 vecA 和 vecB 转换为 16 进制表示
            vecA_hex = ''.join([f"{(x + 256) % 256:02x}" for x in vecA])  # 确保是无符号8位
            vecB_hex = ''.join([f"{(x + 256) % 256:02x}" for x in vecB])

            # 将数据添加到对应的列表中
            vecA_list.append(f'    "h{vecA_hex}".U')
            vecB_list.append(f'    "h{vecB_hex}".U')

            # 计算新的 eleC(i) 为前 i 项之和
            if i == 0:
                eleC_list.append(f'    {dot_product}')
            else:
                eleC_list.append(f'    {eleC_list[i-1]} + {dot_product}')

        # 写入 vecAdata
        f.write("val vecAdata = Seq(\n")
        f.write(",\n".join(vecA_list) + "\n)\n\n")

        # 写入 vecBdata
        f.write("val vecBdata = Seq(\n")
        f.write(",\n".join(vecB_list) + "\n)\n\n")

        # 写入 eleCdata
        f.write("val eleCdata = Seq(\n")
        f.write(",\n".join(eleC_list) + "\n)\n")


# 调用函数，Set中分块矩阵数量为num，向量维度为 k，输出文件为 "PEtestGenSum.txt"
generate_test_data(k=8, num=10, output_file="PEtestGenSum.txt")