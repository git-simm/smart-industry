package smart.industry.train.biz.dao;

import org.springframework.stereotype.Service;
import smart.industry.train.biz.dao.base.BaseBiz;
import smart.industry.train.biz.entity.DesignExcelList;
import smart.industry.train.biz.entity.base.Paging;
import smart.industry.train.biz.mapper.DesignExcelListMapper;

@Service
public class DesignExcelListBiz  extends BaseBiz<DesignExcelListMapper,DesignExcelList> {
    @Override
    public DesignExcelList getFilter(Paging paging) throws Exception {
        return null;
    }
}
