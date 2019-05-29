package smart.industry.train.biz.dao.check.strategies;

import com.alibaba.fastjson.JSONObject;
import smart.industry.train.biz.dao.DesignExcelListBiz;
import smart.industry.train.biz.enums.CheckRuleEnum;

import java.util.HashMap;

/**
 * 短接片PIN错误
 * @author manman.si
 */
public class ErrorShortPinCheckStrategy extends CheckStrategy {
    public ErrorShortPinCheckStrategy() {
        super(CheckRuleEnum.ERROR_SHORT_PIN,false);
    }

    @Override
    public DesignExcelListBiz.ValidInfo validItem(JSONObject excelItem, HashMap<String, DesignExcelListBiz.ValidInfo> validMap) {
        return null;
    }
}
