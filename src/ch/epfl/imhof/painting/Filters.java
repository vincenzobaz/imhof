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
                for (String s : attributeValues) {
                    if (s.equals(x.attributeValue(attributeName)))
                        return true;
                }
            }
            return false;
        };
    }

    public static Predicate<Attributed<?>> isOnLayer(int layer) {
        if (layer < -5 || layer > 5)
            throw new IllegalArgumentException(
                    "the value of the attribute layer is an integer between -5 and 5");
        return x -> layer == x.attributeValue("layer", 0);

    }

}
