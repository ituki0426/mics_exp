%union {
  char   name[128];
}
%token <name> PREPOSITIONAL VERB AUXILIARY_VERB PLACE_NOUN NOUN DETERMINER ADJECTIVE

%type  <name> np det vp pp obj
%%
ss : np vp '.'  { printf("NP = %s\nVP = %s\n", $1, $2); }
   ;

np : DETERMINER NOUN         { sprintf($$,"%s %s", $1, $2); }
   | ADJECTIVE det NOUN      { sprintf($$, "%s %s %s", $1, $2, $3); }
   | NOUN                    { strcpy($$, $1); }
   ;

det : DETERMINER   { strcpy($$, $1); }
    |              { strcpy($$, ""); }
    ;

vp : VERB             { strcpy($$, $1); strcat($$," "); }
   | AUXILIARY_VERB VERB obj { sprintf($$, "%s %s %s", $1, $2, $3); }
   | VERB det NOUN pp        { sprintf($$, "%s %s %s %s", $1, $2, $3, $4); }
   ;

pp : PREPOSITIONAL det PLACE_NOUN    { sprintf($$, "%s %s %s", $1, $2, $3); }
   |                                 { strcpy($$, ""); }
   ;

obj : det NOUN pp   { sprintf($$, "%s %s %s", $1, $2, $3); }
    | PREPOSITIONAL det PLACE_NOUN    { sprintf($$, "%s %s %s", $1, $2, $3); }
    |               { sprintf($$, ""); }
    ;
