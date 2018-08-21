package smart.industry.train.biz.interfaces;

import smart.industry.train.biz.entity.base.Paging;

import java.util.List;

public interface IMapper<T> {
    int deleteByPrimaryKey(Integer id);

    int insert(T record);

    int insertSelective(T record);

    T selectByPrimaryKey(Integer id);

    /**
     * 加载所有的数据
     * @return
     */
    List<T> selectAll();
    /**
     * 通过过滤条件进行查询
     * @param record
     * @return
     */
    List<T> selectByFilter(T record);

    int updateByPrimaryKeySelective(T record);

    int updateByPrimaryKey(T record);
}
