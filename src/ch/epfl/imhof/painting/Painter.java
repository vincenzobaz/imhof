package ch.epfl.imhof.painting;

import java.util.function.Predicate;

import ch.epfl.imhof.Attributed;
import ch.epfl.imhof.Map;
import ch.epfl.imhof.geometry.PolyLine;
import ch.epfl.imhof.geometry.Polygon;
import ch.epfl.imhof.painting.LineStyle.LineCap;
import ch.epfl.imhof.painting.LineStyle.LineJoin;

/**
 * Interface fonctionnelle fournissant une méthode abstraite dont le but est de
 * dessiner la carte et des méthodes statiques et par défaut permettant de
 * générer des peintres ou d'en obtenir des nouveaux en modifiant
 * <code>this</code>.
 * 
 * @author Vincenzo Bazzucchi (249733)
 * @author Nicolas Phan Van (239293)
 *
 * @param <E>
 *            le type d'entité géométrique dessinée par le peintre
 */
public interface Painter<E> {
    /**
     * Consommateur, il dessine une carte sur une toile.
     * 
     * @param map
     *            la carte à dessiner
     * @param canvas
     *            la toile sur laquelle dessiner la carte
     */
    void drawMap(Map map, Canvas canvas);

    /**
     * Retourne un peintre dessinant l'intérieur de tous les
     * {@link ch.epfl.imhof.geometry.Polygon polygones} de la carte fournie avec
     * la couleur donnée.
     * 
     * @param fillColor
     *            la couleur des polygones
     * @return un peintre de polygones
     */
    public static Painter<Polygon> polygon(Color fillColor) {
        return (map, canvas) -> {
            map.polygons().forEach(
                    x -> canvas.drawPolygon(x.value(), fillColor));
        };
    }

    /**
     * Retourne un peintre dessinant toutes les
     * {@link ch.epfl.imhof.geometry.PolyLine lignes} de la carte fournie avec
     * le style donné.
     * 
     * @param style
     *            le style de dessin du trait
     * @return un peintre de polylignes
     */
    public static Painter<PolyLine> line(LineStyle style) {
        return (map, canvas) -> {
            map.polyLines().forEach(x -> canvas.drawPolyLine(x.value(), style));
        };
    }

    /**
     * Retourne un peintre dessinant toutes les
     * {@link ch.epfl.imhof.geometry.PolyLine lignes} de la carte fournie avec
     * les paramètres de style donnés.
     * 
     * @param width
     *            la largeur du trait
     * @param color
     *            la couleur du trait
     * @param cap
     *            le type de terminaison du trait
     * @param join
     *            le type de jointure des segments
     * @param dashingPattern
     *            l'alternance de séquences opaques et transparentes du trait
     * @return un peintre de polyligne
     */
    public static Painter<PolyLine> line(float width, Color color, LineCap cap,
            LineJoin join, float... dashingPattern) {
        return line(new LineStyle(width, color, cap, join, dashingPattern));
    }

    /**
     * 
     * Retourne un peintre dessinant toutes les
     * {@link ch.epfl.imhof.geometry.PolyLine lignes} de la carte avec les deux
     * paramètres de style donnés et des valeurs par défaut pour les autres.
     * 
     * @param width
     *            la largeur du trait
     * @param color
     *            la couleur du trait
     * @return un peintre de polyligne
     */
    public static Painter<PolyLine> line(float width, Color color) {
        return line(new LineStyle(width, color));
    }

    /**
     * 
     * Retourne un peintre dessinant les pourtours de l'enveloppe et des trous
     * de tous les {@link ch.epfl.imhof.geometry.Polygon polygones} de la carte
     * avec le style donné.
     * 
     * @param style
     *            le style de dessin du trait
     * @return un peintre de polyligne
     */
    public static Painter<PolyLine> outline(LineStyle style) {
        return (map, canvas) -> {
            map.polygons().forEach(x -> {
                canvas.drawPolyLine(x.value().shell(), style);
                x.value().holes().forEach(y -> {
                    canvas.drawPolyLine(y, style);
                });
            });
        };
    }

    /**
     * Retourne un peintre dessinant les pourtours de l'enveloppe et des trous
     * de tous les {@link ch.epfl.imhof.geometry.Polygon polygones} de la carte
     * qu'on lui fournit en utilisant les cinq paramètres de style donnés.
     * 
     * @param width
     *            la largeur du trait
     * @param color
     *            la couleur du trait
     * @param cap
     *            le type de terminaison du trait
     * @param join
     *            le type de jointure des segments
     * @param dashingPattern
     *            l'alternance des séquences opaques et transparentes du trait
     * @return un peintre de polyligne
     */
    public static Painter<PolyLine> outline(float width, Color color,
            LineCap cap, LineJoin join, float... dashingPattern) {
        return outline(new LineStyle(width, color, cap, join, dashingPattern));
    }

    /**
     * Retourne un peintre dessinant les pourtours de l'enveloppe et des trous
     * de tous les {@link ch.epfl.imhof.geometry.Polygon polygones} de la carte
     * qu'on lui fournit en utilisant les deux paramètres de style donnés et des
     * valeurs par défaut pour les autres.
     * 
     * @param width
     *            la largeur du trait
     * @param color
     *            la couleur du trait
     * @return un peintre de polyligne
     * 
     */
    public static Painter<PolyLine> outline(float width, Color color) {
        return outline(new LineStyle(width, color));
    }

    /**
     * 
     * Retourne un peintre se comportant comme <code>this</code> mais qui ne
     * dessine que les éléments satisfaisant le prédicat reçu en argument.
     * 
     * @param predicate
     *            le prédicat permettant de sélectionner les éléments de la
     *            carte à dessiner
     * @return un peintre sélectif
     */
    public default Painter<?> when(Predicate<Attributed<?>> predicate) {
        return (map, canvas) -> {
            Map.Builder mapBuilder = new Map.Builder();
            for (Attributed<Polygon> polygon : map.polygons()) {
                if (predicate.test(polygon)) {
                    mapBuilder.addPolygon(polygon);
                }
            }
            for (Attributed<PolyLine> polyline : map.polyLines()) {
                if (predicate.test(polyline)) {
                    mapBuilder.addPolyLine(polyline);
                }
            }
            this.drawMap(mapBuilder.build(), canvas);
        };
    }

    /**
     * 
     * Retourne un peintre dessinant d'abord la carte produite par le peintre en
     * argument puis, par dessus, celle produit par <code>this</code>.
     * 
     * @param painter
     *            le peintre qu'on veut utiliser avant d'utiliser
     *            <code>this</code>
     * @return un peintre empilant deux peintres
     */
    public default Painter<?> above(Painter<?> painter) {
        return (map, canvas) -> {
            painter.drawMap(map, canvas);
            this.drawMap(map, canvas);
        };
    }

    /**
     * 
     * Retourne un peintre se comportant comme <code>this</code> mais qui
     * dessine la carte couche par couche, de la plus basse à la plus élévée.
     * 
     * @return un peintre dessinant la carte par couches, en commençant par le
     *         niveau le plus bas (<code>layer = -5</code>) jusqu'au niveau le
     *         plus haut (<code>layer = 5</code>)
     */
    public default Painter<?> layered() {
        return (map, canvas) -> {
            Painter<?> painter = this.when(Filters.onLayer(-5));
            for (int layer = -4; layer <= 5; ++layer) {
                Painter<?> top = this.when(Filters.onLayer(layer));
                painter = top.above(painter);
            }
            painter.drawMap(map, canvas);
        };
    }
}
