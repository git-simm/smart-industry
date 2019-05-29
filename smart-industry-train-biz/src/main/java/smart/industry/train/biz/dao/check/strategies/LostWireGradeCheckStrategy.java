package smart.industry.train.biz.dao.check.strategies;

import com.alibaba.fastjson.JSONObject;
import smart.industry.train.biz.dao.DesignExcelListBiz;
import smart.industry.train.biz.enums.CheckRuleEnum;
import smart.industry.utils.StringUtils;

import java.util.HashMap;
import java.util.UUID;

/**
 * 电压等级缺失
 * @author manman.si
 */
public class LostWireGradeCheckStrategy extends CheckStrategy {
    public LostWireGradeCheckStrategy() {
        super(CheckRuleEnum.LOST_WIRE_GRADE);
    }

    /**
     * 当【线号(Wire_Number)】不为空时，【电缆等级(Wire_Grade)】必须不为空才是正常数据，其余的都是异常数据并展示。
     * @param excelItem
     * @param validMap
     * @return
     */
    @Override
    public DesignExcelListBiz.ValidInfo validItem(JSONObject excelItem, HashMap<String, DesignExcelListBiz.ValidInfo> validMap) {
        DesignExcelListBiz.ValidInfo valid = new DesignExcelListBiz.ValidInfo();
        String wireNumber = excelItem.getString("Wire_Number");
        String wireGrade = excelItem.getString("Wire_Grade");
        if(!StringUtils.isEmpty(wireNumber)){
            //判断是否为空
            valid.setValidFail(StringUtils.isEmpty(wireGrade));
            valid.getIds().add(excelItem);
            validMap.put(UUID.randomUUID().toString(),valid);
        }
        return valid;
    }
}
