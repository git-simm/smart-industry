package smart.industry.train.web.controllers.manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import smart.industry.train.biz.entity.Organization;
import smart.industry.train.biz.mapper2.OrganizationMapper;

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
    public Integer Add(){
        Organization org = new Organization();
        org.setName("测试部门");
        org.setCode("12");
        org.setSort("12");
        org.setOrgType(1);
        return organizationMapper.insert(org);
    }
}
