package smart.industry.train.biz.dao;

import org.springframework.stereotype.Service;
import smart.industry.train.biz.dao.base.BaseBiz;
import smart.industry.train.biz.entity.DesignSolution;
import smart.industry.train.biz.entity.base.Paging;
import smart.industry.train.biz.mapper.DesignSolutionMapper;

import java.util.List;

/**
 * 设计方案业务逻辑层
 */
@Service
public class DesignSolutionBiz extends BaseBiz<DesignSolutionMapper,DesignSolution> {

    @Override
    public DesignSolution getFilter(Paging paging) throws IllegalAccessException, InstantiationException {
        DesignSolution filter = DesignSolution.class.newInstance();
        filter.setName(paging.getSearchKey());
        filter.setFilter("name like concat('%',#{name},'%')");
        return filter;
    }

    /**
     * 获取方案列表
     * @param classId
     * @return
     */
    public List<DesignSolution> getListByClassId(Integer classId) {
        DesignSolution filter = new DesignSolution();
        filter.setClassId(classId);
        filter.setFilter("classId=#{classId}");
        return selectByFilter(filter);
    }
}
