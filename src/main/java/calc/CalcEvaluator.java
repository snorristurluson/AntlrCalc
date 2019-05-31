package calc;

import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Math.sin;

public class CalcEvaluator extends CalcBaseVisitor<Double> {
    private Map<String, Double> variables = new HashMap<>();

    public void set(String name, double value) {
        variables.put(name, value);
    }

    @Override
    public Double visitExpression(CalcParser.ExpressionContext ctx) {
        double accumulator = ctx.getChild(0).accept(this);
        int childCount = ctx.getChildCount();
        for (int i = 1; i < childCount; i += 2) {
            int op = ((TerminalNode) ctx.getChild(i)).getSymbol().getType();
            Double nextTermValue = ctx.getChild(i + 1).accept(this);
            if(op == CalcParser.PLUS) {
                accumulator += nextTermValue;
            } else if(op == CalcParser.MINUS) {
                accumulator -= nextTermValue;
            }

        }
        return accumulator;
    }

    @Override
    public Double visitTerm(CalcParser.TermContext ctx) {
        ParseTree child = ctx.getChild(0);
        double accumulator = child.accept(this);
        int childCount = ctx.getChildCount();
        for (int i = 1; i < childCount; i += 2) {
            int op = ((TerminalNode) ctx.getChild(i)).getSymbol().getType();
            Double nextTermValue = ctx.getChild(i + 1).accept(this);
            if(op == CalcParser.TIMES) {
                accumulator *= nextTermValue;
            } else if(op == CalcParser.DIV) {
                accumulator /= nextTermValue;
            }

        }
        return accumulator;
    }

    @Override
    public Double visitSigned_factor(CalcParser.Signed_factorContext ctx) {
        int op = ((TerminalNode) ctx.getChild(0)).getSymbol().getType();
        Double value = ctx.getChild(1).accept(this);
        if(op == CalcParser.MINUS) {
            value = -value;
        }
        return value;
    }

    @Override
    public Double visitParen_expr(CalcParser.Paren_exprContext ctx) {
        return ctx.getChild(1).accept(this);
    }

    @Override
    public Double visitFunction_call(CalcParser.Function_callContext ctx) {
        String name = ctx.function_name().getText();
        List<CalcParser.ExpressionContext> arguments = ctx.expression();

        boolean functionNameKnown = false;
        int expectedArguments = -1;

        if(name.equals("sin")) {
            functionNameKnown = true;
            if(arguments.size() == 1) {
                double a0 = arguments.get(0).accept(this);
                return sin(a0);
            } else {
                expectedArguments = 1;
            }
        }
        if(!functionNameKnown) {
            throw new RuntimeException(String.format("Unknown function: %s", name));
        }
        if(arguments.size() < expectedArguments) {
            throw new RuntimeException(String.format("Not enough arguments for %s", name));
        }
        throw new RuntimeException(String.format("Too many arguments for %s", name));
    }

    @Override
    public Double visitNumber(CalcParser.NumberContext ctx) {
        return Double.parseDouble(ctx.getText());
    }

    @Override
    public Double visitVariable(CalcParser.VariableContext ctx) {
        return variables.get(ctx.getText());
    }
}
