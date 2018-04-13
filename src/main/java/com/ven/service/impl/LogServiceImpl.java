package com.ven.service.impl;

import com.ven.domain.permission.Log;
import com.ven.repository.LogRepository;
import com.ven.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class LogServiceImpl implements LogService {

    @Autowired
    private LogRepository logRepository;

    @Override
    public Page<Log> findAll(Log log, Pageable pageable) {
        Specification<Log> spec = new Specification<Log>() {       //查询条件构造

            @Override
            public Predicate toPredicate(Root<Log> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> list = new ArrayList<Predicate>();

                Predicate[] p = new Predicate[list.size()];
                if (p.length > 0) {
                    query.where(cb.or(list.toArray(p)));
                }
                query.orderBy(cb.desc(root.get("createdAt")));
                return query.getRestriction();
            }
        };
        return logRepository.findAll(spec, pageable);
    }

    @Override
    public Log save(Log log){
        return logRepository.save(log);
    }
}
