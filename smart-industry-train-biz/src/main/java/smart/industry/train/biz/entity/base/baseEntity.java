package smart.industry.train.biz.entity.base;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 基础实体
 */
@Getter
@Setter
public class baseEntity {
    protected Integer id;
    protected Date createdate;
    protected Integer createby;
    protected Date modifydate;
    protected Integer modifyby;
}
