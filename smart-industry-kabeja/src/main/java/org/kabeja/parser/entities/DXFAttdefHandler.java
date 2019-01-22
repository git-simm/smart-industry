package org.kabeja.parser.entities;

import org.kabeja.dxf.DXFAttdef;
import org.kabeja.dxf.DXFDocument;
import org.kabeja.dxf.DXFEntity;
import org.kabeja.parser.DXFValue;

/**
 * 解析Attdef节点
 */
public class DXFAttdefHandler extends DXFTextHandler {
    public final static String ENTITY_KEY = "ATTDEF";
    public final static int GROUPCODE_NAME = 2;
    public final static int GROUPCODE_ATTR = 3;
    public final static int GROUPCODE_Flag = 70;
    public final static int GROUPCODE_VALUE = 1;

    private DXFAttdef attdef;

    public DXFAttdefHandler(){
        super();
    }
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
            case GROUPCODE_VALUE:
                attdef.setValue(value.getValue());
                break;
            case GROUPCODE_Flag:
                attdef.setFlag(value.getValue());
                break;
            default:
                super.parseGroup(groupCode, value);
        }
    }

    @Override
    public DXFEntity getDXFEntity() {
        return text;
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
        text = attdef;
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
