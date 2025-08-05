alpha   [a-zA-Z]
digit   [0-9]
white   [\n\t ]
%%
read                        { return READ;    } /*文字入力*/
print                       { return PRINT;   } /*文字出力*/
while                       { return WHILE;   } /*while文*/
"if"                        { return IF;      } /*if文*/
"else"                      { return ELSE;    } /*else文*/
"case"                      { return CASE;    } /*case文*/
"default"                   { return DEFAULT; } /*default文*/
"switch"                    { return SWITCH;  } /*switch文*/
"++"                        { return PLUSPLUS;} /*インクリメント*/
"=="                        { return EQ;}   /*等しい*/
">="                        { return GE;}   
"<="                        { return LE;}
{alpha}({alpha}|{digit})*   { return IDENT; } /*識別子*/
{digit}+                    { return NUM; } /*整数*/
[*%/+\-=();{}<>:]               { return yytext[0]; } /*演算子と括弧*/
{white}                     { ; } /*空白文字は無視*/
