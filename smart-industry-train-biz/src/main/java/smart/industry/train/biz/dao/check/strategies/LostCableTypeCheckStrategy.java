package smart.industry.train.biz.dao.check.strategies;

import com.alibaba.fastjson.JSONObject;
import smart.industry.train.biz.dao.DesignExcelListBiz;
import smart.industry.train.biz.enums.CheckRuleEnum;

import java.util.HashMap;

/**
 * 车型缺失校验策略
 * @author manman.si
 */
public class LostCableTypeCheckStrategy extends CheckStrategy {
    public LostCableTypeCheckStrategy() {
        super(CheckRuleEnum.LOST_CABLE_TYPE);
    }

    @Override
    public DesignExcelListBiz.ValidInfo validItem(JSONObject excelItem, HashMap<String, DesignExcelListBiz.ValidInfo> validMap) {
        return null;
    }
}
