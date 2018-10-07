package smart.industry.train.biz.threads;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 线程启动服务
 */
@Service
public class ThreadService {
    @Autowired
    private ResolveThread resolveThread;
    public void start(){
        /**
         * 任务解析线程
         */
        Thread thread = new Thread(resolveThread);
        thread.start();
        System.out.println("启动后台处理线程");
    }
}
