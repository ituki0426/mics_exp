module sample1_test;
    wire z;
    reg x,y;
    sample1 g1(z,x,y);
    initial 
        begin
                $monitor("%b %b %b", x, y, z, $time);
                $display("x y z                time");
                x = 0; y = 0;
            #50 y = 1;
            #50 x = 1; y = 0;
            #50 y = 1;  
            #50 $finish;
    end
endmodule