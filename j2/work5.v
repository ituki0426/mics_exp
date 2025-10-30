module work5(cu, s, x, y, ci); // 加算器モジュール
parameter n = 4; // ビット幅のパラメータ
input [n-1:0] x, y; // 入力信号
input ci; // キャリー入力
wire [n-1:0] c; // キャリー信号
output [n-1:0] s; // 出力信号
output cu; // キャリー出力
assign c[0] = ci; // キャリー信号の設定
assign {cu, c[n-1:1]} = x[n-1:0]&y[n-1:0] | x[n-1:0] & c[n-1:0] | y[n-1:0] & c[n-1:0]; // キャリー出力の設定
assign s = x ^ y ^ c; // 出力信号の設定
endmodule
