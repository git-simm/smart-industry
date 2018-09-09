package smart.industry.train.biz.mapper;

import smart.industry.train.biz.entity.DesignSolution;
import smart.industry.train.biz.interfaces.IMapper;

public interface DesignSolutionMapper extends IMapper<DesignSolution> {
    int deleteByPrimaryKey(Integer id);

    int insert(DesignSolution record);

    int insertSelective(DesignSolution record);

    DesignSolution selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(DesignSolution record);

    int updateByPrimaryKey(DesignSolution record);
}