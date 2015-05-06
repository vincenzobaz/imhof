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
     * La classe étant non instanciable, le constructeur est privé et vide.
     */
    private RoadPainterGenerator() {
    }

    /**
     * Retourne un peintre du réseau routier à partir d'un nombre variable de
     * <code>RoadSpec</code>.
     * 
     * @param specifications
     *            ellipse contenant toutes les spécifications de routes à
     *            peindre
     * @return le peintre dessinant le réseau routier
     */
    public static Painter<?> painterForRoads(RoadSpec... specifications) {
        // Styles de lignes par défaut à modifier pour chaque type de route à
        // l'aide des méthodes withX de LineStyle
        LineStyle defaultBridgeCasingAndTunnelStyle = new LineStyle(0f,
                Color.WHITE, LineCap.BUTT, LineJoin.ROUND, null);
        LineStyle defaultBridgeInteriorAndRoadStyle = new LineStyle(0f,
                Color.WHITE, LineCap.ROUND, LineJoin.ROUND, null);

        return (map, canvas) -> {
            // Dessin des tunnels
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

            // Dessin des bordures de routes normales
            for (RoadSpec spec : specifications) {
                Painter.line(
                        defaultBridgeCasingAndTunnelStyle
                                .withWidth(spec.getwI() + 2 * spec.getwC())
                                .withColor(spec.getcC()).withCap(LineCap.ROUND))
                        .when(Filters.tagged("bridge").negate()
                                .and(Filters.tagged("tunnel").negate())
                                .and(spec.getFilter())).drawMap(map, canvas);
            }

            // Dessin de l'intérieur des routes normales
            for (RoadSpec spec : specifications) {
                Painter.line(
                        defaultBridgeInteriorAndRoadStyle.withWidth(
                                spec.getwI()).withColor(spec.getcI()))
                        .when(Filters.tagged("bridge").negate()
                                .and(Filters.tagged("tunnel").negate())
                                .and(spec.getFilter())).drawMap(map, canvas);
            }

            // Dessin des bordures de pont
            for (RoadSpec spec : specifications) {
                Painter.line(
                        defaultBridgeCasingAndTunnelStyle.withWidth(
                                spec.getwI() + 2 * spec.getwC()).withColor(
                                spec.getcC()))
                        .when(Filters.tagged("bridge").and(spec.getFilter()))
                        .drawMap(map, canvas);
            }

            // Dessin de l'intérieur des ponts
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
     * Classe imbriquée statiquement définissant une spécification de route.
     * 
     * @author Vincenzo Bazzucchi (249733)
     * @author Nicolas Phan Van (239293)
     *
     */
    public static final class RoadSpec {
        private final Predicate<Attributed<?>> filter;
        private final float wI;
        private final Color cI;
        private final float wC;
        private final Color cC;

        /**
         * Construit une nouvelle spécification de route ayant les paramètres
         * donnés.
         * 
         * @param filter
         *            le prédicat que la route doit satisfaire pour être
         *            dessinée
         * @param wI
         *            la largeur du trait de l'intérieur de la route
         * @param cI
         *            la couleur du trait de l'intérieur de la route
         * @param wC
         *            la largeur du trait de la bordure de la route
         * @param cC
         *            la couleur du trait de la bordure de la route
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
         * Retourne le filtre permettant de sélectionner ce type de route.
         * 
         * @return le prédicat qui doit être satisfait pour que la route soit
         *         dessinée
         */
        public Predicate<Attributed<?>> getFilter() {
            return filter;
        }

        /**
         * Retourne la largeur du trait de l'intérieur de la route.
         * 
         * @return la largeur intérieure
         */
        public float getwI() {
            return wI;
        }

        /**
         * Retourne la couleur du trait de l'intérieur de la route.
         * 
         * @return la couleur intérieure
         */
        public Color getcI() {
            return cI;
        }

        /**
         * Retourne la largeur du trait de la bordure de la route.
         * 
         * @return la largeur extérieure
         */
        public float getwC() {
            return wC;
        }

        /**
         * Retourne la couleur du trait de la bordure de la route.
         * 
         * @return la couleur extérieure
         */
        public Color getcC() {
            return cC;
        }
    }
}
