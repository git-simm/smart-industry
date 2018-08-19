package smart.industry.train.biz.mapper;

import smart.industry.train.biz.entity.User;
import smart.industry.train.biz.interfaces.IMapper;

public interface UserMapper extends IMapper<User> {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);
}