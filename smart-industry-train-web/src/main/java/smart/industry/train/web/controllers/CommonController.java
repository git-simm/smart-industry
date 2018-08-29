package smart.industry.train.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 公选控制器
 */
@Controller
@RequestMapping("/common")
public class CommonController {
    /**
     * 选择分类
     * @return
     */
    @RequestMapping("/selclass")
    public String selClass(){
        return "common/select_class";
    }
}
