package calc;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CodePointCharStream;
import org.antlr.v4.runtime.CommonTokenStream;

import java.util.Random;

import static java.lang.Math.abs;
import static java.lang.Math.sin;

public class CalcBenchmark {

    private static final int NUM_VALUES = 10000000;

    public static void main(String[] args) {
        String expressionSource = "123.456+x*sin(y+3.0/4+1.2)*0.25*3";

        Random random = new Random();
        double[] xValues = new double[NUM_VALUES];
        double[] yValues = new double[NUM_VALUES];
        double[] expectedResults = new double[NUM_VALUES];
        for (int i = 0; i < NUM_VALUES; i++) {
            xValues[i] = random.nextDouble();
            yValues[i] = random.nextDouble();
            double x = xValues[i];
            double y = yValues[i];
            expectedResults[i] = 123.456+x*sin(y+3.0/4+1.2)*0.25*3;
        }

        CalcParser calcParser = getCalcParser(expressionSource);
        CalcParser.ExpressionContext expression = calcParser.expression();

        long eval = timeCalcEvaluator(expression, xValues, yValues, expectedResults);
        long lambda = timeCalcLambdaGenerator(expression, xValues, yValues, expectedResults);
        System.out.println(eval);
        System.out.println(lambda);
    }

    private static long timeCalcEvaluator(CalcParser.ExpressionContext expression, double[] xValues, double[] yValues, double[] expectedResults) {
        long startTime = System.currentTimeMillis();
        CalcEvaluator calcEvaluator = new CalcEvaluator();
        for (int i = 0; i < NUM_VALUES; i++) {
            calcEvaluator.set("x", xValues[i]);
            calcEvaluator.set("y", yValues[i]);
            double result = expression.accept(calcEvaluator).doubleValue;
            if(abs(expectedResults[i]-result) > 1e-6) {
                throw new RuntimeException("Bad result");
            }
        }
        long endTime = System.currentTimeMillis();
        return endTime - startTime;
    }

    private static long timeCalcLambdaGenerator(CalcParser.ExpressionContext expression, double[] xValues, double[] yValues, double[] expectedResults) {
        long startTime = System.currentTimeMillis();
        CalcLambdaGenerator lambdaGenerator = new CalcLambdaGenerator();
        lambdaGenerator.declare("x", TypedValue.Type.DOUBLE);
        lambdaGenerator.declare("y", TypedValue.Type.DOUBLE);
        TypedCalcLambda calcLambda = expression.accept(lambdaGenerator);
        ValueStore vs = new ValueStore();
        for (int i = 0; i < NUM_VALUES; i++) {
            vs.set("x", xValues[i]);
            vs.set("y", yValues[i]);
            double result = calcLambda.dFun.evaluate(vs);
            if(abs(expectedResults[i]-result) > 1e-6) {
                throw new RuntimeException("Bad result");
            }
        }
        long endTime = System.currentTimeMillis();
        return endTime - startTime;
    }

    private static CalcParser getCalcParser(String input) {
        CodePointCharStream stream = CharStreams.fromString(input);
        CalcLexer calcLexer = new CalcLexer(stream);
        CommonTokenStream tokenStream = new CommonTokenStream(calcLexer);
        return new CalcParser(tokenStream);
    }
}
