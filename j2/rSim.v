module clk(ck);
    output ck;
    reg ck;
    initial ck = 0;
    always #50 ck = ~ck; // Toggle clock every 50 time units
endmodule

module dff_nbit(q,d,ck);
    parameter n = 4;
    input [n-1:0] d;
    input ck;
    output [n-1:0] q;
    reg [n-1:0] q;
    initial q = 0;
    always @(negedge ck) q = d;
endmodule

module r(q,d,ck,load);
    parameter n = 4;
    input [n-1:0] d;
    input ck, load;
    output [n-1:0] q;
    wire [n-1:0] tmp;
    dff_nbit dffn1(q, tmp, ck);
    assign tmp = load ? d : q; // Load new value if load is high, else retain current value
endmodule

module rSim;
    reg load;
    initial load = 0; // Initialize load to 0
    reg [3:0] i;
    wire [3:0] o;
    clk clk1(ck);
    r reg1(o, i, ck, load);
    initial
        begin
            $monitor(" %b %b %b %b", ck, i, load, o, $stime);
            $display("ck i load o time");
            $dumpfile("rSim.vcd");
            $dumpvars(0, rSim);
            i = 4'b0000;  
            #100 i = 4'b0001; load = 1;
            #200 i = 4'b0010; load = 0;
            #100 i = 4'b0011; load = 1;
            #200 i = 4'b0100; load = 0;
            #100 $finish;
        end
endmodule
