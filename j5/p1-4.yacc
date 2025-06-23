%token NUM;
%%
expr : NUM          { printf("NUM: %s\n", yytext); }
     | expr '+' NUM { printf("ADD: %s\n", yytext); }
     | expr '-' NUM { printf("SUB: %s\n", yytext); }
     ;
