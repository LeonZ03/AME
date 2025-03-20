import numpy as np
import os

class MatrixGenerator:
    def __init__(self):
        # 参数初始化
        self.tileM = 8
        self.tileN = 16
        self.tileK = 32
        
        self.m = 4
        self.n = 8
        self.k = 4
        
        self.numM = self.tileM // self.m
        self.numN = self.tileN // self.n
        self.numK = self.tileK // self.k
        
        # 生成原始矩阵
        self.A0 = np.random.randint(-128, 127, (self.tileM, self.tileK), dtype=np.int8)
        self.B0 = np.random.randint(-128, 127, (self.tileK, self.tileN), dtype=np.int8)
        self.Ctmp0 = np.random.randint(-2**31, 2**31-1, (self.tileM, self.tileN), dtype=np.int32)
        self.C0 = np.dot(self.A0.astype(np.int32), self.B0.astype(np.int32)).astype(np.int32)
        
        # 转换 A0 为 A
        self.A = self._convert_A()
        # 转换 B0 为 B
        self.B = self._convert_B()
        # 转换 Ctmp0 为 Ctmp
        self.Ctmp = self._convert_C(self.Ctmp0)
        # 转换 C0 为 C
        self.C = self._convert_C(self.C0)

    def _convert_A(self):
        """将 A0 转换为 A"""
        A = [[0] * (self.numK * self.numM) for _ in range(self.m)]
        for i in range(self.numM):
            for j in range(self.m):
                for p in range(self.numK):
                    # 拼接 4 个 8 位数为一个 32 位数
                    val = 0
                    for q in range(self.k):
                        val = (val << 8) | (self.A0[self.m * i + j][p * self.k + q] & 0xFF)
                    A[j][i * self.numK + p] = val
        return A

    def _convert_B(self):
        """将 B0 转换为 B"""
        B = [[0] * (self.numK * self.numN) for _ in range(self.n)]
        for i in range(self.numN):
            for j in range(self.n):
                for p in range(self.numK):
                    # 拼接 4 个 8 位数为一个 32 位数
                    val = 0
                    for q in range(self.k):
                        val = (val << 8) | (self.B0[p * self.k + q][self.n * i + j] & 0xFF)
                    B[j][i * self.numK + p] = val
        return B

    def _convert_C(self, src_matrix):
        """将 C0 或 Ctmp0 转换为 C 或 Ctmp（拼接为字符串）"""
        C = [[0] * (self.numM * self.numN * self.m) for _ in range(self.n // 4)]
        for i in range(self.numM):
            for j in range(self.numN):
                for p in range(self.n // 4):
                    for q in range(self.m):
                        # 拼接 4 个 32 位数为一个 128 位字符串
                        hex_str = ""
                        for r in range(4):
                            # 将每个 32 位数转换为 8 位十六进制字符串
                            elem = src_matrix[i * self.m + q][j * self.n + p * 4 + r]
                            hex_str += f"{elem & 0xFFFFFFFF:08x}"  # 无符号 32 位
                        C[p][i * self.numN * self.m + j * self.m + q] = hex_str
        return C

    def save_to_file(self):
        """保存为完整的 Scala 文件"""
        output_dir = "../src/main/scala/common"
        os.makedirs(output_dir, exist_ok=True)  # 创建目录（如果不存在）
        output_file = os.path.join(output_dir, "TestData.scala")

        with open(output_file, "w") as f:
            # 写入 Scala 文件头部
            f.write("package common\n\n")
            f.write("import chisel3._\n\n")
            f.write("object TestData {\n")

            # 写入 A 矩阵
            f.write("  val A = Seq(\n")
            for row in self.A:
                f.write("    Seq(\n")
                f.write("      " + ",\n      ".join(f"\"h{v:08x}\".U" for v in row) + "\n")
                f.write("    ),\n")
            f.write("  )\n\n")

            # 写入 B 矩阵
            f.write("  val B = Seq(\n")
            for row in self.B:
                f.write("    Seq(\n")
                f.write("      " + ",\n      ".join(f"\"h{v:08x}\".U" for v in row) + "\n")
                f.write("    ),\n")
            f.write("  )\n\n")

            # 写入 Ctmp 矩阵
            f.write("  val Ctmp = Seq(\n")
            for row in self.Ctmp:
                f.write("    Seq(\n")
                f.write("      " + ",\n      ".join(f"\"h{v}\".U" for v in row) + "\n")
                f.write("    ),\n")
            f.write("  )\n\n")

            # 写入 C 矩阵
            f.write("  val C = Seq(\n")
            for row in self.C:
                f.write("    Seq(\n")
                f.write("      " + ",\n      ".join(f"\"h{v}\".U" for v in row) + "\n")
                f.write("    ),\n")
            f.write("  )\n\n")

            # 写入 Scala 文件尾部
            f.write("}\n")

if __name__ == "__main__":
    gen = MatrixGenerator()
    gen.save_to_file()