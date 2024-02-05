grammar XPath;

@header {
    package org.cse232b.antlr4;
}

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
f   : rp        
    | rp EQ rp  
    | rp IS rp  
    | '(' f ')' 
    | f 'and' f 
    | f 'or' f  
    | 'not' f   
    | rp '=' strConst  

    ;




tagName : ID;
attName : ID;
strConst: '"' ID '"' | '\'' ID '\'';


EQ  : '=' | 'eq';
IS  : '==' | 'is';
ID  : [a-zA-Z][a-zA-Z0-9_-]+;


fileName    : FILENAME;
FILENAME    : [a-zA-Z][a-zA-Z0-9-_.]+;
SPC  : [ \t\n\r]+ -> skip;