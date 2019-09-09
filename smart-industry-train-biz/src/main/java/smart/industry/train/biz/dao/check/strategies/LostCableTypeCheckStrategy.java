package smart.industry.train.biz.dao.check.strategies;

import com.alibaba.fastjson.JSONObject;
import smart.industry.train.biz.dao.DesignExcelListBiz;
import smart.industry.train.biz.enums.CheckRuleEnum;
import smart.industry.utils.StringUtils;

import java.util.HashMap;
import java.util.UUID;

/**
 * 电缆型号缺失校验策略
 * @author manman.si
 */
public class LostCableTypeCheckStrategy extends CheckStrategy {
    public LostCableTypeCheckStrategy() {
        super(CheckRuleEnum.LOST_CABLE_TYPE);
    }

    /**
     * 当【线号(Wire_Number)】不为空时，【电缆型号(Cable_type)】必须不为空才是正常数据，其余的都是异常数据并展示。
     * @param excelItem
     * @param validMap
     * @return
     */
    @Override
    public DesignExcelListBiz.ValidInfo validItem(JSONObject excelItem, HashMap<String, DesignExcelListBiz.ValidInfo> validMap) {
        DesignExcelListBiz.ValidInfo valid = new DesignExcelListBiz.ValidInfo();
        String wireNumber = excelItem.getString("Wire_Number");
        String cableType = excelItem.getString("Cable_type");
        if(!StringUtils.isEmpty(wireNumber)){
            //判断是否为空
            valid.setValidFail(StringUtils.isEmpty(cableType));
            valid.getIds().add(excelItem);
            validMap.put(UUID.randomUUID().toString(),valid);
        }
        return valid;
    }
}
