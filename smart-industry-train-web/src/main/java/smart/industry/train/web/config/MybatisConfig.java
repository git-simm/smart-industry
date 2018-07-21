package smart.industry.train.web.config;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.github.pagehelper.PageInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.task.TaskExecutor;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ConcurrentTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.util.IntrospectorCleanupListener;
import smart.industry.utils.resource.MessageSourceUtils;

import javax.sql.DataSource;
import java.util.Properties;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
@MapperScan("smart.industry.train.biz.mapper")
@EnableScheduling
@EnableTransactionManagement(proxyTargetClass = true)
public class MybatisConfig {
    /**
     * github分页插件
     *
     * @return 获取github分页插件
     */
    @Bean
    PageInterceptor pageInterceptor() {
        PageInterceptor pageInterceptor = new PageInterceptor();
        Properties properties = new Properties();
        properties.setProperty("helperDialect", "mysql");
        properties.setProperty("pageSizeZero", "true");
        pageInterceptor.setProperties(properties);
        return pageInterceptor;
    }

    /**
     * 数据源
     *
     * @return 获取数据源
     */
    @Bean(name = "dataSource")
    public DataSource druidDataSource() {
        return DruidDataSourceBuilder.create().build();
    }

    /**
     * 事务驱动
     *
     * @return 获取事务驱动
     */
    @Bean(name = "transactionManager")
    public DataSourceTransactionManager dataSourceTransactionManager() {
        DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager();
        dataSourceTransactionManager.setDataSource(druidDataSource());
        return dataSourceTransactionManager;
    }

    /**
     * 获取事物模版
     * @return
     */
    public TransactionTemplate transactionTemplate(){
        TransactionTemplate transactionTemplate = new TransactionTemplate();
        transactionTemplate.setTransactionManager(dataSourceTransactionManager());
        return transactionTemplate;
    }
    /**
     * 线程池
     *
     * @return 获取线程池
     */
    @Bean(name = "taskExecutor")
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(10);
        threadPoolTaskExecutor.setMaxPoolSize(30);
        threadPoolTaskExecutor.setQueueCapacity(1000);
        threadPoolTaskExecutor.setKeepAliveSeconds(1000);
        threadPoolTaskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
        return threadPoolTaskExecutor;
    }

    /**
     * 异步顺路线程池
     *
     * @return 获取异步顺路线程池
     */
    @Bean(name = "concurrentTaskExecutor")
    public ConcurrentTaskExecutor concurrentTaskExecutor() {
        return new ConcurrentTaskExecutor();
    }

    @Bean(name = "globalValidator")
    public LocalValidatorFactoryBean localValidatorFactoryBean() {
        return new LocalValidatorFactoryBean();
    }

    @ConfigurationProperties(prefix = "spring.messages")
    @Bean(name = "messageSource")
    public ResourceBundleMessageSource messageSource() {
        return new ResourceBundleMessageSource();
    }

    @Bean(name = "messageSourceUtils")
    public MessageSourceUtils messageSourceUtils() {
        MessageSourceUtils messageSourceUtils = new MessageSourceUtils();
        messageSourceUtils.setMessageSource(messageSource());
        return messageSourceUtils;
    }

    @Bean
    public IntrospectorCleanupListener executorListener() {
        return new IntrospectorCleanupListener();
    }

}
