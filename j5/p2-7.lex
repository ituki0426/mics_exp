alpha   [a-zA-Z]
digit   [0-9]
white   [\n\t ]
%%
read                        { return READ;    }
print                       { return PRINT;   }
while                       { return WHILE;   }
"if"                        { return IF;      }
"else"                      { return ELSE;    }
"case"                      { return CASE;    }
"default"                   { return DEFAULT; }
"switch"                    { return SWITCH;  }
"++"                        { return PLUSPLUS;}
"=="                        { return EQ;}
">="                        { return GE;}
"<="                        { return LE;}
{alpha}({alpha}|{digit})*   { return IDENT; }
{digit}+                    { return NUM; }
[+\-=();{}<>:]               { return yytext[0]; }
{white}                     { ; }
