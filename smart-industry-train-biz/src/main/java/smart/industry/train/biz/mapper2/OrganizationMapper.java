package smart.industry.train.biz.mapper2;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.poi.ss.formula.functions.T;
import smart.industry.train.biz.entity.Organization;

import java.util.List;

/**
 * 组织架构Mapper
 */
public interface OrganizationMapper extends BaseMapper<Organization> {
    List<Organization> getOrgList(@Param("ew") Wrapper<Organization> wrapper);
}
