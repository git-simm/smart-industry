package smart.industry.train.biz.dao;

import org.springframework.stereotype.Service;
import smart.industry.train.biz.dao.base.BaseBiz;
import smart.industry.train.biz.entity.ParamGroup;
import smart.industry.train.biz.entity.base.Paging;
import smart.industry.train.biz.mapper.ParamGroupMapper;

@Service
public class ParamGroupBiz extends BaseBiz<ParamGroupMapper,ParamGroup> {
    @Override
    public ParamGroup getFilter(Paging paging) throws Exception {
        ParamGroup filter = ParamGroup.class.newInstance();
        filter.setName(paging.getSearchKey());
        filter.setFilter("name like concat('%',#{name},'%')");
        return filter;
    }
}
