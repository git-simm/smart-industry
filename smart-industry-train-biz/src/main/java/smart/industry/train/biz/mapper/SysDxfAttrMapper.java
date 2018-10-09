package smart.industry.train.biz.mapper;

import smart.industry.train.biz.entity.SysDxfAttr;
import smart.industry.train.biz.interfaces.IMapper;

public interface SysDxfAttrMapper extends IMapper<SysDxfAttr> {
    int deleteByPrimaryKey(Integer id);

    int insert(SysDxfAttr record);

    int insertSelective(SysDxfAttr record);

    SysDxfAttr selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysDxfAttr record);

    int updateByPrimaryKey(SysDxfAttr record);
}