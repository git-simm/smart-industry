package smart.industry.train.biz.mapper;

import smart.industry.train.biz.entity.DesignDetailBlock;
import smart.industry.train.biz.interfaces.IMapper;

public interface DesignDetailBlockMapper extends IMapper<DesignDetailBlock> {
    int deleteByPrimaryKey(Integer id);

    int insert(DesignDetailBlock record);

    int insertSelective(DesignDetailBlock record);

    DesignDetailBlock selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(DesignDetailBlock record);

    int updateByPrimaryKey(DesignDetailBlock record);
}