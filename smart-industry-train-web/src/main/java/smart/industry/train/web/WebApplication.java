package smart.industry.train.web;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = "smart.industry")
@EnableScheduling
@EnableAutoConfiguration
@EnableAspectJAutoProxy
@MapperScan({"smart.industry.train.biz.mapper2","smart.industry.train.biz.mapper"})
public class WebApplication {

    public static void main(String[] args) {
        SpringApplication app =new SpringApplication(WebApplication.class);
        //添加一个启动完成后的监听事件
        app.addListeners(new MyApplicationReadyEventListener());
        app.run(args);
        //SpringApplication.run(WebApplication.class, args);
    }
}
