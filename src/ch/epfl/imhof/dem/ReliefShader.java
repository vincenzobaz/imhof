package ch.epfl.imhof.dem;

import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.IOException;
import java.util.Arrays;
import java.util.function.Function;

import ch.epfl.imhof.PointGeo;
import ch.epfl.imhof.Vector3D;
import ch.epfl.imhof.geometry.Point;
import ch.epfl.imhof.projection.Projection;

/**
 * Classe permettant de dessiner un relief ombré coloré, à partir d'un
 * {@link ch.epfl.imhof.dem.DigitalElevationModel DigitalElevationModel}. Selon
 * le rayon de floutage fourni, cette image peut être floutée pour compenser la
 * basse résolution des fichiers hgt utilisés.
 * 
 * @author Vincenzo Bazzucchi (249733)
 * @author Nicolas Phan Van (239293)
 *
 */
public final class ReliefShader {
    private final Projection projection;
    private final DigitalElevationModel[] models;
    private final Vector3D lightSource;
    public static final byte DEM_DISPOSITION_HORIZONTAL = 0;
    public static final byte DEM_DISPOSITION_VERTICAL = 1;
    private final byte disposition;

    /**
     * Construit un dessinateur de relief ombré.
     * 
     * @param projection
     *            la projection à utiliser lors du calcul des coordonnées des
     *            points de l'image
     * @param model
     *            le modèle numérique de terrain permettant d'obtenir l'altitude
     *            des points
     * @param lightSource
     *            le vecteur à trois dimensions représentant la direction de la
     *            source lumineuse
     */
    public ReliefShader(Projection projection, Vector3D lightSource,
            byte disposition, DigitalElevationModel... models) {
        this.disposition = disposition;
        this.projection = projection;
        this.models = new DigitalElevationModel[models.length];
        for (int i = 0; i < models.length; i++) {
            this.models[i] = models[i];
        }
        this.lightSource = lightSource;
    }

    // 1 = haut guache , 2 = haut droite , 3 = bas gauche , 4 = bas droite
    public ReliefShader(Projection projection, Vector3D lightSource,
            DigitalElevationModel... models) {
        if (models.length != 4 && models.length != 2 && models.length != 1)
            throw new IllegalArgumentException();
        this.projection = projection;
        this.lightSource = lightSource;
        this.models = new DigitalElevationModel[models.length];
        for (int i = 0; i < models.length; i++) {
            this.models[i] = models[i];
        }
        disposition = 4;
        if (models.length == 2 && disposition == 4)
            throw new IllegalArgumentException(
                    "Utiliser le deuxième constructeur dans le cas de deux fichiers hgt");
    }

    /**
     * Dessine et retourne une image du relief selon les paramètres donnés.
     * 
     * @param bottomLeft
     *            le coin bas-gauche du relief à dessiner
     * @param topRight
     *            le coin haut-droite du relief à dessiner
     * @param width
     *            la largeur de l'image, en pixels
     * @param height
     *            la hauteur de l'image, en pixels
     * @param radius
     *            le rayon de floutage
     * @return l'image du relief compris entre les coins bas-gauche
     *         <code>bottomLeft</code> et haut-droite <code>topRight</code>, de
     *         largeur <code>width</code> et hauteur <code>height</code>, flouté
     *         avec un rayon de floutage <code>radius</code>
     * @throws Exception
     */
    public BufferedImage shadedRelief(Point bottomLeft, Point topRight,
            int width, int height, float radius) throws Exception {
        if (radius < 0) {
            throw new IllegalArgumentException(
                    "Le rayon de floutage doit être positif.");
        }

        // Si le rayon de floutage est nul, on produit une image non-floutée
        if (radius == 0f) {
            Function<Point, Point> imageToPlan = Point.alignedCoordinateChange(
                    new Point(0d, height - 1), bottomLeft, new Point(width - 1,
                            0d), topRight);
            Function<Point, Point> planToImage = Point.alignedCoordinateChange(
                    bottomLeft, new Point(0d, height - 1), topRight, new Point(
                            width - 1, 0d));

            return rawMultipleFiles(width, height, imageToPlan, planToImage,
                    projection.inverse(bottomLeft),
                    projection.inverse(topRight));
        } else {
            // Si le rayon de floutage n'est pas nul, on produit un tableau
            // contenant les valeurs correspondantes au rayon de floutage,
            // calculées par échantillonage d'une fonction gaussienne à deux
            // dimensions
            float[] gaussValues = shadingKernel(radius);

            // On calcule la dimension de la zone tampon et on produit une image
            // plus grande afin de compenser la réduction de taille causée par
            // getSubImage (extraction de l'image floutée avec exclusion de la
            // zone non floutée)
            int bufferZoneSize = (gaussValues.length - 1) / 2;
            Function<Point, Point> imageToPlan = Point.alignedCoordinateChange(
                    new Point(bufferZoneSize, height + bufferZoneSize - 1),
                    bottomLeft, new Point(width + bufferZoneSize - 1,
                            bufferZoneSize), topRight);

            Function<Point, Point> planToImage = Point.alignedCoordinateChange(
                    bottomLeft, new Point(bufferZoneSize, height
                            + bufferZoneSize - 1), topRight, new Point(width
                            + bufferZoneSize - 1, bufferZoneSize));
            BufferedImage rawImage = rawMultipleFiles(width + 2
                    * bufferZoneSize, height + 2 * bufferZoneSize, imageToPlan,
                    planToImage, projection.inverse(bottomLeft),
                    projection.inverse(topRight));

            // On peut maintenant flouter notre image en utilisant l'image non
            // floutée et le tableau de valeurs construits avant
            BufferedImage blurredImage = blurredImage(rawImage, gaussValues);

            // On utilise getSubimage pour retourner une image floutée en
            // excluant la zone non floutée, correspondante aux bords de l'image
            // d'épaisseur bufferZoneSize
            return blurredImage.getSubimage(bufferZoneSize, bufferZoneSize,
                    width, height);
        }
    }

    /**
     * Dessine et retourne une image de relief non floutée.
     * 
     * @param width
     *            la largeur de l'image, en pixels
     * @param height
     *            la hauteur de l'image, en pixels
     * @param imageToPlan
     *            la fonction permettant d'effectuer un changement de
     *            coordonnées entre le repère du relief et celui de l'image
     * @return une image de relief
     * @throws Exception
     */
    private void raw(int sX, int sY, int width, int height,
            Function<Point, Point> imageToPlan, DigitalElevationModel model,
            BufferedImage image) throws Exception {
        model.loadBuffer();
        // On calcule la couleur de chaque pixel à partir de l'altitude du
        // point correspondant à ce pixel
        for (int x = sX; x < width; ++x) {
            for (int y = sY; y < height; ++y) {
                Vector3D normal = model.normalAt(projection.inverse(imageToPlan
                        .apply(new Point(x, y))));
                double cosTheta = lightSource.scalarProduct(normal)
                        / (lightSource.norm() * normal.norm());
                int redAndGreenLevel = (int) (0.5 * (cosTheta + 1) * 255.9999);
                int blueLevel = (int) (0.5 * (0.7 * cosTheta + 1) * 255.9999);
                int rgb = blueLevel | redAndGreenLevel << 8
                        | redAndGreenLevel << 16;
                image.setRGB(x, y, rgb);
            }
        }
        model.close();
    }

    private BufferedImage rawMultipleFiles(int width, int height,
            Function<Point, Point> imageToPlan,
            Function<Point, Point> planToImage, PointGeo BL, PointGeo TR)
            throws Exception {
        BufferedImage rawRelief = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_BGR);
        if (models.length == 1) {
            raw(0, 0, width, height, imageToPlan, models[0], rawRelief);
            return rawRelief;
        } else if (models.length == 4) {
            // calcul de la latiude et de la longitude limite en pixel
            int yCrit = (int) Math.ceil(planToImage.apply(
                    projection.project(new PointGeo(BL.longitude(), Math
                            .toRadians(models[2].latitudeSW() + 1)))).x());
            int xCrit = (int) Math.ceil(planToImage.apply(
                    projection.project(new PointGeo(Math.toRadians(models[3]
                            .longitudeSW()), BL.latitude()))).y());
            // haut et droite exclues
            raw(0, 0, xCrit - 1, yCrit, imageToPlan, models[0], rawRelief);
            raw(xCrit, 0, width - xCrit, yCrit, imageToPlan, models[1],
                    rawRelief);
            raw(0, yCrit + 1, xCrit - 1, height - yCrit, imageToPlan,
                    models[3], rawRelief);
            raw(xCrit, yCrit, width - xCrit, height - yCrit, imageToPlan,
                    models[3], rawRelief);
            return rawRelief;
        } else {
            if (disposition == DEM_DISPOSITION_HORIZONTAL) {
                int xCrit = (int) Math.ceil(planToImage.apply(
                        projection.project(new PointGeo(Math
                                .toRadians(models[0].longitudeSW()), BL
                                .latitude()))).x());
                raw(0, 0, xCrit - 1, height, imageToPlan, models[0], rawRelief);
                raw(xCrit, 0, width - xCrit, height, imageToPlan, models[1],
                        rawRelief);
                return rawRelief;
            } else {
                int yCrit = (int) Math.ceil(planToImage.apply(
                        projection.project(new PointGeo(BL.longitude(), Math
                                .toRadians(models[0].latitudeSW())))).y());
                raw(0, 0, width, yCrit, imageToPlan, models[0], rawRelief);
                raw(0, yCrit + 1, width, height - yCrit, imageToPlan,
                        models[1], rawRelief);
                return rawRelief;
            }
        }
    }

    /**
     * Retourne le tableau de valeurs obtenues par échantillonnage d'une
     * fonction gaussienne à deux dimensions dont la dimension est calculée à
     * partir du rayon de floutage fourni.
     * 
     * @param radius
     *            le rayon de floutage
     * @return le tableau des poids des pixels autour du pixel considéré
     */
    private float[] shadingKernel(float radius) {
        double sigmaSquared = Math.pow(radius / 3f, 2);
        int n = 2 * ((int) Math.ceil(radius)) + 1;

        float[] line = new float[n];
        // Le vecteur horizontal (égal au vecteur vertical transposé) est
        // symétrique. Il suffit donc de (tailleTableau - 1) / 2 accès et
        // calculs pour le remplir
        int indexBase = (n - 1) / 2;
        float totalWeight = line[indexBase] = 1f;
        for (int i = 1; i <= indexBase; i++) {
            float weight = (float) Math.exp(-i * i / (2 * sigmaSquared));
            line[indexBase + i] = line[indexBase - i] = weight;
            totalWeight += 2 * weight;
        }
        // Normalisation du vecteur
        for (int i = 0; i < line.length; ++i) {
            line[i] /= totalWeight;
        }
        return line;
    }

    /**
     * Retourne l'image de relief floutée. Le floutage se fait au moyen de la
     * classe {@link java.awt.image.ConvolveOp ConvolveOp}. Pour éviter des
     * ennuyants calculs d'un {@link java.awt.image.Kernel Kernel} matriciel, on
     * utilise une fois un vecteur et une deuxième fois le même vecteur
     * transposé, construits à l'aide du {@link java.awt.image.Kernel#Kernel
     * constructeur} de la classe.
     * 
     * @param image
     *            l'image à flouter
     * @param kernel
     *            le tableau des poids des pixels autour du pixel considéré
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
