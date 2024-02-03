grammar XPath;

//absolute path
ap  : doc '/' rp    
    | doc '//' rp   
    ;

// doc
doc : 'doc("' fileName '")';

// relative path
rp  : tagName       
    | '*'           
    | '.'           
    | '..'          
    | 'text()'      
    | '@' attName   
    | '(' rp ')'    
    | rp '/' rp     
    | rp '//' rp    
    | rp '[' f ']'  
    | rp ',' rp     
    ;

//path filter
f   : rp        #rpFilter
    | rp EQ rp  #eqFilter
    | rp IS rp  #isFilter
    | '(' f ')' #bracketFilter
    | f 'and' f #andFilter
    | f 'or' f  #orFilter
    | 'not' f   #notFilter
    | rp '=' STRCONSTANT  #eqStringConstantFilter

    ;




tagName : ID;
attName : ID;


EQ  : '=' | 'eq';
IS  : '==' | 'is';
ID  : [a-zA-Z][a-zA-Z0-9_-]+;


fileName    : FILENAME;
FILENAME    : [a-zA-Z0-9_.]+;
STRCONSTANT : ('"' [a-zA-Z0-9._!,:? -]+'"' | '\'' [a-zA-Z0-9._!,':? -]+'\'');
SPC  : [ \t\n\r]+ -> skip;