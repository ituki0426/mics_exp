module clk(ck); // クロック信号生成モジュール
output ck; // クロック信号
reg ck; // クロック信号のレジスタ
initial ck = 0; // 初期値を０に設定
always #50 ck = ~ck; // ５０単位時間ごとにトグル
endmodule
module dffn(Q,D,ck); // フリップフロップモジュール D
input D,ck; // データ入力とクロック信号
output Q; // 出力
reg Q; // 出力のレジスタ
initial Q = 0; // 初期値を０に設定
always @(negedge ck) Q <= D; // クロックの立ち下がりでＤをに転送 Q
endmodule
module m2(a,b,ck); // モジュールｍ２の定義
input a,ck; // データ入力とクロック信号
output b; // 出力
wire ns1,ns0,na, s1,s0,d0,d1,c1,c2,c3,c4,c5; // 内部信号
dffn f1(s1, d1, ck); // Ｄフリップフロップｆ１のインスタンス
dffn f2(s0, d0, ck); // Ｄフリップフロップｆ２のインスタンス
assign ns1 = ~s1; // ｓ１のビット反転
assign ns0 = ~s0; // ｓ０のビット反転
assign na = ~a; // ａのビット反転
assign c1 = s0 & na; // ｃ１の生成
assign c2 = ns0 & a; // ｃ２の生成
assign d0 = c1 | c2; // ｄ０の生成
assign c3 = s1 & na; // ｃ３の生成
assign c4 = s1 & ns0; // ｃ４の生成
assign c5 = a & s0 & ns1; // ｃ５の生成
assign d1 = c3 | c4 | c5; // ｄ１の生成
assign b = a & s1 & s0; // ｂの生成
endmodule