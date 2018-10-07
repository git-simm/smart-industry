package smart.industry.train.biz.dao;

import org.springframework.stereotype.Service;
import smart.industry.train.biz.dao.base.BaseBiz;
import smart.industry.train.biz.entity.SysTasks;
import smart.industry.train.biz.entity.base.Paging;
import smart.industry.train.biz.mapper.SysTasksMapper;

/**
 * 系统转换任务业务逻辑层
 */
@Service
public class SysTasksBiz extends BaseBiz<SysTasksMapper,SysTasks> {
    @Override
    public SysTasks getFilter(Paging paging) throws Exception {
        return null;
    }
}
