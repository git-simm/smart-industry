package smart.industry.train.biz.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import smart.industry.train.biz.entity.base.BaseEntity;

/**
 * 测试方案
 */
@TableName("design_solution_partion_list")
public class DesignSolutionPartionList extends BaseEntity {
    @TableId(type=IdType.AUTO)
    private Integer id;
    @TableField(value = "partionId")
    private Integer partionId;
    @TableField(value = "solutionFileId")
    private Integer solutionFileId;
    @TableField(exist = false)
    private String fileName;
    /**
     * 项目文件名
     */
    @TableField(exist = false)
    private String projFile;
    @TableField(exist = false)
    private String showName;

    public String getShowName() {
        return showName;
    }

    public void setShowName(String showName) {
        this.showName = showName;
    }

    public String getProjFile() {
        return projFile;
    }

    public void setProjFile(String projFile) {
        this.projFile = projFile;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPartionId() {
        return partionId;
    }

    public void setPartionId(Integer partionId) {
        this.partionId = partionId;
    }

    public Integer getSolutionFileId() {
        return solutionFileId;
    }

    public void setSolutionFileId(Integer solutionFileId) {
        this.solutionFileId = solutionFileId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}