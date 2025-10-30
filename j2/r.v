module clk(ck);
output ck; // クロック信号を出力するモジュール
reg ck; // クロック信号を出力するレジスタ
initial ck = 0; // 初期値をに設定 0
always #50 ck = ~ck; // ５０秒ごとにクロックを反転
endmodule
module dff_nbit(q,d,ck);
parameter n = 4; // ビット幅のパラメータ
input [n-1:0] d; // 入力データ
input ck; // クロック信号
output [n-1:0] q; // 出力データ
reg [n-1:0] q; // 出力データをレジスタとして定義
initial q = 0; // 初期値を０に設定
always @(negedge ck) q = d; // クロックの立ち下がりで入力データを出力に転送
endmodule
module r(q,d,ck,load);
parameter n = 4; // ビット幅のパラメータ
input [n-1:0] d; // 入力データ
input ck, load; // クロック信号とロード信号
output [n-1:0] q; // 出力データ
wire [n-1:0] tmp; // 一時的な配線
dff_nbit dffn1(q, tmp, ck); // ビットのフリップフロップをインスタンス化 nD
assign tmp = load ? d : q; // ロード信号が１のときは入力データを、０のときは現在の出力データを使用
endmodule