/*
   Copyright 2005 Simon Mieth

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/
package org.kabeja.parser.entities;

import org.kabeja.dxf.DXFAttdef;
import org.kabeja.dxf.DXFAttrib;
import org.kabeja.dxf.DXFConstants;
import org.kabeja.parser.DXFValue;
import sun.plugin.javascript.navig.Array;

import java.util.Arrays;
import java.util.List;


/**
 * @author <a href="mailto:simon.mieth@gmx.de">Simon Mieth</a>
 */
public class DXFAttribHandler extends DXFTextHandler {
    public static final int ATTRIB_VERTICAL_ALIGN = 74;
    public static final int ATTRIB_TEXT_LENGTH = 73;
    public static final int ATTRIB_CODE = 2;
    public static List<String> exceptArr = Arrays.asList(new String[]{
            "comment en","comment cn","plant","function/tag","potential",
            "suppression element","var.macro","conn.diagram symbol","release symbol","version symbol"});

    public DXFAttribHandler() {
        super();
    }

    /* (non-Javadoc)
     * @see de.miethxml.kabeja.parser.entities.DXFEntityHandler#parseGroup(int, de.miethxml.kabeja.parser.DXFValue)
     */
    public void parseGroup(int groupCode, DXFValue value) {
        switch (groupCode) {
            case ATTRIB_TEXT_LENGTH:
                //ignore not used by
                break;
            case ATTRIB_VERTICAL_ALIGN:
                text.setValign(value.getIntegerValue());
                break;
            case ATTRIB_CODE:
                DXFAttdef def = this.doc.getAttdef(value.getValue());
                if(def != null){
                    String code = this.doc.getAttdef(value.getValue()).getAttr();
                    text.setCode(code);
                    String temp = code.toLowerCase();
                    if(exceptArr.contains(temp)){
                        text.setVisibile(false);
                    }
                }
                break;
            default:
                super.parseGroup(groupCode, value);
        }
    }

    public void startDXFEntity() {
        text = new DXFAttrib();
    }

    /* (non-Javadoc)
     * @see de.miethxml.kabeja.parser.entities.DXFEntityHandler#getDXFEntityName()
     */
    public String getDXFEntityName() {
        return DXFConstants.ENTITY_TYPE_ATTRIB;
    }
}
