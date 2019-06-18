package smart.industry.train.biz.dao.check.strategies;

import com.alibaba.fastjson.JSONObject;
import smart.industry.train.biz.dao.DesignExcelListBiz;
import smart.industry.train.biz.enums.CheckRuleEnum;
import smart.industry.utils.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 始末端名称错误
 * @author manman.si
 */
public class ErrorLocationNameCheckStrategy extends CheckStrategy {
    public ErrorLocationNameCheckStrategy() {
        super(CheckRuleEnum.ERROR_LOCATION_NAME);
    }

    /**
     * 当【屏蔽代码(Cable)】不为空时，按照【屏蔽代码(Cable）】与【始端名称(Dest_1_Item)】的值分组的值小于等于“2”是正确的，
     * 否则为错误的。比如【屏蔽代码(Cable)】的值为“BL0001”，
     * 则【始端名称(Dest_1_Item)】的值最多有两种（“-04JC21_1“）才是正确的，其余的都错误。
     * @param excelItem
     * @param validMap
     * @return
     */
    @Override
    public DesignExcelListBiz.ValidInfo validItem(JSONObject excelItem, HashMap<String, DesignExcelListBiz.ValidInfo> validMap) {
        DesignExcelListBiz.ValidInfo valid = new DesignExcelListBiz.ValidInfo();
        String cable = excelItem.getString("Cable");
        String dest1Item = excelItem.getString("Dest_1_Item");
        if(!StringUtils.isEmpty(cable) && !StringUtils.isEmpty(dest1Item)){
            String key = cable;
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
            long repeaters = valid.getValue().getIds().stream().map(a->a.getString("Dest_1_Item")).distinct().count();
            //超过一个，则认为校验失败
            valid.getValue().setValidFail(repeaters > 2);
        }
        return validMap;
    }
}
