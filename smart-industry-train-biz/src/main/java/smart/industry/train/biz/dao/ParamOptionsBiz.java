package smart.industry.train.biz.dao;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import smart.industry.train.biz.dao.base.BaseBiz;
import smart.industry.train.biz.entity.ParamOptions;
import smart.industry.train.biz.entity.base.Paging;
import smart.industry.train.biz.mapper.ParamOptionsMapper;
import smart.industry.utils.exceptions.AjaxException;

import java.util.List;

@Service
public class ParamOptionsBiz extends BaseBiz<ParamOptionsMapper,ParamOptions> {
    @Override
    public ParamOptions getFilter(Paging paging) throws Exception {
        ParamOptions filter = ParamOptions.class.newInstance();
        filter.setGroupid(Integer.parseInt(paging.getSearchKey()));
        filter.setFilter("groupid = #{groupid}");
        return filter;
    }

    /**
     * 验证参数分组是否已经存在
     * @param entity
     */
    public void valid(ParamOptions entity) {
        entity.setFilter("value=#{value} and groupid=#{groupid}");
        if(entity.getId()!=null){
            entity.setFilter(entity.getFilter()+" and id!=#{id}");
        }
        List<ParamOptions> list = selectByFilter(entity);
        if(CollectionUtils.isEmpty(list)) return;
        else{
            throw new AjaxException(entity.getValue()+"在分组中已经存在");
        }
    }
}
