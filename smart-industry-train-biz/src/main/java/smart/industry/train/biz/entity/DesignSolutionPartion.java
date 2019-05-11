package smart.industry.train.biz.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import smart.industry.train.biz.entity.base.BaseEntity;

import java.util.Date;
import java.util.List;

/**
 * 测试方案
 */
@TableName("design_solution_partion")
public class DesignSolutionPartion extends BaseEntity {
    @TableId(type=IdType.AUTO)
    private Integer id;

    private String name;
    @TableField(value = "solutionId")
    private Integer solutionId;

    private String remark;
    @TableField(exist = false)
    private List<DesignSolutionPartionList> partionList;

    public List<DesignSolutionPartionList> getPartionList() {
        return partionList;
    }

    public void setPartionList(List<DesignSolutionPartionList> partionList) {
        this.partionList = partionList;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSolutionId() {
        return solutionId;
    }

    public void setSolutionId(Integer solutionId) {
        this.solutionId = solutionId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}