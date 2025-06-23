%token NUM;
%%
expr : NUM              { value = atoi(yytext); }
     | NUM '+' expr     { value = value + atoi(yytext); }
     ;
