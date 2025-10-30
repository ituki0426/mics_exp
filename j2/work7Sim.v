module work7Sim; // シミュレーションモジュール
parameter n = 4; // ビット幅
reg [n-1:0] x, y; // 入力
reg ci, k; // キャリー入力と加減算選択信号
wire [n-1:0] s; // 出力データ
wire cu; // キャリー出力
work7 UUT (cu, k, s, x, y, ci); // インスタンス化
initial begin
$monitor("x=%b, y=%b, ci=%b, k=%b, s=%b, cu=%b", x, y, ci, k, s, cu); // モニタリング
$dumpfile("work7Sim.vcd"); // 波形出力ファイル
$dumpvars(0, work7Sim); // 波形変数のダンプ
x = 4'b0001; y = 4'b0001; ci = 1; k = 1; #10;
x = 4'b0010; y = 4'b0011; ci = 0; k = 0; #10;
x = 4'b0110; y = 4'b0101; ci = 1; k = 1; #10;
x = 4'b1111; y = 4'b1111; ci = 0; k = 0; #10;
x = 4'b1111; y = 4'b1111; ci = 1; k = 0; #10;
$finish;
end
endmodule