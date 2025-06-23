%token NUM;
%%
exprlist:
        | exprlist expr '\n'    { printf("%d\n", $2); }
        ;

expr    : prim                  { $$ = $1; }
        | expr '+' prim         { $$ = $1 + $3; }
        | expr '*' prim         { $$ = $1 * $3; }
        ;
prim    : NUM                   { $$ = atoi(yytext); }
        | '(' expr ')'          { $$ = $2; }
        ;
