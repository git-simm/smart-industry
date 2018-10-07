package smart.industry.train.biz.threads;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import smart.industry.train.biz.dao.ResolveBiz;
import smart.industry.train.biz.dao.SysTasksBiz;
import smart.industry.train.biz.entity.SysTasks;
import smart.industry.train.biz.enums.TaskStateEnum;

import java.util.List;

/**
 * 任务解析线程
 */
@Component
public class ResolveThread extends Thread {
    @Autowired
    private SysTasksBiz sysTasksBiz;
    @Autowired
    private ResolveBiz resolveBiz;
    /**
     * 运行方法
     */
    @Override
    public void run() {
        while (true) {
            //1.读取数据库中待执行的任务列表
            SysTasks filter = new SysTasks();
            filter.setState(TaskStateEnum.Ready.getValue());
            filter.setFilter("state=#{state}");
            List<SysTasks> tasks = sysTasksBiz.selectByFilter(filter);
            //2.运行执行逻辑
            if (tasks.size() > 0) {
                startResolveTasks(tasks);
            }
            //3.主线程等待10秒钟
            waitLong(10000L);
        }
    }

    /**
     * 执行解析
     * @param tasks
     */
    public void startResolveTasks(List<SysTasks> tasks){
        for (SysTasks sysTasks : tasks){
            resolveBiz.resolveTask(sysTasks);
        }
    }

    /**
     * 等待时长
     * @param timespan
     */
    private void waitLong(long timespan){
        try {
            Thread.sleep(timespan);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
