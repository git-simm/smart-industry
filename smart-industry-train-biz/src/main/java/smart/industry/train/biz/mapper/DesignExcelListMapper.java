package smart.industry.train.biz.mapper;

import smart.industry.train.biz.entity.DesignExcelList;
import smart.industry.train.biz.interfaces.IMapper;

public interface DesignExcelListMapper extends IMapper<DesignExcelList> {
    int deleteByPrimaryKey(Integer id);

    int insert(DesignExcelList record);

    int insertSelective(DesignExcelList record);

    DesignExcelList selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(DesignExcelList record);

    int updateByPrimaryKey(DesignExcelList record);
}