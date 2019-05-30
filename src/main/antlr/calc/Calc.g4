grammar Calc;
@header {
package calc;
}

expression
    : term ((PLUS | MINUS) term)*
    ;

term
    : factor ((TIMES | DIV) factor)*
    ;

factor
    : (PLUS | MINUS) xfactor
    | xfactor
    ;

xfactor
    : '(' expression ')'
    | value '^' factor
    | function_call
    | value
    ;

function_call
    : function_name '(' expression (',' expression)* ')'
    ;

function_name
    : ID
    ;

value
    : variable
    | NUMBER
    ;

variable
    : ID
    ;

PLUS: '+';
MINUS: '-';
TIMES: '*';
DIV: '/';
ID: LETTER (LETTER|DIGIT)*;
LETTER: ('a'..'z')|('A'..'Z')|'_';
NUMBER: DIGIT+ ('.' DIGIT+)?;
DIGIT: ('0'..'9');
WS: [ \r\n\t]+ -> skip;