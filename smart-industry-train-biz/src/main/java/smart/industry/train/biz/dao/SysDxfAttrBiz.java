package smart.industry.train.biz.dao;

import org.springframework.stereotype.Service;
import smart.industry.train.biz.dao.base.BaseBiz;
import smart.industry.train.biz.entity.SysDxfAttr;
import smart.industry.train.biz.entity.base.Paging;
import smart.industry.train.biz.mapper.SysDxfAttrMapper;

@Service
public class SysDxfAttrBiz extends BaseBiz<SysDxfAttrMapper,SysDxfAttr> {
    @Override
    public SysDxfAttr getFilter(Paging paging) throws Exception {
        return null;
    }
}
