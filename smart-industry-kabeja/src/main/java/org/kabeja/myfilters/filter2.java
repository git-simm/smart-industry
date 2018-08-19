package org.kabeja.myfilters;

import org.kabeja.dxf.DXFDocument;
import org.kabeja.processing.AbstractPostProcessor;
import org.kabeja.processing.ProcessorException;

import java.util.Iterator;
import java.util.Map;

public class filter2  extends AbstractPostProcessor {
    /* (non-Javadoc)
     * @see org.kabeja.tools.PostProcessor#process(org.kabeja.dxf.DXFDocument)
     */
    public void process(DXFDocument doc, Map context) throws ProcessorException {
        Iterator i = doc.getDXFLayerIterator();
        System.out.println("myfilters - filter2 执行... ...");
    }

    /* (non-Javadoc)
     * @see org.kabeja.tools.PostProcessor#setProperties(java.util.Map)
     */
    public void setProperties(Map properties) {
        // TODO Auto-generated method stub
    }
}
