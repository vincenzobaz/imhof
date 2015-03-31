package ch.epfl.imhof.osm;

import java.io.InputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;

import org.xml.sax.XMLReader;
import org.xml.sax.InputSource;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import java.lang.Long;
import java.lang.Double;

import ch.epfl.imhof.PointGeo;

/**
 * Classe non-instanciable permettant de construire un objet <code>OSMMap</code>
 * (une carte OpenStreetMap) à partir de données stockées dans un fichier au
 * format OSM.
 * 
 * @author Vincenzo Bazzucchi (249733)
 * @author Nicolas Phan Van (239293)
 *
 */
public final class OSMMapReader {
    /**
     * Constructeur par défaut de la classe, privé et vide car la classe n'est
     * pas instanciable
     */
    private OSMMapReader() {
    }

    /**
     * Méthode statique qui lit un fichier OSM et construit une
     * <code>OSMMap</code> à partir de celui-ci. Décompresse le fichier avec
     * gzip si le second paramètre est vrai.
     * 
     * @param fileName
     *            le nom du fichier OSM à lire
     * @param unGZip
     *            <code>true</code> si le fichier OSM à lire est fourni sous
     *            forme d'archive à décompresser
     * @return un nouvel objet de type <code>Map</code>, construit à partir du
     *         fichier OSM donné
     * @throws IOException
     *             lève une exception en cas d'erreur d'entrée/sortie
     * @throws SAXException
     *             lève une exception en cas d'erreur dans le format du fichier
     *             XML contenant la carte
     */
    public static OSMMap readOSMFile(String fileName, boolean unGZip)
            throws IOException, SAXException {
        OSMMap.Builder mapBuilder = new OSMMap.Builder();

        try (InputStream inputStream = new FileInputStream(fileName)) {
            XMLReader reader = XMLReaderFactory.createXMLReader();

            // On définit une classe anonyme héritant de DefaultHandler pour
            // la gestion du contenu du fichier osm
            reader.setContentHandler(new DefaultHandler() {
                OSMEntity.Builder entityBuilder;

                /**
                 * Redéfinition de la méthode startElement du gestionnaire de
                 * contenu qui va créer les différents éléments constituant la
                 * carte en construction.
                 */
                @Override
                public void startElement(String uri, String lName,
                        String qName, Attributes atts) throws SAXException {
                    // Cette variable contient le id ou la référence de l'objet
                    // en train d'être construit
                    Long idOrRef = null;
                    String id = atts.getValue("id");
                    String ref = atts.getValue("ref");
                    // On vérifie d'abord si le champ id et le champ ref sont
                    // tous les deux nuls. Si ce n'est pas le cas on peut
                    // stocker dans idOrRef l'id ou la référence de l'objet en
                    // cours de construction en choisissant celui qui n'est pas
                    // nul.
                    if (!(id == null && ref == null)) {
                        idOrRef = (ref == null) ? Long.parseLong(id) : Long
                                .parseLong(ref);
                    }

                    switch (qName) {
                    case "node":
                        entityBuilder = new OSMNode.Builder(idOrRef,
                                new PointGeo(Math.toRadians(Double
                                        .parseDouble(atts.getValue("lon"))),
                                        Math.toRadians(Double.parseDouble(atts
                                                .getValue("lat")))));
                        break;
                    case "way":
                        entityBuilder = new OSMWay.Builder(idOrRef);
                        break;
                    case "nd":
                        OSMNode nodeOfWay = mapBuilder.nodeForId(idOrRef);
                        // On vérifie si le point a été déjà reçu par le
                        // bâtisseur de la map. Si c'est le cas on ajoute le
                        // point à l'entité en construction, sinon on déclare
                        // l'entité en construction incomplète.
                        if (nodeOfWay == null) {
                            entityBuilder.setIncomplete();
                        } else {
                            ((OSMWay.Builder) entityBuilder).addNode(nodeOfWay);
                        }
                        break;
                    case "relation":
                        entityBuilder = new OSMRelation.Builder(idOrRef);
                        break;
                    case "member":
                        // Chaque fois qu'on traite un member il nous faut lire
                        // son type, qui est reçu comme String, et le
                        // "convertit" en un objet de type Type (énumération
                        // définie dans la classe OSMRelation.Member)
                        // En plus on ne peut procéder à la construction des
                        // objets que si ceux-ci ne sont pas incomplets.
                        String role = atts.getValue("role");
                        switch (atts.getValue("type")) {
                        case "node":
                            OSMNode nodeOfRelation = mapBuilder
                                    .nodeForId(idOrRef);
                            if (nodeOfRelation == null) {
                                entityBuilder.setIncomplete();
                            } else if (!entityBuilder.isIncomplete()) {
                                ((OSMRelation.Builder) entityBuilder)
                                        .addMember(
                                                OSMRelation.Member.Type.NODE,
                                                role, nodeOfRelation);
                            }
                            break;
                        case "way":
                            OSMWay wayOfRelation = mapBuilder.wayForId(idOrRef);
                            if (wayOfRelation == null) {
                                entityBuilder.setIncomplete();
                            } else if (!entityBuilder.isIncomplete()) {
                                ((OSMRelation.Builder) entityBuilder)
                                        .addMember(OSMRelation.Member.Type.WAY,
                                                role, wayOfRelation);
                            }
                            break;
                        case "relation":
                            OSMRelation relationOfRelation = mapBuilder
                                    .relationForId(idOrRef);
                            if (relationOfRelation == null) {
                                entityBuilder.setIncomplete();
                            } else if (!entityBuilder.isIncomplete()) {
                                ((OSMRelation.Builder) entityBuilder)
                                        .addMember(
                                                OSMRelation.Member.Type.RELATION,
                                                role, relationOfRelation);
                            }
                            break;
                        default:
                            throw new SAXException(
                                    "Le type de membre rencontré n'est pas défini.");
                        }
                    case "tag":
                        entityBuilder.setAttribute(atts.getValue("k"),
                                atts.getValue("v"));
                        break;
                    }
                }

                /**
                 * Redéfinition de la méthode endElement du gestionnaire de
                 * contenu qui va ajouter les éléments créés par startElement à
                 * la carte en construction, s'ils sont complets.
                 * 
                 */
                @Override
                public void endElement(String uri, String lName, String qName) {
                    // Nous devons vérifier encore une fois que l'entité en
                    // cours de construction n'est pas null ou incomplète avant
                    // d'appeler sa méthode build()
                    if (entityBuilder != null && !entityBuilder.isIncomplete()) {
                        switch (qName) {
                        case "node":
                            mapBuilder
                                    .addNode(((OSMNode.Builder) entityBuilder)
                                            .build());
                            break;
                        case "way":
                            mapBuilder.addWay(((OSMWay.Builder) entityBuilder)
                                    .build());
                            break;
                        case "relation":
                            mapBuilder
                                    .addRelation(((OSMRelation.Builder) entityBuilder)
                                            .build());
                            break;
                        }
                    }
                }
            });

            // On appelle la méthode parse de reader sur le bon flot d'entrée:
            // le flot InputStream si on a un fichier osm ou le flot
            // GZInputStream(inputStream)) si on doit lire un fichier en format
            // gzip.
            if (unGZip) {
                reader.parse(new InputSource(new GZIPInputStream(inputStream)));
            } else {
                reader.parse(new InputSource(inputStream));
            }
            return mapBuilder.build();
        }
    }
}
