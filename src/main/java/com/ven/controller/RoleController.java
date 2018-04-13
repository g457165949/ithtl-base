package com.ven.controller;

import com.ven.domain.permission.Permission;
import com.ven.domain.permission.Role;
import com.ven.service.RoleService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping(value = "role")
public class RoleController extends BaseController {

    @Autowired
    private RoleService roleService;

    @GetMapping({"", "index"})
    public String index() {
        return "role/index";
    }

    @GetMapping("list")
    @ResponseBody
    public Map<Object, Object> list(Role role) {
        Page<Role> rolePage = roleService.findAll(role, pageable());
        return success(rolePage);
    }

    @GetMapping("/edit")
    public String edit() {
        return "role/edit";
    }

    @PostMapping("/edit")
    @ResponseBody
    public Map<Object, Object> edit(Role role, @RequestParam(value = "id", defaultValue = "0") Integer id) {
        if (id == 0) {
            role.setCreatedAt((int) (System.currentTimeMillis() / 1000));
        }
        return success(roleService.dynamicSave(id, role));
    }

    @GetMapping("/delete/{id}")
    @ResponseBody
    public Map<Object, Object> delete(@PathVariable Integer id) throws Exception {
        System.out.println("---->>delete:" + id);
        Role role = findModel(id);
        if (role != null) {
            roleService.delete(id);
        }
        return success();
    }

    @GetMapping("/view/{id}")
    public String view(@PathVariable Integer id, ModelMap map) throws Exception {
        System.out.println("---->>vew:" + id);
        Role role = findModel(id);
        List<Map<String,Object>> nodeList = new ArrayList<Map<String,Object>>();
        for (Permission p : role.getPermissions()) {
            Map<String,Object> nodeMap= new HashMap<String,Object>();
            nodeMap.put("id",p.getId());
            nodeMap.put("label",p.getName());
            nodeList.add(nodeMap);
        }
        map.addAttribute("nodeList",nodeList);
        return "role/view";
    }

    @PostMapping("/view/{id}")
    @ResponseBody
    public Map<Object, Object> view(@PathVariable Integer id,@RequestParam String permissionIds) throws Exception {
        Role role = findModel(id);
        String[] pids = permissionIds.split(",");
        List<String> list = new ArrayList<String>();
        List<Permission> plist = new ArrayList<Permission>();
        plist.addAll(role.getPermissions());
        for (Permission p : plist) {
            Arrays.sort(pids);
            int index = Arrays.binarySearch(pids,Integer.toString(p.getId()));
            if(index < 0){
                role.getPermissions().remove(p);
            }else{
                list.add(p.getId().toString());
            }
        }

        for (String s : pids){
            if(!list.contains(s)){
                Permission permission = new Permission();
                permission.setId(Integer.parseInt(s));
                role.getPermissions().add(permission);
            }
        }

        roleService.dynamicSave(id,role);
        return success();
    }


    /**
     * 查询权限对象
     *
     * @param id
     * @return
     * @throws Exception
     */
    protected Role findModel(Integer id) throws Exception {
        Role role = roleService.findOne(id);
        if (role == null) {
            throw new Exception("Object is not find");
        }
        return role;
    }
}
