package calc;

import java.util.HashMap;
import java.util.Map;

class ValueStore {
    private Map<String, TypedValue> values = new HashMap<>();

    void set(String name, double value) {
        values.put(name, new TypedValue(value));
    }

    void set(String name, String value) {
        values.put(name, new TypedValue(value));
    }

    TypedValue get(String name) {
        return values.get(name);
    }
}
