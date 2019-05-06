package smart.industry.train.web.controllers.manager;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import smart.industry.train.biz.entity.Organization;
import smart.industry.train.biz.mapper2.OrganizationMapper;
import smart.industry.utils.annotations.Post;

import java.util.List;
import java.util.Map;

/**
 * 组织架构管理
 */
@Controller
@RequestMapping("/org")
public class OrganizationController {
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
        wrapper.eq("id",id);
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
        return organizationMapper.updateById(entity);
    }

    /**
     * 删除
     *
     * @param id
     * @return
     */
    @Post("/delete")
    public int delete(Integer id) {
        return organizationMapper.deleteById(id);
    }
}
