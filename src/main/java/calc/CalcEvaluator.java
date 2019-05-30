package calc;

import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

public class CalcEvaluator extends CalcBaseVisitor<Double> {
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
    public Double visitFactor(CalcParser.FactorContext ctx) {
        return super.visitFactor(ctx);
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
    public Double visitXfactor(CalcParser.XfactorContext ctx) {
        return super.visitXfactor(ctx);
    }

    @Override
    public Double visitParen_expr(CalcParser.Paren_exprContext ctx) {
        return ctx.getChild(1).accept(this);
    }

    @Override
    public Double visitFunction_call(CalcParser.Function_callContext ctx) {
        return super.visitFunction_call(ctx);
    }

    @Override
    public Double visitFunction_name(CalcParser.Function_nameContext ctx) {
        return super.visitFunction_name(ctx);
    }

    @Override
    public Double visitValue(CalcParser.ValueContext ctx) {
        return Double.parseDouble(ctx.getText());
    }

    @Override
    public Double visitVariable(CalcParser.VariableContext ctx) {
        return super.visitVariable(ctx);
    }
}
