module work2(s,a,b,c,d);
    input a,b,c,d;
    output s;
    wire s1,s2;
    eq m1(s1, a, b);
    eq m2(s2, c, d);
    assign s = s1 & s2;
endmodule