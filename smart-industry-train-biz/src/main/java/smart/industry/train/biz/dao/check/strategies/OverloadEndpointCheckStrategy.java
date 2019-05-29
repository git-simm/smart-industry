package smart.industry.train.biz.dao.check.strategies;

import com.alibaba.fastjson.JSONObject;
import smart.industry.train.biz.dao.DesignExcelListBiz;
import smart.industry.train.biz.enums.CheckRuleEnum;

import java.util.HashMap;

/**
 * 端子排过插
 * @author manman.si
 */
public class OverloadEndpointCheckStrategy extends CheckStrategy{
    public OverloadEndpointCheckStrategy() {
        super(CheckRuleEnum.OVERLOAD_ENDPOINT);
    }

    @Override
    public DesignExcelListBiz.ValidInfo validItem(JSONObject excelItem, HashMap<String, DesignExcelListBiz.ValidInfo> validMap) {
        return null;
    }
}
