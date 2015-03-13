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

public final class OSMMapReader {
    private OSMMapReader() {
    }

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
                    switch (qName) {
                    case "node":
                        newNode = new OSMNode.Builder(Long.parseLong(atts
                                .getValue("id")), new PointGeo(Math.toRadians(Double
                                .parseDouble(atts.getValue("lon"))), Math.toRadians(Double
                                .parseDouble(atts.getValue("lat")))));
                        break;
                    case "way":
                        newWay = new OSMWay.Builder(Long.parseLong(atts
                                .getValue("id")));
                        break;
                    case "nd":
                        if (mapToBe.nodeForId(Long.parseLong(atts
                                .getValue("ref"))) == null) {
                            newWay.setIncomplete();
                        } else {
                            newWay.addNode(mapToBe.nodeForId(Long
                                    .parseLong(atts.getValue("ref"))));
                        }
                        break;
                    case "relation":
                        newRelation = new OSMRelation.Builder(Long
                                .parseLong(atts.getValue("id")));
                        break;
                    case "member":
                        switch (atts.getValue("type")) {
                        case "node":
                            if (mapToBe.nodeForId(Long.parseLong(atts
                                    .getValue("ref"))) == null) {
                                newRelation.setIncomplete();
                            } else {
                                newRelation.addMember(
                                        OSMRelation.Member.Type.NODE, atts
                                                .getValue("role"), mapToBe
                                                .nodeForId(Long.parseLong(atts
                                                        .getValue("ref"))));
                            }
                            break;
                        case "way":
                            if (mapToBe.wayForId(Long.parseLong(atts
                                    .getValue("ref"))) == null) {
                                newRelation.setIncomplete();
                            } else {
                                newRelation.addMember(
                                        OSMRelation.Member.Type.WAY, atts
                                                .getValue("role"), mapToBe
                                                .wayForId(Long.parseLong(atts
                                                        .getValue("ref"))));
                            }
                            break;
                        case "relation":
                            if (mapToBe.relationForId(Long.parseLong(atts
                                    .getValue("ref"))) == null) {
                                newRelation.setIncomplete();
                            } else {
                                newRelation.addMember(
                                        OSMRelation.Member.Type.RELATION,
                                        atts.getValue("role"),
                                        mapToBe.relationForId(Long
                                                .parseLong(atts.getValue("ref"))));
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
