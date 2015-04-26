package ch.epfl.imhof.painting;

import java.util.function.Predicate;

import ch.epfl.imhof.Attributed;
import ch.epfl.imhof.painting.LineStyle.LineCap;
import ch.epfl.imhof.painting.LineStyle.LineJoin;

public final class RoadPainterGenerator {
    private RoadPainterGenerator() {
    }

    public static Painter<?> painterForRoads(RoadSpec... roadSpecs) {
        LineStyle defaultBridgeCasingAndTunnel = new LineStyle(0f, Color.WHITE,
                LineCap.BUTT, LineJoin.ROUND, null);
        LineStyle defaultBridgeInteriorAndRoad = new LineStyle(0f, Color.WHITE,
                LineCap.ROUND, LineJoin.ROUND, null);

        Painter<?> roadsPainter = Painter.line(0, Color.WHITE).when(
                Filters.tagged("not_really"));

        for (RoadSpec spec : roadSpecs) {
            LineStyle bridgeAndRoadInterior = defaultBridgeInteriorAndRoad
                    .withWidth(spec.getwI()).withColor(spec.getcI());
            LineStyle bridgeCasing = defaultBridgeCasingAndTunnel.withWidth(
                    spec.getwI() + 2 * spec.getwC()).withColor(spec.getcC());
            LineStyle roadCasing = bridgeCasing.withCap(LineCap.ROUND);
            LineStyle tunnel = defaultBridgeCasingAndTunnel
                    .withWidth(spec.getwI() / 2f).withColor(spec.getcC())
                    .withDashingPattern(2 * spec.getwI(), 2 * spec.getwI());

            Painter<?> tunnelPainter = Painter.line(tunnel).when(
                    Filters.tagged("tunnel").and(spec.getFilter()));
            Painter<?> intBridgePainter = Painter.line(bridgeAndRoadInterior)
                    .when(Filters.tagged("bridge").and(spec.getFilter()));
            Painter<?> coBridgePainter = Painter.line(bridgeCasing).when(
                    Filters.tagged("bridge").and(spec.getFilter()));
            Painter<?> coNormRoadPainter = Painter.line(roadCasing).when(
                    Filters.tagged("bridge").negate()
                            .and(Filters.tagged("tunnel").negate())
                            .and(spec.getFilter()));

            Painter<?> specsPainter = intBridgePainter.above(coBridgePainter
                    .above(intBridgePainter.above(coNormRoadPainter
                            .above(tunnelPainter))));

            roadsPainter = roadsPainter.above(specsPainter).layered();
        }
        return roadsPainter;
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
