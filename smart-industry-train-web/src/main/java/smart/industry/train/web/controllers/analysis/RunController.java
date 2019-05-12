package smart.industry.train.web.controllers.analysis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import smart.industry.train.biz.entity.DesignSolutionPartion;
import smart.industry.train.biz.mapper2.DesignSolutionPartionMapper;

import java.util.Map;

/**
 * 运行controller
 */
@Controller
@RequestMapping("/run")
public class RunController {
    /**
     * 加载测试窗口
     * @return
     */
    /*
    @GetMapping("/test")
    public String test(){
        return "analysis/test_run";
    }*/
@Autowired
private DesignSolutionPartionMapper designSolutionPartionMapper;
    /**
     * 执行具体的解决方案
     * @param id 设计方案id
     * @param cardId 测试方案id
     * @param map
     * @return
     */
    @GetMapping("/test")
    public String test(int id,Integer cardId,Map<String, Object> map){
        map.put("solutionId",id);
        map.put("cardId",cardId);
        DesignSolutionPartion partion = new DesignSolutionPartion();
        partion.setName("方案测试");
        if(cardId!=null){
            DesignSolutionPartion temp = designSolutionPartionMapper.selectById(cardId);
            if(temp !=null){
                partion.setName(temp.getName());
            }
        }
        map.put("partion",partion);
        return "analysis/test_run";
    }
    @GetMapping("/test2")
    public String test2(){
        return "analysis/thumbnailViewer";
    }
}
