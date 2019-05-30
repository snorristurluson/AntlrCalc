package calc;

import org.antlr.v4.runtime.CodePointCharStream;
import org.junit.Assert;
import org.junit.Test;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

public class TestCalcParser {
    @Test
    public void test() {
        CalcParser calcParser = getCalcParser("4+3");
        CalcParser.ExpressionContext expression = calcParser.expression();
        Assert.assertEquals("4+3", expression.getText());
    }

    @Test
    public void testWhitespace() {
        CalcParser calcParser = getCalcParser("  4  +3   ");
        CalcParser.ExpressionContext expression = calcParser.expression();
        Assert.assertEquals("4+3", expression.getText());
    }

    @Test
    public void testVisitor() {
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

    private void assertResult(String input, int expected) {
        Assert.assertEquals(expected, getResult(input), 1e-6);
    }

    private double getResult(String input) {
        CalcParser calcParser = getCalcParser(input);
        CalcParser.ExpressionContext expression = calcParser.expression();
        CalcEvaluator calcEvaluator = new CalcEvaluator();
        return expression.accept(calcEvaluator);
    }


    private CalcParser getCalcParser(String input) {
        CodePointCharStream stream = CharStreams.fromString(input);
        CalcLexer calcLexer = new CalcLexer(stream);
        CommonTokenStream tokenStream = new CommonTokenStream(calcLexer);
        return new CalcParser(tokenStream);
    }

}
