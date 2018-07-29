package org.kabeja.processing.extend;

import org.kabeja.dxf.DXFDocument;
import org.kabeja.dxf.DXFEntity;
import org.kabeja.dxf.DXFLayer;
import org.kabeja.processing.AbstractPostProcessor;
import org.kabeja.processing.ProcessorException;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * PostProcessor 作为一个拦截器存在
 * 在dxf转换成xml,即将执行默认转换生成svg时。通过注入postprocessor完成对xml文件无效内容的过滤
 */
public class MyPostProcessor extends AbstractPostProcessor {
    /**
     * Postprocess the given DXFDocument
     *
     * @param doc
     * @param context
     * @throws ProcessorException
     */
    @Override
    public void process(DXFDocument doc, Map context) throws ProcessorException {
        Iterator i = doc.getDXFLayerIterator();
        while (i.hasNext()) {
            DXFLayer l = (DXFLayer) i.next();
            if (l.isVisible()) {
                Iterator inner = l.getDXFEntityTypeIterator();
                while (inner.hasNext()) {
                    String type = (String) inner.next();
                    List entities = l.getDXFEntities(type);
                    Iterator ei = entities.iterator();

                    while (ei.hasNext()) {
                        DXFEntity entity = (DXFEntity) ei.next();

                        if (!entity.isVisibile()) {
                            ei.remove();
                        }
                    }
                }
            } else {
                i.remove();
            }
        }
    }
}
