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
 * Classe non-instanciable permettant de construire une carte OpenStreetMap à
 * partir de données stockées dans un fichier au format OSM.
 * 
 * @author Vincenzo Bazzucchi (249733)
 * @author Nicolas Phan Van (239293)
 *
 */
public final class OSMMapReader {
    /**
     * Constructeur par défaut de OSMMapReader, privé et vide car la classe
     * n'est pas instanciable
     */
    private OSMMapReader() {
    }

    /**
     * Méthode statique qui lit un fichier OSM et construit une OSMMap à partir
     * de celui-ci
     * 
     * @param fileName
     *            le nom du fichier OSM à lire
     * @param unGZip
     *            booléen, true si et seulement si le fichier OSM est sous forme
     *            d'archive à décompresser
     * @return la nouvelle Map, construite à partir du fichier OSM donné
     * @throws IOException
     *             lève une exception en cas d'erreur d'entrée/sortie
     * @throws SAXException
     *             lève une exception en cas d'erreur dans le format du fichier
     *             XML contenant la carte
     */
    public static OSMMap readOSMFile(String fileName, boolean unGZip)
            throws IOException, SAXException {
        OSMMap.Builder mapToBe = new OSMMap.Builder();

        try (InputStream i = new FileInputStream(fileName)) {
            XMLReader r = XMLReaderFactory.createXMLReader();

            r.setContentHandler(new DefaultHandler() {
                OSMNode.Builder newNode;
                OSMWay.Builder newWay;
                OSMRelation.Builder newRelation;
                int entityType = -1;

                /**
                 * Redéfinition de la méthode startElement du gestionnaire de
                 * contenu qui va créer les différents éléments constituant la
                 * carte en construction.
                 */
                @Override
                public void startElement(String uri, String lName,
                        String qName, Attributes atts) throws SAXException {
                    Long idOrRef = null;
                    if (!(atts.getValue("id") == null && atts.getValue("ref") == null)) {
                        idOrRef = (atts.getValue("ref") == null) ? Long
                                .parseLong(atts.getValue("id")) : Long
                                .parseLong(atts.getValue("ref"));
                    }

                    switch (qName) {
                    case "node":
                        newNode = new OSMNode.Builder(idOrRef, new PointGeo(
                                Math.toRadians(Double.parseDouble(atts
                                        .getValue("lon"))), Math
                                        .toRadians(Double.parseDouble(atts
                                                .getValue("lat")))));
                        // 0 -> way 1 -> relation 2 -> node
                        entityType = 2;
                        break;
                    case "way":
                        newWay = new OSMWay.Builder(idOrRef);
                        entityType = 0;
                        break;
                    case "nd":
                        OSMNode nodeOfWay = mapToBe.nodeForId(idOrRef);
                        if (nodeOfWay == null) {
                            newWay.setIncomplete();
                        } else {
                            newWay.addNode(nodeOfWay);
                        }
                        break;
                    case "relation":
                        newRelation = new OSMRelation.Builder(idOrRef);
                        entityType = 1;
                        break;
                    case "member":
                        String role = atts.getValue("role");
                        switch (atts.getValue("type")) {
                        case "node":
                            OSMNode nodeOfRelation = mapToBe.nodeForId(idOrRef);
                            if (nodeOfRelation == null) {
                                newRelation.setIncomplete();
                            } else if (!newRelation.isIncomplete()) {
                                newRelation.addMember(
                                        OSMRelation.Member.Type.NODE, role,
                                        nodeOfRelation);
                            }
                            break;
                        case "way":
                            OSMWay wayOfRelation = mapToBe.wayForId(idOrRef);
                            if (wayOfRelation == null) {
                                newRelation.setIncomplete();
                            } else if (!newRelation.isIncomplete()) {
                                newRelation.addMember(
                                        OSMRelation.Member.Type.WAY, role,
                                        wayOfRelation);
                            }
                            break;
                        case "relation":
                            OSMRelation relationOfRelation = mapToBe
                                    .relationForId(idOrRef);
                            if (relationOfRelation == null) {
                                newRelation.setIncomplete();
                            } else if (!newRelation.isIncomplete()) {
                                newRelation.addMember(
                                        OSMRelation.Member.Type.RELATION, role,
                                        relationOfRelation);
                            }
                            break;
                        default:
                            throw new SAXException(
                                    "Le type de membre rencontré n'est pas défini.");
                        }
                    case "tag":
                        String key = atts.getValue("k");
                        String value = atts.getValue("v");

                        switch (entityType) {
                        case 0:
                            newWay.setAttribute(key, value);
                            break;
                        case 1:
                            newRelation.setAttribute(key, value);
                            break;
                        case 2:
                            newNode.setAttribute(key, value);
                            break;
                        }
                        break;
                    }
                }

                /**
                 * Redéfinition de la méthode endElement du gestionnaire de
                 * contenu qui va ajouter les éléments créés par startElement à
                 * la carte en construction, s'ils sont complets.
                 */
                @Override
                public void endElement(String uri, String lName, String qName) {
                    switch (qName) {
                    case "node":
                        if (newNode != null && !newNode.isIncomplete()) {
                            mapToBe.addNode(newNode.build());
                            entityType = -1;
                        }
                        break;
                    case "way":
                        if (newWay != null && !newWay.isIncomplete()) {
                            mapToBe.addWay(newWay.build());
                            entityType = -1;
                        }
                        break;
                    case "relation":
                        if (newRelation != null && !newRelation.isIncomplete()) {
                            mapToBe.addRelation(newRelation.build());
                            entityType = -1;
                        }
                        break;
                    }

                }
            });

            if (unGZip) {
                r.parse(new InputSource(new GZIPInputStream(i)));
            } else {
                r.parse(new InputSource(i));
            }
            return mapToBe.build();
        }
    }
}