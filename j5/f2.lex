%{
#include <math.h>
#include <ctype.h>
#include <stdlib.h>
%}

digit   [0-9]
white   [\t ]
dot     ["."]
sign    [+-]

%%
"cos"        { return COS; }
"sin"        { return SIN; }
"tan"        { return TAN; }
"sqrt"       { return SQRT; }

{sign}?{digit}+{dot}{digit}+  { yylval.db = atof(yytext); return RNUM; }
{sign}?{digit}+               { yylval.db = atof(yytext); return RNUM; }

[*%/+()]     { return yytext[0]; }
"\n"         { return '\n'; }
{white}      { ; }
