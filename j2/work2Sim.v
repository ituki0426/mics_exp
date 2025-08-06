module work2Sim;
    wire z;
    reg x,y,v,w;
    work2 g1(z,x,y,v,w);
    initial 
        begin
                $monitor("%b %b %b %b %b", x, y, v, w, z, $time);
                $display("x y v w z                time");
                x = 0; y = 0; v = 0; w = 0;
            #50 x = 1; y = 0; v = 0; w = 0;
            #50 x = 0; y = 1; v = 0; w = 0;
            #50 x = 0; y = 0; v = 1; w = 0;
            #50 x = 0; y = 0; v = 0; w = 1;
            #50 x = 1; y = 1; v = 0; w = 0;
            #50 x = 1; y = 0; v = 1; w = 0;
            #50 x = 1; y = 0; v = 0; w = 1;
            #50 x = 0; y = 0; v = 1; w = 1;
            #50 x = 1; y = 1; v = 1; w = 0;
            #50 x = 0; y = 1; v = 1; w = 1;
            #50 x = 1; y = 0; v = 1; w = 1;
            #50 x = 1; y = 1; v = 1; w = 1;
            #50 $finish;
    end
endmodule