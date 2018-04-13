package com.ven.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ven.domain.permission.Permission;

@Repository
public interface PermissionRepository extends ExtJpaRepository<Permission,Integer>,JpaSpecificationExecutor {
	
	Permission findByName(String name);
}
