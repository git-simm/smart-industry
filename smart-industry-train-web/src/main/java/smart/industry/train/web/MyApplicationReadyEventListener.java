package smart.industry.train.web;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;
import smart.industry.train.biz.threads.ThreadService;

/**
 * 系统启动完成事件监听
 */
@Component
public class MyApplicationReadyEventListener implements ApplicationListener<ApplicationReadyEvent> {
    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        ConfigurableApplicationContext context =  applicationReadyEvent.getApplicationContext();
        context.getBean(ThreadService.class).start();
        System.out.println("start ready");
    }
}
