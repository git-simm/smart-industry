package smart.industry.train.biz.dao;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.ListUtils;
import org.apache.poi.util.StringUtil;
import org.apache.shiro.crypto.hash.Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import smart.industry.train.biz.dao.base.BaseBiz;
import smart.industry.train.biz.entity.DesignExcelAttr;
import smart.industry.train.biz.entity.DesignExcelList;
import smart.industry.train.biz.entity.base.Paging;
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
        if(CollectionUtils.isEmpty(fields)){
            filter.setFilter("fileId=#{fileId}");
        }
        else{
            String cols = StringUtil.join(",",fields);
            filter.setFields(cols);
        }
        return selectByFilter(filter);
    }

    /**
     * 获取元器件的跳转关系
     * @param fileId
     * @return
     */
    @Transactional
    public Map<String,String> getLinkMap(Integer fileId){
        List<String> fields = Arrays.asList("Item","Representation");
        Map<String,String> attrRels = new HashMap<>();
        List<DesignExcelAttr> attrs = designExcelAttrBiz.getAttrListByFilter(fileId,fields);
        for (DesignExcelAttr attr: attrs ) {
            attrRels.put(attr.getAttrName(),attr.getColName());
        }
        List<DesignExcelList> list = getListByFilter(fileId, new ArrayList<>(attrRels.values()));
        String[] cols = (String[]) attrRels.values().toArray();
        Map<String, String> map = list.stream().collect(Collectors.toMap(
                a-> getValByFieldName(a,cols[0]),
                a->getValByFieldName(a,cols[1])));
        return map;
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
            Field f;
            f = entity.getClass().getField(fieldName);
            f.setAccessible(true);
            Object val;
            val = f.get(entity);
            if(val ==null) return null;
            return val.toString();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
 }
