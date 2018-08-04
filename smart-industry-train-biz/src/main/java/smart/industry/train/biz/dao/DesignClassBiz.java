package smart.industry.train.biz.dao;

import org.apache.shiro.util.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import smart.industry.train.biz.dao.base.BaseBiz;
import smart.industry.train.biz.entity.DesignClass;
import smart.industry.train.biz.mapper.DesignClassMapper;
import smart.industry.utils.exceptions.AjaxException;
import java.util.Date;
import java.util.List;

/**
 * 解决方案中心服务
 */
@Service
public class DesignClassBiz extends BaseBiz<DesignClassMapper,DesignClass> {

    /**
     * 获取列表
     * @return
     */
    public List<DesignClass> getList(){
        List<DesignClass> list = baseMapper.selectAll();
        return list;
    }
    /**
     * 新增记录
     * @param entity
     * @return
     */
    @Transactional
    @Override
    public int add(DesignClass entity){
        //判断文件夹名称是否已经存在
        DesignClass filter = new DesignClass();
        filter.setName(entity.getName());
        filter.setPId(entity.getPId());
        if(!CollectionUtils.isEmpty(baseMapper.selectByCon(filter))) {
            throw new AjaxException("分类名称已经存在，无法创建");
        }
        entity.setCreateby(principalService.getCurrentUser().getId());
        entity.setCreatedate(new Date());
        //基类方法
        super.add(entity);
        return baseMapper.selectByCon(entity).get(0).getId();
    }
    /**
     * 计算排序码
     * @param list
     * @return
     */
    @Transactional
    public int calcSort(List<DesignClass> list) {
        for (DesignClass entity:list) {
            baseMapper.updateByPrimaryKeySelective(entity);
        }
        return 1;
    }

    /**
     * 批量删除
     * @param ids
     * @return
     */
    @Transactional
    public int del(List<Integer> ids) {
        return baseMapper.deleteByBatch(ids);
    }
}
