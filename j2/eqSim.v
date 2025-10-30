module eqSim;
wire s; // 正しいかどうかを示す出力
reg x,y; // 入力信号
eq g1(s,x,y); // ｅｑモジュールのインスタンス化
initial // 初期化ブロック
begin // シミュレーションの開始
$monitor("%b %b %b %b %b", x, y, s, g1.s1, g1.s2, $time); // モニタリング出力
$dumpfile("eq.vcd"); // ＶＣＤファイルの出力
$dumpvars(0, eqSim); // 変数のダンプ
$display("x y s s1 s2 time"); // ヘッダーの表示
x = 0; y = 0; // 初期値設定
#50 y = 1; // ５０時間単位後にｙを１に
#50 x = 1; y = 0; // さらに５０時間単位後にｘを１、ｙを０に
#50 y = 1; // さらに５０時間単位後にｙを１に
#50 $finish; // シミュレーションの終了
end
endmodule
