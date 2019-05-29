package smart.industry.train.biz.dao.check.strategies;

import com.alibaba.fastjson.JSONObject;
import smart.industry.train.biz.dao.DesignExcelListBiz;
import smart.industry.train.biz.enums.CheckRuleEnum;

import java.util.HashMap;

/**
 * 始末端连接件型号错误
 * @author manman.si
 */
public class ErrorLocationConnectCheckStrategy extends CheckStrategy {
    public ErrorLocationConnectCheckStrategy() {
        super(CheckRuleEnum.ERROR_LOCATION_CONNECT);
    }

    @Override
    public DesignExcelListBiz.ValidInfo validItem(JSONObject excelItem, HashMap<String, DesignExcelListBiz.ValidInfo> validMap) {
        return null;
    }
}
