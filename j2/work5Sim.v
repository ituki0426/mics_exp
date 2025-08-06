module work5(cu, s, x, y, ci);
    parameter n = 4; 
    input [n-1:0] x, y;
    input ci;
    wire [n-1:0] c;
    output [n-1:0] s;
    output cu;
    assign c[0] = ci;
    assign {cu, c[n-1:1]} = x[n-1:0]&y[n-1:0] | x[n-1:0] & c[n-1:0] | y[n-1:0] & c[n-1:0];
    assign s = x ^ y ^ c;
endmodule

module work5Sim;
    reg [3:0] x, y;
    reg ci;
    wire [3:0] s;
    wire cu;

    work5 add4(cu, s, x, y, ci);

    initial begin
        $monitor("x=%b, y=%b, ci=%b, s=%b, cu=%b", x, y, ci, s, cu);
        x = 4'b0000; y = 4'b0000; ci = 0; #10;
        x = 4'b0001; y = 4'b0001; ci = 0; #10;
        x = 4'b0010; y = 4'b0011; ci = 1; #10;
        x = 4'b0100; y = 4'b0101; ci = 0; #10;
        x = 4'b1111; y = 4'b1111; ci = 1; #10;
        $finish;
    end
endmodule
