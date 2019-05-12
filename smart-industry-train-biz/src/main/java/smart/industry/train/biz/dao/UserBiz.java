package smart.industry.train.biz.dao;

import org.apache.commons.collections.ListUtils;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import smart.industry.train.biz.dao.base.BaseBiz;
import smart.industry.train.biz.entity.DesignSolution;
import smart.industry.train.biz.entity.User;
import smart.industry.train.biz.entity.base.Paging;
import smart.industry.train.biz.mapper.UserMapper;
import smart.industry.utils.exceptions.AjaxException;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserBiz  extends BaseBiz<UserMapper,User> {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private OrganizationBiz organizationBiz;

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

    /**
     * 获取用户信息
     * @param code
     * @return
     */
    public User findUserByCode(String code) {
        User user = new User();
        user.setCode(code);
        user.setFilter("code=#{code}");
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
        List<String> orgIds = organizationBiz.getAllOrgs(paging.getOrgId())
                .stream().map(a->a.toString()).collect(Collectors.toList());
        filter.setOrgIds(String.join(",",orgIds));
        String filterSeq = "name like concat('%',#{name},'%')";
        if(!StringUtils.isEmpty(filter.getOrgIds())){
            filterSeq +=" and org_id in ("+ filter.getOrgIds() +")";
        }
        filter.setFilter(filterSeq);
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