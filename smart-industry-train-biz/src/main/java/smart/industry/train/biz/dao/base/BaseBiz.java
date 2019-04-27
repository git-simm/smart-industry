package smart.industry.train.biz.dao.base;

import com.alibaba.fastjson.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import smart.industry.train.biz.dao.PrincipalService;
import smart.industry.train.biz.entity.DesignExcelList;
import smart.industry.train.biz.entity.SysDxfAttr;
import smart.industry.train.biz.entity.base.BaseEntity;
import smart.industry.train.biz.entity.base.Paging;
import smart.industry.train.biz.interfaces.IMapper;
import smart.industry.utils.StringUtils;
import java.util.Date;
import java.util.List;

/**
 * 抽象基类
 * @param <TMapper>
 * @param <TEntity>
 */
public abstract class BaseBiz<TMapper extends IMapper<TEntity>,TEntity extends BaseEntity> {
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
        entity.setCreateBy(getCurrentUserId());
        entity.setCreateDate(new Date());
        return baseMapper.insertSelective(entity);
    }
    /**
     * 修改记录
     * @param entity
     * @return
     */
    @Transactional
    public int update(TEntity entity){
        entity.setModifyBy(getCurrentUserId());
        entity.setModifyDate(new Date());
        return baseMapper.updateByPrimaryKeySelective(entity);
    }

    /**
     * 获取当前用户ID
     * @return
     */
    public Integer getCurrentUserId(){
        try{
            return principalService.getCurrentUser().getId();
        }catch(Exception e){
            System.out.println(e.getMessage());
            return 1;
        }
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
     * 删除记录
     * @param entity
     * @return
     */
    @Transactional
    public int deleteByFilter(TEntity entity){ return baseMapper.deleteByFilter(entity); }
    /**
     * 获取记录
     * @param id
     * @return
     */
    public TEntity selectByPrimaryKey(Integer id){
        return baseMapper.selectByPrimaryKey(id);
    }

    /**
     * 获取所有的记录
     * @return
     */
    public List<TEntity> selectAll(){return baseMapper.selectAll();}

    /*public List<TEntity> selectByFilter(String filter){
        return baseMapper.selectAll(filter,);
    }*/
    /**
     * 获取分页信息
     * @param paging
     * @return
     */
    public List<TEntity> selectByPage(Paging paging){
        TEntity filter = null;
        try {
            filter = getFilter(paging);
            if(StringUtils.isNotBlank(paging.getSort())){
                filter.setOrderBy(String.format("%s %s",paging.getSort(),paging.getOrder()));
            }
            return baseMapper.selectByFilter(filter);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取过滤条件
     * @param entity
     * @return
     */
    public List<TEntity> selectByFilter(TEntity entity){
        return baseMapper.selectByFilter(entity);
    }
    /**
     * 获取过滤条件
     * @param paging
     * @return
     * @throws Exception
     */
    public abstract TEntity getFilter(Paging paging) throws Exception;

    /**
     * 批量添加实体
     *
     * @param list
     */
    public void batchAdd(List<TEntity> list) {
         baseMapper.batchAdd(list);
    }
}