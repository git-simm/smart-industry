/*
   Copyright 2008 Simon Mieth

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
package org.kabeja.svg.generators;

import org.kabeja.dxf.*;
import org.kabeja.dxf.helpers.DXFUtils;
import org.kabeja.dxf.helpers.MLineConverter;
import org.kabeja.dxf.objects.DXFMLineStyle;
import org.kabeja.math.TransformContext;
import org.kabeja.svg.*;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import java.util.Map;


public class SVGMLineGenerator extends AbstractSVGSAXGenerator {
    public void toSAX(ContentHandler handler, Map svgContext, DXFEntity entity,
                      TransformContext transformContext) throws SAXException {
        DXFMLine mline = (DXFMLine) entity;

        DXFPolyline[] pl = MLineConverter.toDXFPolyline(mline);

        DXFMLineStyle style = (DXFMLineStyle) mline.getDXFDocument()
                .getDXFObjectByID(mline.getMLineStyleID());
        SVGSAXGeneratorManager manager = (SVGSAXGeneratorManager) svgContext.get(SVGContext.SVGSAXGENERATOR_MANAGER);
        SVGPathBoundaryGenerator gen = manager.getSVGPathBoundaryGenerator(DXFConstants.ENTITY_TYPE_POLYLINE);

        if (style.isFilled()) {
            // we create a filled polyline
            StringBuffer buf = new StringBuffer();
            DXFPolyline p1 = pl[0];
            buf.append(gen.getSVGPath(p1));

            DXFPolyline p2 = pl[pl.length - 1];
            DXFUtils.reverseDXFPolyline(p2);

            String str = gen.getSVGPath(p2).trim();

            if (str.startsWith("M")) {
                buf.append(" L ");
                buf.append(str.substring(1));
            } else {
                buf.append(str);
            }

            buf.append(" z");

            AttributesImpl atts = new AttributesImpl();
            SVGUtils.addAttribute(atts, "d", buf.toString());
            SVGUtils.addAttribute(atts, SVGConstants.SVG_ATTRIBUTE_STROKE,
                    "none");
            SVGUtils.addAttribute(atts, SVGConstants.SVG_ATTRIBUTE_FILL,
                    "rgb(" + DXFColor.getRGBString(style.getFillColor()) + ")");
            SVGUtils.emptyElement(handler, SVGConstants.SVG_PATH, atts);
        }

        try {
            SVGSAXGenerator saxGenerator = manager.getSVGGenerator(DXFConstants.ENTITY_TYPE_POLYLINE);

            for (int i = 0; i < pl.length; i++) {
                saxGenerator.toSAX(handler, svgContext, pl[i], transformContext);
            }
        } catch (SVGGenerationException e) {
            throw new SAXException(e);
        }
    }
}
