package smart.industry.train.biz.entity;

import smart.industry.train.biz.entity.base.BaseEntity;

import java.util.Date;

public class ParamOptions extends BaseEntity {

    private Date createDate;

    private Date modifyDate;

    private Integer groupid;

    private String value;

    private String showVal;

    private Integer sort;

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

    public Integer getGroupid() {
        return groupid;
    }

    public void setGroupid(Integer groupid) {
        this.groupid = groupid;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getShowVal() {
        return showVal;
    }

    public void setShowVal(String showVal) {
        this.showVal = showVal;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }
}