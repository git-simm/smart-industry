package smart.industry.train.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

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
        map.put("greeting", "欢迎进入工业智能，有轨车辆动态模拟项目！");
        return "index";
    }
}
