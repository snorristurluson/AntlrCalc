package calc;

import org.junit.Test;

import static org.junit.Assert.*;

public class CalcEvaluatorTest extends CalcTestBase {
    @Test
    public void testSimpleExpressions() {
        assertResult("2+4", 6);
        assertResult("2*4", 8);
        assertResult("2*4+1", 9);
        assertResult("1+2*4", 9);
        assertResult("1+(2*4)", 9);
        assertResult("(1+2)*4", 12);
        assertResult("((1+2))*4", 12);
        assertResult("1+2+3+4+5+6", 21);
        assertResult("-4", -4);
        assertResult("+4", 4);
        assertResult("-(1+2)*4", -12);
    }

    @Test
    public void testStringExpressions() {
        assertResult("'a'", "a");
        assertResult("\"a\"", "a");
        assertResult("'a' + 'b'", "ab");
        assertResult("'a' + 'b' + 'c' + 'd'", "abcd");
        assertResult("('a' + 'b') + 'c' + 'd'", "abcd");
    }

    @Test
    public void testIfStatement() {
        assertResult("if(1>0, 'true', 'false')", "true");
    }

    @Test
    public void testFunctionCalls() {
        assertResult("sin(0.5)", 0.479425539);
    }

    @Test
    public void testVariableInExpression() {
        CalcEvaluator calcEvaluator = new CalcEvaluator();
        calcEvaluator.set("x", 3.14);
        calcEvaluator.set("y", 2.1);
        assertResult("x+y*3", calcEvaluator, 9.44);
        assertResult("sin(x)", calcEvaluator, 0.0015926529164868282);
    }

    @Test
    public void testStringVariableInExpression() {
        CalcEvaluator calcEvaluator = new CalcEvaluator();
        calcEvaluator.set("a", "bingo");
        calcEvaluator.set("b", "bongo");
        assertResult("a+b", calcEvaluator, "bingobongo");
        assertResult("'this is a ' + a + ' and a ' + b", calcEvaluator, "this is a bingo and a bongo");
    }


    private void assertResult(String input, CalcEvaluator calcEvaluator, double expected) {
        assertEquals(expected, getResult(input, calcEvaluator).doubleValue, 1e-6);
    }

    private void assertResult(String input, CalcEvaluator calcEvaluator, String expected) {
        assertEquals(expected, getResult(input, calcEvaluator).stringValue);
    }

    private void assertResult(String input, double expected) {
        assertEquals(expected, getResult(input).doubleValue, 1e-6);
    }

    private void assertResult(String input, String expected) {
        assertEquals(expected, getResult(input).stringValue);
    }

    private TypedValue getResult(String input) {
        CalcEvaluator calcEvaluator = new CalcEvaluator();
        return getResult(input, calcEvaluator);
    }

    private TypedValue getResult(String input, CalcEvaluator calcEvaluator) {
        CalcParser calcParser = getCalcParser(input);
        CalcParser.InputContext expression = calcParser.input();
        return expression.accept(calcEvaluator);
    }
}