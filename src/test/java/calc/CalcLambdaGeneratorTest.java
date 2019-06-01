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
    public void testVariableOnly() {
        CalcLambda f = compileLambda("x");

        ValueStore valueStore = new ValueStore();
        valueStore.set("x", 4);
        assertEquals(4, f.evaluate(valueStore), 1e-6);
        valueStore.set("x", 3.14);
        assertEquals(3.14, f.evaluate(valueStore), 1e-6);
    }

    @Test
    public void testVariablesInSimpleExpression() {
        CalcLambda f = compileLambda("x+y-z");

        ValueStore vs = new ValueStore();
        vs.set("x", 3);
        vs.set("y", 4);
        vs.set("z", 6);
        assertEquals(1, f.evaluate(vs), 1e-6);

        vs.set("x", 5);
        vs.set("y", 2);
        vs.set("z", 3);
        assertEquals(4, f.evaluate(vs), 1e-6);
    }

    @Test
    public void testFunctionCall() {
        CalcLambda f = compileLambda("sin(0.5)");
        assertEquals(0.479425539, f.evaluate(null), 1e-6);
    }

    private CalcLambda compileLambda(String input) {
        CalcParser calcParser = getCalcParser(input);
        CalcParser.ExpressionContext expression = calcParser.expression();
        CalcLambdaGenerator lambdaGen = new CalcLambdaGenerator();
        return expression.accept(lambdaGen);
    }

    private void assertResult(String input, double expected) {
        CalcLambda f = compileLambda(input);
        assertEquals(expected, f.evaluate(null), 1e-6);
    }

}