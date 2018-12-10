package smart.industry.train.biz.dao;

import org.apache.poi.util.StringUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import smart.industry.train.biz.dao.base.BaseBiz;
import smart.industry.train.biz.entity.DesignExcelAttr;
import smart.industry.train.biz.entity.DesignSolutionList;
import smart.industry.train.biz.entity.base.Paging;
import smart.industry.train.biz.mapper.DesignExcelAttrMapper;

import java.util.List;

@Service
public class DesignExcelAttrBiz extends BaseBiz<DesignExcelAttrMapper,DesignExcelAttr> {
    @Override
    public DesignExcelAttr getFilter(Paging paging) throws Exception {
        return null;
    }

    /**
     * 获取文件属性列表
     * @param fileId
     * @param fields
     * @return
     */
    public List<DesignExcelAttr> getAttrListByFilter(Integer fileId, List<String> fields) {
        DesignExcelAttr filter = new DesignExcelAttr();
        filter.setFileId(fileId);
        if(CollectionUtils.isEmpty(fields)){
            filter.setFilter("fileId=#{fileId}");
        }
        else{
            String str = StringUtil.join("','",fields);
            filter.setFilter("fileId=#{fileId} and attrName in ('"+ str +"')");
        }
        return selectByFilter(filter);
    }
}
