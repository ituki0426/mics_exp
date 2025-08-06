module eq(s,a,b);
    input a,b;
    output s;
    wire na,nb,s1,s2;
    assign na = ~a;
    assign nb = ~b;
    assign s1 = a & b;
    assign s2 = na & nb;
    assign s = s1 | s2;
endmodule