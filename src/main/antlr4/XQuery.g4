grammar XQuery;
import XPath;

var : '$' attName;

xq : var                                            #varXQ
   | StringConstant                                 #strXQ
   | ap                                             #apXQ
   | '(' xq ')'                                     #braceXQ
   | xq ',' xq                                      #commaXQ
   | xq '/' rp                                      #singleSlashXQ
   | xq '//' rp                                     #doubleSlashXQ
   | '<' tagName '>' '{' xq '}' '</' tagName '>'    #tagXQ
   | forClause letClause? whereClause? returnClause #flwrXQ
   | letClause xq                                   #letXQ
   ;

forClause : 'for' var 'in' xq (',' var 'in' xq)* ;
letClause : 'let' var ':=' xq (',' var ':=' xq)* ;
whereClause : 'where' cond ;
returnClause : 'return' xq ;

cond : xq EQ xq                                                #eqCond
     | xq IS xq                                                #isCond
     | 'empty' '(' xq ')'                                      #emptyCond
     | 'some' var 'in' xq (',' var 'in' xq)* 'satisfies' cond  #satisfyCond
     | '(' cond ')'                                            #braceCond
     | cond 'and' cond                                         #andCond
     | cond 'or' cond                                          #orCond
     | 'not' cond                                              #notCond
     ;