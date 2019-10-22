grammar Calc;
@header {
package calc;
}

input : expression EOF ;

expression : term ((PLUS | MINUS) term)* ;

term : factor ((TIMES | DIV) factor)* ;

factor : signed_factor | xfactor ;

signed_factor : (PLUS | MINUS) xfactor ;

xfactor : paren_expr | function_expr | value ;

paren_expr : OPEN_PAREN expression CLOSE_PAREN ;

function_expr :
	if_expr
	| function_call
	;

function_call :
	function_name OPEN_PAREN (expression (',' expression)*)? CLOSE_PAREN
	;

if_expr :
	IF OPEN_PAREN rel_expr ',' expression ',' expression CLOSE_PAREN
	;

rel_expr : rel_term (OR rel_term)* ;

rel_term : predicate (AND predicate)*;

predicate : expression (EQ | NE | GT | GE | LT | LE) expression ;

function_name : IDENTIFIER ;

value : variable | number | string_literal;

variable : IDENTIFIER ;

number : NUMBER ;

string_literal : SingleQuoteString | DoubleQuoteString ;

SingleQuoteString
    :
    '\'' (ESC_S|.)*? '\''
    ;

DoubleQuoteString
    :
    '"' (ESC_D|.)*? '"'
    ;

OPEN_PAREN: '(';
CLOSE_PAREN: ')';
PLUS: '+';
MINUS: '-';
TIMES: '*';
DIV: '/';
EQ: '=' | '==';
NE: '!=' | '<>';
GT: '>';
GE: '>=';
LT: '<';
LE: '<=';

IF: I F ;
OR: O R ;
AND: A N D ;


IDENTIFIER: LETTER (LETTER|DIGIT)*;
LETTER: ('a'..'z')|('A'..'Z')|'_';
NUMBER: DIGIT+ ('.' DIGIT+)?;
DIGIT: ('0'..'9');

fragment
ESC_D: '\\"' | '\\\\' ;

fragment
ESC_S: '\\\'' | '\\\\' ;

WS: [ \r\n\t]+ -> channel(HIDDEN);
ERRCHAR
	:	. -> channel(HIDDEN)
	;

fragment A : [aA]; // match either an 'a' or 'A'
fragment B : [bB];
fragment C : [cC];
fragment D : [dD];
fragment E : [eE];
fragment F : [fF];
fragment G : [gG];
fragment H : [hH];
fragment I : [iI];
fragment J : [jJ];
fragment K : [kK];
fragment L : [lL];
fragment M : [mM];
fragment N : [nN];
fragment O : [oO];
fragment P : [pP];
fragment Q : [qQ];
fragment R : [rR];
fragment S : [sS];
fragment T : [tT];
fragment U : [uU];
fragment V : [vV];
fragment W : [wW];
fragment X : [xX];
fragment Y : [yY];
fragment Z : [zZ];
