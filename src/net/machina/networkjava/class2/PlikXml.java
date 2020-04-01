package net.machina.networkjava.class2;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.*;
import java.io.*;

public class PlikXml {

    public PlikXml() {}

    public void saveXmlFile(String name) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.newDocument();

            Element root = doc.createElement("root");
            doc.appendChild(root);

            Element personElement = doc.createElement("osoba");
            personElement.setAttribute("nr", "1");
            root.appendChild(personElement);

            Element nameElement = doc.createElement("imie");
            nameElement.setTextContent("Jan");

            Element surnameElement = doc.createElement("nazwisko");
            Text surnameTextNode = doc.createTextNode("Kowalski");
            surnameElement.appendChild(surnameTextNode);

            Element addressElement = doc.createElement("adres");

            personElement.appendChild(nameElement);
            personElement.appendChild(surnameElement);
            personElement.appendChild(addressElement);

            Element streetElement = doc.createElement("ulica");
            streetElement.setTextContent("Kwiatowa");
            Attr streetNoAttr = doc.createAttribute("nr");
            streetNoAttr.setValue("12");
            Attr streetApartNoAttr = doc.createAttribute("nrlok");
            streetApartNoAttr.setValue("2");
            streetElement.setAttributeNode(streetNoAttr);
            streetElement.setAttributeNode(streetApartNoAttr);

            Element cityElement = doc.createElement("miasto");
            cityElement.setTextContent("Kraków");
            cityElement.setAttribute("kodpoczt", "30-123");

            addressElement.appendChild(streetElement);
            addressElement.appendChild(cityElement);

            Transformer t = TransformerFactory.newInstance().newTransformer();
            t.setOutputProperty(OutputKeys.METHOD, "XML");
            t.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            t.setOutputProperty(OutputKeys.INDENT, "yes");

            StringWriter sw = new StringWriter();
            t.transform(new DOMSource(doc), new StreamResult(sw));
            System.out.println(sw);
            t.transform(new DOMSource(doc), new StreamResult(new FileOutputStream(name)));
        } catch (ParserConfigurationException | TransformerException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void readXmlFile(String name) {
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            FileInputStream f = new FileInputStream(name);
            Document doc = builder.parse(f);
//            Element root = doc.getDocumentElement();
//            NodeList personList = root.getElementsByTagName("adres");
//            System.out.println("Pobrano " + personList.getLength() + " elementow");
//            Node tmpPerson = personList.item(0);
//            NodeList childrenNodes = tmpPerson.getChildNodes();
//            for(int i=0; i<childrenNodes.getLength(); i++) {
//                Node current = childrenNodes.item(i);
//                if(current instanceof Element) {
//                    Element tmp = (Element) current;
//                    System.out.println(tmp.getNodeName() + " -> " + tmp.getTextContent());
//                    if(tmp.hasAttributes()) {
//                        NamedNodeMap attrMap = tmp.getAttributes();
//                        for(int j = 0; j < attrMap.getLength(); j++) {
//                            System.out.println(attrMap.item(j).getNodeName() + ": " + attrMap.item(j).getNodeValue());
//                        }
//                    }
//                    System.out.println("----------------");
//                }
//            }

            XPath xpath = XPathFactory.newInstance().newXPath();
            XPathExpression expr = xpath.compile("//Rate[Mid > 1.5 and Mid < 4.2]");
            NodeList list = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
            int elemNumber = list.getLength();
            System.out.println("Pobrano " + elemNumber + " elementow");
            for(int i=0; i<list.getLength(); i++) {
                System.out.println(list.item(i).getTextContent());
            }
        } catch (ParserConfigurationException | SAXException | IOException | XPathExpressionException e) {
            e.printStackTrace();
        }
    }
}
