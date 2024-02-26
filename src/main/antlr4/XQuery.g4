grammar XQuery;
import XPath;

xq : var                                            #variable
   | StringConstant                                 #string
   | ap                                             #xqBracket
   | '(' xq ')'                                     #xqBrace
   | xq '/' rp                                      #xqSingleSlash
   | xq '//' rp                                     #xqDoubleSlash
   | xq ',' xq                                      #xqConcat
   | forClause letClause? whereClause? returnClause #FLWR
   | letClause xq                                   #xqLet
   | startTag '{' xq '}' endTag                     #xqTag
   | joinClause                                     #xqJoin
   ;

forClause : 'for' var 'in' xq (',' var 'in' xq)* ;
letClause : 'let' var ':=' xq (',' var ':=' xq)* ;
whereClause : 'where' cond ;
returnClause : 'return' xq ;
joinClause: 'join' '(' xq ',' xq ',' idList ',' idList ')';

cond : xq eq xq                                                #eqCond
     | xq IS xq                                                #isCond
     | 'empty(' xq ')'                                         #emptyCond
     | 'some' var 'in' xq (',' var 'in' xq)* 'satisfies' cond  #satisfyCond
     | '(' cond ')'                                            #braceCond
     | cond 'and' cond                                         #andCond
     | cond 'or' cond                                          #orCond
     | 'not' cond                                              #notCond
     ;


startTag: '<' tagName '>';
endTag: '<' '/' tagName '>';
var : '$' ID;
StringConstant: '"'+[a-zA-Z0-9,.!?; '"-]+'"';
idList: '[' ID (',' ID)* ']' | '[' ']';
