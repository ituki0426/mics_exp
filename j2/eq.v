module eq(s,a,b); // モジュールの定義 eq
input a,b; // 入力信号
output s; // 出力信号
wire na,nb,s1,s2; // 内部信号の定義
assign na = ~a; // ａの否定
assign nb = ~b; // ｂの否定
assign s1 = a & b; // ａとｂの論理積
assign s2 = na & nb; // ａの否定とｂの否定の論理積
assign s = s1 | s2; // ｓ１とｓ２の論理和を出力ｓに
endmodule