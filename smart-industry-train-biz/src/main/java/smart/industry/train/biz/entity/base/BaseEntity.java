package smart.industry.train.biz.entity.base;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 基础实体
 */
@Data
public class BaseEntity {
    @TableField("id")
    protected Integer id;
    @TableField("createDate")
    protected Date createDate;
    @TableField("createBy")
    protected Integer createBy;
    @TableField("modifyDate")
    protected Date modifyDate;
    @TableField("modifyBy")
    protected Integer modifyBy;
    @TableField(exist = false)
    protected String orderBy;
    @TableField(exist = false)
    protected String filter;
    //字段列表
    @TableField(exist = false)
    protected String fields;
}
