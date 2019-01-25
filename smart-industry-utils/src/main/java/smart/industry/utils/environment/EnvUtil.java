package smart.industry.utils.environment;

import java.util.Map;

/**
 * 环境工具
 */
public class EnvUtil {
    /**
     * 获取机器名
     * @return
     */
    public static String getMachineName(){
        Map<String, String> map = System.getenv();
        String computerName = map.get("COMPUTERNAME");// 获取计算机名
        return computerName;
    }
}
