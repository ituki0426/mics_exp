#include <stdio.h>
extern char *yytext;

int value;

#include "y.tab.c"
#include "lex.yy.c"

int main() {
  yyparse();
  return 0;
}
