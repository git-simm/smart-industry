package smart.industry.train.biz.dao.base;

import org.springframework.stereotype.Service;
import smart.industry.train.biz.entity.DesignSolutionList;
import smart.industry.train.biz.entity.base.Paging;
import smart.industry.train.biz.mapper.DesignSolutionListMapper;

/**
 * 设计方案业务逻辑层
 */
@Service
public class DesignSolutionListBiz extends BaseBiz<DesignSolutionListMapper,DesignSolutionList> {

    @Override
    public DesignSolutionList getFilter(Paging paging) throws IllegalAccessException, InstantiationException {
        DesignSolutionList filter = DesignSolutionList.class.newInstance();
        filter.setName(paging.getSearchKey());
        return filter;
    }
}
