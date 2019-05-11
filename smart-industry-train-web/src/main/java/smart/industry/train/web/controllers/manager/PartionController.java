package smart.industry.train.web.controllers.manager;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import smart.industry.train.biz.dao.DesignSolutionPartionBiz;
import smart.industry.train.biz.entity.DesignSolutionPartion;
import smart.industry.train.biz.entity.DesignSolutionPartionList;
import smart.industry.train.biz.mapper2.DesignSolutionPartionListMapper;
import smart.industry.train.biz.mapper2.DesignSolutionPartionMapper;
import smart.industry.utils.annotations.Post;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 测试方案控制器
 */
@Controller
@RequestMapping("/partion")
public class PartionController {
    @Autowired
    private DesignSolutionPartionMapper designSolutionPartionMapper;
    @Autowired
    private DesignSolutionPartionBiz designSolutionPartionBiz;
    @Autowired
    private DesignSolutionPartionListMapper designSolutionPartionListMapper;

    /**
     * 获取组织架构列表
     * @return
     */
    @RequestMapping("/cards")
    public String cards(Integer id,Map<String, Object> map){
        map.put("solutionId", id);
        map.put("cards", designSolutionPartionBiz.getCards(id,null));
        return "manager/solution_cards";
    }

    /**
     * 新增窗口
     * @param solutionId
     * @param map
     * @return
     */
    @RequestMapping("/addwin")
    public String addWin(Integer solutionId,Map<String, Object> map) {
        DesignSolutionPartion entity = new DesignSolutionPartion();
        entity.setSolutionId(solutionId);
        map.put("entity", JSON.toJSONString(entity));
        map.put("formMode", "add");
        return "manager/partion_edit";
    }

    /**
     * 编辑窗口
     *
     * @param id
     * @return
     */
    @RequestMapping("/editwin")
    public String editWin(int id, Map<String, Object> map) {
        List<DesignSolutionPartion> list = designSolutionPartionBiz.getCards(null,id);
        if(list.size()>0){
            map.put("entity", JSON.toJSONString(list.get(0)));
        }else{
            map.put("entity", JSON.toJSONString(new DesignSolutionPartion()));
        }
        map.put("formMode", "edit");
        return "manager/partion_edit";
    }

    /**
     * 新增
     *
     * @return
     */
    @Post("/add")
    @Transactional
    public int add(@RequestBody DesignSolutionPartion entity) {
        designSolutionPartionBiz.valid(entity);
        designSolutionPartionMapper.insert(entity);
        //list保存
        for (DesignSolutionPartionList item : entity.getPartionList()) {
            item.setPartionId(entity.getId());
            designSolutionPartionListMapper.insert(item);
        }
        return 1;
    }

    /**
     * 编辑
     *
     * @param entity
     * @return
     */
    @Post("/edit")
    @Transactional
    public int edit(@RequestBody DesignSolutionPartion entity) {
        designSolutionPartionBiz.valid(entity);
        //list保存
        Map<String,Object> filter = new HashMap<>();
        filter.put("partionId",entity.getId());
        designSolutionPartionListMapper.deleteByMap(filter);
        for (DesignSolutionPartionList item : entity.getPartionList()) {
            item.setPartionId(entity.getId());
            designSolutionPartionListMapper.insert(item);
        }
        //主表保存
        return designSolutionPartionMapper.updateById(entity);
    }
    /**
     * 删除
     *
     * @param id
     * @return
     */
    @Post("/delete")
    @Transactional
    public int delete(Integer id) {
        //list保存
        Map<String,Object> filter = new HashMap<>();
        filter.put("partionId",id);
        designSolutionPartionListMapper.deleteByMap(filter);
        return designSolutionPartionMapper.deleteById(id);
    }

}
