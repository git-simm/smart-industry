package smart.industry.train.biz.dao.check.strategies;

import com.alibaba.fastjson.JSONObject;
import smart.industry.train.biz.dao.DesignExcelListBiz;
import smart.industry.train.biz.enums.CheckRuleEnum;

import java.util.HashMap;

/**
 * 屏蔽纤芯错误
 * @author manman.si
 */
public class HideCodeNumberCheckStrategy extends CheckStrategy{
    public HideCodeNumberCheckStrategy() {
        super(CheckRuleEnum.HIDE_CORE_NUMBER);
    }

    @Override
    public DesignExcelListBiz.ValidInfo validItem(JSONObject excelItem, HashMap<String, DesignExcelListBiz.ValidInfo> validMap) {
        return null;
    }
}
