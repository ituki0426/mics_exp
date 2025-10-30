module work5Sim;
parameter n = 4; // ビット幅のパラメータ
reg [n-1:0] x, y; // 入力信号
reg ci; // キャリー入力
wire [n-1:0] s; // 出力信号
wire cu; // キャリー出力
work5 add4(cu, s, x, y, ci); // 加算器のインスタンス化
initial begin // 初期化ブロック
$monitor("x=%b, y=%b, ci=%b, s=%b, cu=%b", x, y, ci, s, cu); // モニタリング
$dumpfile("work5Sim.vcd"); // 波形出力ファイルの指定
$dumpvars(0, work5Sim); // 波形出力の変数指定
x = 4'b0000; y = 4'b0000; ci = 0; #10; // 入力信号の設定
x = 4'b0001; y = 4'b0001; ci = 0; #10; // 入力信号の設定
x = 4'b0010; y = 4'b0011; ci = 1; #10; // 入力信号の設定
x = 4'b0100; y = 4'b0101; ci = 0; #10; // 入力信号の設定
x = 4'b1111; y = 4'b1111; ci = 1; #10; // 入力信号の設定
$finish;
end
endmodule
