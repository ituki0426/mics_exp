%token NUM;
%%
exprlist: 
        | exprlist expr '\n'    { printf("%d\n", value); }
        ;

expr    :
        | NUM                   { value = atoi(yytext); }
        | expr '+' NUM          { value = value + atoi(yytext); }
        ;
