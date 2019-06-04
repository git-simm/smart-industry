package smart.industry.train.biz.threads;

import smart.industry.train.biz.dao.ResolveBiz;
import smart.industry.train.biz.dao.SysTasksBiz;
import smart.industry.train.biz.entity.SysTasks;
import smart.industry.train.biz.enums.TaskStateEnum;
import smart.industry.utils.environment.EnvUtil;

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
            filter.setMachine(EnvUtil.getMachineName());
            if(filter.getMachine()==null){
                filter.setFilter("state=#{state} and machine is null");
            }else{
                filter.setFilter("state=#{state} and machine=#{machine}");
            }

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
            try{
                //1.先清理关联数据，避免死锁
                resolveBiz.delRelationData(sysTasks);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        for (SysTasks sysTasks : tasks){
            try{
                //2.开启线程池，多线程运行
                executorService.submit(() -> resolveBiz.resolveTask(sysTasks));
            }catch (Exception e){
                e.printStackTrace();
            }
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
