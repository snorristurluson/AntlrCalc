package calc;

import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.List;

import static java.lang.Math.sin;

public class CalcLambdaGenerator extends CalcBaseVisitor<CalcLambda> {
    @Override
    public CalcLambda visitExpression(CalcParser.ExpressionContext ctx) {
        CalcLambda accumulator = ctx.getChild(0).accept(this);
        int childCount = ctx.getChildCount();
        for (int i = 1; i < childCount; i += 2) {
            CalcLambda current = accumulator;
            int op = ((TerminalNode) ctx.getChild(i)).getSymbol().getType();
            CalcLambda nextTerm = ctx.getChild(i + 1).accept(this);
            if(op == CalcParser.PLUS) {
                accumulator = (ValueStore vs) -> current.evaluate(vs) + nextTerm.evaluate(vs);
            } else if(op == CalcParser.MINUS) {
                accumulator = (ValueStore vs) -> current.evaluate(vs) - nextTerm.evaluate(vs);
            }

        }
        return accumulator;
    }

    @Override
    public CalcLambda visitTerm(CalcParser.TermContext ctx) {
        CalcLambda accumulator = ctx.getChild(0).accept(this);
        int childCount = ctx.getChildCount();
        for (int i = 1; i < childCount; i += 2) {
            CalcLambda current = accumulator;
            int op = ((TerminalNode) ctx.getChild(i)).getSymbol().getType();
            CalcLambda nextTerm = ctx.getChild(i + 1).accept(this);
            if(op == CalcParser.TIMES) {
                accumulator = (ValueStore vs) -> current.evaluate(vs) * nextTerm.evaluate(vs);
            } else if(op == CalcParser.DIV) {
                accumulator = (ValueStore vs) -> current.evaluate(vs) / nextTerm.evaluate(vs);
            }

        }
        return accumulator;
    }

    @Override
    public CalcLambda visitParen_expr(CalcParser.Paren_exprContext ctx) {
        return ctx.getChild(1).accept(this);
    }

    @Override
    public CalcLambda visitSigned_factor(CalcParser.Signed_factorContext ctx) {
        int op = ((TerminalNode) ctx.getChild(0)).getSymbol().getType();
        CalcLambda f = ctx.getChild(1).accept(this);
        if(op == CalcParser.MINUS) {
            return (ValueStore vs) -> -f.evaluate(vs);
        }
        return f;
    }

    @Override
    public CalcLambda visitFunction_call(CalcParser.Function_callContext ctx) {
        String name = ctx.function_name().getText();
        List<CalcParser.ExpressionContext> arguments = ctx.expression();

        boolean functionNameKnown = false;
        int expectedArguments = -1;

        if(name.equals("sin")) {
            functionNameKnown = true;
            if(arguments.size() == 1) {
                CalcLambda a0 = arguments.get(0).accept(this);
                return (ValueStore vs) -> sin(a0.evaluate(vs));
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
    public CalcLambda visitVariable(CalcParser.VariableContext ctx) {
        return (ValueStore vs) -> vs.get(ctx.getText());
    }

    @Override
    public CalcLambda visitNumber(CalcParser.NumberContext ctx) {
        return (ValueStore vs) -> Double.parseDouble(ctx.getText());
    }
}
