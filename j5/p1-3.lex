digit [0-9]
alpha [a-zA-Z]
white [\n\t ]
dot ["."]
sign [+-]
exp [e]
%%
{sign}?{digit}+{exp}?{digit}*    { return NUM; }
{alpha}({alpha}|{digit})* { return IDENT; }
{sign}?{digit}+{dot}{digit}+{exp}?{sign}?{digit}*  {return RNUM;}

{white}                   { ; }
