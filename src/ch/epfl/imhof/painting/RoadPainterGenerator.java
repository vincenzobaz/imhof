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
    // Styles de lignes par défaut à modifier pour chaque type de route à
    // l'aide des méthodes withX de LineStyle
    private static final LineStyle DEFAULT_BRIDGE_CASING_AND_TUNNEL_STYLE = new LineStyle(
            0f, Color.WHITE, LineCap.BUTT, LineJoin.ROUND, null);
    private static final LineStyle DEFAULT_BRIDGE_INTERIOR_AND_ROAD_STYLE = DEFAULT_BRIDGE_CASING_AND_TUNNEL_STYLE
            .withCap(LineCap.ROUND);

    /**
     * La classe étant non instanciable, le constructeur est privé et vide.
     */
    private RoadPainterGenerator() {
    }

    /**
     * Retourne un peintre du réseau routier à partir d'un nombre variable de
     * spécifications de routes.
     * 
     * @param specifications
     *            ellipse contenant toutes les spécifications de routes à
     *            peindre
     * @return le peintre dessinant le réseau routier
     */
    public static Painter<?> painterForRoads(RoadSpec... specifications) {
        return (map, canvas) -> {
            // Dessin des tunnels
            for (RoadSpec spec : specifications) {
                Painter.line(
                        DEFAULT_BRIDGE_CASING_AND_TUNNEL_STYLE
                                .withWidth(spec.wI() / 2f)
                                .withColor(spec.cC())
                                .withDashingPattern(2 * spec.wI(),
                                        2 * spec.wI()))
                        .when(Filters.tagged("tunnel").and(spec.filter()))
                        .drawMap(map, canvas);
            }

            // Dessin des bordures de routes normales
            for (RoadSpec spec : specifications) {
                Painter.line(
                        DEFAULT_BRIDGE_INTERIOR_AND_ROAD_STYLE.withWidth(
                                spec.wI() + 2 * spec.wC()).withColor(spec.cC()))
                        .when(Filters.tagged("bridge").negate()
                                .and(Filters.tagged("tunnel").negate())
                                .and(spec.filter())).drawMap(map, canvas);
            }

            // Dessin de l'intérieur des routes normales
            for (RoadSpec spec : specifications) {
                Painter.line(
                        DEFAULT_BRIDGE_INTERIOR_AND_ROAD_STYLE.withWidth(
                                spec.wI()).withColor(spec.cI()))
                        .when(Filters.tagged("bridge").negate()
                                .and(Filters.tagged("tunnel").negate())
                                .and(spec.filter())).drawMap(map, canvas);
            }

            // Dessin des bordures de pont
            for (RoadSpec spec : specifications) {
                Painter.line(
                        DEFAULT_BRIDGE_CASING_AND_TUNNEL_STYLE.withWidth(
                                spec.wI() + 2 * spec.wC()).withColor(spec.cC()))
                        .when(Filters.tagged("bridge").and(spec.filter()))
                        .drawMap(map, canvas);
            }

            // Dessin de l'intérieur des ponts
            for (RoadSpec spec : specifications) {
                Painter.line(
                        DEFAULT_BRIDGE_INTERIOR_AND_ROAD_STYLE.withWidth(
                                spec.wI()).withColor(spec.cI()))
                        .when(Filters.tagged("bridge").and(spec.filter()))
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
        public Predicate<Attributed<?>> filter() {
            return filter;
        }

        /**
         * Retourne la largeur du trait de l'intérieur de la route.
         * 
         * @return la largeur intérieure
         */
        public float wI() {
            return wI;
        }

        /**
         * Retourne la couleur du trait de l'intérieur de la route.
         * 
         * @return la couleur intérieure
         */
        public Color cI() {
            return cI;
        }

        /**
         * Retourne la largeur du trait de la bordure de la route.
         * 
         * @return la largeur extérieure
         */
        public float wC() {
            return wC;
        }

        /**
         * Retourne la couleur du trait de la bordure de la route.
         * 
         * @return la couleur extérieure
         */
        public Color cC() {
            return cC;
        }
    }
}
