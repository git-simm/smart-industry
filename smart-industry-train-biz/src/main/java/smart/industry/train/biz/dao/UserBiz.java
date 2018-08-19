package smart.industry.train.biz.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import smart.industry.train.biz.dao.base.BaseBiz;
import smart.industry.train.biz.entity.DesignSolution;
import smart.industry.train.biz.entity.User;
import smart.industry.train.biz.entity.base.Paging;
import smart.industry.train.biz.mapper.UserMapper;

import java.util.List;

@Service
public class UserBiz  extends BaseBiz<UserMapper,User> {
    @Autowired
    private UserMapper userMapper;
    public List<User> getList() {
        return userMapper.selectAll();
    }

    /**
     * 获取用户信息
     * @param username
     * @return
     */
    public User findUserByName(String username) {
        User user = new User();
        user.setName(username);
        List<User> users = userMapper.selectByCon(user);
        if(users!=null && users.size()>0){
            return users.get(0);
        }
        return null;
    }

    @Override
    public User getFilter(Paging paging) throws Exception {
        User filter = User.class.newInstance();
        filter.setName(paging.getSearchKey());
        return filter;
    }
}