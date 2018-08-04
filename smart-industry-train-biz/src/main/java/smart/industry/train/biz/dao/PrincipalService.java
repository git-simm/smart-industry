package smart.industry.train.biz.dao;

import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Service;
import smart.industry.train.biz.entity.User;

@Service
public class PrincipalService {
    /**
     * 获取当前用户信息
     */
    public User getCurrentUser(){
        return (User) SecurityUtils.getSubject().getPrincipal();
    }
}
