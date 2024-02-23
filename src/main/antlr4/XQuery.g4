grammar XQuery;
import XPath;

var : '$' attrName;

xq : var                                            # variable
   | StringConstant                                 # string
   | ap                                             # xqBracket
   | '(' xq ')'                                     # xqBrace
   | xq '/' rp                                      # xqChildren
   | xq '//' rp                                     # xqDesc
   | xq ',' xq                                      # xqConcat
   | forClause letClause? whereClause? returnClause # xqflwr
   | letClause xq                                   # xqLet
   | '<' tagName '>' '{' xq '}' '</' tagName '>'    # xqTag
   ;

forClause : 'for' var 'in' xq (',' var 'in' xq)* ;
letClause : 'let' var ':=' xq (',' var ':=' xq)* ;
whereClause : 'where' cond ;
returnClause : 'return' xq ;

cond : xq EQ xq                                                #eqCond
     | xq IS xq                                                #isCond
     | 'empty(' xq ')'                                         #emptyCond
     | 'some' var 'in' xq (',' var 'in' xq)* 'satisfies' cond  #satisfyCond
     | '(' cond ')'                                            #braceCond
     | cond 'and' cond                                         #andCond
     | cond 'or' cond                                          #orCond
     | 'not' cond                                              #notCond
     ;






