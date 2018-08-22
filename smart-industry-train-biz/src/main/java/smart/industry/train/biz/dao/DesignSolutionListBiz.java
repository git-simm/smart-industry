package smart.industry.train.biz.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import smart.industry.train.biz.dao.base.BaseBiz;
import smart.industry.train.biz.entity.DesignSolution;
import smart.industry.train.biz.entity.DesignSolutionList;
import smart.industry.train.biz.entity.SysUpfiles;
import smart.industry.train.biz.entity.base.Paging;
import smart.industry.train.biz.mapper.DesignSolutionListMapper;
import smart.industry.train.biz.mapper.DesignSolutionMapper;

import java.util.List;

/**
 * 设计方案明细信息
 */
@Service
public class DesignSolutionListBiz extends BaseBiz<DesignSolutionListMapper,DesignSolutionList> {
    @Override
    public DesignSolutionList getFilter(Paging paging) throws IllegalAccessException, InstantiationException {
        DesignSolutionList filter = DesignSolutionList.class.newInstance();
        filter.setName(paging.getSearchKey());
        filter.setFilter("name=#{Name}");
        return filter;
    }

    /**
     * 获取方案文件列表
     * @param solutionId
     * @param type
     * @return
     */
    public List<DesignSolutionList> getListBySolution(Integer solutionId,Integer type) {
        DesignSolutionList filter = new DesignSolutionList();
        filter.setSolutionId(solutionId);
        filter.setType(type);
        filter.setFilter("solutionId=#{solutionId} and type=#{type}");
        return selectByFilter(filter);
    }

    /**
     * 获取方案下所有的文件列表
     * @param solutionId
     * @return
     */
    public List<DesignSolutionList> getAllListBySolution(Integer solutionId) {
        DesignSolutionList filter = new DesignSolutionList();
        filter.setSolutionId(solutionId);
        filter.setFilter("solutionId=#{solutionId}");
        return selectByFilter(filter);
    }
}
