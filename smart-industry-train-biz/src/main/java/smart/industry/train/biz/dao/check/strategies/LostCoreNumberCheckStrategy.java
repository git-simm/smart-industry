package smart.industry.train.biz.dao.check.strategies;

import com.alibaba.fastjson.JSONObject;
import smart.industry.train.biz.dao.DesignExcelListBiz;
import smart.industry.train.biz.enums.CheckRuleEnum;
import smart.industry.utils.StringUtils;

import java.util.HashMap;
import java.util.UUID;

/**
 * 屏蔽纤芯缺失
 * @author manman.si
 */
public class LostCoreNumberCheckStrategy extends CheckStrategy {
    public LostCoreNumberCheckStrategy() {
        super(CheckRuleEnum.LOST_CORE_NUMBER);
    }

    /**
     * 当【屏蔽代码(Cable)】不为空时，【线芯(Core_Number)】必须不为空才是正常数据，其余的都是异常数据并展示。
     * @param excelItem
     * @param validMap
     * @return
     */
    @Override
    public DesignExcelListBiz.ValidInfo validItem(JSONObject excelItem, HashMap<String, DesignExcelListBiz.ValidInfo> validMap) {
        DesignExcelListBiz.ValidInfo valid = new DesignExcelListBiz.ValidInfo();
        String cable = excelItem.getString("Cable");
        String coreNumber = excelItem.getString("Core_Number");
        if(!StringUtils.isEmpty(cable)){
            //判断是否为空
            valid.setValidFail(StringUtils.isEmpty(coreNumber));
            valid.getIds().add(excelItem);
            validMap.put(UUID.randomUUID().toString(),valid);
        }
        return valid;
    }
}
