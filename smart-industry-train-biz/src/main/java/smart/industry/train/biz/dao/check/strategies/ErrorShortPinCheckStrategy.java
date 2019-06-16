package smart.industry.train.biz.dao.check.strategies;

import com.alibaba.fastjson.JSONObject;
import smart.industry.train.biz.dao.DesignExcelListBiz;
import smart.industry.train.biz.enums.CheckRuleEnum;
import smart.industry.utils.StringUtils;

import java.util.HashMap;
import java.util.UUID;

/**
 * 短接片PIN错误
 * @author manman.si
 */
public class ErrorShortPinCheckStrategy extends CheckStrategy {
    public ErrorShortPinCheckStrategy() {
        super(CheckRuleEnum.ERROR_SHORT_PIN,true);
    }

    /**
     * 当【线号(Wire_Number)】为空时，【始端名称(Dest_1_Item)】以“-TB”开头，
     * 【始端分配点（Dest_1_Pin_assign）】的值必须是“0”或“6”才是正常数据，其余的都是异常数据并展示。
     * @param excelItem
     * @param validMap
     * @return
     */
    @Override
    public DesignExcelListBiz.ValidInfo validItem(JSONObject excelItem, HashMap<String, DesignExcelListBiz.ValidInfo> validMap) {
        DesignExcelListBiz.ValidInfo valid = new DesignExcelListBiz.ValidInfo();
        String wireNumber = excelItem.getString("Wire_Number");
        String item = excelItem.getString("Dest_1_Item");
        String assign = excelItem.getString("Dest_1_Pin_assign");
        if(StringUtils.isEmpty(wireNumber) && StringUtils.isNotBlank(item)){
            boolean b1 = item.startsWith("-TB");
            boolean b2 = StringUtils.isNotBlank(assign) && (assign.equals("0") || assign.equals("6"));
            valid.setValidFail(!(b1 && b2));
            valid.getIds().add(excelItem);
            validMap.put(UUID.randomUUID().toString(),valid);
        }
        return valid;
    }
}
