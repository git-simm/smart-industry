package smart.industry.train.biz.dao;

import org.apache.shiro.util.CollectionUtils;
import org.springframework.stereotype.Service;
import smart.industry.train.biz.dao.base.BaseBiz;
import smart.industry.train.biz.entity.ParamGroup;
import smart.industry.train.biz.entity.base.Paging;
import smart.industry.train.biz.mapper.ParamGroupMapper;
import smart.industry.utils.exceptions.AjaxException;

import java.util.List;

@Service
public class ParamGroupBiz extends BaseBiz<ParamGroupMapper,ParamGroup> {
    @Override
    public ParamGroup getFilter(Paging paging) throws Exception {
        ParamGroup filter = ParamGroup.class.newInstance();
        filter.setName(paging.getSearchKey());
        filter.setFilter("name like concat('%',#{name},'%') and valid=1");
        return filter;
    }

    /**
     * 验证参数分组是否已经存在
     * @param entity
     */
    public void valid(ParamGroup entity) {
        entity.setFilter("name=#{name} and valid=1");
        if(entity.getId()!=null){
            entity.setFilter(entity.getFilter()+" and id!=#{id}");
        }
        List<ParamGroup> list = selectByFilter(entity);
        if(CollectionUtils.isEmpty(list)) return;
        else{
            throw new AjaxException(entity.getName()+"在数据库中已经存在，请重新输入");
        }
    }
}
