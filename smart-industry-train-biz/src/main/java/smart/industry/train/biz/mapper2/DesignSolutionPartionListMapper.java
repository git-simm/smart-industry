package smart.industry.train.biz.mapper2;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import smart.industry.train.biz.entity.DesignSolutionPartion;
import smart.industry.train.biz.entity.DesignSolutionPartionList;

import java.util.List;

/**
 * 测试方案列表mapper
 */
public interface DesignSolutionPartionListMapper extends BaseMapper<DesignSolutionPartionList> {
    List<DesignSolutionPartionList> selectBySolutionId(Integer solutionId);
    List<DesignSolutionPartionList> selectByPartionId(Integer partionId);
}
