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
 * <code> this </code>
 * 
 * @author Vincenzo Bazzucchi (249733)
 * @author Nicolas Phan Van (239293)
 *
 * @param <E>
 */
public interface Painter<E> {
    /**
     * Consommateur, il dessine une carte sur une toile
     * 
     * @param map
     *            la carte à dessinner
     * @param canvas
     *            la toile sur laquelle dessiner la carte
     */
    void drawMap(Map map, Canvas canvas);

    /**
     * Retourne un peintre peignant l'intérieur des polygones
     * 
     * @param fillColor
     *            la coulour de remplissage des polygones
     * @return un peintre dessinant l'intérieur de tous les polygones de la
     *         carte qu'il reçoit
     */
    public static Painter<Polygon> polygon(Color fillColor) {
        return (map, canvas) -> {
            map.polygons().forEach(
                    x -> canvas.drawPolygon(x.value(), fillColor));
        };
    }

    /**
     * Rtourne un peintre dessinant les lignes de la carte
     * 
     * @param style
     *            le style de dessin du trait
     * @return un peintre dessinant toutes les lignes de la carte qu'on lui
     *         fournit en utilisant le style fourni en argument
     */
    public static Painter<PolyLine> line(LineStyle style) {
        return (map, canvas) -> {
            map.polyLines().forEach(x -> canvas.drawPolyLine(x.value(), style));
        };
    }

    /**
     * Retourne un peintre dessinant les lignes de la carte
     * 
     * @param width
     *            l'épaisseur du trait
     * @param color
     *            la couleur du trait
     * @param cap
     *            le type de terminaison du trait
     * @param join
     *            le type de jointure du segment
     * @param dashingPattern
     *            l'alternance de séquences opaques et transparentes du trait
     * @return un peintre dessinant toutes les lignes de la carte qu'on lui
     *         fournit en utilisant les cinq paramètres de style fournis en
     *         argument
     */
    public static Painter<PolyLine> line(float width, Color color, LineCap cap,
            LineJoin join, float... dashingPattern) {
        return line(new LineStyle(width, color, cap, join, dashingPattern));
    }

    /**
     * 
     * Retourne un peintre dessinant les lignes de la carte
     * 
     * @param width
     *            l'épaisseur du trait
     * @param color
     *            la couleur du trait
     * @return un peintre dessinant otutes les lignes de la carte qu'on lui
     *         fournit en utilisant les deux paramètrs de style fournis en
     *         argument et des valeurs par défaut pour les autres trois
     */
    public static Painter<PolyLine> line(float width, Color color) {
        return line(new LineStyle(width, color));
    }

    /**
     * 
     * Retourne un peintre dessinant les pourtours de l'enveloppe et des trous
     * des polygones de la carte
     * 
     * @param style
     *            le style du trait à utiliser lors du dessin.
     * @return un peintre dessinant les pourtours de l'enveloppe et des trous de
     *         toues les polygones de la carte qu'on lui forunit en utilisant le
     *         style fourni en argument
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
     * des polygones de la carte
     * 
     * @param width
     *            l'épaisseur du trait
     * @param color
     *            la couleur du trait
     * @param cap
     *            le type de terminaison du trait
     * @param join
     *            le type de jointure du segment
     * @param dashingPattern
     *            l'alternance des séquences opaques et transparentes du trait
     * @return un peintre dessinant les pourtours de l'enveloppe et des trous de
     *         tous les polygones de la carte qu'on lui fournit en appliquant
     *         les cinq paramètres de style d'une ligne fournis en argument
     */
    public static Painter<PolyLine> outline(float width, Color color,
            LineCap cap, LineJoin join, float... dashingPattern) {
        return outline(new LineStyle(width, color, cap, join, dashingPattern));
    }

    /**
     * Retourne un peintre dessinant les pourtours de l'enveloppe et des trous
     * des polygones de la carte
     * 
     * @param width
     *            l'épaisseur du trait
     * @param color
     *            la couleur du trait
     * @return un peintre dessinant les pourtours de l'enveloppe et des trous de
     *         tous les polygones de la carte qu'on lui fournit en appliquant
     *         les deux paramètres de style d'une ligne fournis en argument et
     *         en utilisant des valeurs par défaut pour les autres trois
     */
    public static Painter<PolyLine> outline(float width, Color color) {
        return outline(new LineStyle(width, color));
    }

    /**
     * 
     * Retourne un peintre qui agit comme <code> this </code> mais qui n'agit
     * que lorsque le prédicat est satisfait
     * 
     * @param predicate
     *            le prédicat permettant de sélectionner les éléments de la
     *            carte à dessiner
     * @return un peintre se comportant comme <code>this</code> mais qui ne
     *         dessine que les éléments satisfaisant le prédicat réçu en
     *         argument
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
     * Retourne un peintre se comportant comme <code> this </code> mais qui
     * n'agit qu'après le peintre reçu en argument
     * 
     * @param painter
     *            le peintre qu'on veut utiliser avant d'utiliser
     *            <code>this</code>
     * @return un noveuau peintre dessinant d'abord la carte produite par le
     *         peintre en argument puis, par dessus, celle produit par
     *         <code>this</code>
     */
    public default Painter<?> above(Painter<?> painter) {
        return (map, canvas) -> {
            painter.drawMap(map, canvas);
            this.drawMap(map, canvas);
        };
    }

    /**
     * 
     * Retourne un peintre se comportant comme <code> this </code> mais qui
     * dessine la carte par couche, en commençant par la plus basse (-5) jusqu'à
     * la plus élévée (5)
     * 
     * @return un peintre dessinant la carte par couches: en commençant par le
     *         niveau le plus bas (<code>layer=-5"</code>) jusqu'au niveau le
     *         plus haut (<code>layer=5</code>)
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
