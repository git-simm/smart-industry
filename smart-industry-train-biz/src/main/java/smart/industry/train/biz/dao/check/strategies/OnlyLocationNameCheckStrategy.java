package smart.industry.train.biz.dao.check.strategies;

import com.alibaba.fastjson.JSONObject;
import smart.industry.train.biz.dao.DesignExcelListBiz;
import smart.industry.train.biz.enums.CheckRuleEnum;
import smart.industry.utils.StringUtils;
import java.util.HashMap;

/**
 * 始末端名称的位置唯一
 * @author manman.si
 */
public class OnlyLocationNameCheckStrategy extends CheckStrategy {
    public OnlyLocationNameCheckStrategy() {
        super(CheckRuleEnum.ONLY_LOCATION_NAME);
    }

    /**
     * 当【始端位置(Dest_1_Location)】与【始端名称(Dest_1_Item)】的值是一一对应。
     * 比如【始端位置(Dest_1_Location)】的值为“+RO_END1”，
     * 则【始端名称(Dest_1_Item)】的值必须是唯一值（-04SACV1CN12）才是正确的，其余的都错误。
     * @param excelItem
     * @param validMap
     * @return
     */
    @Override
    public DesignExcelListBiz.ValidInfo validItem(JSONObject excelItem, HashMap<String, DesignExcelListBiz.ValidInfo> validMap) {
        DesignExcelListBiz.ValidInfo valid = new DesignExcelListBiz.ValidInfo();
        String dest1Location = excelItem.getString("Dest_1_Location");
        String dest1Item = excelItem.getString("Dest_1_Item");
        if(!StringUtils.isEmpty(dest1Location) && !StringUtils.isEmpty(dest1Item)){
            String key = dest1Location + "@@" + dest1Item;
            if(!validMap.containsKey(key)){
                validMap.put(key,valid);
            }
            valid = validMap.get(key);
            valid.getIds().add(excelItem);
            //超过一个，则认为校验失败
            valid.setValidFail(valid.getIds().size() > 1);
        }
        return valid;
    }
}
