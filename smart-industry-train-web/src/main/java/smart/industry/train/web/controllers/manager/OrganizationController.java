package smart.industry.train.web.controllers.manager;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import smart.industry.train.biz.dao.OrganizationBiz;
import smart.industry.train.biz.dao.UserBiz;
import smart.industry.train.biz.entity.Organization;
import smart.industry.train.biz.entity.User;
import smart.industry.train.biz.entity.base.Paging;
import smart.industry.train.biz.mapper2.OrganizationMapper;
import smart.industry.utils.annotations.Post;
import smart.industry.utils.exceptions.AjaxException;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 组织架构管理
 */
@Controller
@RequestMapping("/org")
public class OrganizationController {
    @Autowired
    private OrganizationBiz organizationBiz;
    @Autowired
    private OrganizationMapper organizationMapper;
    @RequestMapping("/add")
    @ResponseBody
    public Integer add(){
        Organization org = new Organization();
        org.setName("测试部门");
        org.setCode("12");
        org.setSort("12");
        org.setOrgType(1);
        return organizationMapper.insert(org);
    }

    /**
     * 获取组织架构列表
     * @return
     */
    @Post("/list")
    public List<Organization> list(){
        QueryWrapper<Organization> wrapper = new QueryWrapper<>();
        wrapper.setEntity(new Organization());
        wrapper.eq("1",1);
        return organizationMapper.getOrgList(wrapper);
    }

    /**
     * 新增窗口
     *
     * @return
     */
    @RequestMapping("/addwin")
    public String addWin(Map<String, Object> map) {
        map.put("entity", new Organization());
        map.put("formMode", "add");
        return "manager/org_edit";
    }

    /**
     * 编辑窗口
     *
     * @param id
     * @return
     */
    @RequestMapping("/editwin")
    public String editWin(int id, Map<String, Object> map) {
        QueryWrapper<Organization> wrapper = new QueryWrapper<>();
        wrapper.setEntity(new Organization());
        wrapper.eq("a.id",id);
        List<Organization> list = organizationMapper.getOrgList(wrapper);
        if(list.size()>0){
            map.put("entity", list.get(0));
        }else{
            map.put("entity", new Organization());
        }
        map.put("formMode", "edit");
        return "manager/org_edit";
    }

    /**
     * 新增
     *
     * @return
     */
    @Post("/add")
    public int add(Organization entity) {
        organizationBiz.validOrg(entity);
        return organizationMapper.insert(entity);
    }

    /**
     * 编辑
     *
     * @param entity
     * @return
     */
    @Post("/edit")
    public int edit(Organization entity) {
        organizationBiz.validOrg(entity);
        return organizationMapper.updateById(entity);
    }

    @Autowired
    private UserBiz userBiz;
    /**
     * 删除
     *
     * @param id
     * @return
     */
    @Post("/delete")
    public int delete(Integer id) {
        //如果组织架构下有人员，则不允许删除
        Paging json = new Paging();
        json.setOrgId(id);
        json.setSearchKey("");
        PageHelper.offsetPage(0, 1000);
        List<User> users = userBiz.selectByPage(json);
        if(CollectionUtils.isNotEmpty(users)){
            throw new AjaxException("当前组织下有人员挂载，若想移除，请先移除人员");
        }
        return organizationMapper.deleteById(id);
    }
    /**
     * 删除事件
     * @param ids
     * @return
     */
    @Post("/del")
    @Transactional
    public int del(@RequestBody List<Integer> ids){
        User filter = new User();
        if(StringUtils.isEmpty(ids)){
            ids = Arrays.asList(-1000);
        }
        String strIds = String.join(",",ids.stream().map(a->a.toString()).collect(Collectors.toList()));
        filter.setFilter("org_id in ("+ strIds +")");
        List<User> users = userBiz.selectByFilter(filter);
        if(CollectionUtils.isNotEmpty(users)){
            throw new AjaxException("当前组织下有人员挂载，若想移除，请先移除人员");
        }
        return organizationMapper.deleteBatchIds(ids);
    }
}
