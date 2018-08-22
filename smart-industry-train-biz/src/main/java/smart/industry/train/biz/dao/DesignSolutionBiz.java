package smart.industry.train.biz.dao;

import org.apache.shiro.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import smart.industry.train.biz.dao.base.BaseBiz;
import smart.industry.train.biz.entity.DesignSolution;
import smart.industry.train.biz.entity.DesignSolutionList;
import smart.industry.train.biz.entity.base.Paging;
import smart.industry.train.biz.mapper.DesignSolutionMapper;

import java.util.List;

/**
 * 设计方案业务逻辑层
 */
@Service
public class DesignSolutionBiz extends BaseBiz<DesignSolutionMapper,DesignSolution> {
    @Autowired
    private SysUpfilesBiz sysUpfilesBiz;
    @Autowired
    private DesignSolutionListBiz designSolutionListBiz;

    @Override
    public DesignSolution getFilter(Paging paging) throws IllegalAccessException, InstantiationException {
        DesignSolution filter = DesignSolution.class.newInstance();
        filter.setName(paging.getSearchKey());
        filter.setFilter("name like concat('%',#{name},'%')");
        return filter;
    }

    /**
     * 获取方案列表
     * @param classId
     * @return
     */
    public List<DesignSolution> getListByClassId(Integer classId) {
        DesignSolution filter = new DesignSolution();
        filter.setClassId(classId);
        filter.setFilter("classId=#{classId}");
        return selectByFilter(filter);
    }

    /**
     * 删除方案相关信息
     * @param id
     * @return
     */
    @Transactional
    public Integer delSolution(Integer id){
        Integer result = this.delete(id);
        List<DesignSolutionList> files = designSolutionListBiz.getAllListBySolution(id);
        //删除方案
        result += delFiles2(files);
        return result;
    }
    /**
     * 删除文件
     * @param files
     * @return
     */
    @Transactional
    public Integer delFiles2(List<DesignSolutionList> files) {
        Integer result = 0;
        if(CollectionUtils.isEmpty(files)) return result;
        //准备删除方案列表对应的文件
        for(DesignSolutionList detail : files){
            if(detail!=null && detail.getFileId()!=null){
                result += sysUpfilesBiz.delete(detail.getFileId());
            }
            result += designSolutionListBiz.delete(detail.getId());
        }
        return result;
    }
    /**
     * 删除文件
     * @param files
     * @return
     */
    @Transactional
    public Integer delFiles(List<Integer> files) {
        Integer result = 0;
        if(CollectionUtils.isEmpty(files)) return result;
        //准备删除方案列表对应的文件
        for(Integer file : files){
            DesignSolutionList detail = designSolutionListBiz.selectByPrimaryKey(file);
            if(detail!=null && detail.getFileId()!=null){
                result += sysUpfilesBiz.delete(detail.getFileId());
            }
            result += designSolutionListBiz.delete(file);
        }
        return result;
    }
}
