package smart.industry.train.web.controllers;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.SavedRequest;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import smart.industry.train.biz.entity.User;
import smart.industry.utils.StringUtils;
import smart.industry.utils.exceptions.AjaxException;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 首页
 */
@Controller
@RequestMapping("/")
public class IndexController {
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
    @PostMapping("/logout")
    @ResponseBody
    public int logout() {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return 1;
    }
    //end
}
