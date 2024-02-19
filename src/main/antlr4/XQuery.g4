grammar XPath;

@header {
    package org.cse232b.antlr4;
}
//absolute path
ap  : doc '/' rp    #singleAP
    | doc '//' rp   #doubleAP
    ;

// document
doc : ('doc("' | 'document("') fileName '")'
    ;

// relative path
rp  : tagName       #tagRP
    | '*'           #childrenRP
    | '.'           #selfRP
    | '..'          #parentRP
    | 'text()'      #textRP
    | '@' attrName  #attrRP
    | '(' rp ')'    #bracketRP
    | rp '/' rp     #singleSlashRP
    | rp '//' rp    #doubleSlashRP
    | rp '[' f ']'  #filterRP
    | rp ',' rp     #commaRP
    ;

//filter
f   : rp        #rpFilter
    | rp eq rp  #eqFilter
    | rp IS rp  #isFilter
	| rp '=' '"' stringConstant '"' #stringFilter
    | '(' f ')' #bracketFilter
    | f 'and' f #andFilter
    | f 'or' f  #orFilter
    | 'not' f   #notFilter
    ;

tagName : ID;
attrName : ID;
stringConstant: '"' ID '"' | '\'' ID '\'';

eq  : '=' | 'eq';
IS  : '==' | 'is';
ID  : [a-zA-Z][a-zA-Z0-9_-]*;

fileName    : FILENAME;
FILENAME    : [a-zA-Z0-9_.-]+;
SPC  : [ \t\n\r]+ -> skip;