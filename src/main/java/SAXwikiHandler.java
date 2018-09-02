import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

public class SAXwikiHandler extends DefaultHandler
{
    Page currPg;
    StringBuffer buff;
    public static int counter = 0, tag_flag = -1;
    private String last_qName;

    public void readDatafromXML(String filename) throws SAXException, IOException, ParserConfigurationException
    {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser parser = factory.newSAXParser();
        parser.parse(new File(filename), this);
    }

    @Override
    public void startDocument() throws SAXException
    {
        new GlobalHash();
        System.out.print("\n\t PARSING XML FILE...");
    }

    @Override
    public void endDocument() throws SAXException
    {
        System.out.println(".. COMPLETED");
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException
    {
        if ("page".equals(qName))
        {
            currPg = new Page();
            tag_flag = 0;
        }
        else if ("title".equals(qName))
            tag_flag = 1;
        else if ("id".equals(qName) && "ns".equals(last_qName))
            tag_flag = 2;
        else if ("text".equals(qName))
            tag_flag = 3;

        if (tag_flag > 0)
            buff = new StringBuffer();
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException
    {
        if ("title".equals(qName))
            currPg.setTitle(buff.toString());
        else if ("id".equals(qName))
            currPg.setId(buff.toString());
        else if ("text".equals(qName))
        {
            currPg.setText(buff.toString());
            // MULTITHREADING ENABLER
//            Thread thread = new Thread (currPg);
//            thread.start();
            currPg.run();
        }

        tag_flag = 0;
        last_qName = qName;
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException
    {
        if (tag_flag > 0)
        {
            String readText = new String(ch, start, length);
            buff.append(readText);
        }
    }
}