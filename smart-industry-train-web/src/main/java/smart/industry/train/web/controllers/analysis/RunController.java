package smart.industry.train.web.controllers.analysis;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.Map;

/**
 * 运行controller
 */
@Controller
@RequestMapping("/run")
public class RunController {
    @GetMapping("/test")
    public String test(Map<String, Object> map){
        return "analysis/test_run";
    }
}
