package smart.industry.train.biz.dao.check.strategies;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.collections.keyvalue.DefaultMapEntry;
import org.apache.commons.collections4.KeyValue;
import org.springframework.util.NumberUtils;
import smart.industry.train.biz.dao.DesignExcelListBiz;
import smart.industry.train.biz.enums.CheckRuleEnum;
import smart.industry.utils.StringUtils;

import java.security.KeyPair;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * 电缆线芯在端子排上过远
 *
 * @author manman.si
 */
public class FarEndpointCheckStrategy extends CheckStrategy {
    public FarEndpointCheckStrategy() {
        super(CheckRuleEnum.FAR_ENDPOINT);
    }

    /**
     * 当【屏蔽代码(Cable)】不为空，【始端名称(Dest_1_Item)】以“-TB”开头时，
     * 按照【【屏蔽代码(Cable)】】和【始端点位(Dest_1_Connector)】分组，获取【始端点位(Dest_1_Connector)】的最大和最小值，
     * 如果最大值与最小值的差小于等于10才是正常数据，大于10是异常数据并展示。
     *
     * @param excelItem
     * @param validMap
     * @return
     */
    @Override
    public DesignExcelListBiz.ValidInfo validItem(JSONObject excelItem, HashMap<String, DesignExcelListBiz.ValidInfo> validMap) {
        DesignExcelListBiz.ValidInfo valid = new DesignExcelListBiz.ValidInfo();
        String cable = excelItem.getString("Cable");
        String dest1Item = excelItem.getString("Dest_1_Item");
        String dest1Connector = excelItem.getString("Dest_1_Connector");
        if (StringUtils.isNotBlank(cable) && StringUtils.isNotBlank(dest1Item) && dest1Item.startsWith("-TB")) {
            if (StringUtils.isBlank(dest1Connector)) {
                dest1Connector = "";
            }
            String key = cable + "@" + dest1Connector;
            if (!validMap.containsKey(key)) {
                validMap.put(key, valid);
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
        Set<String> keys = validMap.keySet();
        List<Map.Entry<String,Integer>> keyList = new ArrayList<>();
        for (String item:keys) {
            String[] arr = item.split("@");
            String keyTemp = arr[0];
            String num = arr[1];
            if (StringUtils.isBlank(num)) continue;
            Integer val;
            try{
                val = Integer.parseInt(num);
            }catch (Exception ex){
                ex.printStackTrace();
                continue;
            }
            Map.Entry<String,Integer> keyVal = new DefaultMapEntry(keyTemp,val);
            keyList.add(keyVal);
        }
        //查找到分组中最大的值和最小的值
        Set<Map.Entry<String, Integer>> validSets = keyList.stream().filter(a -> a.getValue() != null).collect(Collectors.toSet());
        Map<String, List<Map.Entry<String, Integer>>> groups = validSets.stream().collect(Collectors.groupingBy(a -> a.getKey()));
        for (Map.Entry<String, List<Map.Entry<String, Integer>>> group: groups.entrySet()) {
            List<Map.Entry<String, Integer>> list = group.getValue();
            IntSummaryStatistics summaryStatistics = list.stream().mapToInt(a -> a.getValue()).summaryStatistics();
            Integer min = summaryStatistics.getMin();
            list.stream().forEach(a -> {
                //异常数据进行处理
                String errorKey = a.getKey() + "@" + a.getValue();
                validMap.get(errorKey).setValidFail(a.getValue() - min > 10);
            });
        }
        return validMap;
    }
}
