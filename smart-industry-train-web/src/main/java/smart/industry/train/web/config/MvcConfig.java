package smart.industry.train.web.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.NumberDeserializers;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.resource.VersionResourceResolver;

import java.io.IOException;
import java.nio.charset.Charset;
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
     * @param list
     */
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> list) {
        StringHttpMessageConverter stringHttpMessageConverter = new StringHttpMessageConverter(Charset.forName("UTF-8"));
        list.add(stringHttpMessageConverter);

        MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
        ObjectMapper objectMapper = mappingJackson2HttpMessageConverter.getObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Byte.class, new JsonDeserializer<Byte>() {
            @Override
            public Byte deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {

                if (p.currentToken().equals(JsonToken.VALUE_FALSE)) {
                    return 0;
                }
                return new NumberDeserializers.ByteDeserializer(Byte.class, null).deserialize(p, ctxt);
            }
        });
        objectMapper.registerModule(module);
        list.add(mappingJackson2HttpMessageConverter);
    }
}
