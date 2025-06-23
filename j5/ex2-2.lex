digit        [0-9]
white        [\t ]
%%
{digit}+     { return NUM; }
"+"          { return '+'; }
"\n"         { return '\n'; }
{white}      { ; }
