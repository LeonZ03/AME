import numpy as np

class MatrixGenerator:
    def __init__(self):
        # 参数初始化
        self.tileM = 8
        self.tileN = 8
        self.tileK = 16
        
        self.m = 4
        self.n = 4
        self.k = 4
        
        self.numM = self.tileM // self.m
        self.numN = self.tileN // self.n
        self.numK = self.tileK // self.k
        
        # 生成原始矩阵
        self.A0 = np.random.randint(-128, 127, (self.tileM, self.tileK), dtype=np.int8)
        self.B0 = np.random.randint(-128, 127, (self.tileK, self.tileN), dtype=np.int8)
        self.Ctmp0 = np.random.randint(-2**31, 2**31-1, (self.tileM, self.tileN), dtype=np.int32)
        self.C0 = np.dot(self.A0.astype(np.int64), self.B0.astype(np.int64)).astype(np.int32) + self.Ctmp0
        
        # 转换后的二维矩阵
        self.A = self._convert_A()
        self.B = self._convert_B()
        self.Ctmp = self._convert_C(self.Ctmp0)
        self.C = self._convert_C(self.C0)

    def _convert_A(self):
        """转换A矩阵为二维结构"""
        A = [[0]*(self.numK*self.numM) for _ in range(self.m)]
        for i in range(self.m):
            for tile_row in range(self.numM):
                for k_block in range(self.numK):
                    elements = []
                    for x in range(tile_row*self.m + i, (tile_row+1)*self.m, self.m):
                        elements.extend(self.A0[x, k_block*self.k : (k_block+1)*self.k])
                    # 大端拼接
                    val = 0
                    for elem in reversed(elements):
                        val = (val << 8) | (elem & 0xFF)
                    A[i][tile_row*self.numK + k_block] = val
        return A

    def _convert_B(self):
        """转换B矩阵为二维结构"""
        B = [[0]*(self.numK*self.numN) for _ in range(self.n)]
        for i in range(self.n):
            for tile_col in range(self.numN):
                for k_block in range(self.numK):
                    elements = []
                    for y in range(tile_col*self.n + i, (tile_col+1)*self.n, self.n):
                        elements.extend(self.B0[k_block*self.k : (k_block+1)*self.k, y])
                    # 大端拼接
                    val = 0
                    for elem in reversed(elements):
                        val = (val << 8) | (elem & 0xFF)
                    B[i][tile_col*self.numK + k_block] = val
        return B

    def _convert_C(self, src_matrix):
        """转换C矩阵为二维结构（修复符号扩展问题）"""
        C = []
        for i in range(self.n // 4):
            row = []
            for step in range(self.numM * self.numN):
                for col_block in range(self.m):
                    elements = []
                    x_start = (step // self.numN) * self.m
                    y_start = (step % self.numN) * self.n + i * 4
                    
                    # 提取4个SInt32元素
                    for x in range(x_start, x_start + self.m):
                        elements.extend(src_matrix[x][y_start:y_start+4])
                    
                    # 关键改进：正确处理符号扩展
                    val = 0
                    for elem in reversed(elements):
                        # 将SInt32转换为32位无符号表示
                        uint32_val = elem & 0xFFFFFFFF
                        # 扩展为128位（自动符号扩展）
                        val = (val << 32) | uint32_val
                    row.append(val)
            C.append(row)
        return C

    def save_to_file(self):
        """保存为Chisel格式"""
        with open("MMAUtestGen.txt", "w") as f:
            # 写入A矩阵 (k=4, 32位, 8字符)
            f.write("val A = Seq(\n")
            for row in self.A:
                f.write("  Seq(\n")
                f.write("    " + ",\n    ".join(f"\"h{v:08x}\".U" for v in row) + "\n")
                f.write("  ),\n")
            f.write(")\n\n")

            # 写入B矩阵 (k=4, 32位, 8字符)
            f.write("val B = Seq(\n")
            for row in self.B:
                f.write("  Seq(\n")
                f.write("    " + ",\n    ".join(f"\"h{v:08x}\".U" for v in row) + "\n")
                f.write("  ),\n")
            f.write(")\n\n")

            # 写入Ctmp矩阵（示例）
            f.write("val Ctmp = Seq(\n")
            for row in self.Ctmp:
                f.write("  Seq(\n")
                for elem in row:
                    # 关键改进：强制转换为无符号128位十六进制
                    hex_str = f"h{elem & 0xFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF:032x}".upper()
                    f.write(f"    \"{hex_str}\".U,\n")
                f.write("  ),\n")
            f.write(")\n\n")

            # 写入C矩阵 (4个SInt32拼接, 128位, 32字符)
            f.write("val C = Seq(\n")
            for row in self.C:
                f.write("  Seq(\n")
                f.write("    " + ",\n    ".join(f"\"h{v:032x}\".U" for v in row) + "\n")
                f.write("  ),\n")
            f.write(")\n\n")


if __name__ == "__main__":
    gen = MatrixGenerator()
    gen.save_to_file()