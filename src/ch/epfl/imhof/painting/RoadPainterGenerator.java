package ch.epfl.imhof.painting;

import java.util.function.Predicate;
import static ch.epfl.imhof.painting.LineStyle.LineCap;
import static ch.epfl.imhof.painting.LineStyle.LineJoin;

import ch.epfl.imhof.Attributed;
import ch.epfl.imhof.geometry.PolyLine;

public final class RoadPainterGenerator {
    private RoadPainterGenerator() {
    };

    public static Painter<?> painterForRoads(RoadSpec spec) {
        LineStyle intBridge = new LineStyle(spec.getwI(), spec.getcI(),
                LineCap.ROUND, LineJoin.ROUND, null);
        LineStyle coBridge = new LineStyle(spec.getwI() + 2 * spec.getwC(),
                spec.getcC(), LineCap.BUTT, LineJoin.ROUND, null);
        LineStyle intNormRoad = intBridge;
        LineStyle coNormRoad = coBridge.withCap(LineCap.ROUND);
        LineStyle intTunnel = new LineStyle(spec.getwI() / 2, spec.getcC(),
                LineCap.BUTT, LineJoin.ROUND, 2 * spec.getwI(),
                2 * spec.getwI());

    }

}
