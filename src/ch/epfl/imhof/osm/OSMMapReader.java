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

    public static OSMMap readOSMFile(String fileName, boolean unGZip) throws IOException, SAXException {
        
        try (InputStream i = new FileInputStream(fileName)) {
            XMLReader r = XMLReaderFactory.createXMLReader();
            OSMMap.Builder mapToBe = new OSMMap.Builder();
            OSMNode.Builder newNode;
            OSMWay.Builder newWay;
            OSMRelation.Builder newRelation;
            OSMRelation.Member newMember;
            
            r.setContentHandler(new DefaultHandler() {
                
                @Override
                public void startElement(String uri, String lName,
                        String qName, Attributes atts) throws SAXException {
                    switch (qName) {
                    case "node":
                        newNode = new OSMNode.Builder(Long.parseLong(atts
                                .getValue("id")), new PointGeo(Double
                                .parseDouble(atts.getValue("lon")), Double
                                .parseDouble(atts.getValue("lat"))));
                        mapToBe.addNode(newNode.build());
                    case "way":
                        newWay = new OSMWay.Builder(Long.parseLong(atts
                                .getValue("id")));
                    case "nd":
                        newWay.addNode(mapToBe.nodeForId(Long.parseLong(atts
                                .getValue("ref"))));
                    case "relation":
                        newRelation = new OSMRelation.Builder(Long
                                .parseLong(atts.getValue("id")));
                    case "member":
                        if (atts.getValue("type").equals("way")) {
                            newMember = new OSMRelation.Member(
                                    OSMRelation.Member.Type.WAY, atts
                                            .getValue("role"), member);
                        } else if (atts.getValue("type").equals("node")) {
                            newMember = new OSMRelation.Member(
                                    OSMRelation.Member.Type.NODE, atts
                                            .getValue("role"), member);
                        } else {
                            newMember = new OSMRelation.Member(
                                    OSMRelation.Member.Type.RELATION, atts
                                            .getValue("role"), member);
                        }
                    case "tag":

                    default:
                        break;
                    }
                }
            });
            if (unGZip) {
                r.parse(new InputSource(new GZIPInputStream(i)));
            } else {
                r.parse(new InputSource(i));
            }
        }
    }
}
