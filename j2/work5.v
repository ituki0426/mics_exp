module work5(cu, s, x, y, ci,n);
    input [n-1:0] x, y;
    input ci;
    wire [n-1:0] c;
    output [n-1:0] s;
    output cu;
    assign c[0] = ci;
    assign {cu, c[n-1:1]} = x[n-1:0]&y[n-1:0] | x[n-1:0] & c[n-1:] | y[n-1:0] & c[n-1:0];
    assign s = x ^ y ^ c;
endmodule