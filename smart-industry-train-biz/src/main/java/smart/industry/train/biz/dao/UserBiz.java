package smart.industry.train.biz.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import smart.industry.train.biz.entity.User;
import smart.industry.train.biz.mapper.UserMapper;
import java.util.List;

@Service
public class UserBiz {
    @Autowired
    private UserMapper userMapper;
    public List<User> getList() {
        return userMapper.selectAll();
    }
}