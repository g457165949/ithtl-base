package com.ven.controller;

import com.ven.domain.permission.Permission;
import com.ven.service.PermissionService;
import org.apache.commons.lang.StringUtils;
import org.omg.CosNaming.NamingContextPackage.NotEmpty;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("permission")
public class PermissionController extends BaseController{

    @Resource
    PermissionService permissionService;

    @RequestMapping({"/", "/index"})
    public String Index() {
        return "permission/index";
    }

    @GetMapping("list")
    @ResponseBody
    public Map<Object, Object> list(Permission permission) {
        Page<Permission> permissionPage = permissionService.findAll(permission,pageable());
        return success(permissionPage);
    }


    @GetMapping("/items")
    @ResponseBody
    public Map<Object, Object> items() {
        // 原始的数据
        List<Permission> rootMenu = permissionService.findAll();
        // 最后的结果
        List<Permission> menuList = new ArrayList<Permission>();
        // 先找到所有的一级菜单
        for (int i = 0; i < rootMenu.size(); i++) {
            // 一级菜单没有parentId
            if (rootMenu.get(i).getParentId() == 0) {
                menuList.add(rootMenu.get(i));
            }
        }
        // 为一级菜单设置子菜单，getChild是递归调用的
        for (Permission menu : menuList) {
            menu.setChildPermissions(getChild(menu.getId(), rootMenu));
        }
        return success(menuList);
    }

    @GetMapping("/tree")
    public String Tree(){
        return "permission/tree";
    }

    @GetMapping("/edit")
    public String edit(@RequestParam(value = "parent_id", defaultValue = "0") Integer parent_id,ModelMap map) {
        if(parent_id > 0){
            Permission permission = permissionService.findById(parent_id);
            map.addAttribute("parent",permission);
        }
        return "permission/edit";
    }

    @PostMapping("/edit")
    @ResponseBody
    public Map<Object, Object> edit(Permission permission, @RequestParam(value = "id", defaultValue = "0") Integer id) {
        System.out.println(permission.toString());
        return success(permissionService.dynamicSave(id, permission));
    }

    @GetMapping("/delete/{id}")
    @ResponseBody
    public Map<Object, Object> delete(@PathVariable Integer id) throws Exception {
        System.out.println("---->>delete:" + id);
        Permission permission = findModel(id);
        if (permission != null) {
            permissionService.delete(id);
        }
        return success();
    }
//
//    @GetMapping("view")
//    public String View(@RequestParam int id, Model model) throws Exception {
//        model.addAttribute("model", findModel(id));
//        return "permission/view";
//    }

    /**
     * 递归查找子菜单
     * @param id 当前菜单id
     * @param rootMenu 要查找的列表
     * @return
     * */
    private List<Permission> getChild(Integer id, List<Permission> rootMenu) {
        // 子菜单
        List<Permission> childList = new ArrayList<>();
        for (Permission permission : rootMenu) {
            // 遍历所有节点，将父菜单id与传过来的id比较
            if (permission.getParentId() > 0) {
                if (permission.getParentId().equals(id)) {
                    childList.add(permission);
                }
            }
        }
        // 把子菜单的子菜单再循环一遍
        for (Permission permission : childList) {
            // 没有url子菜单还有子菜单
            if (permission.getType() == 1) {
                permission.setChildPermissions(getChild(permission.getId(), rootMenu));
            }
        }
        // 递归退出条件
        if (childList.size() == 0) {
            return null;
        }
        return childList;
    }

    /**
     * 查询权限对象
     *
     * @param id
     * @return
     * @throws NotEmpty
     */
    protected Permission findModel(int id) throws Exception {
        Permission permission = permissionService.findById(id);
        if (permission == null) {
            throw new Exception("Object is not find");
        }
        return permission;
    }
}
