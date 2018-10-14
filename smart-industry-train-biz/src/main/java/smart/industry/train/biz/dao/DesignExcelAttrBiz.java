package smart.industry.train.biz.dao;

import org.springframework.stereotype.Service;
import smart.industry.train.biz.dao.base.BaseBiz;
import smart.industry.train.biz.entity.DesignExcelAttr;
import smart.industry.train.biz.entity.base.Paging;
import smart.industry.train.biz.mapper.DesignExcelAttrMapper;

@Service
public class DesignExcelAttrBiz extends BaseBiz<DesignExcelAttrMapper,DesignExcelAttr> {
    @Override
    public DesignExcelAttr getFilter(Paging paging) throws Exception {
        return null;
    }
}
