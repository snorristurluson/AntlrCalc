package calc;

import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Math.sin;

public class CalcLambdaGenerator extends CalcBaseVisitor<TypedCalcLambda> {
    private Map<String, TypedValue.Type> variables = new HashMap<>();

    public void declare(String name, TypedValue.Type type) {
        variables.put(name, type);
    }

    @Override
    public TypedCalcLambda visitInput(CalcParser.InputContext ctx) {
        return ctx.getChild(0).accept(this);
    }

    @Override
    public TypedCalcLambda visitExpression(CalcParser.ExpressionContext ctx) {
        TypedCalcLambda accumulator = ctx.getChild(0).accept(this);
        int childCount = ctx.getChildCount();
        for (int i = 1; i < childCount; i += 2) {
            int op = ((TerminalNode) ctx.getChild(i)).getSymbol().getType();
            TypedCalcLambda nextTerm = ctx.getChild(i + 1).accept(this);
            if(op == CalcParser.PLUS) {
                accumulator.add(nextTerm);
            } else if(op == CalcParser.MINUS) {
                accumulator.subtract(nextTerm);
            }

        }
        return accumulator;
    }

    @Override
    public TypedCalcLambda visitTerm(CalcParser.TermContext ctx) {
        TypedCalcLambda accumulator = ctx.getChild(0).accept(this);
        int childCount = ctx.getChildCount();
        for (int i = 1; i < childCount; i += 2) {
            int op = ((TerminalNode) ctx.getChild(i)).getSymbol().getType();
            TypedCalcLambda nextTerm = ctx.getChild(i + 1).accept(this);
            if(op == CalcParser.TIMES) {
                accumulator.multiply(nextTerm);
            } else if(op == CalcParser.DIV) {
                accumulator.divide(nextTerm);
            }

        }
        return accumulator;
    }

    @Override
    public TypedCalcLambda visitParen_expr(CalcParser.Paren_exprContext ctx) {
        return ctx.getChild(1).accept(this);
    }

    @Override
    public TypedCalcLambda visitSigned_factor(CalcParser.Signed_factorContext ctx) {
        int op = ((TerminalNode) ctx.getChild(0)).getSymbol().getType();
        TypedCalcLambda f = ctx.getChild(1).accept(this);
        if(op == CalcParser.MINUS) {
            f.negate();
        }
        return f;
    }

    @Override
    public TypedCalcLambda visitFunction_call(CalcParser.Function_callContext ctx) {
        String name = ctx.function_name().getText();
        List<CalcParser.ExpressionContext> arguments = ctx.expression();

        boolean functionNameKnown = false;
        int expectedArguments = -1;

        if(name.equals("sin")) {
            functionNameKnown = true;
            if(arguments.size() == 1) {
                TypedCalcLambda a0 = arguments.get(0).accept(this);
                return new TypedCalcLambda((ValueStore vs) -> sin(a0.evaluateDouble(vs)));
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
    public TypedCalcLambda visitVariable(CalcParser.VariableContext ctx) {
        String name = ctx.getText();
        TypedValue.Type type = variables.get(name);
        if (type == null) {
            throw new RuntimeException(String.format("%s is not a recognized variable", name));
        }
        switch(type) {
            case DOUBLE:
                return new TypedCalcLambda((ValueStore vs) -> vs.get(name).doubleValue);
            case STRING:
                return new TypedCalcLambda((ValueStore vs) -> vs.get(name).stringValue);
            default:
                throw new IllegalStateException("Unexpected value: " + type);
        }
    }

    @Override
    public TypedCalcLambda visitPredicate(CalcParser.PredicateContext ctx) {
        CalcParser.ExpressionContext leftCtx = ctx.getChild(CalcParser.ExpressionContext.class, 0);
        CalcParser.ExpressionContext rightCtx = ctx.getChild(CalcParser.ExpressionContext.class, 1);
        int op = ((TerminalNode) ctx.getChild(1)).getSymbol().getType();

        TypedCalcLambda left = leftCtx.accept(this);
        TypedCalcLambda right = rightCtx.accept(this);

        return left.comp(right, op);
    }

    @Override
    public TypedCalcLambda visitIf_expr(CalcParser.If_exprContext ctx) {
        CalcParser.Rel_exprContext relExprContext = ctx.getChild(CalcParser.Rel_exprContext.class, 0);
        TypedCalcLambda predicate = relExprContext.accept(this);

        CalcParser.ExpressionContext thenExprContext = ctx.getChild(CalcParser.ExpressionContext.class, 0);
        TypedCalcLambda thenExpr = thenExprContext.accept(this);
        CalcParser.ExpressionContext elseExprContext = ctx.getChild(CalcParser.ExpressionContext.class, 1);
        TypedCalcLambda elseExpr = elseExprContext.accept(this);

        if (thenExpr.type != elseExpr.type) {
            throw new RuntimeException("Both sides of if expression must have the same type");
        }

        switch (thenExpr.type) {
            case STRING:
                return new TypedCalcLambda( (ValueStore vs) -> {
                    if (predicate.bFun.evaluate(vs)) {
                        return thenExpr.sFun.evaluate(vs);
                    } else {
                        return elseExpr.sFun.evaluate(vs);
                    }
                });
            case DOUBLE:
            case DOUBLE_CONSTANT:
                return new TypedCalcLambda( (ValueStore vs) -> {
                    if (predicate.bFun.evaluate(vs)) {
                        return thenExpr.evaluateDouble(vs);
                    } else {
                        return elseExpr.evaluateDouble(vs);
                    }
                });

            default:
                throw new RuntimeException("Unsupported type in if statement");
        }
    }

    @Override
    public TypedCalcLambda visitNumber(CalcParser.NumberContext ctx) {
        double d = Double.parseDouble(ctx.getText());
        return new TypedCalcLambda(d);
    }

    @Override
    public TypedCalcLambda visitString_literal(CalcParser.String_literalContext ctx) {
        String text = ctx.getText();
        String unqouted = text.substring(1, text.length() - 1);
        return new TypedCalcLambda((ValueStore vs) -> unqouted);
    }
}
