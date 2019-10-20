package calc;

public class TypedCalcLambda {
    TypedValue.Type type;

    CalcLambdaDouble dFun;
    CalcLambdaString sFun;
    CalcLambdaBoolean bFun;

    TypedCalcLambda(CalcLambdaDouble d) {
        type = TypedValue.Type.DOUBLE;
        dFun = d;
    }

    TypedCalcLambda(CalcLambdaString s) {
        type = TypedValue.Type.STRING;
        sFun = s;
    }

    TypedCalcLambda(CalcLambdaBoolean b) {
        type = TypedValue.Type.BOOLEAN;
        bFun = b;
    }

    void add(TypedCalcLambda other) {
        if (type == TypedValue.Type.STRING && other.type == TypedValue.Type.STRING) {
            CalcLambdaString prev = this.sFun;
            sFun = vs -> prev.evaluate(vs) + other.sFun.evaluate(vs);
        } else if (type == TypedValue.Type.DOUBLE && other.type == TypedValue.Type.DOUBLE) {
            CalcLambdaDouble prev = this.dFun;
            this.dFun = vs -> prev.evaluate(vs) + other.dFun.evaluate(vs);
        } else {
            throw new RuntimeException("Incompatible types for add");
        }
    }

    void subtract(TypedCalcLambda other) {
        if (type == TypedValue.Type.DOUBLE && other.type == TypedValue.Type.DOUBLE) {
            CalcLambdaDouble prev = this.dFun;
            dFun = vs -> prev.evaluate(vs) - other.dFun.evaluate(vs);
        } else {
            throw new RuntimeException("Incompatible types for add");
        }
    }

    void multiply(TypedCalcLambda other) {
        if (type == TypedValue.Type.DOUBLE && other.type == TypedValue.Type.DOUBLE) {
            CalcLambdaDouble prev = this.dFun;
            dFun = vs -> prev.evaluate(vs) * other.dFun.evaluate(vs);
        } else {
            throw new RuntimeException("Incompatible types for add");
        }
    }

    void divide(TypedCalcLambda other) {
        if (type == TypedValue.Type.DOUBLE && other.type == TypedValue.Type.DOUBLE) {
            CalcLambdaDouble prev = this.dFun;
            dFun = vs -> prev.evaluate(vs) / other.dFun.evaluate(vs);
        } else {
            throw new RuntimeException("Incompatible types for add");
        }
    }

    void negate() {
        if (type == TypedValue.Type.DOUBLE) {
            CalcLambdaDouble prev = this.dFun;
            dFun = vs -> -prev.evaluate(vs);
        } else {
            throw new RuntimeException("Incompatible types for add");
        }
    }

    TypedCalcLambda comp(TypedCalcLambda other, int op) {
        if (type == TypedValue.Type.STRING && other.type == TypedValue.Type.STRING) {
            switch (op) {
                case CalcParser.EQ:
                    return (new TypedCalcLambda((ValueStore vs) -> sFun.evaluate(vs).equals(other.sFun.evaluate(vs))));
                case CalcParser.NE:
                    return (new TypedCalcLambda((ValueStore vs) -> !sFun.evaluate(vs).equals(other.sFun.evaluate(vs))));
                case CalcParser.GT:
                    return (new TypedCalcLambda((ValueStore vs) -> sFun.evaluate(vs).compareTo(other.sFun.evaluate(vs)) > 0));
                case CalcParser.GE:
                    return (new TypedCalcLambda((ValueStore vs) -> sFun.evaluate(vs).compareTo(other.sFun.evaluate(vs)) >= 0));
                case CalcParser.LT:
                    return (new TypedCalcLambda((ValueStore vs) -> sFun.evaluate(vs).compareTo(other.sFun.evaluate(vs)) < 0));
                case CalcParser.LE:
                    return (new TypedCalcLambda((ValueStore vs) -> sFun.evaluate(vs).compareTo(other.sFun.evaluate(vs)) <= 0));
                default:
                    throw new RuntimeException("Unknown operator");
            }
        } else if (type == TypedValue.Type.DOUBLE && other.type == TypedValue.Type.DOUBLE) {
            switch (op) {
                case CalcParser.EQ:
                    return (new TypedCalcLambda((ValueStore vs) -> dFun.evaluate(vs) == (other.dFun.evaluate(vs))));
                case CalcParser.NE:
                    return (new TypedCalcLambda((ValueStore vs) -> dFun.evaluate(vs) != (other.dFun.evaluate(vs))));
                case CalcParser.GT:
                    return (new TypedCalcLambda((ValueStore vs) -> dFun.evaluate(vs) > (other.dFun.evaluate(vs))));
                case CalcParser.GE:
                    return (new TypedCalcLambda((ValueStore vs) -> dFun.evaluate(vs) >= (other.dFun.evaluate(vs))));
                case CalcParser.LT:
                    return (new TypedCalcLambda((ValueStore vs) -> dFun.evaluate(vs) < (other.dFun.evaluate(vs))));
                case CalcParser.LE:
                    return (new TypedCalcLambda((ValueStore vs) -> dFun.evaluate(vs) <= (other.dFun.evaluate(vs))));
                default:
                    throw new RuntimeException("Unknown operator");
            }
        }
        throw new RuntimeException("Incompatible types for compare");
    }

}
