module clk(ck);
    output ck;
    reg ck;
    initial ck = 0;
    always #50 ck = ~ck; // Toggle clock every 50 time units
endmodule

module dffn(Q,D,ck);
    input D,ck;
    output Q;
    reg Q;
    initial Q = 0;
    always @(negedge ck) Q = D;
endmodule

module m2(a,b,ck);
    input a,ck;
    output b;
    reg b;
    wire ns1,ns0,na, s1,s0,d0,d1,c1,c2,c3,c4,c5;
    dffn f1(s1, d1, ck);
    dffn f2(s0, d0, ck);
    assign ns1 = ~s1;
    assign ns0 = ~s0;
    assign na = ~a;
    assign c1 = s0 & na;
    assign c2 = ns0 & a;
    assign d0 = c1 | c2;
    assign c3 = s1 & na;
    assign c4 = s1 & ns0;
    assign c5 = a & s0 & ns1;
    assign d1 = c3 | c4 | c5;
    assign b = a & s1 & s0;
endmodule


module m2Sim;
    reg a;
    wire b;
    clk clk1(ck);
    m2 m2_1(a, b, ck);
    initial
        begin
            $monitor("%b %b %b", ck, a, b, $stime);
            $display("ck a b time");

            $dumpfile("m2_re.vcd");
            $dumpvars(0, m2Sim);
            a = 0;
            #100 a = 1;
            #200 a = 0;
            #100 a = 1;
            #200 a = 0;
            #100 $finish;
        end
endmodule
