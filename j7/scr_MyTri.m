clear
lx = 1.0;
ly = 1.5;
nx = 2;
ny = 3;

[x, y, e2n] = MyTri_ann(lx, ly, nx, ny);

figure
triplot(e2n, x, y);
