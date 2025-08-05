/* yacc p2-7.yacc */
%union {
  Node* np;
  int i;
  char* s;
}
%type <s> var
%type <np> stlist stat expr prim cond case_e caselist_e /*型の指定*/
%token NUM /*数字*/
%token IDENT /*識別子*/
%token READ /*文字入力*/
%token PRINT /*文字出力*/
%token WHILE /*while文*/
%token PLUSPLUS /*インクリメント*/
%token EQ /*等しい*/
%token LE /*小なりイコール*/
%token GE /*大なりイコール*/
%token SWITCH /*switch文*/
%token CASE /*case文*/
%token DEFAULT /*default文*/
%token IF /*if文*/
%token ELSE /*else文*/


%%
prog   : IDENT '{' stlist '}'              { emit_c($3); return 0; }
       ;

case_e : CASE expr ':' stat                { $$ = createNode(T_CASE, $2, $4); }
       | DEFAULT ':' stat                  { $$ = createNode(T_DEFAULT, NULL, $3); }
       ;

caselist_e
       : case_e                            { $$ = $1; }
       | caselist_e case_e                 { $$ = createNode(T_STLIST, $1, $2); }
       ;

/*複数命令の定義*/
stlist :                                   { $$ = NULL; }
       | stlist stat                       { $$ = createNode(T_STLIST, $1, $2); }
       ;

/*命令の定義*/
stat   : var '=' expr ';'                  { $$ = createNode(T_ASSIGN, $1, $3); }
       | READ var ';'                      { $$ = createNode(T_READ, $2, NULL); }
       | PRINT expr ';'                    { $$ = createNode(T_PRINT, $2, NULL); }
       | WHILE '(' cond ')' stat           { $$ = createNode(T_WHILE, $3, $5); }
       | WHILE '(' cond ')' '{' stlist '}' { $$ = createNode(T_WHILE, $3, $6); }
       | SWITCH '(' expr ')' '{' caselist_e '}' { $$ = createNode(T_SWITCH, $3, $6); }
       | IF '(' cond ')' '{' stlist '}' ELSE '{' stlist '}'{Node* pair = createNode(T_STMT_PAIR, $6, $10);$$ = createNode(T_IFELSE, $3, pair);}
       | IF '(' cond ')' stat { $$ = createNode(T_IF, $3, $5); }
       ;
/*数式の定義*/
expr   : prim                              { $$ = $1; }
       | expr '+' prim                     { $$ = createNode(T_ADD, $1, $3); }
       | expr '-' prim                     { $$ = createNode(T_SUB, $1, $3); }
       | expr '*' prim                     { $$ = createNode(T_MUL, $1, $3); }
       | expr '/' prim                     { $$ = createNode(T_DIV, $1, $3); }
       | expr '%' prim                     { $$ = createNode(T_MOD, $1, $3); }
       ;

prim   : NUM                               { $$ = createNode(T_NUM, atoi(yytext), NULL); }
       | var                               { $$ = createNode(T_VAR, $1, NULL); }
       | '(' expr ')'                      { $$ = $2; }
       | var PLUSPLUS                      { $$ = createNode(T_PP, $1, NULL); }
       ;

/*変数の定義*/
var    : IDENT                             { $$ = lookup(yytext); }
       ;
/*比較演算子の定義*/
cond   : expr '<' expr                     { $$ = createNode(T_LT, $1, $3); }
       | expr '>' expr                     { $$ = createNode(T_GT, $1, $3); }
       | expr GE expr                      { $$ = createNode(T_GE, $1, $3); }
       | expr LE expr                      { $$ = createNode(T_LE, $1, $3); }
       | expr EQ expr                      { $$ = createNode(T_EQ, $1, $3); }
       ;
%%
