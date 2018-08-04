package smart.industry.train.biz.dao.base;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import smart.industry.train.biz.dao.PrincipalService;
import smart.industry.train.biz.entity.base.baseEntity;
import smart.industry.train.biz.interfaces.IMapper;
import java.util.Date;

public class BaseBiz<TMapper extends IMapper<TEntity>,TEntity extends baseEntity> {
    @Autowired
    protected TMapper baseMapper;
    @Autowired
    protected PrincipalService principalService;
    /**
     * 新增记录
     * @param entity
     * @return
     */
    @Transactional
    public int add(TEntity entity){
        entity.setCreateby(principalService.getCurrentUser().getId());
        entity.setCreatedate(new Date());
        return baseMapper.insertSelective(entity);
    }
    /**
     * 修改记录
     * @param entity
     * @return
     */
    @Transactional
    public int update(TEntity entity){
        entity.setModifyby(principalService.getCurrentUser().getId());
        entity.setModifydate(new Date());
        return baseMapper.updateByPrimaryKeySelective(entity);
    }

    /**
     * 删除记录
     * @param id
     * @return
     */
    @Transactional
    public int delete(Integer id){
        return baseMapper.deleteByPrimaryKey(id);
    }

    /**
     * 获取记录
     * @param id
     * @return
     */
    public TEntity selectByPrimaryKey(Integer id){
        return baseMapper.selectByPrimaryKey(id);
    }
}
