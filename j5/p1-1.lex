digit [0-9]
alpha [a-zA-Z]
white [\n\t ]
dot ["."]
sign [+-]
%%
{sign}?{digit}+    { return NUM; }
{alpha}({alpha}|{digit})* { return IDENT; }
{sign}?{digit}+{dot}{digit}+  {return RNUM;}

{white}                   { ; }
