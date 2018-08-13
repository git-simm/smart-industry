package smart.industry.train.biz.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import smart.industry.train.biz.entity.base.BaseEntity;

public class DesignClass extends BaseEntity {
    private String name;

    @JsonProperty("pId")
    private Integer pId;

    private Integer sort;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonIgnore
    public Integer getPId() {
        return pId;
    }
    @JsonIgnore
    public void setPId(Integer pId) {
        this.pId = pId;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }
}