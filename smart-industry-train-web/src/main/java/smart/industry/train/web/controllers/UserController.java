package smart.industry.train.web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import smart.industry.train.biz.dao.UserBiz;
import smart.industry.train.biz.entity.User;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserBiz userBiz;

    @RequestMapping("/say")
    @ResponseBody
    public String sayHello(){
        return "hello";
    }

    @RequestMapping("/list")
    @ResponseBody
    public List<User> list(){
        return userBiz.getList();
    }
}
