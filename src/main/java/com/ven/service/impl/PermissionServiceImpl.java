package com.ven.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.ven.domain.permission.Permission;
import com.ven.repository.PermissionRepository;
import com.ven.service.PermissionService;

import javax.persistence.criteria.*;

@Service
public class PermissionServiceImpl implements PermissionService {

	@Autowired
	private PermissionRepository permissionRepository;

	@Override
	public Page<Permission> findAll(Permission permission, Pageable pageable) {
		Specification<Permission> spec = new Specification<Permission>() {       //查询条件构造

			@Override
			public Predicate toPredicate(Root<Permission> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> list = new ArrayList<Predicate>();

				if (StringUtils.isNotBlank(permission.getName())) {
					list.add(cb.like(root.get("name").as(String.class), "%" + permission.getName() + "%"));
				}

				Predicate[] p = new Predicate[list.size()];
				if (p.length > 0) {
					query.where(cb.or(list.toArray(p)));
				}

				return query.getRestriction();
			}
		};
		return permissionRepository.findAll(spec, pageable);
	}

	@Override
	public List<Permission> findAll(){
		return (List<Permission>) permissionRepository.findAll();
	}

	@Override
	public Permission findByName(String name) {
		return permissionRepository.findByName(name);
	}
	
	@Override
	public Permission findById(int id) {
		return permissionRepository.findOne(id);
	}
	
	@Override
	public Permission dynamicSave(Integer id,Permission permission) {
		return permissionRepository.dynamicSave(id,permission);
	}

	@Override
	public void delete(Integer id){
		permissionRepository.delete(id);
	}
}
