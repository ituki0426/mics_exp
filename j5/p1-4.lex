/* lex p1-4.lex */
digit     [0-9]
white     [\n\t ]
%%
{digit}+  { return NUM; }
"+"       { return '+'; }
"-"       { return '-'; }
{white}   { ; }
