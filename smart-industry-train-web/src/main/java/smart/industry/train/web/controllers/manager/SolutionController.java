package smart.industry.train.web.controllers.manager;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import smart.industry.train.biz.dao.DesignSolutionBiz;
import smart.industry.train.biz.entity.DesignSolution;
import smart.industry.train.biz.entity.User;
import smart.industry.train.biz.entity.base.Paging;
import smart.industry.utils.annotations.Post;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
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
        return "manager/solution_edit";
    }

    /**
     * 编辑窗口
     * @param id
     * @return
     */
    @RequestMapping("/editwin")
    public String editWin(int id,Map<String, Object> map){
        map.put("entity",new DesignSolution());
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
        List<DesignSolution> list = designSolutionBiz.selectByCon(json);
        PageInfo<DesignSolution> p=new PageInfo<>(list);
        JSONObject result = new JSONObject();
        result.put("rows", list);
        result.put("total", p.getTotal());
        return result;
    }
}
