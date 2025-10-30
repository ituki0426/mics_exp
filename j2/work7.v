module work7(cu, k, s, x, y, ci);
parameter n = 4;
input [n-1:0] x, y;
input ci, k;
wire [n-1:0] not_y;
wire [n-1:0] c;
output [n-1:0] s;
output cu;
assign not_y = k ? ~y : y;
assign c[0] = k ? 1 : ci;
assign {cu, c[n-1:1]} = x[n-1:0]&not_y[n-1:0] | x[n-1:0] & c[n-1:0] | not_y[n-1:0] & c[n-1:0];
assign s = x ^ not_y ^ c;
endmodule