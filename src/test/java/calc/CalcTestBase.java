package calc;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CodePointCharStream;
import org.antlr.v4.runtime.CommonTokenStream;

class CalcTestBase {
    CalcParser getCalcParser(String input) {
        CodePointCharStream stream = CharStreams.fromString(input);
        CalcLexer calcLexer = new CalcLexer(stream);
        CommonTokenStream tokenStream = new CommonTokenStream(calcLexer);
        return new CalcParser(tokenStream);
    }
}
