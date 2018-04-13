package com.ven.repository;

import com.ven.domain.permission.Log;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface LogRepository extends ExtJpaRepository<Log,Integer>,JpaSpecificationExecutor {
}
