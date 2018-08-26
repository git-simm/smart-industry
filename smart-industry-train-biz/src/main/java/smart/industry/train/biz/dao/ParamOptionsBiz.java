package smart.industry.train.biz.dao;

import org.springframework.stereotype.Service;
import smart.industry.train.biz.dao.base.BaseBiz;
import smart.industry.train.biz.entity.ParamOptions;
import smart.industry.train.biz.entity.base.Paging;
import smart.industry.train.biz.mapper.ParamOptionsMapper;

@Service
public class ParamOptionsBiz extends BaseBiz<ParamOptionsMapper,ParamOptions> {
    @Override
    public ParamOptions getFilter(Paging paging) throws Exception {
        ParamOptions filter = ParamOptions.class.newInstance();
        filter.setGroupid(Integer.parseInt(paging.getSearchKey()));
        filter.setFilter("groupid = #{groupid}");
        return filter;
    }
}
