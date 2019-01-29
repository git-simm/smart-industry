package smart.industry.train.biz.dao;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import smart.industry.train.biz.dao.base.BaseBiz;
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
     * 获取exceldata数据
     * @param fileId
     * @return
     */
    public List<JSONObject> getExcelData(Integer fileId) {
        List<String> fields = Arrays.asList("Item","Wire_Number","Dest_1_Item","Dest_1_Connector","Dest_2_Item","Dest_2_Connector");
        Map<String,String> attrRels = new HashMap<>();
        List<DesignExcelAttr> attrs = designExcelAttrBiz.getAttrListByFilter(fileId,fields);
        for (DesignExcelAttr attr: attrs ) {
            attrRels.put(attr.getAttrName(),attr.getColName());
        }
        List<DesignExcelList> list = getListByFilter(fileId, new ArrayList<>(attrRels.values()));
        //String[] cols = (String[]) attrRels.values().toArray();
        List<JSONObject> result = list.stream().map(a->{
            JSONObject obj = new JSONObject();
            for(Map.Entry<String,String> entry: attrRels.entrySet()){
                obj.put(entry.getKey(),getValByFieldName(a,entry.getValue()));
            }
            return obj;
        }).collect(Collectors.toList());
        return result;
    }
}
