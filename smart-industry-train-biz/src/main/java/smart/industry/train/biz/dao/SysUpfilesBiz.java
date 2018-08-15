package smart.industry.train.biz.dao;

import org.springframework.stereotype.Service;
import smart.industry.train.biz.dao.base.BaseBiz;
import smart.industry.train.biz.entity.SysUpfiles;
import smart.industry.train.biz.entity.base.Paging;
import smart.industry.train.biz.mapper.SysUpfilesMapper;

@Service
public class SysUpfilesBiz extends BaseBiz<SysUpfilesMapper,SysUpfiles> {
    @Override
    public SysUpfiles getFilter(Paging paging) throws Exception {
        SysUpfiles filter = SysUpfiles.class.newInstance();
        filter.setFileName(paging.getSearchKey());
        return filter;
    }
}
