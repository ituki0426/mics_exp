#ifndef MYTRIG_H
#define MYTRIG_H

/* 数値は double 前提。fabs の代わりに自作 abs_d を使う */
static inline double abs_d(double x) { return x < 0 ? -x : x; }

/* 円周率の定義 */
#define MY_PI 3.14159265358979323846264338327950288419716939937510

/*余りを求めるコード*/
static double my_mod(double x,double y){
    return (double)((int)x % (int)y);
}
/*平方根をニュートン法で計算するアルゴリズム*/
static double my_sqrt(double num) {
    double x = num;
    double dx;
    while (1) {
        dx = (x * x - num) / (2.0 * x);
        if (dx * dx < 0.00001) break;
        x -= dx;
    }
    return x;
}

/* 角度を [-pi, pi] に正規化 */
static double reduce_pi(double x) {
    double two_pi = 2.0 * MY_PI;
    while (x >  MY_PI) x -= two_pi;
    while (x < -MY_PI) x += two_pi;
    return x;
}

/* テイラー展開をある程度の次数で打ち切り */
static double my_sin(double x) {
    // x を [-pi, pi] に正規化
    x = reduce_pi(x);
    double term = x;    // x^(2n+1)
    double sum  = x;
    double x2   = x * x;
    term *= -x2 / (2.0 * 3.0);
    sum  += term;
    term *= -x2 / (4.0 * 5.0);
    sum  += term;
    term *= -x2 / (6.0 * 7.0);
    sum  += term;
    term *= -x2 / (8.0 * 9.0);
    sum  += term;
    return sum;
}
/* テイラー展開でcosを求める関数*/
static double my_cos(double x) {
    // x を [-pi, pi] に正規化
    x = reduce_pi(x);
    double term = 1.0;  // x^(2n)
    double sum  = 1.0;
    double x2   = x * x;

    term *= -x2 / (1.0 * 2.0);
    sum  += term;
    term *= -x2 / (3.0 * 4.0);
    sum  += term;
    term *= -x2 / (5.0 * 6.0);
    sum  += term;
    term *= -x2 / (7.0 * 8.0);
    sum  += term;

    return sum;
}
/* テイラー展開でtanを求める関数*/
static double my_tan(double x) {
    double c = my_cos(x);
    if (abs_d(c) < 1e-12) {
        return (x >= 0) ? 1e12 : -1e12;
    }
    return my_sin(x) / c;
}

#endif /* MYTRIG_H */
