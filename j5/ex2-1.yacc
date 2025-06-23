%token NUM;
%%
expr : NUM              { value = atoi(yytext); }
     | expr '+' NUM     { value = value + atoi(yytext); }
     ;
