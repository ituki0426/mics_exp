%token NUM;
%token IDENT;
%token READ;
%token PRINT;
%%
prog   : IDENT '{' stlist '}'       { return $3; }
       ;
stlist :
       | stlist stat
       ;
stat   : var '=' expr ';'           { stab[$1].val = $3; }
       | READ var ';'               { scanf("%d", &stab[$2].val); }
       | PRINT expr ';'             { printf("%d\n", $2); }
       ;
expr   : prim              { $$ = $1; }
       | expr '+' prim     { $$ = $1 + $3; }
       | expr '-' prim     { $$ = $1 - $3; }
       ;
prim   : NUM               { $$ = atoi(yytext); }
       | var               { $$ = stab[$1].val; }
       | '(' expr ')'      { $$ = $2; }
       ;
var    : IDENT             { $$ = lookup(yytext); }
       ;
