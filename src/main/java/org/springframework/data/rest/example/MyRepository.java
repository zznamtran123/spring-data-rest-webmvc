package org.springframework.data.rest.example;

import java.io.Serializable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;

@NoRepositoryBean
public interface MyRepository<T, ID extends Serializable> extends PagingAndSortingRepository<T, ID>, QueryDslPredicateExecutor<T> {
	Page<T> findByRsql(String query);
	Page<T> findByRsql(String query, Pageable pageable);
}