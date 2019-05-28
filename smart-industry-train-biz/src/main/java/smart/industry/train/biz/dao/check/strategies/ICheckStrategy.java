package smart.industry.train.biz.dao.check.strategies;

import smart.industry.train.biz.enums.CheckRuleEnum;

import java.util.List;

/**
 * 检查策略
 */
public interface ICheckStrategy {
    /**
     * 检查规则
     */
    CheckRuleEnum CHECK_RULE_ENUM = null;
    /**
     * 执行检查
     * @return
     */
    List<Object> check();
}
