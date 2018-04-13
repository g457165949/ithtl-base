package com.ven.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ven.domain.account.User;

@Repository
public interface UserRepository extends ExtJpaRepository<User,Integer>,JpaSpecificationExecutor {

	User findByUsername(String name);

	User dynamicSave(Integer id,User user);
}
