package ch.epfl.imhof.painting;

import java.util.function.Predicate;

import ch.epfl.imhof.Attributed;
import ch.epfl.imhof.painting.LineStyle.LineCap;
import ch.epfl.imhof.painting.LineStyle.LineJoin;

public final class RoadPainterGenerator {
    private RoadPainterGenerator() {
    }

    public static Painter<?> painterForRoads(RoadSpec spec) {
        LineStyle intBridge = new LineStyle(spec.getwI(), spec.getcI(),
                LineCap.ROUND, LineJoin.ROUND, null);
        LineStyle coBridge = new LineStyle(spec.getwI() + 2 * spec.getwC(),
                spec.getcC(), LineCap.BUTT, LineJoin.ROUND, null);
        LineStyle coNormRoad = coBridge.withCap(LineCap.ROUND);
        LineStyle intTunnel = new LineStyle(spec.getwI() / 2f, spec.getcC(),
                LineCap.BUTT, LineJoin.ROUND, 2 * spec.getwI(),
                2 * spec.getwI());

        Painter<?> tunnelPainter = Painter.line(intTunnel).when(
                Filters.tagged("tunnel").and(spec.getFilter()));
        Painter<?> intBridgePainter = Painter.line(intBridge).when(
                Filters.tagged("bridge").and(spec.getFilter()));
        Painter<?> coBridgePainter = Painter.line(coBridge).when(
                Filters.tagged("bridge").and(spec.getFilter()));
        Painter<?> coNormRoadPainter = Painter.line(coNormRoad).when(
                Filters.tagged("bridge").negate()
                        .and(Filters.tagged("tunnel").negate())
                        .and(spec.getFilter()));

        return intBridgePainter.above(coBridgePainter.above(intBridgePainter
                .above(coNormRoadPainter.above(tunnelPainter))));
    }

    public final static class RoadSpec {
        private final Predicate<Attributed<?>> filter;
        private final float wI;
        private final Color cI;
        private final float wC;
        private final Color cC;

        public RoadSpec(Predicate<Attributed<?>> filter, float wI, Color cI,
                float wC, Color cC) {
            this.filter = filter;
            this.wI = wI;
            this.cI = cI;
            this.wC = wC;
            this.cC = cC;
        }

        /**
         * @return the filter
         */
        public Predicate<Attributed<?>> getFilter() {
            return filter;
        }

        /**
         * @return the wI
         */
        public float getwI() {
            return wI;
        }

        /**
         * @return the cI
         */
        public Color getcI() {
            return cI;
        }

        /**
         * @return the wC
         */
        public float getwC() {
            return wC;
        }

        /**
         * @return the cC
         */
        public Color getcC() {
            return cC;
        }
    }
}
