package org.kabeja.dxf;

public class DXFAttdef extends DXFText {
    public DXFAttdef() {
    }

    @Override
    public Bounds getBounds() {
        return null;
    }

    private String code;
    private String attr;
    private String value;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getAttr() {
        return attr;
    }

    public void setAttr(String attr) {
        this.attr = attr;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    /* (non-Javadoc)
     * @see de.miethxml.kabeja.dxf.DXFEntity#getType()
     */
    public String getType() {
        return DXFConstants.ENTITY_TYPE_ATTDEF;
    }

    @Override
    public double getLength() {
        return 0;
    }
}
