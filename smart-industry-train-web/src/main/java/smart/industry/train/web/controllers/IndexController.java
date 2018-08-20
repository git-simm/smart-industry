package smart.industry.train.web.controllers;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.SavedRequest;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import smart.industry.train.biz.dao.UserBiz;
import smart.industry.train.biz.entity.User;
import smart.industry.utils.StringUtils;
import smart.industry.utils.encode.MD5;
import smart.industry.utils.exceptions.AjaxException;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 首页
 */
@Controller
@RequestMapping("/")
public class IndexController {
    @Autowired
    UserBiz userBiz;
    @RequestMapping("/")
    public String index(Map<String, Object> map){
        return index2(map);
    }
    @RequestMapping("/index")
    public String index2(Map<String, Object> map){
        Subject subject = SecurityUtils.getSubject();
        User user = (User)subject.getPrincipal();
        if(user!=null){
            map.put("user",user.getName());
        }
        return "index";
    }

    /**
     * 修改密码
     * @param map
     * @return
     */
    @RequestMapping("/changePsw")
    public String changePsw(Map<String, Object> map){
        return "auth/changepsw";
    }
    //用户登录 begin
    @GetMapping(value = "/login")
    public String login(HttpServletRequest request, Map<String, Object> map){
        if (SecurityUtils.getSubject().isAuthenticated()) {
            return "redirect:/index";
        }
        return "auth/login";
    }
    /**
     * 用户登录
     * @param request
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/loginvalid")
    @ResponseBody
    public String loginValid(HttpServletRequest request, User user) throws Exception{
        System.out.println("HomeController.login()");
        // 登录失败从request中获取shiro处理的异常信息。
        // shiroLoginFailure:就是shiro异常类的全类名.
        SavedRequest redirect = WebUtils.getSavedRequest(request);

        //添加用户认证信息
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(user.getName(),user.getPsw());
        String msg = "";
        try{
            //进行验证，这里可以捕获异常，然后返回对应信息
            subject.login(token);
        }catch(UnknownAccountException e){
            msg = "账号不存在";
        }catch(IncorrectCredentialsException e){
            msg = "密码不正确";
        }catch(Exception e){
            msg = "用户信息校验失败";
        }
        System.out.println(msg);
        if(StringUtils.isNotBlank(msg)){
            throw new AjaxException(msg);
        }
        //String exception = (String) request.getAttribute("shiroLoginFailure");
        //System.out.println("exception=" + exception);
        if(redirect!=null && StringUtils.isNotBlank(redirect.getRequestUrl())){
            if(redirect.getRequestUrl().contains("login"))
                return "/index";
            return redirect.getRequestUrl();
        }
        return "/index";
    }
    /**
     * 退出
     * @return
     */
    @RequestMapping("/logout")
    @ResponseBody
    public int logout() {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return 1;
    }

    /**
     * 更新密码
     * @param psw
     * @param newpsw
     * @param confirmpsw
     * @return
     */
    @RequestMapping("/updatepsw")
    @ResponseBody
    public int updatepsw(String psw,String newpsw,String confirmpsw){
        if(!newpsw.equals(confirmpsw)){
            throw new AjaxException("新密码与确认密码不匹配，请重新输入");
        }
        String oldPsw = MD5.encode(psw);
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        if(!user.getPsw().equals(oldPsw)){
            throw new AjaxException("原始密码错误，请重新输入");
        }
        user.setPsw(MD5.encode(newpsw));
        return userBiz.update(user);
    }
    //end
}
