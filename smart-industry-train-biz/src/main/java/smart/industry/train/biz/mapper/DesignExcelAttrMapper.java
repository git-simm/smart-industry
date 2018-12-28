package smart.industry.train.biz.mapper;

import smart.industry.train.biz.entity.DesignExcelAttr;
import smart.industry.train.biz.interfaces.IMapper;

public interface DesignExcelAttrMapper extends IMapper<DesignExcelAttr> {
    int deleteByPrimaryKey(Integer id);

    int insert(DesignExcelAttr record);

    int insertSelective(DesignExcelAttr record);

    DesignExcelAttr selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(DesignExcelAttr record);

    int updateByPrimaryKey(DesignExcelAttr record);
}