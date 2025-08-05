clear
lx = 1.0;
ly = 1.5;
nx = 2;
ny = 3;
%{
lx = 1.5;
ly = 1.0;
nx = 4;
ny = 6;
%}
[x, y, e2n] = MyTri(lx, ly, nx, ny);
%-------------------
figure
triplot(e2n, x, y);
%-------------------
%{
xlim([0, 1.5]);
ylim([0, 1.5]);
axis square
%}
