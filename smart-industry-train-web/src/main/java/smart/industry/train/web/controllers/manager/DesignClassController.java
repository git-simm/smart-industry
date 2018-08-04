package smart.industry.train.web.controllers.manager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import smart.industry.train.biz.dao.DesignClassBiz;
import smart.industry.train.biz.entity.DesignClass;
import smart.industry.utils.annotations.Post;

import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/solucls")
public class DesignClassController {
    @Autowired
    private DesignClassBiz designClassBiz;

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
     * 删除事件
     * @param ids
     * @return
     */
    @Post("/del")
    public int del(@RequestBody List<Integer> ids){
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
