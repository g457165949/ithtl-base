package com.ven.domain.permission;

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import net.sf.json.JSONObject;

/**
 * The persistent class for the permission database table.
 */
@Entity
@Table(name = "sys_permission")
@NamedQuery(name = "Permission.findAll", query = "SELECT p FROM Permission p")
public class Permission implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @NotBlank(message = "权限名称不能为空！")
    private String name;

    @NotBlank(message = "url不能为空！")
    private String url;

    @NotNull(message = "类型不能为空！")
    private Integer type;

    private Integer sort;

    @Column(name = "parent_id")
    private Integer parentId;

    private String perms;

    private String data;

    private String description;

    @ManyToMany
    @JoinTable(name = "sys_role_permission", joinColumns = {@JoinColumn(name = "pid")}, inverseJoinColumns = {@JoinColumn(name = "rid")})
    @JsonBackReference
    private List<Role> roles;

    @Transient
    private List<Permission> childPermissions;

    public Permission() {

    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public List<Permission> getChildPermissions() {
        return childPermissions;
    }

    public void setChildPermissions(List<Permission> childPermissions) {
        this.childPermissions = childPermissions;
    }

    public String getPerms() {
        return perms;
    }

    public void setPerms(String perms) {
        this.perms = perms;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

//    @Override
//    public String toString() {
//        return JSONObject.fromObject(this).toString();
//    }
}