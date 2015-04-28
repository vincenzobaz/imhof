package ch.epfl.imhof.painting;

import java.util.function.Predicate;

import static ch.epfl.imhof.painting.LineStyle.LineCap;
import static ch.epfl.imhof.painting.LineStyle.LineJoin;
import ch.epfl.imhof.Attributed;

/**
 * Classe non instanciable fournissant uniquement une méthode statique
 * permettant de générer un peintre de routes.
 * 
 * @author Vincenzo Bazzucchi (249733)
 * @author Nicolas Phan Van (239293)
 *
 */
public final class RoadPainterGenerator {
    /**
     * La classe étant non-instanciable, le constructeur est vidé et privé.
     */
    private RoadPainterGenerator() {
    }

    /**
     * Méthode statique retournant un peintre du réseau routier à partir d'un
     * nombre variable de <code> RoadSpec</code>
     * 
     * @param specifications
     *            ellipse contenant toutes les specifications des routes à
     *            peindre.
     * @return le peintre dessinant le réseau routier
     */
    public static Painter<?> painterForRoads(RoadSpec... specifications) {
        LineStyle defaultBridgeCasingAndTunnelStyle = new LineStyle(0f,
                Color.WHITE, LineCap.BUTT, LineJoin.ROUND, null);
        LineStyle defaultBridgeInteriorAndRoadStyle = new LineStyle(0f,
                Color.WHITE, LineCap.ROUND, LineJoin.ROUND, null);

        return (map, canvas) -> {
            for (RoadSpec spec : specifications) {
                Painter.line(
                        defaultBridgeCasingAndTunnelStyle
                                .withWidth(spec.getwI() / 2f)
                                .withColor(spec.getcC())
                                .withDashingPattern(2 * spec.getwI(),
                                        2 * spec.getwI()))
                        .when(Filters.tagged("tunnel").and(spec.getFilter()))
                        .drawMap(map, canvas);
            }

            for (RoadSpec spec : specifications) {
                Painter.line(
                        defaultBridgeCasingAndTunnelStyle
                                .withWidth(spec.getwI() + 2 * spec.getwC())
                                .withColor(spec.getcC()).withCap(LineCap.ROUND))
                        .when(Filters.tagged("bridge").negate()
                                .and(Filters.tagged("tunnel").negate())
                                .and(spec.getFilter())).drawMap(map, canvas);
            }

            for (RoadSpec spec : specifications) {
                Painter.line(
                        defaultBridgeInteriorAndRoadStyle.withWidth(
                                spec.getwI()).withColor(spec.getcI()))
                        .when(Filters.tagged("bridge").negate()
                                .and(Filters.tagged("tunnel").negate())
                                .and(spec.getFilter())).drawMap(map, canvas);
            }

            for (RoadSpec spec : specifications) {
                Painter.line(
                        defaultBridgeCasingAndTunnelStyle.withWidth(
                                spec.getwI() + 2 * spec.getwC()).withColor(
                                spec.getcC()))
                        .when(Filters.tagged("bridge").and(spec.getFilter()))
                        .drawMap(map, canvas);
            }

            for (RoadSpec spec : specifications) {
                Painter.line(
                        defaultBridgeInteriorAndRoadStyle.withWidth(
                                spec.getwI()).withColor(spec.getcI()))
                        .when(Filters.tagged("bridge").and(spec.getFilter()))
                        .drawMap(map, canvas);
            }
        };
    }

    /**
     * Classe imbriqué statiquement définissant une spécification de route.
     * 
     * @author Vincenzo Bazzucchi (249733)
     * @author Nicolas Phan Van (239293)
     *
     */
    public final static class RoadSpec {
        private final Predicate<Attributed<?>> filter;
        private final float wI;
        private final Color cI;
        private final float wC;
        private final Color cC;

        /**
         * Constructeur d'un objet de type <code> RoadSpec </code>
         * 
         * @param filter
         *            le prédicat que la route doit satisfaire pour être
         *            dessinée
         * @param wI
         *            la largeur de l'intérieur de la route
         * @param cI
         *            la couleur de l'intérieur de la route
         * @param wC
         *            la largeur de la bordure de la route
         * @param cC
         *            la couleur de la bordure de la route
         */
        public RoadSpec(Predicate<Attributed<?>> filter, float wI, Color cI,
                float wC, Color cC) {
            this.filter = filter;
            this.wI = wI;
            this.cI = cI;
            this.wC = wC;
            this.cC = cC;
        }

        /**
         * Accesseur du prédicat qui doit être satisfait pour que la route soit
         * dessinée
         * 
         * @return the filter
         */
        public Predicate<Attributed<?>> getFilter() {
            return filter;
        }

        /**
         * Accesseur de la largeur de l'intérieur de la route
         * 
         * @return the wI
         */
        public float getwI() {
            return wI;
        }

        /**
         * Accesseur de la couleur de l'intérieur de la route
         * 
         * @return the cI
         */
        public Color getcI() {
            return cI;
        }

        /**
         * Accesseur de la largeur de la bordure de la route
         * 
         * @return the wC
         */
        public float getwC() {
            return wC;
        }

        /**
         * Accesseur de la couleur de la bordure de la route
         * 
         * @return the cC
         */
        public Color getcC() {
            return cC;
        }
    }
}
