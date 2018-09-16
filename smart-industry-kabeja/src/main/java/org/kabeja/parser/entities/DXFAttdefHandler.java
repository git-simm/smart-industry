package org.kabeja.parser.entities;

import org.kabeja.dxf.DXFAttdef;
import org.kabeja.dxf.DXFDocument;
import org.kabeja.dxf.DXFEntity;
import org.kabeja.parser.DXFValue;

/**
 * 解析Attdef节点
 */
public class DXFAttdefHandler extends AbstractEntityHandler {
    public final static String ENTITY_KEY = "ATTDEF";
    private DXFAttdef attdef;
    public final static int GROUPCODE_NAME = 2;
    public final static int GROUPCODE_ATTR = 3;
    /* (non-Javadoc)
     * @see de.miethxml.kabeja.parser.table.DXFTableHandler#parseGroup(int, de.miethxml.kabeja.parser.DXFValue)
     */
    public void parseGroup(int groupCode, DXFValue value) {
        switch (groupCode) {
            case GROUPCODE_NAME:
                attdef.setCode(value.getValue());
                break;
            case GROUPCODE_ATTR:
                attdef.setAttr(value.getValue());
                break;
        }
    }

    @Override
    public DXFEntity getDXFEntity() {
        return attdef;
    }

    @Override
    public void endDXFEntity() {
        doc.addAttdef(attdef);
    }
    /*
     * (non-Javadoc)
     *
     * @see org.dxf2svg.parser.entities.EntityHandler#startParsing()
     */
    public void startDXFEntity() {
        attdef = new DXFAttdef();
        attdef.setVisibile(false);
    }
    /*
     * (non-Javadoc)
     *
     * @see org.dxf2svg.parser.entities.EntityHandler#setDXFDocument(org.dxf2svg.xml.DXFDocument)
     */
    public void setDXFDocument(DXFDocument doc) {
        this.doc = doc;
    }

    @Override
    public String getDXFEntityName() {
        return ENTITY_KEY;
    }

    /* (non-Javadoc)
     * @see org.dxf2svg.parser.entities.EntityHandler#isFollowSequence()
     */
    public boolean isFollowSequence() {
        return false;
    }
}
