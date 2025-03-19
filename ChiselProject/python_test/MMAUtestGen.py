import numpy as np

# 定义参数
WORD_LEN = 64
ADDR_LEN = 32
tileM, tileN, tileK = 16, 16, 64
m, n, k = 8, 8, 4
numM, numN, numK = tileM // m, tileN // n, tileK // k

# 生成随机数据
A0 = np.random.randint(-128, 127, (tileM, tileK), dtype=np.int8)
B0 = np.random.randint(-128, 127, (tileK, tileN), dtype=np.int8)
Ctmp0 = np.random.randint(-2147483648, 2147483647, (tileM, tileN), dtype=np.int32)
C0 = np.dot(A0.astype(np.int32), B0.astype(np.int32)) + Ctmp0  # 计算 C0 矩阵

# 数据格式转换函数
def format_matrix(mat, row_group, col_group):
    formatted = []
    for i in range(row_group):
        row_data = []
        for j in range(col_group):
            elements = mat[i::row_group, j::col_group].flatten()
            packed_value = sum((e.astype(np.uint64) & 0xFF) << (8 * (k - 1 - idx))
                               for idx, e in enumerate(elements))
            row_data.append(f'"h{packed_value:016x}".U')
        formatted.append(row_data)
    return formatted

# 转换矩阵格式
A = format_matrix(A0, m, numK * numM)
B = format_matrix(B0.T, n, numK * numN)  # B 需转置
Ctmp = format_matrix(Ctmp0, n // 4, m * numM * numN)
C = format_matrix(C0, n // 4, m * numM * numN)

# 输出到文件
with open("MMAUtestGen.txt", "w") as f:
    for name, matrix in [("A", A), ("B", B), ("Ctmp", Ctmp), ("C", C)]:
        f.write(f"val {name} = Seq(\n")
        for row in matrix:
            f.write("    " + ",\n    ".join(row) + ",\n")
        f.write(")\n\n")
