grammar XQuery;
import XPath;

var : '$' attName;

xq : var                                                         # variable
   | StringConstant                                              # string
   | ap                                                          
   | '(' xq ')'                                                  # xqBracket
   | xq '/' rp                                                   # xqChildren
   | xq '//' rp                                                  # xqDesc
   | xq ',' xq                                                   # xqConcat
   | forClause letClause? whereClause? returnClause              # flwrClause
   | letClause xq                                                # let_clause
   | '<' tagName '>' '{' xq '}' '</' tagName '>'                 # xqTag
   ;


forClause : 'for' var 'in' xq (',' var 'in' xq)*;

letClause : 'let' var ':=' xq (',' var ':=' xq)*;

whereClause : 'where' cond;

returnClause : 'return' xq;


cond : xq '=' xq                         # eqCond
     | xq 'eq' xq                        # eqCond
     | xq '==' xq                        # isCond
     | xq 'is' xq                        # isCond
     | 'empty' '(' xq ')'                # emptyCond
     | 'some' var 'in' xq (',' var 'in' xq)* 'satisfies' cond  # satisfyCond
     | '(' cond ')'                      # Brackets
     | cond 'and' cond                   # andCond
     | cond 'or' cond                    # orCond
     | 'not' cond                        # notCond
     ;



