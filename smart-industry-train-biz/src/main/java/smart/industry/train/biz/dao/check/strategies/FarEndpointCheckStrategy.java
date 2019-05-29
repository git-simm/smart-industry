package smart.industry.train.biz.dao.check.strategies;

import com.alibaba.fastjson.JSONObject;
import smart.industry.train.biz.dao.DesignExcelListBiz;
import smart.industry.train.biz.enums.CheckRuleEnum;

import java.util.HashMap;

/**
 * 电缆线芯在端子排上过远
 * @author manman.si
 */
public class FarEndpointCheckStrategy extends CheckStrategy {
    public FarEndpointCheckStrategy() {
        super(CheckRuleEnum.FAR_ENDPOINT);
    }

    @Override
    public DesignExcelListBiz.ValidInfo validItem(JSONObject excelItem, HashMap<String, DesignExcelListBiz.ValidInfo> validMap) {
        return null;
    }
}
