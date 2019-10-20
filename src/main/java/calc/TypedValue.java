package calc;

public class TypedValue {
    enum Type {
        DOUBLE,
        STRING,
        BOOLEAN
    }

    Type type;
    String stringValue;
    double doubleValue;
    boolean booleanValue;

    TypedValue(TypedValue other) {
        type = other.type;
        stringValue = other.stringValue;
        doubleValue = other.doubleValue;
        booleanValue = other.booleanValue;
    }

    TypedValue(double d) {
        type = Type.DOUBLE;
        doubleValue = d;
    }

    TypedValue(String s) {
        type = Type.STRING;
        stringValue = s;
    }

    TypedValue(boolean b) {
        type = Type.BOOLEAN;
        booleanValue = b;
    }

    void add(TypedValue other) {
        if (type == Type.STRING && other.type == Type.STRING) {
            stringValue += other.stringValue;
        } else if (type == Type.DOUBLE && other.type == Type.DOUBLE) {
            doubleValue += other.doubleValue;
        } else {
            throw new RuntimeException("Incompatible types for add");
        }
    }

    void subtract(TypedValue other) {
        if (type == Type.DOUBLE && other.type == Type.DOUBLE) {
            doubleValue -= other.doubleValue;
        } else {
            throw new RuntimeException("Incompatible types for subtract");
        }
    }

    void multiply(TypedValue other) {
        if (type == Type.DOUBLE && other.type == Type.DOUBLE) {
            doubleValue *= other.doubleValue;
        } else {
            throw new RuntimeException("Incompatible types for multiply");
        }
    }

    void divide(TypedValue other) {
        if (type == Type.DOUBLE && other.type == Type.DOUBLE) {
            doubleValue /= other.doubleValue;
        } else {
            throw new RuntimeException("Incompatible types for divide");
        }
    }

    boolean comp(TypedValue other, int op) {
        if (type == Type.STRING && other.type == Type.STRING) {
            if (op == CalcParser.EQ) {
                return stringValue.equals(other.stringValue);
            } else if (op == CalcParser.NE) {
                return !stringValue.equals(other.stringValue);
            } else if (op == CalcParser.GT) {
                return stringValue.compareTo(other.stringValue) > 0;
            } else if (op == CalcParser.GE) {
                return stringValue.compareTo(other.stringValue) >= 0;
            } else if (op == CalcParser.LT) {
                return stringValue.compareTo(other.stringValue) < 0;
            } else if (op == CalcParser.LE) {
                return stringValue.compareTo(other.stringValue) <= 0;
            }
        } else if (type == Type.DOUBLE && other.type == Type.DOUBLE) {
            if (op == CalcParser.EQ) {
                return doubleValue == other.doubleValue;
            } else if (op == CalcParser.NE) {
                return doubleValue != other.doubleValue;
            } else if (op == CalcParser.GT) {
                return doubleValue > other.doubleValue;
            } else if (op == CalcParser.GE) {
                return doubleValue >= other.doubleValue;
            } else if (op == CalcParser.LT) {
                return doubleValue < other.doubleValue;
            } else if (op == CalcParser.LE) {
                return doubleValue <= other.doubleValue;
            }
        }
        throw new RuntimeException("Incompatible types for compare");
    }

    void add(String s) {
        if (type == Type.STRING) {
            stringValue += s;
        } else {
            throw new RuntimeException("Can't add string to number");
        }
    }

    void add(double d) {
        if (type == Type.DOUBLE) {
            doubleValue += d;
        } else {
            throw new RuntimeException("Can't add number to string");
        }
    }
}
