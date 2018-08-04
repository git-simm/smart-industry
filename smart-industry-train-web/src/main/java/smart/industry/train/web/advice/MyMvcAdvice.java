package smart.industry.train.web.advice;

import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.resource.ResourceUrlProvider;
import smart.industry.utils.entity.ResponseJson;
import smart.industry.utils.exceptions.AjaxException;
import smart.industry.utils.resource.MessageSourceUtils;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@ControllerAdvice
public class MyMvcAdvice extends ResponseEntityExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(MyMvcAdvice.class);

    @Autowired
    ResourceUrlProvider resourceUrlProvider;

    @Autowired
    protected HttpServletRequest request;

    @Autowired
    protected HttpSession session;

    @Autowired
    protected ServletContext application;

    @ModelAttribute("urls")
    public ResourceUrlProvider urls() {
        return this.resourceUrlProvider;
    }

    /**
     * 应用到所有@RequestMapping注解方法，在其执行之前初始化数据绑定器
     *
     * @param binder
     */
    @InitBinder
    public void initBinder(WebDataBinder binder) {
    }

    /**
     * 把值绑定到Model中，使全局@RequestMapping可以获取到该值
     *
     * @param model
     */
    @ModelAttribute
    public void addAttributes(HttpServletRequest request, HttpServletResponse response, Model model) {
        //model.addAttribute("author", "zq-simm");
        //model.addAttribute("sex", "male");
        //model.addAttribute("session", request.getSession(true).getAttribute("user"));
    }

    /**
     * 接口层异常:参数校验,转换等标准的spring异常
     */
    /*@Override
    @ExceptionHandler({
            Exception.class
    })
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
                                                             HttpStatus status, WebRequest request) {
        logger.error("Global catch exception log: ", ex);
        throw ex;
    }*/

    /**
     * 业务层异常:自定义的业务校验,操作等异常
     */
    @ExceptionHandler({
            AjaxException.class
    })
    public final ResponseEntity<ResponseJson> handleThrowable(AjaxException ex, WebRequest request) {
        logger.error("Global catch exception log: ", ex);
        throw ex;
    }

    @ExceptionHandler(org.apache.shiro.authz.AuthorizationException.class)
    public String handleException(RedirectAttributes redirectAttributes, Exception exception, HttpServletRequest request) {
        redirectAttributes.addFlashAttribute("message", "抱歉！您没有权限执行这个操作，请联系管理员！");
        String url = WebUtils.getRequestUri(request);
        return "redirect:/" + url.split("/")[1];    // 请求的规则 : /page/operate
    }
    /**
     * 其它异常:未处理的其它异常
     */
    /*@ExceptionHandler({ Throwable.class })
    public final ResponseEntity<ResponseJson> handleThrowable(Throwable ex, WebRequest request) {
        logger.error("Global catch exception log: ", ex);
        throw ex;
    }*/
}
