package com.ven.repository;

import com.ven.domain.account.User;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ven.domain.permission.Role;

@Repository
public interface RoleRepository extends ExtJpaRepository<Role,Integer>,JpaSpecificationExecutor {

    Role findByName(String name);

    Role dynamicSave(Integer id,Role role);
}
