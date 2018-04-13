package com.ven.controller;

import com.ven.config.shiro.MyShiroRealm;
import com.ven.domain.account.User;
import com.ven.domain.permission.Role;
import com.ven.service.RoleService;
import com.ven.service.UserService;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.omg.CosNaming.NamingContextPackage.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "user")
public class UserController extends BaseController {

    @Resource
    private UserService userService;

    @Resource
    private RoleService roleService;

    @RequestMapping(value = {"", "/index"})
    public String index() {
        System.out.println("index");
        return "user/index";
    }

    @RequestMapping("/list")
    @ResponseBody
    public Map<Object, Object> list(User user) {
        Page<User> userPage = userService.findAll(user, pageable());
        return success(userPage);
    }

    @GetMapping(value = {"/edit", "edit"})
    public String edit(ModelMap map) throws Exception {
        List<Role> roleList = roleService.findAll();
        List<Integer> roleIds = new ArrayList<Integer>();
        if (request.getParameter("id") != null) {
            Integer id = Integer.parseInt(request.getParameter("id"));
            User user = findModel(id);
            if (user.getRoleList() != null) {
                for (Role role : user.getRoleList()) {
                    roleIds.add(role.getId());
                }
            }
        }
        map.addAttribute("roleIds", roleIds);
        map.addAttribute("roleList", roleList);
        return "user/edit";
    }

    @PostMapping(value = {"/edit", "edit"})
    @ResponseBody
    public Map<Object, Object> edit(User user) {
        String id = request.getParameter("id");
        String[] userRole = request.getParameterValues("userRole[]");
        List<Role> lr = new ArrayList<Role>();
        System.out.println("----->>user:" + user);

        // 设置角色
        if (userRole != null) {
            for (String roleId : userRole) {
                Role role = new Role();
                role.setId(Integer.parseInt(roleId));
                lr.add(role);
            }
        }
        user.setRoleList(lr);
        // 重新刷新当前角色权限
        if(user.getId() == getUser().getId()) {
            clearAuth();
//            MyShiroRealm.reloadPermissions();
//            MyShiroRealm.reloadAuthorizing(getUser().getUsername());
        }else {
//            myShiroRealm.clearAllCachedAuthorizationInfo();
        }

        if (StringUtils.isNotBlank(id)) {
            if (StringUtils.isBlank(user.getPassword())) {
                user.setPassword(null);
            } else {
                user.setPassword(md5Password(user.getUsername(), user.getPassword()));
            }
            return success(userService.dynamicSave(Integer.parseInt(id), user));
        } else {
            return success(userService.save(user));
        }
    }

    @GetMapping("/delete/{id}")
    @ResponseBody
    public Map<Object, Object> delete(@PathVariable Integer id) throws Exception {
        System.out.println("---->>delete:" + id);
        User user = findModel(id);
        if (user != null) {
            user.setStatus(0);
            userService.dynamicSave(id,user);
        }
        return success();
    }

    @PostMapping("/delete")
    @ResponseBody
    public Map<Object, Object> delete(@RequestParam("ids[]") String[] ids) {
        if (ids != null || (ids == null && ids.length != 0)) {
            for (String id : ids) {
                userService.delete(Integer.parseInt(id));
            }
        }
        return success();
    }


    /**
     * 查询权限对象
     *
     * @param id
     * @return
     * @throws NotEmpty
     */
    protected User findModel(Integer id) throws Exception {
        User user = userService.findOne(id);
        if (user == null) {
            throw new Exception("Object is not find");
        }
        return user;
    }


    /**
     * 密码MD5加密
     *
     * @param username
     * @param password
     * @return
     */
    private String md5Password(String username, String password) {
        ByteSource salt = ByteSource.Util.bytes(username);    //以账号作为盐值
        int hashIterations = 3;
        Object result = new SimpleHash(Md5Hash.ALGORITHM_NAME, password, salt, hashIterations);
        System.out.println(username + ":" + result + ":" + salt);
        return result.toString();
    }
}
