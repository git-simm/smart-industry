package smart.industry.train.web.controllers.manager;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import smart.industry.train.biz.dao.DesignClassBiz;
import smart.industry.train.biz.dao.DesignSolutionBiz;
import smart.industry.train.biz.dao.DesignSolutionListBiz;
import smart.industry.train.biz.dao.SysUpfilesBiz;
import smart.industry.train.biz.entity.*;
import smart.industry.train.biz.entity.base.Paging;
import smart.industry.utils.annotations.Post;
import smart.industry.utils.exceptions.AjaxException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 解决方案管理
 */
@Controller
@RequestMapping("/solution")
public class SolutionController {
    @Autowired
    private  HttpServletRequest request;
    @Autowired
    private DesignSolutionBiz designSolutionBiz;
    @Autowired
    private DesignSolutionListBiz designSolutionListBiz;
    @Autowired
    private DesignClassBiz designClassBiz;
    @RequestMapping("/list")
    public String list(Map<String, Object> map){
        Subject subject = SecurityUtils.getSubject();
        User user = (User)subject.getPrincipal();
        if(user!=null){
            map.put("user",user.getName());
        }
        return "manager/solution_list";
    }

    /**
     * 新增窗口
     * @return
     */
    @RequestMapping("/addwin")
    public String addWin(Map<String, Object> map){
        map.put("entity",new DesignSolution());
        map.put("formMode","add");
        return "manager/solution_edit";
    }

    /**
     * 编辑窗口
     * @param id
     * @return
     */
    @RequestMapping("/editwin")
    public String editWin(int id,Map<String, Object> map){
        DesignSolution designSolution = designSolutionBiz.selectByPrimaryKey(id);
        if(designSolution.getClassId()!=null){
            DesignClass designClass = designClassBiz.selectByPrimaryKey(designSolution.getClassId());
            map.put("className",designClass.getName());
        }
        map.put("entity",designSolution);
        map.put("formMode","edit");
        map.put("type1",designSolutionListBiz.getListBySolution(id,1));
        map.put("type2",designSolutionListBiz.getListBySolution(id,2));
        map.put("type3",designSolutionListBiz.getListBySolution(id,3));
        return "manager/solution_edit";
    }
    /**
     * 获取所有的对象列表
     * @param map
     * @return
     */
    @Post("/getlist")
    @ResponseBody
    public JSONObject getList(Paging json, Map<String, Object> map){
        System.out.println(request);
        PageHelper.offsetPage(json.getOffset(),json.getLimit());
        List<DesignSolution> list = designSolutionBiz.selectByPage(json);
        PageInfo<DesignSolution> p=new PageInfo<>(list);
        JSONObject result = new JSONObject();
        result.put("rows", p.getList());
        result.put("total", p.getTotal());
        return result;
    }

    /**
     * 获取所有的对象列表
     * @param id
     * @return
     */
    @Post("/getfiles")
    @ResponseBody
    public List<JSONObject> getFiles(Integer id){
        return designSolutionBiz.getFileTree(id);
    }

    /**
     * 新增
     * @return
     */
    @Post("/add")
    public Integer add(DesignSolution solution){
        designSolutionBiz.add(solution);
        return solution.getId();
    }

    /**
     * 编辑方案
     * @param param 参数
     * @return
     */
    @Post(value = "/edit",consumes="application/json")
    @Transactional
    public Integer edit(@RequestBody JSONObject param){
        DesignSolution solution = JSONObject.parseObject(param.getString("solution"),DesignSolution.class);
        List<Integer> fileIds = JSONArray.parseArray(param.getString("fileIds"),Integer.class);
        designSolutionBiz.delFiles(fileIds);
        return designSolutionBiz.update(solution);
    }

    /**
     * 文件信息统计
     * @param id
     * @return
     */
    @Post(value = "/fileSummary")
    @Transactional
    public Integer fileSummary(Integer id){
        return designSolutionBiz.fileSummary(id);
    }
    /**
     * 删除事件
     * @param id
     * @return
     */
    @Post(value = "/del")
    public Integer del(Integer id){
         return designSolutionBiz.delSolution(id);
    }
}
