package smart.industry.train.biz.dao;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import smart.industry.train.biz.dao.base.BaseBiz;
import smart.industry.train.biz.dao.check.strategies.CheckStrategy;
import smart.industry.train.biz.entity.CompareRecord;
import smart.industry.train.biz.entity.DesignExcelAttr;
import smart.industry.train.biz.entity.DesignExcelList;
import smart.industry.train.biz.entity.base.Paging;
import smart.industry.train.biz.enums.CheckRuleEnum;
import smart.industry.train.biz.mapper.DesignExcelListMapper;
import smart.industry.utils.StringUtils;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DesignExcelListBiz  extends BaseBiz<DesignExcelListMapper,DesignExcelList> {
    /**
     * 属性业务逻辑
     */
    @Autowired
    private DesignExcelAttrBiz designExcelAttrBiz;
    /**
     * dxf属性处理
     */
    @Autowired
    private DesignBlockAttrBiz designBlockAttrBiz;
    @Override
    public DesignExcelList getFilter(Paging paging) throws Exception {
        return null;
    }
    /**
     * 获取文件属性列表
     * @param fileId
     * @param fields
     * @return
     */
    public List<DesignExcelList> getListByFilter(Integer fileId, List<String> fields) {
        DesignExcelList filter = new DesignExcelList();
        filter.setFileId(fileId);
        filter.setFilter("fileId=#{fileId}");
        if(CollectionUtils.isNotEmpty(fields)){
            String cols = String.join(",",fields);
            filter.setFields(cols);
        }
        return selectByFilter(filter);
    }
    private static Class _clazz = DesignExcelList.class;
    private static Map<String,Field> _fields = new HashMap<>();
    private static Object _lock = new Object();

    /**
     * 获取带有缓存信息的字段信息
     * @param fieldName
     * @return
     */
    private Field getField(String fieldName){
        if(_fields.containsKey(fieldName)) return _fields.get(fieldName);
        synchronized (_lock){
            if(_fields.containsKey(fieldName)) return _fields.get(fieldName);
            Field f = null;
            try {
                f = _clazz.getDeclaredField(fieldName);
                f.setAccessible(true);
                _fields.put(fieldName,f);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
            return f;
        }
    }
    /**
     * 获取字段对应的值
     * @param entity
     * @param fieldName
     * @param <U>
     * @return
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    private <U> String getValByFieldName(U entity,String fieldName) {
        try {
            Field f = getField(fieldName);
            if(f==null) return null;
            Object val;
            val = f.get(entity);
            if(val ==null) return null;
            return val.toString();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 属性列表
     */
    private static List<String> _attrs = Arrays.asList("Item","Location","Function/Tag",
            "Representation","Wire_Number","Dest_1_Item",
            "Dest_1_Connector","Dest_1_Pin_assign","Dest_2_Item","Dest_2_Connector","Dest_2_Pin_assign");
    /**
     * 获取excel数据
     * @param fileId
     * @param solutionId
     * @return
     */
    public List<JSONObject> getExcelData(Integer fileId,Integer solutionId) {
        List<JSONObject> result = getExcelData(fileId);
        //记录信息
        List<CompareRecord> records = designBlockAttrBiz.getCompareRecordList(solutionId);
        return getComparedResult(result,records);
    }

    /**
     * 获取纯净的excel数据
     * @param fileId
     * @return
     */
    public List<JSONObject> getExcelData(Integer fileId){
        Map<String,String> attrRels = new HashMap<>();
        List<DesignExcelAttr> attrs = designExcelAttrBiz.getAttrListByFilter(fileId,_attrs);
        for (DesignExcelAttr attr: attrs ) {
            attrRels.put(attr.getAttrName(),attr.getColName());
        }
        List<DesignExcelList> list = getListByFilter(fileId, new ArrayList<>(attrRels.values()));
        List<JSONObject> result = list.stream().map(a->{
            JSONObject obj = new JSONObject();
            obj.put("id",a.getId());
            for(Map.Entry<String,String> entry: attrRels.entrySet()){
                obj.put(entry.getKey(),getValByFieldName(a,entry.getValue()));
            }
            return obj;
        }).collect(Collectors.toList());
        return result;
    }

    /**
     * excel检查
     * @param fileId
     * @param solutionId
     */
    public HashMap<String,Set<JSONObject>> excelCheck(Integer fileId, Integer solutionId){
        List<CheckStrategy> checkStrategies = new ArrayList<>();
        //遍历检查枚举类，生成校验策略列表
        for(CheckRuleEnum ruleEnum:CheckRuleEnum.values()){
            try {
                checkStrategies.add((CheckStrategy) ruleEnum.getStrategyClazz().newInstance());
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return excelCheck(fileId,solutionId,checkStrategies);
    }
    /**
     * excel检查
     * @param fileId
     * @param solutionId
     * @param checkStrategies
     */
    public HashMap<String,Set<JSONObject>> excelCheck(Integer fileId, Integer solutionId, List<CheckStrategy> checkStrategies){
        //1.先把校验策略转换成结果列表
        HashMap<String,HashMap<String,ValidInfo>> validMap = new HashMap<>();
        HashMap<String,Set<JSONObject>> resultMap = new HashMap<>();
        for (CheckStrategy checkStrategy:checkStrategies) {
            String key = checkStrategy.CHECK_RULE_ENUM.getName();
            validMap.put(key, new HashMap<>(1024));
            resultMap.put(key, new LinkedHashSet<>(1024));
        }
        //2.获取excel数据
        List<JSONObject> list = getExcelData(fileId);
        //记录信息
        for (Iterator ed = list.iterator(); ed.hasNext();) {
            JSONObject excelItem = (JSONObject) ed.next();
            //数据复制
            JSONObject excelItemCopy = JSON.parseObject(excelItem.toJSONString());
            for (CheckStrategy checkStrategy:checkStrategies) {
                String key = checkStrategy.CHECK_RULE_ENUM.getName();
                ValidInfo valid = checkStrategy.validItem(excelItemCopy,validMap.get(key));
                if(boolCompared(valid.getValidFail(),true)){
                    //验证失败的状态(即标识该字段异常)
                    excelItemCopy.put("state",1);
                }
            }
        }
        for (CheckStrategy checkStrategy:checkStrategies) {
            String key = checkStrategy.CHECK_RULE_ENUM.getName();
            HashMap<String,ValidInfo> validResult = validMap.get(key);
            Set<JSONObject> result = resultMap.get(key);
            //搜集结果
            validResult.forEach((k,v)->{
                if(boolCompared(v.getValidFail(),true)){
                    result.addAll(v.getIds());
                }
            });
        }
        return resultMap;
    }

    /**
     * 获取验证结果
     * @param fileId
     * @param solutionId
     * @return
     */
    public Object[] getValidResult(Integer fileId, Integer solutionId){
        //验证集合
        HashMap<String,ValidInfo> validMap = new HashMap<>();
        List<JSONObject> list = getExcelData(fileId);
        Set<JSONObject> result = new LinkedHashSet<>();
        //记录信息
        for (Iterator ed = list.iterator(); ed.hasNext();) {
            JSONObject excelItem = (JSONObject) ed.next();
            String dest1Item = excelItem.getString("Dest_1_Item");
            String dest1Connector = excelItem.getString("Dest_1_Connector");
            String dest1Pin = excelItem.getString("Dest_1_Pin_assign");
            Integer id = excelItem.getInteger("id");
            String key = dest1Item + "@@" + dest1Connector;
            ValidInfo valid = new ValidInfo();
            if(StringUtils.isBlank(dest1Pin)){
                valid.setValidFail(true);
            }
            if(!validMap.containsKey(key)){
                valid.setDest1Item(dest1Item).setDest1Connector(dest1Connector);
                if(dest1Pin.contains("M")){
                    valid.setDest1PinM(dest1Pin);
                }else if(dest1Pin.contains("F")){
                    valid.setDest1PinF(dest1Pin);
                }else{
                    valid.setValidFail(true);
                }
                valid.getIds().add(excelItem);
                validMap.put(key,valid);
            }else{
                valid = validMap.get(key);
                valid.getIds().add(excelItem);
                if(boolCompared(valid.getValidFail(),false)){
                    if(StringUtils.isBlank(dest1Pin)){
                        valid.setValidFail(true);
                    }else if(dest1Pin.contains("M")){
                        if(StringUtils.isBlank(valid.getDest1PinM())){
                            valid.setDest1PinM(dest1Pin);
                        }else{
                            valid.setValidFail(true);
                        }
                    }else if(dest1Pin.contains("F")){
                        valid.setDest1PinF(dest1Pin);
                        if(StringUtils.isBlank(valid.getDest1PinF())){
                            valid.setDest1PinF(dest1Pin);
                        }else{
                            valid.setValidFail(true);
                        }
                    }else{
                        valid.setValidFail(true);
                    }
                }
            }
            if(boolCompared(valid.getValidFail(),true)){
                //验证失败的状态(即标识该字段异常)
                excelItem.put("state",1);
            }
        }
        //搜集结果
        validMap.forEach((k,v)->{
            if(boolCompared(v.getValidFail(),true)){
                result.addAll(v.getIds());
            }
        });
        return result.toArray();
    }

    /**
     * 布尔值比较
     * @param b1
     * @param b2
     * @return
     */
    public boolean boolCompared(Boolean b1,Boolean b2){
        if(b1 == null){
            b1 = false;
        }
        return b1.equals(b2);
    }
    /**
     * 获取比较结果
     * @param excelData
     * @param dxfAttrs
     * @return
     */
    public List<JSONObject> getComparedResult(List<JSONObject> excelData,List<CompareRecord> dxfAttrs){
        // state: -1 在dxf中单独存在，0 正常情况，1 在excel中单独存在
        //1、查找缺失的记录(dxfAttrs有,excelData没有)
        for (Iterator it = dxfAttrs.iterator(); it.hasNext();) {
            CompareRecord record = (CompareRecord) it.next();
            boolean matched = false;
            for (Iterator ed = excelData.iterator(); ed.hasNext();) {
                JSONObject excelItem = (JSONObject) ed.next();
                //已经匹配到的记录不需要再匹配
                if(excelItem.getString("state").equals("0") ||
                        excelItem.getString("state").equals("-1")) continue;
                boolean b1 = isEqual(record.getItem(),excelItem.getString("Item"));
                boolean b2 = isEqual(record.getWireNumber(),excelItem.getString("Wire_Number"));
                boolean b3 = isEqual(record.getDestItem1(),excelItem.getString("Dest_1_Item"));
                boolean b4 = isEqual(record.getDestConnector1(),excelItem.getString("Dest_1_Connector"));
                boolean b5 = isEqual(record.getDestItem2(),excelItem.getString("Dest_2_Item"));
                boolean b6 = isEqual(record.getDestConnector2(),excelItem.getString("Dest_2_Connector"));
                if (b1 && b2 && b3 && b4 && b5 && b6) {
                    excelItem.put("state","0");
                    //找到了正常的数据后，两边都执行移除操作
                    //ed.remove();
                    matched = true;
                }
            }
            if(!matched){
                //没有匹配到，则将该记录保存下来，标记为只在dxg中存在
                JSONObject obj = new JSONObject();
                obj.put("Item",record.getItem());
                obj.put("Representation",record.getRepresentation());
                obj.put("Wire_Number",record.getWireNumber());
                obj.put("Dest_1_Item",record.getDestItem1());
                obj.put("Dest_1_Connector",record.getDestConnector1());
                obj.put("Dest_2_Item",record.getDestItem2());
                obj.put("Dest_2_Connector",record.getDestConnector2());
                obj.put("state","-1");
                excelData.add(obj);
            }
        }
        //2、查找多出的记录(excelData有,dxfAttrs没有)
        return excelData;
    }

    /**
     * 是否相等
     * @param val1
     * @param val2
     * @return
     */
    private boolean isEqual(String val1,String val2){
        if(val1 == null){
            if(val2 == null) return true;
            return false;
        }
        return val1.equals(val2);
    }


    /**
     * 验证信息
     */
    @Data
    @Accessors(chain = true)
    public static class ValidInfo{
        public ValidInfo(){
            ids = new ArrayList<>();
        }
        private String dest1Item;
        private String dest1Connector;
        private String dest1PinM;
        private String dest1PinF;
        private List<JSONObject> ids;
        private Boolean validFail;
    }
}
