package smart.industry.train.web.controllers.manager;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import smart.industry.train.biz.dao.ParamGroupBiz;
import smart.industry.train.biz.dao.ParamOptionsBiz;
import smart.industry.train.biz.entity.ParamGroup;
import smart.industry.train.biz.entity.ParamOptions;
import smart.industry.train.biz.entity.base.Paging;
import smart.industry.utils.annotations.Post;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 字典项控制器
 */
@Controller
@RequestMapping("/param")
public class ParamController {
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private HttpServletResponse response;
    @Autowired
    private ParamGroupBiz paramGroupBiz;
    @Autowired
    private ParamOptionsBiz paramOptionsBiz;
    @GetMapping("/list")
    public String list() {
        return "manager/param_list";
    }
    /**
     * 获取所有的对象列表
     * @param map
     * @return
     */
    @Post("/getlist")
    public JSONObject getList(Paging json, Map<String, Object> map){
        json.setLimit(10000);
        json.setOffset(0);
        PageHelper.offsetPage(json.getOffset(),json.getLimit());
        List<ParamGroup> list = paramGroupBiz.selectByPage(json);
        PageInfo<ParamGroup> p=new PageInfo<>(list);
        JSONObject result = new JSONObject();
        result.put("rows", p.getList());
        result.put("total", p.getTotal());
        return result;
    }

    @Post("/getOptionList")
    public JSONObject getOptionList(Paging json, Map<String, Object> map){
        json.setLimit(10000);
        json.setOffset(0);
        PageHelper.offsetPage(json.getOffset(),json.getLimit());
        List<ParamOptions> list = paramOptionsBiz.selectByPage(json);
        PageInfo<ParamOptions> p=new PageInfo<>(list);
        JSONObject result = new JSONObject();
        result.put("rows", p.getList());
        result.put("total", p.getTotal());
        return result;
    }

    /**
     * 添加新的字典项
     * @param groupid
     * @param map
     * @return
     */
    @GetMapping("/addOptionWin")
    public String addOptionWin(Integer groupid, Map<String, Object> map){
        ParamOptions entity = new ParamOptions();
        entity.setGroupid(groupid);
        map.put("db", entity);
        map.put("formMode","add");
        return "manager/option_edit";
    }
    @GetMapping("/editOptionWin")
    public String editOptionWin(Integer id, Map<String, Object> map){
        ParamOptions entity = paramOptionsBiz.selectByPrimaryKey(id);
        map.put("db", entity);
        map.put("formMode","edit");
        return "manager/option_edit";
    }
    @GetMapping("/addwin")
    public String addwin(Map<String, Object> map){
        ParamGroup entity = new ParamGroup();
        map.put("db", entity);
        map.put("formMode","add");
        return "manager/param_edit";
    }
    @GetMapping("/editwin")
    public String editwin(Integer id, Map<String, Object> map){
        ParamGroup entity = paramGroupBiz.selectByPrimaryKey(id);
        map.put("db", entity);
        map.put("formMode","edit");
        return "manager/param_edit";
    }

    /**************** ajax 处理 段落 begin ********************/
    @Post("/add")
    @Transactional
    public Integer add(ParamGroup entity){
        paramGroupBiz.valid(entity);
        paramGroupBiz.add(entity);
        return entity.getId();
    }
    @Post("/edit")
    @Transactional
    public Integer edit(ParamGroup entity){
        /** post 请求中，发起界面重定向事件
        /*try {
            response.sendRedirect("/login");
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        paramGroupBiz.valid(entity);
        return paramGroupBiz.update(entity);
    }
    /**
     * 删除分组，开启事务
     */
    @Post("/delete")
    @Transactional
    public Integer delete(Integer id){
        ParamGroup entity = paramGroupBiz.selectByPrimaryKey(id);
        entity.setValid(false);
        return paramGroupBiz.update(entity);
    }
    @Post("/addOption")
    @Transactional
    public Integer addOption(ParamOptions entity){
        paramOptionsBiz.valid(entity);
        paramOptionsBiz.add(entity);
        return entity.getId();
    }
    @Post("/editOption")
    @Transactional
    public Integer editOption(ParamOptions entity){
        paramOptionsBiz.valid(entity);
        return paramOptionsBiz.update(entity);
    }
    @Post("/delOption")
    @Transactional
    public Integer delOption(Integer id){
        return paramOptionsBiz.delete(id);
    }
    /**************** ajax 处理 段落 end ********************/
}
