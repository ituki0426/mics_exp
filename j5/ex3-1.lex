alpha   [a-zA-Z]
white   [\t ]
%%
CATS|DOGS|MAN|WOMAN         { strcpy(yylval.name, yytext); return NOUN; }

ROOM|HOUSE|SKY              { strcpy(yylval.name, yytext); return PLACE_NOUN; }

THE|A|AN|THIS|THAT|THOSE|MY { strcpy(yylval.name, yytext); return DETERMINER; }

FIRST|LAST|NEXT|SMALL|BIG   { strcpy(yylval.name, yytext); return ADJECTIVE; }

IS|ARE|RUN|FLY|FIND|BE      { strcpy(yylval.name, yytext); return VERB; }

CAN|SHOULD|MAY              { strcpy(yylval.name, yytext); return AUXILIARY_VERB; }

IN|AT|ON|TO|INSIDE          { strcpy(yylval.name, yytext); return PREPOSITIONAL; }

[\.,']                      { return yytext[0]; }
{white}                     { ; }
