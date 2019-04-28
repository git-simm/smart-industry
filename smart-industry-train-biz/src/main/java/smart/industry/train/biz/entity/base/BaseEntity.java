package smart.industry.train.biz.entity.base;

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
    protected Integer id;
    protected Date createDate;
    protected Integer createBy;
    protected Date modifyDate;
    protected Integer modifyBy;
    protected String orderBy;
    protected String filter;
    //字段列表
    protected String fields;
}
