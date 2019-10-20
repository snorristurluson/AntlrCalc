package calc;

import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Math.sin;

public class CalcEvaluator extends CalcBaseVisitor<TypedValue> {
    private ValueStore variables = new ValueStore();

    public void set(String name, double value) {
        variables.set(name, value);
    }

    public void set(String name, String value) {
        variables.set(name, value);
    }

    @Override
    public TypedValue visitInput(CalcParser.InputContext ctx) {
        return ctx.getChild(0).accept(this);
    }

    @Override
    public TypedValue visitExpression(CalcParser.ExpressionContext ctx) {
        TypedValue accumulator = ctx.getChild(0).accept(this);
        int childCount = ctx.getChildCount();
        for (int i = 1; i < childCount; i += 2) {
            int op = ((TerminalNode) ctx.getChild(i)).getSymbol().getType();
            TypedValue nextTermValue = ctx.getChild(i + 1).accept(this);

            if(op == CalcParser.PLUS) {
                accumulator.add(nextTermValue);
            } else if(op == CalcParser.MINUS ) {
                accumulator.subtract(nextTermValue);
            }

        }
        return accumulator;
    }

    @Override
    public TypedValue visitTerm(CalcParser.TermContext ctx) {
        ParseTree child = ctx.getChild(0);
        TypedValue accumulator = child.accept(this);
        int childCount = ctx.getChildCount();
        for (int i = 1; i < childCount; i += 2) {
            int op = ((TerminalNode) ctx.getChild(i)).getSymbol().getType();
            TypedValue nextTermValue = ctx.getChild(i + 1).accept(this);
            if(op == CalcParser.TIMES) {
                accumulator.multiply(nextTermValue);
            } else if(op == CalcParser.DIV) {
                accumulator.divide(nextTermValue);
            }

        }
        return accumulator;
    }

    @Override
    public TypedValue visitSigned_factor(CalcParser.Signed_factorContext ctx) {
        int op = ((TerminalNode) ctx.getChild(0)).getSymbol().getType();
        Double value = ctx.getChild(1).accept(this).doubleValue;
        if(op == CalcParser.MINUS) {
            value = -value;
        }
        return new TypedValue(value);
    }

    @Override
    public TypedValue visitParen_expr(CalcParser.Paren_exprContext ctx) {
        return ctx.getChild(1).accept(this);
    }

    @Override
    public TypedValue visitFunction_call(CalcParser.Function_callContext ctx) {
        String name = ctx.function_name().getText();
        List<CalcParser.ExpressionContext> arguments = ctx.expression();

        boolean functionNameKnown = false;
        int expectedArguments = -1;

        if(name.equals("sin")) {
            functionNameKnown = true;
            if(arguments.size() == 1) {
                double a0 = arguments.get(0).accept(this).doubleValue;
                return new TypedValue(sin(a0));
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
    public TypedValue visitPredicate(CalcParser.PredicateContext ctx) {
        CalcParser.ExpressionContext leftCtx = ctx.getChild(CalcParser.ExpressionContext.class, 0);
        CalcParser.ExpressionContext rightCtx = ctx.getChild(CalcParser.ExpressionContext.class, 1);
        int op = ((TerminalNode) ctx.getChild(1)).getSymbol().getType();

        TypedValue left = leftCtx.accept(this);
        TypedValue right = rightCtx.accept(this);

        return new TypedValue(left.comp(right, op));
    }

    @Override
    public TypedValue visitIf_expr(CalcParser.If_exprContext ctx) {
        CalcParser.Rel_exprContext relExprContext = ctx.getChild(CalcParser.Rel_exprContext.class, 0);
        TypedValue predicate = relExprContext.accept(this);

        if (predicate.booleanValue) {
            CalcParser.ExpressionContext thenExprContext = ctx.getChild(CalcParser.ExpressionContext.class, 0);
            return thenExprContext.accept(this);
        } else {
            CalcParser.ExpressionContext elseExprContext = ctx.getChild(CalcParser.ExpressionContext.class, 1);
            return elseExprContext.accept(this);
        }
    }

    @Override
    public TypedValue visitString_literal(CalcParser.String_literalContext ctx) {
        String text = ctx.getText();
        return new TypedValue(text.substring(1, text.length() - 1));
    }

    @Override
    public TypedValue visitNumber(CalcParser.NumberContext ctx) {
        return new TypedValue(Double.parseDouble(ctx.getText()));
    }

    @Override
    public TypedValue visitVariable(CalcParser.VariableContext ctx) {
        return new TypedValue(variables.get(ctx.getText()));
    }
}
