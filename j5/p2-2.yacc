%token NUM;
%left '+' '-'
%left '*' '/'
%%
exprlist:
        | exprlist expr '\n'    { printf("%d\n", $2); }
        ;

expr    : term
        | expr '+' term         { $$ = $1 + $3; }
        | expr '-' term         { $$ = $1 - $3; }
        | expr '%' term         { $$ = $1 % $3; }
        ;
term    : prim       
        | term '*' term {$$  = $1 * $3; }
        | term '/' term {$$  = $1 / $3; }
        ;
prim    : NUM                   { $$ = atoi(yytext); }
        | '(' expr ')'          { $$ = $2; }
        ;
