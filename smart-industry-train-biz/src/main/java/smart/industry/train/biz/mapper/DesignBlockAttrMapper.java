package smart.industry.train.biz.mapper;

import smart.industry.train.biz.entity.DesignBlockAttr;
import smart.industry.train.biz.interfaces.IMapper;

public interface DesignBlockAttrMapper extends IMapper<DesignBlockAttr> {
    int deleteByPrimaryKey(Integer id);

    int insert(DesignBlockAttr record);

    int insertSelective(DesignBlockAttr record);

    DesignBlockAttr selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(DesignBlockAttr record);

    int updateByPrimaryKey(DesignBlockAttr record);
}