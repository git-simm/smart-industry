package smart.industry.train.biz.dao;

import org.apache.shiro.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import smart.industry.train.biz.dao.base.BaseBiz;
import smart.industry.train.biz.entity.DesignSolution;
import smart.industry.train.biz.entity.User;
import smart.industry.train.biz.entity.base.Paging;
import smart.industry.train.biz.mapper.UserMapper;
import smart.industry.utils.exceptions.AjaxException;

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
        user.setFilter("name=#{name}");
        List<User> users = userMapper.selectByFilter(user);
        if(users!=null && users.size()>0){
            return users.get(0);
        }
        return null;
    }

    @Override
    public User getFilter(Paging paging) throws Exception {
        User filter = User.class.newInstance();
        filter.setName(paging.getSearchKey());
        filter.setFilter("name like concat('%',#{name},'%')");
        return filter;
    }

    /**
     * 用户验证
     * @param user
     * @return
     */
    public boolean validUsers(User user){
        user.setFilter("code=#{code}");
        if(user.getId()!=null){
            user.setFilter(user.getFilter()+" and id!=#{id}");
        }
        List<User> users = selectByFilter(user);
        if(CollectionUtils.isEmpty(users)) return true;
        else{
            throw new AjaxException(user.getCode()+"在数据库中已经存在，请重新输入");
        }
    }
}