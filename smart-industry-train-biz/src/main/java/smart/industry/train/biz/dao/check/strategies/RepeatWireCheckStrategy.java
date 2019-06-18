package smart.industry.train.biz.dao.check.strategies;

import com.alibaba.fastjson.JSONObject;
import smart.industry.train.biz.dao.DesignExcelListBiz;
import smart.industry.train.biz.enums.CheckRuleEnum;
import smart.industry.utils.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 线号重复
 * @author manman.si
 */
public class RepeatWireCheckStrategy extends CheckStrategy {
    public RepeatWireCheckStrategy() {
        super(CheckRuleEnum.REPEAT_WIRE);
    }

    /**
     * 当【车型(Plant)】相同时，相同的【线号(Wire_Number)】只能有两条记录才是正常数据，其余的都是异常数据并展示。
     * 比如车型是“MP”，线号是“1387”只能是两条记录才是正确的，其余的都错误。
     * @param excelItem
     * @param validMap
     * @return
     */
    @Override
    public DesignExcelListBiz.ValidInfo validItem(JSONObject excelItem, HashMap<String, DesignExcelListBiz.ValidInfo> validMap) {
        DesignExcelListBiz.ValidInfo valid = new DesignExcelListBiz.ValidInfo();
        String plant = excelItem.getString("Plant");
        String wireNumber = excelItem.getString("Wire_Number");
        if(!StringUtils.isEmpty(plant) && !StringUtils.isEmpty(wireNumber)){
            String key = plant + "@@" + wireNumber;
            if(!validMap.containsKey(key)){
                validMap.put(key,valid);
            }
            valid = validMap.get(key);
            valid.getIds().add(excelItem);
        }
        return valid;
    }
    /**
     * 处理完成的回调
     * @param validMap
     * @return
     */
    @Override
    public HashMap<String,DesignExcelListBiz.ValidInfo> endCallback(HashMap<String,DesignExcelListBiz.ValidInfo> validMap) {
        //对所有的分组进行校验
        for(Map.Entry<String,DesignExcelListBiz.ValidInfo> valid : validMap.entrySet()){
            valid.getValue().setValidFail(valid.getValue().getIds().size()!=2);
        }
        return validMap;
    }
}
