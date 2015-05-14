package ch.epfl.imhof.dem;

import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.util.function.Function;

import ch.epfl.imhof.Vector3D;
import ch.epfl.imhof.geometry.Point;
import ch.epfl.imhof.projection.Projection;

/**
 * Classe décrivant un objet qui, à partir d'un
 * <code>DigitalElevationModel</code>, permet de produire une image de relief.
 * Selon le rayon de floutage fourni, cette image peut être floutée pour
 * compenser la basse resolution des fichiers hgt utilisés.
 * 
 * @author Vincenzo Bazzucchi (249733)
 * @author Nicolas Phan Van (239293)
 *
 */
public final class ReliefShader {
    private final Projection projection;
    private final DigitalElevationModel model;
    private final Vector3D lightSource;

    /**
     * Construit une instance de la classe
     * 
     * @param projection
     *            la projection à utiliser lors de calcul des coordonnées des
     *            points de l'image
     * @param model
     *            le DigitalElevationModel permettant d'avoir les hauteurs des
     *            points
     * @param lightSource
     *            le <code>Vector3D</code> représentant la direciton de la
     *            lumière
     */
    public ReliefShader(Projection projection, DigitalElevationModel model,
            Vector3D lightSource) {
        this.projection = projection;
        this.model = model;
        this.lightSource = lightSource;
    }

    /**
     * Produit une <code>BufferedImage</code> du relief.
     * 
     * @param BL
     *            le point bas-gauche de l'image
     * @param TR
     *            le point haut-droite de l'image
     * @param width
     *            la largeur de l'image
     * @param height
     *            la hauteur de l'image
     * @param radius
     *            le rayon de floutage
     * @return l'image de relief comprise entre le point bas-gauche
     *         <code>BL</code> et haut-droite <code>TR</code>, de largeur
     *         <code>width</code> et hauteur <code>height</code>, flouté avec un
     *         rayon de floutage <code>radius</code>
     */
    public BufferedImage shadedRelief(Point BL, Point TR, int width,
            int height, float radius) {
        // si le rayon de floutage est nul, on prduit une image non--floutée
        if (radius == 0) {
            return raw(width, height, Point.alignedCoordinateChange(new Point(
                    0d, height - 1), BL, new Point(width - 1, 0d), TR));
        } else {
            // Si le rayon de floutage n'est pas nul, on produit un tableau
            // contenant les valeurs correspondantes au rayon de floutage,
            // calculées par échantillonage d'une fonction gaussienne à deux
            // dimensions
            float[] gaussValues = shadingKernel(radius);
            // on calcule la dimension de la zone tampon et on produit une image
            // plus grande afin de compenser la reduction de taille causée par
            // getSubImage (extraction de l'image floutée avec exclusion de la
            // zone non floutée)
            int bufferZoneSize = (gaussValues.length - 1) / 2;
            BufferedImage rawImage = raw(width + 2 * bufferZoneSize, height + 2
                    * bufferZoneSize, Point.alignedCoordinateChange(new Point(
                    bufferZoneSize, height + bufferZoneSize - 1), BL,
                    new Point(width + bufferZoneSize - 1, bufferZoneSize), TR));
            // on peut maintenant flouter notre image en utilisant l'image non
            // floutée et le tableau de valeurs construits avant
            BufferedImage blurredImage = blurredImage(rawImage, gaussValues);
            // On utiliser getSubimage pour retourner une image floutée en
            // excluant la zone non floutée, correspondante aux bords de l'image
            // d'épaisseur bufferZoneSize
            return blurredImage.getSubimage(bufferZoneSize, bufferZoneSize,
                    width, height);
        }
    }

    /**
     * Produit une image de relief non floutée
     * 
     * @param width
     *            la hauteur de l'image
     * @param height
     *            la largeur de l'image
     * @param imageToPlan
     *            la fonction permettant de effectueur un changement de
     *            coordonnées du repère de la Terre au repère de l'image
     * @return une image de relief
     */
    private BufferedImage raw(int width, int height,
            Function<Point, Point> imageToPlan) {
        BufferedImage rawRelief = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_RGB);
        // On calcule la couleur de chaque pixel à partir de la hauteur de la
        // surface terrestre au point correspondant au pixel considéré
        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                Vector3D normal = model.normalAt(projection.inverse(imageToPlan
                        .apply(new Point(x, y))));
                double cosTheta = lightSource.scalarProduct(normal)
                        / (lightSource.norm() * normal.norm());
                int redAndGreenLevel = (int) (0.5 * (cosTheta + 1) * 255.9999);
                int blueLevel = (int) (0.5 * (0.7 * cosTheta + 1) * 255.9999);
                int rgb = blueLevel | redAndGreenLevel << 8
                        | redAndGreenLevel << 16;
                rawRelief.setRGB(x, y, rgb);
            }
        }
        return rawRelief;
    }

    /**
     * Retourne le tableau de valeurs obtenues par échantillonnage d'une
     * fonction gaussienne à deux dimensions dont la dimension est calculée à
     * partir du rayon de floutage fourni
     * 
     * @param radius
     *            le rayon de floutage
     * @return le tableau des poids des pixels autour du pixel considéré
     */
    private float[] shadingKernel(float radius) {
        float sigma = radius / 3f;
        int n = 2 * ((int) Math.ceil(radius)) + 1;

        float[] line = new float[n];
        // Le vecteur horizontal (égal au vecteur vertical transposé) est
        // symétrique. Il suffit donc de (tailleTableau -1)/ 2 accés et calculs
        // pour le remplir
        int indexBase = (n - 1) / 2;
        float totalWeight = line[indexBase] = 1f;
        for (int i = 1; i < indexBase; i++) {
            float weight = (float) Math.exp(-i * i / (2 * sigma * sigma));
            line[indexBase + i] = line[indexBase - i] = weight;
            totalWeight += 2 * weight;
        }
        // normalisation du vecteur
        for (int i = 0; i < line.length; ++i) {
            line[i] /= totalWeight;
        }
        return line;
    }

    /**
     * Retourne l'image de relief floutée. Le floutage se fait au moyen de la
     * classe <code>ConvolveOp</code>. Pour eviter des ennuyants calculs d'un
     * <code>Kernel</code> matriciel, on utilise une fois un vecteur et une
     * deuxième fois le même vecteur transposée, construits à l'aide du
     * constructeur de <code>Kernel</code>
     * 
     * @param image l'image à flouter
     * @param kernel le tableau des poids des pixels autour du pixel considéré
     * @return l'image floutée
     */
    private BufferedImage blurredImage(BufferedImage image, float[] kernel) {
        ConvolveOp horizontalConvolution = new ConvolveOp(new Kernel(
                kernel.length, 1, kernel), ConvolveOp.EDGE_NO_OP, null);
        ConvolveOp verticalConvolution = new ConvolveOp(new Kernel(1,
                kernel.length, kernel), ConvolveOp.EDGE_NO_OP, null);
        return verticalConvolution.filter(
                horizontalConvolution.filter(image, null), null);
    }
}
