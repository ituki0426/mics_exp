module work2Sim;
wire s;
reg a,b,c,d;
work2 g1(s,a,b,c,d);
initial
begin
$monitor("%b %b %b %b %b", a, b, c, d, s, $time);
$display("a b c d s time");
a = 0; b = 0; c = 0; d = 0;
#50 a = 1; b = 0; c = 0; d = 0;
#50 a = 0; b = 1; c = 0; d = 0;
#50 a = 0; b = 0; c = 1; d = 0;
#50 a = 0; b = 0; c = 0; d = 1;
#50 a = 1; b = 1; c = 0; d = 0;
#50 a = 1; b = 0; c = 1; d = 0;
#50 a = 1; b = 0; c = 0; d = 1;
#50 a = 0; b = 0; c = 1; d = 1;
#50 a = 1; b = 1; c = 1; d = 0;
#50 a = 0; b = 1; c = 1; d = 1;
#50 a = 1; b = 0; c = 1; d = 1;
#50 a = 1; b = 1; c = 1; d = 1;
#50 $finish;
end
endmodule