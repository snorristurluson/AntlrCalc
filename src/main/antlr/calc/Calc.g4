grammar Calc;
@header {
package calc;
}

expression : term ((PLUS | MINUS) term)* ;

term : factor ((TIMES | DIV) factor)* ;

factor : signed_factor | xfactor ;

signed_factor : (PLUS | MINUS) xfactor ;

xfactor : paren_expr | function_call | value ;

paren_expr : '(' expression ')' ;

function_call : function_name '(' (expression (',' expression)*)? ')' ;

function_name : ID ;

value : variable | number ;

variable : ID ;

number : NUMBER ;

PLUS: '+';
MINUS: '-';
TIMES: '*';
DIV: '/';
ID: LETTER (LETTER|DIGIT)*;
LETTER: ('a'..'z')|('A'..'Z')|'_';
NUMBER: DIGIT+ ('.' DIGIT+)?;
DIGIT: ('0'..'9');
WS: [ \r\n\t]+ -> skip;