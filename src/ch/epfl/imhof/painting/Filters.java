package ch.epfl.imhof.painting;

import java.util.function.Predicate;
import ch.epfl.imhof.Attributed;

public final class Filters {
    private Filters() {
    }

    public static Predicate<Attributed<?>> tagged(String attributeName) {
        // return Attributed<?>::hasAttribute;
        return x -> x.hasAttribute(attributeName);
    }

    public static Predicate<Attributed<?>> tagged(String attributeName,
            String... attributeValues) {
        return x -> {
            if (x.hasAttribute(attributeName)) {
                boolean attributesValuesContainsKey = false;
                for (String s : attributeValues) {
                    attributesValuesContainsKey = attributesValuesContainsKey
                            || (s.equals(x.attributeValue(attributeName)));
                }
                return attributesValuesContainsKey;
            } else
                return false;
        };
    }

    public Predicate<Attributed<?>> isOnLayer(int layer) {
        if (layer < -5 || layer > 5)
            throw new IllegalArgumentException(
                    "the value of the attribute layer is an integer between -5 and 5");
        return x -> layer == x.attributeValue("layer", 0);

    }

}
