function [A, x, b] = work2(n)
    A = zeros(n, n);
    x = zeros(n, 1);
    for i = 1:n
        A(i,i)= 2;
        if i > 1
            A(i, i-1) = -1;
            A(i-1, i) = -1;
        end
    end
    for i = 1:n
        x(i) = i;
    end
    b = A * x;