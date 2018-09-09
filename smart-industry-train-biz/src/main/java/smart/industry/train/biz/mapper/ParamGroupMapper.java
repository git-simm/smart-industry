package smart.industry.train.biz.mapper;

import smart.industry.train.biz.entity.ParamGroup;
import smart.industry.train.biz.interfaces.IMapper;

public interface ParamGroupMapper extends IMapper<ParamGroup> {
    int deleteByPrimaryKey(Integer id);

    int insert(ParamGroup record);

    int insertSelective(ParamGroup record);

    ParamGroup selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ParamGroup record);

    int updateByPrimaryKey(ParamGroup record);
}