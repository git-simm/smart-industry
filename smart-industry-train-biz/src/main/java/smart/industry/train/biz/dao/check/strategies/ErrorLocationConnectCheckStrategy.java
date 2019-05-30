package smart.industry.train.biz.dao.check.strategies;

import com.alibaba.fastjson.JSONObject;
import smart.industry.train.biz.dao.DesignExcelListBiz;
import smart.industry.train.biz.enums.CheckRuleEnum;
import smart.industry.utils.StringUtils;

import java.util.HashMap;
import java.util.UUID;

/**
 * 始末端连接件型号错误
 * @author manman.si
 */
public class ErrorLocationConnectCheckStrategy extends CheckStrategy {
    public ErrorLocationConnectCheckStrategy() {
        super(CheckRuleEnum.ERROR_LOCATION_CONNECT);
    }

    /**
     * 当【始端连接线型号(Dest_1_Endwire_Type)】以“I”开头时，提供“I”后面的第一个分割项数字必须包含在【电缆型号(Cable_type)】里是正确的，否则为错误的。
     * 比如【始端连接线型号(Dest_1_Endwire_Type)】的值为“I/0.5/8”，则“0.5”必须在其【电缆型号(Cable_type)】中有包含有。
     * @param excelItem
     * @param validMap
     * @return
     */
    @Override
    public DesignExcelListBiz.ValidInfo validItem(JSONObject excelItem, HashMap<String, DesignExcelListBiz.ValidInfo> validMap) {
        DesignExcelListBiz.ValidInfo valid = new DesignExcelListBiz.ValidInfo();
        String dest1EndwireType = excelItem.getString("Dest_1_Endwire_Type");
        String cableType = excelItem.getString("Cable_type");
        if(!StringUtils.isEmpty(dest1EndwireType) && dest1EndwireType.startsWith("I")){
            //判断是否为空
            String[] arr = dest1EndwireType.split("/");
            if(arr.length>1){
                String num = arr[1];
                valid.setValidFail(StringUtils.isNotBlank(cableType) && cableType.contains(num));
            }else{
                valid.setValidFail(true);
            }
            valid.getIds().add(excelItem);
            validMap.put(UUID.randomUUID().toString(),valid);
        }
        return valid;
    }
}
