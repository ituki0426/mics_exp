module rSim;
parameter n = 4; // ビット幅のパラメータ
reg load; // ロード信号を制御するレジスタ
initial load = 0; // 初期値を０に設定
reg [n-1:0] d; // 入力データを４ビットのレジスタとして定義
wire [n-1:0] q; // 出力データを４ビットのワイヤとして定義
clk clk1(ck); // クロック信号を生成するモジュールをインスタンス化
r reg1(q, d, ck, load); // レジスタモジュールをインスタンス化
initial
begin
$monitor(" %b %b %b %b", ck, d, load, q, $stime); // モニタリングの設定
$display("ck d load q time"); // ヘッダーの表示
$dumpfile("rSim.vcd"); // ファイルの出力設定 VCD
$dumpvars(0, rSim); // 変数のダンプ設定
d = 4'b0000; // 初期入力データを設定
#50 d = 4'b0001; load = 1; // 入力データを変更し、ロード信号を１に設定
#100 d = 4'b0010; load = 0; // 入力データを変更し、ロード信号を０に設定
#50 d = 4'b0011; load = 1; // 入力データを変更し、ロード信号を１に設定
#100 d = 4'b0100; load = 0; // 入力データを変更し、ロード信号を０に設定
#50 $finish;
end
endmodule