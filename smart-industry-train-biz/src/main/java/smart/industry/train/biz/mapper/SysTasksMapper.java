package smart.industry.train.biz.mapper;

import smart.industry.train.biz.entity.SysTasks;
import smart.industry.train.biz.interfaces.IMapper;

public interface SysTasksMapper extends IMapper<SysTasks> {
    int deleteByPrimaryKey(Integer id);

    int insert(SysTasks record);

    int insertSelective(SysTasks record);

    SysTasks selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysTasks record);

    int updateByPrimaryKey(SysTasks record);
}