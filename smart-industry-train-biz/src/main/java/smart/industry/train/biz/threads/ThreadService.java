package smart.industry.train.biz.threads;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import smart.industry.train.biz.dao.ResolveBiz;
import smart.industry.train.biz.dao.SysTasksBiz;

/**
 * 线程启动服务
 */
@Service
public class ThreadService {
    @Autowired
    private SysTasksBiz sysTasksBiz;
    @Autowired
    private ResolveBiz resolveBiz;

    public void start(){
        /**
         * 任务解析线程
         */
        Thread thread = new Thread(new ResolveThread(sysTasksBiz,resolveBiz));
        thread.start();
        System.out.println("启动后台处理线程");
    }
}
