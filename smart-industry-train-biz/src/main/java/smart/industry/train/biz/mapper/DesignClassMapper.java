package smart.industry.train.biz.mapper;

import smart.industry.train.biz.entity.DesignClass;
import smart.industry.train.biz.interfaces.IMapper;

import java.util.List;

public interface DesignClassMapper extends IMapper<DesignClass> {
    int deleteByPrimaryKey(Integer id);

    int deleteByBatch(List<Integer> ids);

    int insert(DesignClass record);

    int insertSelective(DesignClass record);

    DesignClass selectByPrimaryKey(Integer id);

    List<DesignClass> selectAll();

    List<DesignClass> selectByCon(DesignClass record);

    int updateByPrimaryKeySelective(DesignClass record);

    int updateByPrimaryKey(DesignClass record);
}