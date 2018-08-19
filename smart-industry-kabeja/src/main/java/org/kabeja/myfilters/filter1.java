package org.kabeja.myfilters;

import org.kabeja.xml.AbstractSAXFilter;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class filter1 extends AbstractSAXFilter {
    public void startElement(String uri, String localName, String name,
                             Attributes atts) throws SAXException {
        System.out.println("myfilter - filter1 执行...");
        super.startElement(uri, localName, name, atts);
    }
}
