package smart.industry.train.biz.entity;

import smart.industry.train.biz.entity.base.BaseEntity;

import java.util.Date;

public class DesignSolution extends BaseEntity {

    private Date createDate;

    private Date modifyDate;

    private String name;

    private Integer classId;

    private Integer billCount;

    private Integer standardCount;

    private Integer designCount;

    private String createByName;

    private String remark;

    public String getCreateByName() {
        return createByName;
    }

    public void setCreateByName(String createByName) {
        this.createByName = createByName;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getClassId() {
        return classId;
    }

    public void setClassId(Integer classId) {
        this.classId = classId;
    }

    public Integer getBillCount() {
        return billCount;
    }

    public void setBillCount(Integer billCount) {
        this.billCount = billCount;
    }

    public Integer getStandardCount() {
        return standardCount;
    }

    public void setStandardCount(Integer standardCount) {
        this.standardCount = standardCount;
    }

    public Integer getDesignCount() {
        return designCount;
    }

    public void setDesignCount(Integer designCount) {
        this.designCount = designCount;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}