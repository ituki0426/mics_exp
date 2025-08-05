%union {
  Node* np;
  int i;
}
%type <np> stlist stat expr prim var
%token NUM;
%token IDENT;
%token READ;
%token PRINT;
%%
prog : IDENT '{' stlist '}'
      {
          traverse_tree(opt($3,NULL,NULL));   /* AST 変換 */
          return 0;
      };
stlist :                            { $$ = NULL; }
       | stlist stat                { $$ = createNode(T_STLIST, $1, $2); }
       ;
stat   : var '=' expr ';'           { $$ = createNode(T_ASSIGN, $1, $3); }
       | READ var ';'               { $$ = createNode(T_READ, $2, NULL); }
       | PRINT expr ';'             { $$ = createNode(T_PRINT, $2, NULL); }
       ;
expr   : prim              { $$ = $1; }
       | expr '+' prim     { $$ = createNode(T_ADD, $1, $3); }
       | expr '-' prim     { $$ = createNode(T_SUB, $1, $3); }
       ;
prim :  NUM    { $$ = createNode(T_NUM, INT_PTR(atoi(yytext)), NULL); }
       | var  { $$ = $1; }  
       | '(' expr ')'      { $$ = $2; }
       ;
var    : IDENT { $$ = createNode(T_VAR, INT_PTR(lookup(yytext)), NULL); }
       ;
