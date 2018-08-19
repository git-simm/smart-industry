package smart.industry.train.web.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SimpleDateFormatSerializer;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.converter.HttpMessageNotWritableException;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;

/**
 * fastjson 日期格式转换
 */
@Configuration
public class FastJsonHttpDateConverter  extends FastJsonHttpMessageConverter {
    private static SerializeConfig mapping = new SerializeConfig();
    private static String dateFormat;
    static {
        dateFormat = "yyyy-MM-dd HH:mm:ss";
        mapping.put(Date.class, new SimpleDateFormatSerializer(dateFormat));
    }

    @Override
    protected void writeInternal(Object obj, HttpOutputMessage outputMessage)
            throws IOException, HttpMessageNotWritableException {
        // TODO Auto-generated method stub
        OutputStream out = outputMessage.getBody();
        byte[] bytes;
        if(obj instanceof String){
            bytes = obj.toString().getBytes(this.getCharset());
        }else{
            String text = JSON.toJSONString(obj, mapping, this.getFeatures());
            bytes = text.getBytes(this.getCharset());
        }
        out.write(bytes);
    }
}