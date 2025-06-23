digit        [0-9]
white        [\t ]
%%
{digit}+     { return NUM; }
[+*()]       { return yytext[0]; }
"\n"         { return '\n'; }
{white}      { ; }
