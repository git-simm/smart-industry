package smart.industry.train.biz.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import smart.industry.train.biz.entity.Organization;
import smart.industry.train.biz.mapper2.OrganizationMapper;
import smart.industry.utils.exceptions.AjaxException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 组织架构业务逻辑层控制
 */
@Service
public class OrganizationBiz {
    @Autowired
    private OrganizationMapper organizationMapper;
    /**
     * 组织架构验证
     * @param org
     * @return
     */
    public boolean validOrg(Organization org){
        QueryWrapper<Organization> wrapper = new QueryWrapper<>();
        wrapper.setEntity(new Organization());
        if(org.getId()!=null){
            wrapper.ne("a.id",org.getId());
        }
        wrapper.eq("a.code",org.getCode());

        List<Organization> orgs = organizationMapper.getOrgList(wrapper);
        if(CollectionUtils.isEmpty(orgs)) return true;
        else{
            throw new AjaxException("编码为"+org.getCode()+"的组织在数据库中已经存在，请重新输入");
        }
    }

    /**
     * 获取组织架构下所有的信息
     * @param parentId
     * @return
     */
    public List<Integer> getAllOrgs(Integer parentId){
        List<Integer> result = new ArrayList<>();
        List<Organization> list = organizationMapper.getOrgList(null);
        getAllOrgIds(list,result,parentId);
        return result;
    }

    /**
     * 获取所有组织架构信息
     * @param list
     * @param result
     * @param parentId
     * @return
     */
    private void getAllOrgIds(List<Organization> list,List<Integer> result,Integer parentId){
        if(result.contains(parentId)) return;
        result.add(parentId);
        //查找子级
        List<Integer> children = list.stream().filter(a-> parentId.equals(a.getpId())).map(a->a.getId()).collect(Collectors.toList());
        children.stream().forEach(a->{
            getAllOrgIds(list,result,a);
        });
    }
}
