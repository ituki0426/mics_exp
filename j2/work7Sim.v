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

module work7Sim;
   reg [3:0] x, y;
   reg ci, k;
   wire [3:0] s;
   wire cu;
   work7 UUT (cu, k, s, x, y, ci);
   initial begin
       $monitor("x=%b, y=%b, ci=%b, k=%b, s=%b, cu=%b", x, y, ci, k, s, cu);
       x = 4'b0000; y = 4'b0000; ci = 0; k = 0; #10;
       x = 4'b0001; y = 4'b0001; ci = 1; k = 1; #10;
       x = 4'b0010; y = 4'b0011; ci = 0; k = 0; #10;
       x = 4'b0100; y = 4'b0101; ci = 1; k = 1; #10;
       x = 4'b1111; y = 4'b1111; ci = 0; k = 0; #10;
       x = 4'b0000; y = 4'b0000; ci = 0; k = 1; #10;
       x = 4'b1111; y = 4'b1111; ci = 1; k = 0; #10;
       x = 4'b1010; y = 4'b0101; ci = 0; k = 1; #10;
       $finish;
   end
endmodule