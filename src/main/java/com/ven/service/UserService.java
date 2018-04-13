package com.ven.service;

import com.ven.domain.account.User;
import com.ven.utils.PageBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface UserService {
	User save(User user);

	User dynamicSave(Integer id,User user);

	Page<User> findAll(User user,Pageable pageable);
	
	User findByUsername(String name);

	User findOne(Integer id);

	void delete(Integer id);

}
