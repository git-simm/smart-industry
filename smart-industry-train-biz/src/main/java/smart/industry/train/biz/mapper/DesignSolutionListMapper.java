package smart.industry.train.biz.mapper;

import smart.industry.train.biz.entity.DesignSolutionList;
import smart.industry.train.biz.interfaces.IMapper;

public interface DesignSolutionListMapper extends IMapper<DesignSolutionList> {
    int deleteByPrimaryKey(Integer id);

    int insert(DesignSolutionList record);

    int insertSelective(DesignSolutionList record);

    DesignSolutionList selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(DesignSolutionList record);

    int updateByPrimaryKey(DesignSolutionList record);
}