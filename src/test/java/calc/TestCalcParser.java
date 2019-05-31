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

    private CalcParser getCalcParser(String input) {
        CodePointCharStream stream = CharStreams.fromString(input);
        CalcLexer calcLexer = new CalcLexer(stream);
        CommonTokenStream tokenStream = new CommonTokenStream(calcLexer);
        return new CalcParser(tokenStream);
    }

}
