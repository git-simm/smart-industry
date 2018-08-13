package smart.industry.train.biz.dao;

import org.springframework.stereotype.Service;
import smart.industry.train.biz.dao.base.BaseBiz;
import smart.industry.train.biz.entity.DesignSolution;
import smart.industry.train.biz.entity.base.Paging;
import smart.industry.train.biz.mapper.DesignSolutionMapper;

/**
 * 设计方案业务逻辑层
 */
@Service
public class DesignSolutionBiz extends BaseBiz<DesignSolutionMapper,DesignSolution> {

    @Override
    public DesignSolution getFilter(Paging paging) throws IllegalAccessException, InstantiationException {
        DesignSolution filter = DesignSolution.class.newInstance();
        filter.setName(paging.getSearchKey());
        return filter;
    }
}
