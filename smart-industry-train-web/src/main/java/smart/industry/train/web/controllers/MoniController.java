package smart.industry.train.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 模拟控制器
 */
@Controller
@RequestMapping("/moni")
public class MoniController {
    @RequestMapping(method = RequestMethod.GET)
    public String sayHello(ModelMap model) {
        return "/scene/train";
    }

    @RequestMapping(value = "/train", method = RequestMethod.GET)
    public String sayHelloAgain(ModelMap model) {
        return "/scene/train";
    }
}
