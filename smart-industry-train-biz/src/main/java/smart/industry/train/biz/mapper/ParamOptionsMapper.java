package smart.industry.train.biz.mapper;

import smart.industry.train.biz.entity.ParamOptions;
import smart.industry.train.biz.interfaces.IMapper;

public interface ParamOptionsMapper extends IMapper<ParamOptions> {

    int insert(ParamOptions record);

    int insertSelective(ParamOptions record);

    int updateByPrimaryKeySelective(ParamOptions record);

    int updateByPrimaryKey(ParamOptions record);
}