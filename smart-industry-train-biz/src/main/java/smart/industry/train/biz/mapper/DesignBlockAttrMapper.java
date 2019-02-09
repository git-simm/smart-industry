package smart.industry.train.biz.mapper;

import smart.industry.train.biz.entity.CompareRecord;
import smart.industry.train.biz.entity.DesignBlockAttr;
import smart.industry.train.biz.interfaces.IMapper;

import java.util.List;

public interface DesignBlockAttrMapper extends IMapper<DesignBlockAttr> {
    int deleteByPrimaryKey(Integer id);

    int insert(DesignBlockAttr record);

    int insertSelective(DesignBlockAttr record);

    DesignBlockAttr selectByPrimaryKey(Integer id);

    /**
     * 查找方案对应的比较对象列表
     * @param solutionId
     * @return
     */
    List<CompareRecord> selectBySolutionId(Integer solutionId);

    int updateByPrimaryKeySelective(DesignBlockAttr record);

    int updateByPrimaryKey(DesignBlockAttr record);
}