module eqSim2;
    wire s;
    reg x,y;
    eq2 g(s,x,y);
    initial
        begin
                $monitor("%b %b", g.s1,g.s2, $time);
                $dumpfile("eq2.vcd");
                $dumpvars(0, eqSim2);
                $display("g.s1 g.s2           time");
                x = 0; y = 0;
            #50 x = 1;
            #50 y = 1;
            #50 x = 0;  
            #50 $finish;
        end
endmodule