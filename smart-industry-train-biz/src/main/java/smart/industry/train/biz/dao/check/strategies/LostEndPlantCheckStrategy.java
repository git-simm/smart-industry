package smart.industry.train.biz.dao.check.strategies;

import com.alibaba.fastjson.JSONObject;
import smart.industry.train.biz.dao.DesignExcelListBiz;
import smart.industry.train.biz.enums.CheckRuleEnum;
import smart.industry.utils.StringUtils;

import java.util.HashMap;
import java.util.UUID;

/**
 * 车型缺失校验策略
 * @author manman.si
 */
public class LostEndPlantCheckStrategy extends CheckStrategy {
    public LostEndPlantCheckStrategy() {
        super(CheckRuleEnum.LOST_ENDPLANT);
    }

    /**
     * 当【线号(Wire_Number)】不为空时，【始端车型(Dest_1_Plant)】或【末端车型（Dest_2_Plant）】必须不为空才是正常数据，其余的都是异常数据并展示。
     * @param excelItem
     * @param validMap
     * @return
     */
    @Override
    public DesignExcelListBiz.ValidInfo validItem(JSONObject excelItem, HashMap<String, DesignExcelListBiz.ValidInfo> validMap) {
        DesignExcelListBiz.ValidInfo valid = new DesignExcelListBiz.ValidInfo();
        String wireNumber = excelItem.getString("Wire_Number");
        String plant1 = excelItem.getString("Dest_1_Plant");
        String plant2 = excelItem.getString("Dest_2_Plant");
        if(!StringUtils.isEmpty(wireNumber)){
            //判断是否为空
            valid.setValidFail(StringUtils.isEmpty(plant1) || StringUtils.isEmpty(plant2));
            valid.getIds().add(excelItem);
            validMap.put(UUID.randomUUID().toString(),valid);
        }
        return valid;
    }
}
