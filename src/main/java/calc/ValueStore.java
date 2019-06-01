package calc;

import java.util.HashMap;
import java.util.Map;

class ValueStore {
    private Map<String, Double> values = new HashMap<>();

    void set(String name, double value) {
        values.put(name, value);
    }

    double get(String name) {
        return values.get(name);
    }
}
