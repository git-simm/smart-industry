package smart.industry.train.web.config;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import smart.industry.train.biz.dao.UserBiz;
import smart.industry.train.biz.entity.User;

public class UserRealm extends AuthorizingRealm {

    @Autowired
    private UserBiz userService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        /*String username = principals.getPrimaryPrincipal().toString();
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        Set<String> roleName = userService.findRolesByUserName(username);
        Set<String> permissions = userService.findPermissionsByUserName(username);
        info.setRoles(roleName);
        info.setStringPermissions(permissions);
        return info;*/
        return null;
    }

    /**
     * 验证当前登录的Subject LoginController.login()方法中执行Subject.login()时 执行此方法
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) {
        UsernamePasswordToken utoken = (UsernamePasswordToken) token;// 获取用户输入的token
        String username = utoken.getUsername();
        User user = userService.findUserByName(username);
        return new SimpleAuthenticationInfo(user, user.getPsw(), this.getClass().getName());// 放入shiro.调用CredentialsMatcher检验密码
    }
}
