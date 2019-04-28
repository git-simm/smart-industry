package smart.industry.train.biz.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import smart.industry.train.biz.entity.base.BaseEntity;

/**
 * 组织架构命名
 */
@Data
public class Organization extends BaseEntity {
    @TableId(type=IdType.AUTO)
    private Integer id;
    private String name;
    private String code;
    private Integer orgType;
    private Integer pId;
    private String sort;
}
