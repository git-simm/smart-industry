package smart.industry.train.web.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import smart.industry.train.biz.dao.UserBiz;
import smart.industry.train.biz.entity.User;
import smart.industry.train.web.models.MyUser;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/user")
public class UserController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserBiz userBiz;

    @RequestMapping("/say")
    @ResponseBody
    public String sayHello(){
        return "hello";
    }

    @RequestMapping("/list")
    @ResponseBody
    public List<MyUser> list(){
        logger.debug("This is a debug message");
        logger.info("This is an info message");
        logger.warn("This is a warn message");
        logger.error("This is an error message");

        List<User> users = userBiz.getList();
        List<MyUser> list = users.stream().map(u->getMyUser(u)).collect(Collectors.toList());
        User user = users.stream().max((u1, u2)->(u1.getId()-u2.getId())).get();
        Integer count = users.stream().mapToInt(u->u.getId()).sum();
        System.out.println("ID汇总的大小："+count);
        return list;
    }

    /**
     * 测试一次lombok 语法糖的用法
     * @param user
     * @return
     */
    private MyUser getMyUser(User user){
        MyUser my = new MyUser();
        my.setName(user.getName());
        my.setPsw(user.getPsw());
        System.out.println("MyUser：" + my.toString());
        return my;
    }
}
