package calc;

public class TypedCalcLambda {
    TypedValue.Type type;

    CalcLambdaDouble dFun;
    double dConst;
    CalcLambdaString sFun;
    String sConst;
    CalcLambdaBoolean bFun;

    TypedCalcLambda(CalcLambdaDouble d) {
        type = TypedValue.Type.DOUBLE;
        dFun = d;
    }

    TypedCalcLambda(double d) {
        type = TypedValue.Type.DOUBLE_CONSTANT;
        dConst = d;
    }

    TypedCalcLambda(CalcLambdaString s) {
        type = TypedValue.Type.STRING;
        sFun = s;
    }

    TypedCalcLambda(CalcLambdaBoolean b) {
        type = TypedValue.Type.BOOLEAN;
        bFun = b;
    }

    double evaluateDouble(ValueStore values) {
        switch (type) {
            case DOUBLE:
                return dFun.evaluate(values);
            case DOUBLE_CONSTANT:
                return dConst;

            default:
                throw new RuntimeException("Invalid type");
        }
    }

    String evaluateString(ValueStore values) {
        return sFun.evaluate(values);
    }

    void add(TypedCalcLambda other) {
        if (type == TypedValue.Type.DOUBLE ) {
            CalcLambdaDouble prev = this.dFun;
            if (other.type == TypedValue.Type.DOUBLE) {
                dFun = vs -> prev.evaluate(vs) + other.dFun.evaluate(vs);
            } else if (other.type == TypedValue.Type.DOUBLE_CONSTANT) {
                double d = other.dConst;
                dFun = vs -> prev.evaluate(vs) + d;
            }
        } else if (type == TypedValue.Type.DOUBLE_CONSTANT) {
            if (other.type == TypedValue.Type.DOUBLE) {
                double d = dConst;
                dFun = vs -> other.dFun.evaluate(vs) + d;
                type = TypedValue.Type.DOUBLE;
            } else if (other.type == TypedValue.Type.DOUBLE_CONSTANT) {
                dConst += other.dConst;
            }
        } else if (type == TypedValue.Type.STRING && other.type == TypedValue.Type.STRING) {
            CalcLambdaString prev = this.sFun;
            sFun = vs -> prev.evaluate(vs) + other.sFun.evaluate(vs);
        } else {
            throw new RuntimeException("Incompatible types for add");
        }
    }

    void subtract(TypedCalcLambda other) {
        if (type == TypedValue.Type.DOUBLE ) {
            CalcLambdaDouble prev = this.dFun;
            if (other.type == TypedValue.Type.DOUBLE) {
                dFun = vs -> prev.evaluate(vs) - other.dFun.evaluate(vs);
            } else if (other.type == TypedValue.Type.DOUBLE_CONSTANT) {
                double d = other.dConst;
                dFun = vs -> prev.evaluate(vs) - d;
            }
        } else if (type == TypedValue.Type.DOUBLE_CONSTANT) {
            if (other.type == TypedValue.Type.DOUBLE) {
                double d = dConst;
                dFun = vs -> other.dFun.evaluate(vs) - d;
                type = TypedValue.Type.DOUBLE;
            } else if (other.type == TypedValue.Type.DOUBLE_CONSTANT) {
                dConst -= other.dConst;
            }
        } else {
            throw new RuntimeException("Incompatible types for subtract");
        }
    }

    void multiply(TypedCalcLambda other) {
        if (type == TypedValue.Type.DOUBLE ) {
            CalcLambdaDouble prev = this.dFun;
            if (other.type == TypedValue.Type.DOUBLE) {
                dFun = vs -> prev.evaluate(vs) * other.dFun.evaluate(vs);
            } else if (other.type == TypedValue.Type.DOUBLE_CONSTANT) {
                double d = other.dConst;
                dFun = vs -> prev.evaluate(vs) * d;
            }
        } else if (type == TypedValue.Type.DOUBLE_CONSTANT) {
            if (other.type == TypedValue.Type.DOUBLE) {
                double d = dConst;
                dFun = vs -> other.dFun.evaluate(vs) * d;
                type = TypedValue.Type.DOUBLE;
            } else if (other.type == TypedValue.Type.DOUBLE_CONSTANT) {
                dConst *= other.dConst;
            }
        } else {
            throw new RuntimeException("Incompatible types for multiply");
        }
    }

    void divide(TypedCalcLambda other) {
        if (type == TypedValue.Type.DOUBLE ) {
            CalcLambdaDouble prev = this.dFun;
            if (other.type == TypedValue.Type.DOUBLE) {
                dFun = vs -> prev.evaluate(vs) / other.dFun.evaluate(vs);
            } else if (other.type == TypedValue.Type.DOUBLE_CONSTANT) {
                double d = other.dConst;
                dFun = vs -> prev.evaluate(vs) / d;
            }
        } else if (type == TypedValue.Type.DOUBLE_CONSTANT) {
            if (other.type == TypedValue.Type.DOUBLE) {
                double d = dConst;
                dFun = vs -> other.dFun.evaluate(vs) / d;
                type = TypedValue.Type.DOUBLE;
            } else if (other.type == TypedValue.Type.DOUBLE_CONSTANT) {
                dConst /= other.dConst;
            }
        } else {
            throw new RuntimeException("Incompatible types for divide");
        }
    }

    void negate() {
        if (type == TypedValue.Type.DOUBLE) {
            CalcLambdaDouble prev = this.dFun;
            dFun = vs -> -prev.evaluate(vs);
        } else if (type == TypedValue.Type.DOUBLE_CONSTANT) {
            dConst = -dConst;
        } else {
            throw new RuntimeException("Incompatible types for negate");
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
        } else if ((type == TypedValue.Type.DOUBLE || type == TypedValue.Type.DOUBLE_CONSTANT) &&
                (other.type == TypedValue.Type.DOUBLE || other.type == TypedValue.Type.DOUBLE_CONSTANT)) {
            switch (op) {
                case CalcParser.EQ:
                    return (new TypedCalcLambda((ValueStore vs) -> evaluateDouble(vs) == (other.evaluateDouble(vs))));
                case CalcParser.NE:
                    return (new TypedCalcLambda((ValueStore vs) -> evaluateDouble(vs) != (other.evaluateDouble(vs))));
                case CalcParser.GT:
                    return (new TypedCalcLambda((ValueStore vs) -> evaluateDouble(vs) > (other.evaluateDouble(vs))));
                case CalcParser.GE:
                    return (new TypedCalcLambda((ValueStore vs) -> evaluateDouble(vs) >= (other.evaluateDouble(vs))));
                case CalcParser.LT:
                    return (new TypedCalcLambda((ValueStore vs) -> evaluateDouble(vs) < (other.evaluateDouble(vs))));
                case CalcParser.LE:
                    return (new TypedCalcLambda((ValueStore vs) -> evaluateDouble(vs) <= (other.evaluateDouble(vs))));
                default:
                    throw new RuntimeException("Unknown operator");
            }
        }
        throw new RuntimeException("Incompatible types for compare");
    }

}
