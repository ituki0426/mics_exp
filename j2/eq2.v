module eq2(s,a,b);
    input a,b;
    output s;
    wire s1,s2;
    assign s1 = ~(a & b);
    assign s2 = a | b;
    assign s = ~(s1 & s2);
endmodule