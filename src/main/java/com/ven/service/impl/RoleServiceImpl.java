package com.ven.service.impl;

import com.ven.domain.permission.Role;
import com.ven.repository.RoleRepository;
import com.ven.service.RoleService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public Role dynamicSave(Integer id, Role role) {
        return roleRepository.dynamicSave(id, role);
    }

    @Override
    public Page<Role> findAll(Role role, Pageable pageable) {
        Specification<Role> spec = new Specification<Role>() {       //查询条件构造

            @Override
            public Predicate toPredicate(Root<Role> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> list = new ArrayList<Predicate>();

                if (StringUtils.isNotBlank(role.getName())) {
                    list.add(cb.like(root.get("name").as(String.class), "%" + role.getName() + "%"));
                }

                Predicate[] p = new Predicate[list.size()];
                if (p.length > 0) {
                    query.where(cb.or(list.toArray(p)));
                }

                return query.getRestriction();
            }
        };
        return roleRepository.findAll(spec, pageable);
    }

    @Override
    public List<Role> findAll(){
        return roleRepository.findAll();
    }

    @Override
    public Role findByName(String name) {
        return roleRepository.findByName(name);
    }

    @Override
    public Role findOne(Integer id) {
        return roleRepository.findOne(id);
    }

    @Override
    public void delete(Integer id) {
        roleRepository.delete(id);
    }
}
