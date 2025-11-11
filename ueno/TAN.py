import pandas as pd
import numpy as np
from collections import deque
import math


class TAN:
    NC: int = 2  # カテゴリ数
    NV: int = 0  # 変数の数
    LD = []  # 学習データ
    TD = []  # テストデータ
    # 頻度表
    ft_c = []
    ft_ic = []
    ft_ijc = (
        []
    )  # N_{X_i=k,X_j=m,X_0=c}の格納先．例えば，ft_ijc[1][0][0]=N_{X_i=1,X_j=0,X_0=0}, ft_ijc[0][1][0]=N_{X_i=0,X_j=1,X_0=0}となる(X_0は目的変数を表す)．
    # 目的変数の確率 P(X_0=c)
    p_x0 = []
    # 同時確率 (X_i=k, X_j=m, X_0=c)
    p_joint_ijc = []
    # 条件付き同時確率 (X_i=k, X_j=m | X_0=c)
    p_cond_ij_given_c = []
    # 条件付き確率 (X_i=k | X_0=c)
    p_cond_i_given_c = []
    # 条件付き相互情報量 cmi[i][j]=I(X_i,X_j|X_0)
    cmi = []

    def __init__(self):
        self.readData("data/sentiment/")
        self.setFrequencyTable()
        self.setProb()
        self.cmi = self.getConditionalProbability()
        self.mst_edges = self.getMaximumSpanningTree(self.cmi)
        self.parents = self.assign_parent()
        print(f"route node: {self.route_node}")
        print("TAN model constructed.")
        (
            self.theta_x0,
            self.theta_root_given_class,
            self.theta_feature_given_parent_and_class,
        ) = self._get_parameters()
        self.classification()

    def assign_parent(self):
        F = self.NV - 1  # 特徴の本数
        adj = [[] for _ in range(F)]
        for u, v in self.mst_edges:
            adj[u].append(v)
            adj[v].append(u)
        root = 0  # 例: 最初の辺の一端を根にする
        self.route_node = root
        parents = [None for _ in range(F)]
        visited = [False] * F
        visited[root] = True
        q = deque([root])
        while q:
            u = q.popleft()
            for v in adj[u]:
                if not visited[v]:
                    visited[v] = True
                    # TAN: v の親は (u, クラス
                    parents[v] = u
                    q.append(v)
        return parents

    def readData(self, data_path):
        self.LD = pd.read_csv(data_path + "LD.csv").values.tolist()
        self.TD = pd.read_csv(data_path + "TD.csv").values.tolist()

        self.LD_pd = pd.read_csv(data_path + "LD.csv")
        self.TD_pd = pd.read_csv(data_path + "TD.csv")
        self.NV = len(self.LD[0])
        print(f"shape of LD: {len(self.LD)} x {self.NV}")
        print(f"shape of TD: {len(self.TD)} x {self.NV}")

    def setFrequencyTable(self):
        """
        N_{Y=c}, N_{Xi=k, Y=c}, N_{Xi=k, Xj=m, Y=c} を計算
        ※ 最後の列は目的変数。特徴は 0..(NV-2)
        ※ Xi, Xj は2値 {0,1} を仮定（データが0/1ならOK）
        """
        F = self.NV - 1  # 特徴の本数
        K = 2  # 特徴の取り得る値（0/1）
        C = self.NC

        # 初期化
        self.ft_c = [0 for _ in range(C)]  # N_{Y=c}
        self.ft_ic = [
            [[0 for _ in range(C)] for _ in range(K)] for _ in range(F)
        ]  # [i][k][c]
        self.ft_ijc = [
            [
                [[[0 for _ in range(C)] for _ in range(K)] for _ in range(K)]
                for _ in range(F)
            ]
            for __ in range(F)
        ]  # [i][j][k][m][c]

        for x in self.LD:
            y = x[self.NV - 1]  # 目的変数
            self.ft_c[y] += 1

            # 単体
            for i in range(F):
                xi = x[i]  # 0 or 1
                self.ft_ic[i][xi][y] += 1

            # ペア
            for i in range(F):
                xi = x[i]
                for j in range(i, F):  # ← i<=j にする（自己ペア含む）
                    xj = x[j]
                    if i == j:
                        # (k,m)=(xi,xi) だけ1カウント、(0,1)や(1,0)は0のまま
                        self.ft_ijc[i][i][xi][xi][y] += 1
                    else:
                        # 片側だけだと [j][i] を検証すると0になるので、対称に入れておく
                        self.ft_ijc[i][j][xi][xj][y] += 1
                        self.ft_ijc[j][i][xj][xi][y] += 1

    def setProb(self):
        """
        ft_c, ft_ic, ft_ijcを用いて条件付き相互情報量を求めるのに必要な確率を計算
        self.p_joint_ijc = 同時確率 (X_i=k, X_j=m, X_0=c)
        self.p_cond_ij_given_c = 条件付き同時確率 (X_i=k, X_j=m | X_0=c)
        self.p_cond_i_given_c = 条件付き確率 (X_i=k | X_0=c)
        """
        F = self.NV - 1  # 特徴の本数
        C = self.NC
        # 目的変数の確率 P(X_0=c)
        self.p_x0 = [self.ft_c[c] / len(self.LD) for c in range(C)]  # P(X_0=c)
        # 同時確率 (X_i=k, X_j=m, X_0=c)
        self.p_joint_ijc = [
            [
                [[[0 for _ in range(C)] for _ in range(C)] for _ in range(C)]
                for _ in range(F)
            ]
            for __ in range(F)
        ]
        # 条件付き同時確率 (X_i=k, X_j=m | X_0=c)
        self.p_cond_ij_given_c = [
            [
                [[[0 for _ in range(C)] for _ in range(C)] for _ in range(C)]
                for _ in range(F)
            ]
            for __ in range(F)
        ]
        # 条件付き確率 (X_i=k | X_0=c)
        self.p_cond_i_given_c = [
            [[0 for _ in range(C)] for _ in range(C)] for _ in range(F)
        ]
        self.cmi = [
            [0 for _ in range(F + 1)] for __ in range(F + 1)
        ]  # cmi[i][j]=I(X_i,X_j|X_0)
        for i in range(F):
            for j in range(C):
                for k in range(C):
                    self.p_cond_i_given_c[i][j][k] = self.ft_ic[i][j][k] / self.ft_c[k]
            for j in range(i, F):
                for k in range(C):
                    for m in range(C):
                        self.p_joint_ijc[i][j][k][m][0] = self.ft_ijc[i][j][k][m][
                            0
                        ] / len(self.LD)
                        self.p_joint_ijc[i][j][k][m][1] = self.ft_ijc[i][j][k][m][
                            1
                        ] / len(self.LD)
                        self.p_joint_ijc[j][i][m][k][0] = self.p_joint_ijc[i][j][k][m][
                            0
                        ]
                        self.p_joint_ijc[j][i][m][k][1] = self.p_joint_ijc[i][j][k][m][
                            1
                        ]
                        self.p_cond_ij_given_c[i][j][k][m][0] = (
                            self.ft_ijc[i][j][k][m][0] / self.ft_c[0]
                        )
                        self.p_cond_ij_given_c[j][i][m][k][0] = self.p_cond_ij_given_c[
                            i
                        ][j][k][m][0]
                        self.p_cond_ij_given_c[i][j][k][m][1] = (
                            self.ft_ijc[i][j][k][m][1] / self.ft_c[1]
                        )
                        self.p_cond_ij_given_c[j][i][m][k][1] = self.p_cond_ij_given_c[
                            i
                        ][j][k][m][1]

    def _get_parameters(self):
        """
        ベイズネットのパラメータ θ を計算
        返り値:  theta_class, theta_root_given_class, theta_feature_given_parent_and_class  の3つのリスト
        それぞれ、
        - theta_class: P(Y=c) のリスト
        - theta_root_given_class: P(X_root=k | Y=c) の2次元リスト
        - theta_feature_given_parent_and_class: P(X_i=k | X_parent=m, Y=c) の3次元リスト
        となる
        """
        theta_x0 = [0 for _ in range(self.NC)]
        theta_root_given_class = [[0 for _ in range(self.NC)] for _ in range(self.NC)]
        theta_feature_given_parent_and_class = [
            [
                [[0 for _ in range(self.NC)] for _ in range(self.NC)]
                for _ in range(self.NC)
            ]
            for _ in range(self.NV - 1)
        ]
        for c in range(self.NC):
            if self.ft_c[c] == 0:
                theta_x0[c] = 1.0
            else:
                theta_x0[c] = self.ft_c[c] / len(self.LD)

        for k in range(self.NC):
            for c in range(self.NC):
                if self.ft_ic[self.route_node][k][c] == 0:
                    theta_root_given_class[k][c] = 1.0
                else:
                    theta_root_given_class[k][c] = (
                        self.ft_ic[self.route_node][k][c] / self.ft_c[c]
                    )

        for i in range(self.NV - 1):
            if i == self.route_node:
                continue
            for j in range(self.NC):
                for k in range(self.NC):
                    for c in range(self.NC):
                        if self.ft_ijc[i][self.parents[i]][k][j][c] == 0:
                            theta_feature_given_parent_and_class[i][k][j][c] = 1.0
                        else:
                            theta_feature_given_parent_and_class[i][k][j][c] = (
                                self.ft_ijc[i][self.parents[i]][k][j][c]
                                / self.ft_ic[self.parents[i]][j][c]
                            )
        return theta_x0, theta_root_given_class, theta_feature_given_parent_and_class

    def getConditionalProbability(self):
        """
        p_joint_ijc, p_cond_i_given_c, p_cond_ij_given_c用いて条件付き相互情報量を求める
        cmi[2][5]=変数X_2,X_5間の条件付き相互情報量I(X_2,X_5|X_0)
        """
        cmi = [[0 for _ in range(self.NV - 1)] for _ in range(self.NV - 1)]
        F = self.NV - 1  # 特徴の本数
        C = self.NC
        # 条件付き相互情報量の計算
        for i in range(F):
            for j in range(i, F):
                cmi_ij = 0.0
                for c in range(C):
                    for k in range(C):
                        for m in range(C):
                            if (
                                self.p_joint_ijc[i][j][k][m][c] == 0
                                or self.p_cond_i_given_c[i][k][c] == 0
                                or self.p_cond_i_given_c[j][m][c] == 0
                            ):
                                continue
                            p_ijc = self.p_joint_ijc[i][j][k][m][c]
                            p_ij_c = self.p_cond_ij_given_c[i][j][k][m][c]
                            p_i_given_c = self.p_cond_i_given_c[i][k][c]
                            p_j_given_c = self.p_cond_i_given_c[j][m][c]
                            cmi_ij += p_ijc * math.log2(
                                p_ij_c / (p_i_given_c * p_j_given_c)
                            )
                cmi[i][j] = cmi_ij
                cmi[j][i] = cmi_ij
        return cmi

    def classification(self):
        """
        TANモデルを用いてTDの分類を行う
        予測結果と正解を表示し，最終的に精度を表示する
        """
        num_correct = 0
        num_test = len(self.TD)
        for idx, x in enumerate(self.TD):
            X_route = x[self.route_node]
            prob_0 = self.theta_x0[0] * self.theta_root_given_class[X_route][0]
            prob_1 = self.theta_x0[1] * self.theta_root_given_class[X_route][1]
            for i in range(self.NV - 1):
                if i == self.route_node:
                    continue
                X_i = x[i]
                parent_node = self.parents[i]
                X_parent = x[parent_node]

                prob_0 *= self.theta_feature_given_parent_and_class[i][X_i][X_parent][0]
                prob_1 *= self.theta_feature_given_parent_and_class[i][X_i][X_parent][1]

            if prob_0 > prob_1:
                predicted_class = 0
            else:
                predicted_class = 1
            true_class = x[self.NV - 1]
            if predicted_class == true_class:
                num_correct += 1
            print(f"data id: {idx}, predicted: {predicted_class}, true: {true_class}")
        print(
            f"accuracy of TAN on TD: {num_correct / num_test}",
            end="",
        )

    def getMaximumSpanningTree(self, cmi):
        """
        条件付き相互情報量 cmi をもとに最大全域木を求める
        クラスカル法を用いる
        """
        F = self.NV - 1  # 特徴の本数
        parent = [i for i in range(F)]  # Union-Findの親配列

        def find(u):
            while parent[u] != u:
                parent[u] = parent[parent[u]]
                u = parent[u]
            return u

        def union(u, v):
            root_u = find(u)
            root_v = find(v)
            if root_u != root_v:
                parent[root_v] = root_u

        edges = []
        for i in range(F):
            for j in range(i + 1, F):
                edges.append((cmi[i][j], i, j))
        edges.sort(reverse=True)

        mst_edges = []
        for weight, u, v in edges:
            if find(u) != find(v):
                union(u, v)
                mst_edges.append((u, v))

        return mst_edges


from contextlib import redirect_stdout

if __name__ == "__main__":
    with open("result.txt", "w", encoding="utf-8") as f:
        with redirect_stdout(f):
            TAN()  # ここでの print はすべて tan_output.txt に書かれる
