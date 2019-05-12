package smart.industry.train.web.controllers.manager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import smart.industry.train.biz.dao.DesignClassBiz;
import smart.industry.train.biz.dao.DesignSolutionBiz;
import smart.industry.train.biz.entity.DesignClass;
import smart.industry.train.biz.entity.DesignSolution;
import smart.industry.utils.annotations.Post;
import smart.industry.utils.exceptions.AjaxException;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/solucls")
public class DesignClassController {
    @Autowired
    private DesignClassBiz designClassBiz;
@Autowired
private DesignSolutionBiz designSolutionBiz;
    /**
     * 分类列表
     * @return
     */
    @Post("/list")
    public List<DesignClass> list(){
        List<DesignClass> list = designClassBiz.getList();
        return list;
    }

    /**
     * 添加分类
     * @param entity
     * @return
     */
    @Post("/add")
    public int add(DesignClass entity){
        return designClassBiz.add(entity);
    }
    /**
     * 添加分类
     * @param entity
     * @return
     */
    @Post("/update")
    public int update(DesignClass entity){
        return designClassBiz.update(entity);
    }
    /**
     * 删除事件
     * @param ids
     * @return
     */
    @Post("/del")
    public int del(@RequestBody List<Integer> ids){
        if(!CollectionUtils.isEmpty(ids)){
            DesignSolution filter = new DesignSolution();
            List<String> classIds = ids.stream().map(a->a.toString()).collect(Collectors.toList());
            filter.setFilter("classId in ("+ String.join(",",classIds) +")");
            List<DesignSolution> list = designSolutionBiz.selectByFilter(filter);
            if(!CollectionUtils.isEmpty(list)){
                throw new AjaxException("当前分类下有设计方案挂载，若想移除，请先移除相关设计方案");
            }
        }
        return designClassBiz.del(ids);
    }

    /**
     * 计算排序码
     * @param list
     * @return
     */
    @Post("/calcSort")
    public int calcSort(@RequestBody List<DesignClass> list){
        return designClassBiz.calcSort(list);
    }
}
