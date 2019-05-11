package smart.industry.train.biz.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import smart.industry.train.biz.dao.base.BaseBiz;
import smart.industry.train.biz.entity.DesignSolutionPartion;
import smart.industry.train.biz.entity.DesignSolutionPartionList;
import smart.industry.train.biz.entity.base.Paging;
import smart.industry.train.biz.mapper2.DesignSolutionPartionListMapper;
import smart.industry.train.biz.mapper2.DesignSolutionPartionMapper;
import smart.industry.utils.StringUtils;
import smart.industry.utils.exceptions.AjaxException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DesignSolutionPartionBiz extends BaseBiz<DesignSolutionPartionMapper,DesignSolutionPartion> {
    @Autowired
    private DesignSolutionPartionListMapper designSolutionPartionListMapper;

    @Override
    public DesignSolutionPartion getFilter(Paging paging) throws Exception {
        return null;
    }

    /**
     * 获取方案对应的card列表
     * @param solutionId
     * @param id
     * @return
     */
    public List<DesignSolutionPartion> getCards(Integer solutionId,Integer id){
        List<DesignSolutionPartion> list;
        //1.获取所有的测试方案
        QueryWrapper<DesignSolutionPartion> wrapper = new QueryWrapper<>();
        wrapper.setEntity(new DesignSolutionPartion());
        wrapper.eq("solutionId",solutionId);
        if(id != null){
            wrapper.eq("id",id);
        }
        list = baseMapper.selectList(wrapper);
        //2.获取所有的测试方案列表
        List<DesignSolutionPartionList> partionList;
        if(id != null){
            partionList = designSolutionPartionListMapper.selectByPartionId(id);
        }else{
            partionList = designSolutionPartionListMapper.selectBySolutionId(solutionId);
        }
        partionList.forEach(a->{
            String fileName = a.getFileName().substring(0,a.getFileName().lastIndexOf("."));
            String projName = a.getProjFile();
            if(StringUtils.isNotBlank(projName)){
                projName = "("+ projName +")";
            }else{
                projName = "";
            }
            a.setShowName(fileName + projName);
        });
        //3.数据组装
        list.forEach(a->{
            List<DesignSolutionPartionList> temp = partionList.stream().filter(b->b.getPartionId().equals(a.getId())).collect(Collectors.toList());
            a.setPartionList(temp);
        });
        return list;
    }

    /**
     * 验证名称重复
     * @param entity
     */
    public boolean valid(DesignSolutionPartion entity) {
        QueryWrapper<DesignSolutionPartion> wrapper = new QueryWrapper<>();
        wrapper.setEntity(new DesignSolutionPartion());
        if(entity.getId()!=null){
            wrapper.ne("id",entity.getId());
        }
        wrapper.eq("name",entity.getName());

        List<DesignSolutionPartion> list = baseMapper.selectList(wrapper);
        if(CollectionUtils.isEmpty(list)) return true;
        else{
            throw new AjaxException("测试方案["+ entity.getName() +"]在数据库中已经存在，请重新输入名称");
        }
    }
}
