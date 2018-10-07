package smart.industry.train.biz.dao;

import org.springframework.stereotype.Service;
import smart.industry.train.biz.dao.base.BaseBiz;
import smart.industry.train.biz.entity.DesignBlockAttr;
import smart.industry.train.biz.entity.base.Paging;
import smart.industry.train.biz.mapper.DesignBlockAttrMapper;

/**
 * 设计明细属性信息
 */
@Service
public class DesignBlockAttrBiz extends BaseBiz<DesignBlockAttrMapper,DesignBlockAttr> {
    @Override
    public DesignBlockAttr getFilter(Paging paging) throws Exception {
        return null;
    }
}
