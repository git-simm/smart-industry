package smart.industry.train.biz.dao.check.strategies;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.collections.keyvalue.DefaultMapEntry;
import org.apache.logging.log4j.util.Strings;
import smart.industry.train.biz.dao.DesignExcelListBiz;
import smart.industry.train.biz.enums.CheckRuleEnum;
import smart.industry.utils.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 连接器过插
 * @author manman.si
 */
public class OverloadConnectorCheckStrategy extends CheckStrategy {
    public OverloadConnectorCheckStrategy() {
        super(CheckRuleEnum.OVERLOAD_CONNECTOR);
    }

    /**
     * 当【始端分配点（Dest_1_Pin_assign）】等于【F,M】时，按照【车型(Plant)】、【始端名称(Dest_1_Item)】、
     * 【始端点位(Dest_1_Connector)】分组行数等于1才是正常数据，大于1是异常数据并展示。
     * @param excelItem
     * @param validMap
     * @return
     */
    @Override
    public DesignExcelListBiz.ValidInfo validItem(JSONObject excelItem, HashMap<String, DesignExcelListBiz.ValidInfo> validMap) {
        DesignExcelListBiz.ValidInfo valid = new DesignExcelListBiz.ValidInfo();
        String dest1PinAssign = excelItem.getString("Dest_1_Pin_assign");
        String plant = excelItem.getString("Plant");
        String dest1Item = excelItem.getString("Dest_1_Item");
        String dest1Connector = excelItem.getString("Dest_1_Connector");
        if(StringUtils.isNotBlank(dest1PinAssign) && dest1PinAssign.equals("F,M")){
            if(Strings.isBlank(plant)){
                plant = "";
            }
            if(Strings.isBlank(dest1Item)){
                dest1Item = "";
            }
            if(Strings.isBlank(dest1Connector)){
                dest1Connector = "";
            }
            String key = plant+"@"+dest1Item+"@"+dest1Connector;
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
            valid.getValue().setValidFail(valid.getValue().getIds().stream().count() > 1);
        }
        return validMap;
    }
}
