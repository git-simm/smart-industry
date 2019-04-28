package smart.industry.train.web.controllers.manager;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import smart.industry.train.biz.common.SysConfig;
import smart.industry.train.biz.dao.UserBiz;
import smart.industry.train.biz.entity.DesignSolution;
import smart.industry.train.biz.entity.User;
import smart.industry.train.biz.entity.base.Paging;
import smart.industry.train.web.models.MyUser;
import smart.industry.utils.annotations.Post;
import smart.industry.utils.encode.MD5;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/user")
public class UserController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private SysConfig sysConfig;
    @Autowired
    private UserBiz userBiz;

    @RequestMapping("/say")
    @ResponseBody
    public String sayHello() {
        return "hello";
    }

    @RequestMapping("/list")
    public String list() {
        return "manager/user_list";
    }

    @Post("/getlist")
    @ResponseBody
    public JSONObject getList(Paging json, Map<String, Object> map) {
        PageHelper.offsetPage(json.getOffset(), json.getLimit());
        List<User> users = userBiz.selectByPage(json);
        PageInfo<User> p = new PageInfo<>(users);
        JSONObject result = new JSONObject();
        result.put("rows", p.getList());
        result.put("total", p.getTotal());
        return result;
    }

    /**
     * 新增窗口
     *
     * @return
     */
    @RequestMapping("/addwin")
    public String addWin(Map<String, Object> map) {
        map.put("entity", new User());
        map.put("formMode", "add");
        return "manager/user_edit";
    }

    /**
     * 编辑窗口
     *
     * @param id
     * @return
     */
    @RequestMapping("/editwin")
    public String editWin(int id, Map<String, Object> map) {
        map.put("entity", userBiz.selectByPrimaryKey(id));
        map.put("formMode", "edit");
        return "manager/user_edit";
    }

    /**
     * 新增
     *
     * @return
     */
    @Post("/add")
    public int add(User user) {
        userBiz.validUsers(user);
        user.setPsw(MD5.encode(user.getPsw()));
        return userBiz.add(user);
    }

    /**
     * 编辑
     *
     * @param user
     * @return
     */
    @Post("/edit")
    public int edit(User user) {
        userBiz.validUsers(user);
        //不允许编辑密码
        user.setPsw(null);
        return userBiz.update(user);
    }

    /**
     * 编辑
     *
     * @param id
     * @return
     */
    @Post("/delete")
    public int delete(Integer id) {
        return userBiz.delete(id);
    }

    /**
     * 重置密码
     *
     * @param id
     * @return
     */
    @Post("/reset")
    public int reset(Integer id) {
        User user = userBiz.selectByPrimaryKey(id);
        user.setPsw(MD5.encode(sysConfig.getResetPsw()));
        return userBiz.update(user);
    }
}
