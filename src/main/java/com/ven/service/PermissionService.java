package com.ven.service;

import java.util.List;

import com.ven.domain.permission.Permission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PermissionService {

	List<Permission> findAll();

	Page<Permission> findAll(Permission permission,Pageable pageable);

	Permission findByName(String name);
	
	Permission findById(int id);

	void delete(Integer id);
	
	Permission dynamicSave(Integer id,Permission permission);
}
