package calc;

import org.junit.Test;

import static org.junit.Assert.*;

public class CalcLambdaGeneratorTest extends CalcTestBase {
    @Test
    public void testNumbersOnly() {
        assertResult("4", 4);
        assertResult("3.14", 3.14);
    }

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
    public void testVariableOnly() {
        CalcParser calcParser = getCalcParser("x");
        CalcParser.ExpressionContext expression = calcParser.expression();
        CalcLambdaGenerator lambdaGen = new CalcLambdaGenerator();
        lambdaGen.declare("x", TypedValue.Type.DOUBLE);
        TypedCalcLambda f = expression.accept(lambdaGen);

        ValueStore valueStore = new ValueStore();
        valueStore.set("x", 4);
        assertEquals(4, f.evaluateDouble(valueStore), 1e-6);
        valueStore.set("x", 3.14);
        assertEquals(3.14, f.evaluateDouble(valueStore), 1e-6);
    }

    @Test
    public void testVariablesInSimpleExpression() {
        CalcParser calcParser = getCalcParser("x+y-z");
        CalcParser.ExpressionContext expression = calcParser.expression();
        CalcLambdaGenerator lambdaGen = new CalcLambdaGenerator();
        lambdaGen.declare("x", TypedValue.Type.DOUBLE);
        lambdaGen.declare("y", TypedValue.Type.DOUBLE);
        lambdaGen.declare("z", TypedValue.Type.DOUBLE);
        TypedCalcLambda f = expression.accept(lambdaGen);

        ValueStore vs = new ValueStore();
        vs.set("x", 3);
        vs.set("y", 4);
        vs.set("z", 6);
        assertEquals(1, f.evaluateDouble(vs), 1e-6);

        vs.set("x", 5);
        vs.set("y", 2);
        vs.set("z", 3);
        assertEquals(4, f.evaluateDouble(vs), 1e-6);
    }

    @Test
    public void testFunctionCall() {
        TypedCalcLambda f = compileLambda("sin(0.5)");
        assertEquals(0.479425539, f.evaluateDouble(null), 1e-6);
    }

    @Test
    public void testIfStatement() {
        assertResult("if(1>0, 'true', 'false')", "true");
        assertResult("if(1>0, 1, 0)", 1);
    }

    private TypedCalcLambda compileLambda(String input) {
        CalcParser calcParser = getCalcParser(input);
        CalcParser.InputContext expression = calcParser.input();
        CalcLambdaGenerator lambdaGen = new CalcLambdaGenerator();
        return expression.accept(lambdaGen);
    }

    private void assertResult(String input, double expected) {
        TypedCalcLambda f = compileLambda(input);
        assertEquals(expected, f.evaluateDouble(null), 1e-6);
    }

    private void assertResult(String input, String expected) {
        TypedCalcLambda f = compileLambda(input);
        assertEquals(expected, f.evaluateString(null));
    }

}