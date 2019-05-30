package smart.industry.train.biz.dao.check.strategies;

import com.alibaba.fastjson.JSONObject;
import smart.industry.train.biz.dao.DesignExcelListBiz;
import smart.industry.train.biz.enums.CheckRuleEnum;
import smart.industry.utils.StringUtils;

import java.util.HashMap;
import java.util.UUID;

/**
 * 线号缺失校验规则
 * @author manman.si
 */
public class LostWireCheckStrategy extends CheckStrategy {
    public LostWireCheckStrategy() {
        super(CheckRuleEnum.LOST_WIRE);
    }

    /**
     * 当【线号(Wire_Number)】为空时，【始端名称】必须都要“-TB”开头才是正常数据，其余的都是异常数据并展示。
     * @param excelItem
     * @param validMap
     * @return
     */
    @Override
    public DesignExcelListBiz.ValidInfo validItem(JSONObject excelItem, HashMap<String, DesignExcelListBiz.ValidInfo> validMap) {
        DesignExcelListBiz.ValidInfo valid = new DesignExcelListBiz.ValidInfo();

        String wireNumber = excelItem.getString("Wire_Number");
        String dest1Location = excelItem.getString("Dest_1_Location");
        String dest1Item = excelItem.getString("Dest_1_Item");
        if(StringUtils.isEmpty(wireNumber)){
            boolean b1 = (StringUtils.isNotBlank(dest1Location) && dest1Location.startsWith("-TB")) ;
            boolean b2 = (StringUtils.isNotBlank(dest1Item) && dest1Item.startsWith("-TB")) ;
            //判断是否为空
            valid.setValidFail(!(b1 && b2));
            valid.getIds().add(excelItem);
            validMap.put(UUID.randomUUID().toString(),valid);
        }
        return valid;
    }
}
