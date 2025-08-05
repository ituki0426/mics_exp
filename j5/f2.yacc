%{
#include <stdio.h>
#include <stdlib.h>
#include "mytrig.h"
%}
%union {
    double db;
}
%token <db> RNUM COS SIN TAN SQRT
%type <db> expr term prim
%left '+'
%left '*' '/' '%'
%%
exprlist:
        | exprlist expr '\n'    { printf("%.2f\n", $2); }
        ;

expr    : term
        | expr '+' term         { $$ = $1 + $3; } /*足し算*/
        | expr '-' term         { $$ = $1 - $3; } /*引き算*/
        ;
term    : prim       
        | term '*' term {$$  = $1 * $3; } /*掛け算*/
        | term '/' term {
                if($3 == 0){
                    printf("Error: division by zero\n");
                    exit(1);
                }else{
                    $$  = $1 / $3; 
                }
        } /*割り算*/
        | term '%' term {
                if($3 == 0){
                    printf("Error: division by zero\n");
                    exit(0);
                }else{
                    $$ = my_mod($1,$3);
                }
        } /*余り演算*/
        ;
prim
    : RNUM                      { $$ = $1; }
    | '(' expr ')'              { $$ = $2; }
    | COS '(' expr ')'          { $$ = my_cos($3); }
    | SIN '(' expr ')'          { $$ = my_sin($3); }
    | TAN '(' expr ')'          { $$ = my_tan($3); }
    | SQRT '(' expr ')'         { $$ = my_sqrt($3); }
    ;
