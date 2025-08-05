clear
n = 5; 
[A, x, b] = work2(n);
disp('Matrix A:');  
disp(A);
disp('Vector x:');
disp(x);
disp('Vector b:');  
disp(b);
disp('x = A\\b:');
x_solution = A\b;
disp(x_solution);