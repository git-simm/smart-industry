package smart.industry.train.web.config;

import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.resource.VersionResourceResolver;

import java.util.List;

@Configuration
public class MvcConfig extends WebMvcConfigurerAdapter {
    /**
     * 允许跨域请求的设置
     * @param corsRegistry
     */
    @Override
    public void addCorsMappings(CorsRegistry corsRegistry) {
        corsRegistry.addMapping("/**")
                .allowedOrigins("*");
    }

    /**
     * 静态资源拦截处理，追加版本号
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        VersionResourceResolver resourceResolver = new VersionResourceResolver();
        resourceResolver.addContentVersionStrategy("/**");

        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/")
                .resourceChain(true)
                .addResolver(resourceResolver);
    }

    /**
     * 消息的序列化转换器
     * @param converters
     */
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        //配置SpringMVC的消息序列化器
        super.configureMessageConverters(converters);
        /*
         * 1.定义一个convert消息转换对象
         * 2.添加fastJson配置信息，如：是否需要格式化返回的json数据
         * 3.在convert中添加配置信息
         * 4.将convert添加到converters当中
         */
        FastJsonHttpMessageConverter fastConverter = new FastJsonHttpDateConverter();
        converters.add(fastConverter);
    }
}
