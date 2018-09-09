package smart.industry.train.biz.mapper;

import smart.industry.train.biz.entity.SysUpfiles;
import smart.industry.train.biz.interfaces.IMapper;

public interface SysUpfilesMapper extends IMapper<SysUpfiles> {
    int deleteByPrimaryKey(Integer id);

    int insert(SysUpfiles record);

    int insertSelective(SysUpfiles record);

    SysUpfiles selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysUpfiles record);

    int updateByPrimaryKey(SysUpfiles record);
}