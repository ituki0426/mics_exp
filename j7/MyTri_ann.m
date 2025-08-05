function [x, y, e2n] = MyTri_ann(lx, ly, nx, ny)
    N = (nx+1) * (ny+1);
    M = N;
    x = zeros(N, 1);
    y = zeros(N, 1);
    for i = 1:N
        r = (floor((i-1)/(ny+1)) * lx / nx) + 1;
        theta = mod(i-1, ny+1) * 30 * pi / 180;  
        x(i) = r * cos(theta);
        y(i) = r * sin(theta);
    end
    k = 1;
    for i = 1:nx
        for j = 1:ny
            SW = (i-1)*(ny+1) + j;
            NW = SW + 1;
            SE = SW + (ny+1);
            NE = SE + 1;
            e2n(2*k-1, :) = [SW, SE, NE];
            e2n(2*k, :) = [SW, NE, NW];
            k = k + 1;
        end
    end
