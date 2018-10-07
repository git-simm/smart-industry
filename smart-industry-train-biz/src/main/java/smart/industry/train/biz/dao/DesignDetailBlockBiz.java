package smart.industry.train.biz.dao;

import org.springframework.stereotype.Service;
import smart.industry.train.biz.dao.base.BaseBiz;
import smart.industry.train.biz.entity.DesignDetailBlock;
import smart.industry.train.biz.entity.base.Paging;
import smart.industry.train.biz.mapper.DesignDetailBlockMapper;

@Service
public class DesignDetailBlockBiz extends BaseBiz<DesignDetailBlockMapper,DesignDetailBlock> {
    @Override
    public DesignDetailBlock getFilter(Paging paging) throws Exception {
        return null;
    }
}
