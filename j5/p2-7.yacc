%union {
  Node* np;
  int i;
  char* s;
}
%type <s> var
%type <np> stlist stat expr prim cond case_e caselist_e
%token NUM
%token IDENT
%token READ
%token PRINT
%token WHILE
%token PLUSPLUS
%token EQ
%token LE
%token GE
%token SWITCH
%token CASE
%token DEFAULT
%token IF
%token ELSE

/* ぶら下がり else 対策のための優先順位宣言 */
%nonassoc LOWER_THAN_ELSE
%nonassoc ELSE

%%
prog   : IDENT '{' stlist '}'              { dotree($3); return 0; }
       ;

case_e : CASE expr ':' stat                { $$ = createNode(T_CASE, $2, $4); }
       | DEFAULT ':' stat                  { $$ = createNode(T_DEFAULT, NULL, $3); }
       ;

caselist_e
       : case_e                            { $$ = $1; }
       | caselist_e case_e                 { $$ = createNode(T_STLIST, $1, $2); }
       ;

stlist :                                   { $$ = NULL; }
       | stlist stat                       { $$ = createNode(T_STLIST, $1, $2); }
       ;

stat   : var '=' expr ';'                  { $$ = createNode(T_ASSIGN, $1, $3); }
       | READ var ';'                      { $$ = createNode(T_READ, $2, NULL); }
       | PRINT expr ';'                    { $$ = createNode(T_PRINT, $2, NULL); }
       | WHILE '(' cond ')' stat           { $$ = createNode(T_WHILE, $3, $5); }
       | WHILE '(' cond ')' '{' stlist '}' { $$ = createNode(T_WHILE, $3, $6); }
       | SWITCH '(' expr ')' '{' caselist_e '}' { $$ = createNode(T_SWITCH, $3, $6); }
       | IF '(' cond ')' '{' stlist '}' %prec LOWER_THAN_ELSE { $$ = createNode(T_IF, $3, $6); }
       | IF '(' cond ')' '{' stlist '}' ELSE '{' stlist '}'{Node* pair = createNode(T_STMT_PAIR, $6, $10);$$ = createNode(T_IFELSE, $3, pair);}
       | IF '(' cond ')' stat %prec LOWER_THAN_ELSE{ $$ = createNode(T_IF, $3, $5); }
       | IF '(' cond ')' stat ELSE stat    { Node* pair = createNode(T_STMT_PAIR, $5, $7); $$ = createNode(T_IFELSE, $3, pair);}
       ;

expr   : prim                              { $$ = $1; }
       | expr '+' prim                     { $$ = createNode(T_ADD, $1, $3); }
       | expr '-' prim                     { $$ = createNode(T_SUB, $1, $3); }
       ;

prim   : NUM                               { $$ = createNode(T_NUM, atoi(yytext), NULL); }
       | var                               { $$ = createNode(T_VAR, $1, NULL); }
       | '(' expr ')'                      { $$ = $2; }
       | var PLUSPLUS                      { $$ = createNode(T_PP, $1, NULL); }
       ;

var    : IDENT                             { $$ = lookup(yytext); }
       ;

cond   : expr '<' expr                     { $$ = createNode(T_LT, $1, $3); }
       | expr '>' expr                     { $$ = createNode(T_GT, $1, $3); }
       | expr GE expr                      { $$ = createNode(T_GE, $1, $3); }
       | expr LE expr                      { $$ = createNode(T_LE, $1, $3); }
       | expr EQ expr                      { $$ = createNode(T_EQ, $1, $3); }
       ;
%%
