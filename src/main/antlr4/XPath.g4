grammar XPath;

@header {
    package org.cse232b.antlr4;
}

// abs path
ap  : doc '/' rp    #singleAP
    | doc '//' rp   #doubleAP
    ;

// document
doc : 'doc("' FileName '")'
    ;


//rp
rp  : tagName   #tagRP
    | '*'       #childrenRP
    | '.'       #selfRP
    | '..'      #parentRP
    | 'text()'  #textRP
    | '@' attName   #attNameRP
    | '(' rp ')'    #bracketRP
    | rp '/' rp     #singleSlashRP
    | rp '//' rp    #doubleSlashRP
    | rp '[' f ']'  #filterRP
    | rp ',' rp     #commaRP
    ;

//filter
f   : rp            #rpFilter
    | rp EQ rp      #eqFilter
    | rp IS rp      #isFilter
    | rp EQ StringConstant #stringConstantFilter
    | '(' f ')'     #bracketFilter
    | f 'and' f     #andFilter
    | f 'or' f      #orFilter
    | 'not' f       #notFilter
    ;

tagName: String;
attName: String;

EQ  : '=' | 'eq';
IS  : '==' | 'is';
FileName: [a-zA-Z0-9._]+ '.xml';
String: [a-zA-Z0-9._]+;
StringConstant: '"'+[a-zA-Z0-9,.!?; '"-]+'"';
SPC  : [ \t\r\n]+ -> skip;


