# import random

# def generate_test_data(k, num, output_file):
#     # 打开文件准备写入
#     with open(output_file, 'w') as f:
#         for _ in range(num):
#             # 生成vecA和vecB
#             vecA = [random.randint(-128, 127) for _ in range(k)]
#             vecB = [random.randint(-128, 127) for _ in range(k)]
            
#             # 计算点积结果
#             dot_product = sum(vecA[i] * vecB[i] for i in range(k))
            
#             # 将vecA和vecB转换为16进制表示，并拼接为一个长的字符串
#             vecA_hex = ''.join([f"{(x + 256) % 256:02x}" for x in vecA])  # 确保是无符号8位
#             vecB_hex = ''.join([f"{(x + 256) % 256:02x}" for x in vecB])
            
#             # 格式化为所需的输出
#             line = f'"h{vecA_hex}".U  "h{vecB_hex}".U  {dot_product}\n'
#             f.write(line)

# # 调用函数，生成n组测试数据，向量维度为k，输出文件为 "test_data.txt"
# generate_test_data(k=4, num=10, output_file="PEtetsGen.txt")



import random

def generate_test_data(k, num, output_file):
    # 打开文件准备写入
    with open(output_file, 'w') as f:
        # 初始化存储列表
        vecA_list = []
        vecB_list = []
        eleC_list = []

        # 生成 num 组测试数据
        for _ in range(num):
            # 生成 vecA 和 vecB
            vecA = [random.randint(-128, 127) for _ in range(k)]
            vecB = [random.randint(-128, 127) for _ in range(k)]

            # 计算点积结果
            dot_product = sum(vecA[i] * vecB[i] for i in range(k))

            # 将 vecA 和 vecB 转换为 16 进制表示
            vecA_hex = ''.join([f"{(x + 256) % 256:02x}" for x in vecA])  # 确保是无符号8位
            vecB_hex = ''.join([f"{(x + 256) % 256:02x}" for x in vecB])

            # 将数据添加到对应的列表中
            vecA_list.append(f'    "h{vecA_hex}".U')
            vecB_list.append(f'    "h{vecB_hex}".U')
            eleC_list.append(f'    {dot_product}')

        # 写入 vecAdata
        f.write("val vecAdata = Seq(\n")
        f.write(",\n".join(vecA_list) + "\n)\n\n")

        # 写入 vecBdata
        f.write("val vecBdata = Seq(\n")
        f.write(",\n".join(vecB_list) + "\n)\n\n")

        # 写入 eleCdata
        f.write("val eleCdata = Seq(\n")
        f.write(",\n".join(eleC_list) + "\n)\n")

# 调用函数，生成 num 组测试数据，向量维度为 k，输出文件为 "PEtestGen.txt"
generate_test_data(k=8, num=10, output_file="PEtestGen.txt")