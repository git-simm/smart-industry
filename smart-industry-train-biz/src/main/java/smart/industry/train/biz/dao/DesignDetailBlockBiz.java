package smart.industry.train.biz.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import smart.industry.train.biz.dao.base.BaseBiz;
import smart.industry.train.biz.entity.DesignBlockAttr;
import smart.industry.train.biz.entity.DesignDetailBlock;
import smart.industry.train.biz.entity.base.Paging;
import smart.industry.train.biz.mapper.DesignDetailBlockMapper;
import smart.industry.utils.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class DesignDetailBlockBiz extends BaseBiz<DesignDetailBlockMapper,DesignDetailBlock> {
    @Autowired
    private DesignBlockAttrBiz designBlockAttrBiz;

    @Override
    public DesignDetailBlock getFilter(Paging paging) throws Exception {
        return null;
    }
    /**
     * 获取block列表
     * @param detailId
     * @return
     */
    public List<DesignDetailBlock> getListByDetailId(Integer detailId) {
        DesignDetailBlock filter = new DesignDetailBlock();
        filter.setDetailId(detailId);
        filter.setFilter("detailId=#{detailId}");
        return selectByFilter(filter);
    }
    /**
     * 获取元器件的跳转关系
     * @param detailId
     * @return
     */
    @Transactional
    public Map<String,String> getLinkMap(Integer detailId){
        List<DesignDetailBlock> blocks = getListByDetailId(detailId);
        List<DesignBlockAttr> blockAttrs = designBlockAttrBiz.getBlockLink(detailId);
        Map<String, String> map = new HashMap<>();
        for (DesignBlockAttr attr:blockAttrs) {
            Optional<DesignDetailBlock> blockOptional = blocks.stream().filter(a->a.getId().equals(attr.getBlockId())).findFirst();
            if(!blockOptional.isPresent()) continue;
            DesignDetailBlock block = blockOptional.get();
            String fileName = resolveFileName(attr.getValue());
            if(StringUtils.isNotBlank(fileName)){
                map.put(block.getName(),fileName);
            }
        }
        return map;
    }
    /**
     * 正则匹配
     */
    private static String regex = "(?<=/).+(?=\\.)";
    /**
     * 解析文件名
     * @param val
     * @return
     */
    public String resolveFileName(String val){
        Pattern pattern= Pattern.compile(regex);
        Matcher m = pattern.matcher(val);
        if(m.find()) return m.group();
        return null;
    }
}
