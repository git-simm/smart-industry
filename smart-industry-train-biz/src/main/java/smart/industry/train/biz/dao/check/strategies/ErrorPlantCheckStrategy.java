package smart.industry.train.biz.dao.check.strategies;

import com.alibaba.fastjson.JSONObject;
import smart.industry.train.biz.dao.DesignExcelListBiz;
import smart.industry.train.biz.enums.CheckRuleEnum;
import smart.industry.utils.StringUtils;

import java.util.HashMap;
import java.util.UUID;

/**
 * 整车车型错误
 *
 * @author manman.si
 */
public class ErrorPlantCheckStrategy extends CheckStrategy {
    public ErrorPlantCheckStrategy() {
        super(CheckRuleEnum.ERROR_PLANT);
    }

    /**
     * 当【始端车型(Dest_1_Plant)】=【末端车型（Dest_2_Plant）】时，
     * 【车型(Plant)】必须跟【始端车型(Dest_1_Plant)】和【末端车型（Dest_2_Plant）】一致。
     * 比如【始端车型(Dest_1_Plant)】和【末端车型（Dest_2_Plant）】都是"MP"，
     * 则【车型(Plant)】是“MP”才是正确的，其余的都错误。
     *
     * @param excelItem
     * @param validMap
     * @return
     */
    @Override
    public DesignExcelListBiz.ValidInfo validItem(JSONObject excelItem, HashMap<String, DesignExcelListBiz.ValidInfo> validMap) {
        DesignExcelListBiz.ValidInfo valid = new DesignExcelListBiz.ValidInfo();
        String dest1Plant = excelItem.getString("Dest_1_Plant");
        String dest2Plant = excelItem.getString("Dest_2_Plant");
        String plant = excelItem.getString("Plant");
        if (!StringUtils.isEmpty(dest1Plant) && dest1Plant.equals(dest2Plant)) {
            valid.getIds().add(excelItem);
            if (dest1Plant.startsWith("=") && !StringUtils.isEmpty(plant) && !plant.startsWith("=")) {
                plant = "=" + plant;
            }
            valid.setValidFail(!dest1Plant.equals(plant));
            validMap.put(UUID.randomUUID().toString(), valid);
        }
        return valid;
    }
}
