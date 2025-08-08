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

module dffnSim;
    reg i;
    wire o;
    clk clk1(ck);
    dffn dffn1(o, i, ck);
    initial
        begin
            $monitor(" %b %b %b",ck,i,o,$stime);
            $display("ck i o time");
            $dumpfile("dffn_3.vcd");
            $dumpvars(0, dffnSim);
                i = 0;  
           #100 i = 1;
           #200 i = 0;
           #100 $finish;
        end
endmodule
