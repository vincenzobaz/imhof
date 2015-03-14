package ch.epfl.imhof.osm;

import java.io.IOException;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;
import java.lang.Long;
import java.lang.Double;

import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.InputSource;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;
import org.xml.sax.Attributes;

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
                OSMWay.Builder newWay = null;
                OSMRelation.Builder newRelation = null;

                @Override
                public void startElement(String uri, String lName,
                        String qName, Attributes atts) throws SAXException {
                    Long id = Long.parseLong(atts.getValue("id"));
                    Long ref = Long.parseLong(atts.getValue("ref"));
                    switch (qName) {
                    case "node":
                        newNode = new OSMNode.Builder(id, new PointGeo(Math
                                .toRadians(Double.parseDouble(atts
                                        .getValue("lon"))), Math
                                .toRadians(Double.parseDouble(atts
                                        .getValue("lat")))));
                        break;
                    case "way":
                        newWay = new OSMWay.Builder(id);
                        break;
                    case "nd":
                        if (mapToBe.nodeForId(ref) == null) {
                            newWay.setIncomplete();
                        } else {
                            newWay.addNode(mapToBe.nodeForId(ref));
                        }
                        break;
                    case "relation":
                        newRelation = new OSMRelation.Builder(id);
                        break;
                    case "member":
                        switch (atts.getValue("type")) {
                        case "node":
                            if (mapToBe.nodeForId(ref) == null) {
                                newRelation.setIncomplete();
                            } else {
                                newRelation.addMember(
                                        OSMRelation.Member.Type.NODE, atts
                                                .getValue("role"), mapToBe
                                                .nodeForId(ref));
                            }
                            break;
                        case "way":
                            if (mapToBe.wayForId(ref) == null) {
                                newRelation.setIncomplete();
                            } else {
                                newRelation.addMember(
                                        OSMRelation.Member.Type.WAY, atts
                                                .getValue("role"), mapToBe
                                                .wayForId(ref));
                            }
                            break;
                        case "relation":
                            if (mapToBe.relationForId(ref) == null) {
                                newRelation.setIncomplete();
                            } else {
                                newRelation.addMember(
                                        OSMRelation.Member.Type.RELATION,
                                        atts.getValue("role"),
                                        mapToBe.relationForId(ref));
                            }
                            break;
                        default:
                            throw new SAXException(
                                    " le type de member rencontré n'est pas défini");
                        }
                    case "tag":
                        if (newWay == null && newRelation != null) {
                            newRelation.setAttribute(atts.getValue("k"),
                                    atts.getValue("v"));
                        } else if (newWay != null && newRelation == null) {
                            newWay.setAttribute(atts.getValue("k"),
                                    atts.getValue("v"));
                        }
                        break;
                    }
                }

                @Override
                public void endElement(String uri, String lName, String qName) {
                    switch (qName) {
                    case "node":
                        mapToBe.addNode(newNode.build());
                        break;
                    case "way":
                        if (!newWay.isIncomplete()) {
                            mapToBe.addWay(newWay.build());
                        }
                        break;
                    case "relation":
                        if (!newRelation.isIncomplete()) {
                            mapToBe.addRelation(newRelation.build());
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
