package smart.industry.train.biz.threads;

import smart.industry.train.biz.dao.ResolveBiz;
import smart.industry.train.biz.dao.SysTasksBiz;
import smart.industry.train.biz.entity.SysTasks;
import smart.industry.train.biz.enums.TaskStateEnum;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 任务解析线程
 */
public class ResolveThread extends Thread {
    //创建一个线程池，最大10个线程
    private ExecutorService executorService = Executors.newFixedThreadPool(10);
    private SysTasksBiz sysTasksBiz;
    private ResolveBiz resolveBiz;
    public ResolveThread(SysTasksBiz sysTasksBiz,ResolveBiz resolveBiz){
        this.sysTasksBiz = sysTasksBiz;
        this.resolveBiz = resolveBiz;
    }
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
            //开启线程池，多线程运行
            executorService.submit(() -> resolveBiz.resolveTask(sysTasks));
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
