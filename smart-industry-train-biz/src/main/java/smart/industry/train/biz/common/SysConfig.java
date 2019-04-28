package smart.industry.train.biz.common;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 系统配置信息
 */
@Component
@Data
public class SysConfig {
    @Value("${application.param.resetPsw}")
    private String resetPsw;
}
