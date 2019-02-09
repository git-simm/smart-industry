package smart.industry.train.biz.dao;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import smart.industry.train.biz.dao.base.BaseBiz;
import smart.industry.train.biz.entity.CompareRecord;
import smart.industry.train.biz.entity.DesignExcelAttr;
import smart.industry.train.biz.entity.DesignExcelList;
import smart.industry.train.biz.entity.base.Paging;
import smart.industry.train.biz.mapper.DesignExcelListMapper;
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
            "Dest_1_Connector","Dest_2_Item","Dest_2_Connector");
    /**
     * 获取excel数据
     * @param fileId
     * @param solutionId
     * @return
     */
    public List<JSONObject> getExcelData(Integer fileId,Integer solutionId) {
        Map<String,String> attrRels = new HashMap<>();
        List<DesignExcelAttr> attrs = designExcelAttrBiz.getAttrListByFilter(fileId,_attrs);
        for (DesignExcelAttr attr: attrs ) {
            attrRels.put(attr.getAttrName(),attr.getColName());
        }
        List<DesignExcelList> list = getListByFilter(fileId, new ArrayList<>(attrRels.values()));
        List<JSONObject> result = list.stream().map(a->{
            JSONObject obj = new JSONObject();
            for(Map.Entry<String,String> entry: attrRels.entrySet()){
                obj.put(entry.getKey(),getValByFieldName(a,entry.getValue()));
            }
            obj.put("state","1");
            return obj;
        }).collect(Collectors.toList());
        //记录信息
        List<CompareRecord> records = designBlockAttrBiz.getCompareRecordList(solutionId);
        return getComparedResult(result,records);
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
}
