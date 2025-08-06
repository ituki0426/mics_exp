module sample2(s,a,b);
    input a, b;
    wire w1, w2;
    output s;
    assign w1 = a & b;
    assign w2 = a ^ b;
    assign s = w1 | w2;
endmodule