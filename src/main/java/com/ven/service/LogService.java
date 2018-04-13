package com.ven.service;

import com.ven.domain.permission.Log;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LogService {

    Page<Log> findAll(Log log, Pageable pageable);

    Log save(Log log);
}
