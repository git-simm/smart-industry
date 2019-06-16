package smart.industry.train.biz.dao.check.strategies;

import com.alibaba.fastjson.JSONObject;
import org.apache.logging.log4j.util.Strings;
import smart.industry.train.biz.dao.DesignExcelListBiz;
import smart.industry.train.biz.enums.CheckRuleEnum;
import smart.industry.utils.StringUtils;

import java.util.HashMap;

/**
 * 端子排过插
 * @author manman.si
 */
public class OverloadEndpointCheckStrategy extends CheckStrategy{
    public OverloadEndpointCheckStrategy() {
        super(CheckRuleEnum.OVERLOAD_ENDPOINT);
    }

    /**
     * 当【线号(Wire_Number)】不为空时，【始端名称(Dest_1_Item)】以“-TB”开头，
     *  按照【车型(Plant)】、【始端名称(Dest_1_Item)】、
     * 【始端点位(Dest_1_Connector)】分组行数小于等于5才是正常数据，大于5是异常数据并展示。
     * @param excelItem
     * @param validMap
     * @return
     */
    @Override
    public DesignExcelListBiz.ValidInfo validItem(JSONObject excelItem, HashMap<String, DesignExcelListBiz.ValidInfo> validMap) {
        DesignExcelListBiz.ValidInfo valid = new DesignExcelListBiz.ValidInfo();
        String wireNumber = excelItem.getString("Wire_Number");
        String dest1Item = excelItem.getString("Dest_1_Item");
        String plant = excelItem.getString("Plant");
        String dest1Connector = excelItem.getString("Dest_1_Connector");
        if(StringUtils.isNotBlank(wireNumber) && StringUtils.isNotBlank(dest1Item)
                && dest1Item.startsWith("-TB")){
            if(Strings.isBlank(plant)){
                plant = "";
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
            long repeaters = valid.getIds().stream().count();
            //大于5是异常数据
            valid.setValidFail(repeaters > 5);
        }
        return valid;
    }
}
