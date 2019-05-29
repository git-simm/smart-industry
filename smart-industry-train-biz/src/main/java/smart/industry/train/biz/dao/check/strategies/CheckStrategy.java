package smart.industry.train.biz.dao.check.strategies;

import com.alibaba.fastjson.JSONObject;
import smart.industry.train.biz.dao.DesignExcelListBiz;
import smart.industry.train.biz.enums.CheckRuleEnum;

import java.util.HashMap;

/**
 * 基础策略
 * @author manman.si
 */
public abstract class CheckStrategy{
    /**
     * 检查规则
     */
    public CheckRuleEnum CHECK_RULE_ENUM = null;
    /**
     * 是否启用
     */
    public boolean ENABLED = true;
    public CheckStrategy(CheckRuleEnum ruleEnum,boolean enabled){
        this.CHECK_RULE_ENUM = ruleEnum;
        this.ENABLED = enabled;
    }
    public CheckStrategy(CheckRuleEnum ruleEnum){
        this.CHECK_RULE_ENUM = ruleEnum;
    }
    /**
     * 实现校验方法
     * @param excelItem
     * @param validMap
     * @return
     */
    public abstract DesignExcelListBiz.ValidInfo validItem(JSONObject excelItem
            ,HashMap<String,DesignExcelListBiz.ValidInfo> validMap);

    /**
     * 布尔值比较
     * @param b1
     * @param b2
     * @return
     */
    public boolean boolCompared(Boolean b1,Boolean b2){
        if(b1==null){
            b1 = false;
        }
        return b1.equals(b2);
    }
}
