module work2(s,a,b,c,d);
input a,b,c,d; // 入力信号
output s; // 出力信号
wire s1,s2; // 中間信号
eq m1(s1, a, b); // ａ＝ｂかどうかを判定
eq m2(s2, c, d); // ｃ＝ｄかどうかを判定
assign s = s1 & s2; // 出力信号
endmodule