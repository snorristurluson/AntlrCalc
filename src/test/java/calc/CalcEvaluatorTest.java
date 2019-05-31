package calc;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CodePointCharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.junit.Assert;
import org.junit.Test;

public class CalcEvaluatorTest {
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


    private void assertResult(String input, CalcEvaluator calcEvaluator, double expected) {
        Assert.assertEquals(expected, getResult(input, calcEvaluator), 1e-6);
    }
    private void assertResult(String input, double expected) {
        Assert.assertEquals(expected, getResult(input), 1e-6);
    }

    private double getResult(String input) {
        CalcEvaluator calcEvaluator = new CalcEvaluator();
        return getResult(input, calcEvaluator);
    }

    private double getResult(String input, CalcEvaluator calcEvaluator) {
        CalcParser calcParser = getCalcParser(input);
        CalcParser.ExpressionContext expression = calcParser.expression();
        return expression.accept(calcEvaluator);
    }

    private CalcParser getCalcParser(String input) {
        CodePointCharStream stream = CharStreams.fromString(input);
        CalcLexer calcLexer = new CalcLexer(stream);
        CommonTokenStream tokenStream = new CommonTokenStream(calcLexer);
        return new CalcParser(tokenStream);
    }
}