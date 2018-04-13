package com.ven.service;

import com.ven.domain.permission.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RoleService {

    Role dynamicSave(Integer id,Role role);

    Page<Role> findAll(Role role, Pageable pageable);

    List<Role> findAll();

    Role findByName(String name);

    Role findOne(Integer id);

    void delete(Integer id);
}
