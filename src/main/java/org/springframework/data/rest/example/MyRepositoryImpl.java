package org.springframework.data.rest.example;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.QueryDslJpaRepository;

import com.github.vineey.rql.RqlInput;
import com.github.vineey.rql.querydsl.DefaultQuerydslRqlParser;
import com.github.vineey.rql.querydsl.QuerydslMappingParam;
import com.github.vineey.rql.querydsl.QuerydslMappingResult;
import com.github.vineey.rql.querydsl.QuerydslRqlParser;
import com.github.vineey.rql.querydsl.spring.SpringUtil;
import com.google.common.collect.ImmutableMap;
import com.querydsl.core.QueryModifiers;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.Predicate;

public class MyRepositoryImpl<T, ID extends Serializable> extends QueryDslJpaRepository<T, ID>
		implements MyRepository<T, ID> {

	private final EntityManager entityManager;

	public MyRepositoryImpl(JpaEntityInformation entityInformation, EntityManager entityManager) {
		super(entityInformation, entityManager);
		this.entityManager = entityManager;
	}

	public void sharedCustomMethod(String query) {
		String rqlSelect = "select(contact.name, contact.age)";
		String rqlFilter = "(contact.age =='1' and contact.name == 'A*') or (contact.age > '1'  and contact.bday == '2015-05-05')";
		String limit = "limit(0, 10)";
		String sort = "sort(+contact.name)";

		RqlInput rqlInput = new RqlInput().setSelect(rqlSelect).setFilter(rqlFilter).setLimit(limit).setSort(sort);

		Map<String, Path> pathMapping = ImmutableMap.<String, Path>builder()
				.put("contact.name", QPerson.person.name)
				.put("contact.age", QPerson.person.name)
				.put("contact.bday", QPerson.person.name).build();
		DefaultQuerydslRqlParser querydslRqlParser = new DefaultQuerydslRqlParser();
		QuerydslMappingResult querydslMappingResult = querydslRqlParser.parse(rqlInput,
				new QuerydslMappingParam().setRootPath(QPerson.person).setPathMapping(pathMapping));

		Expression selectExpression = querydslMappingResult.getProjection();
		Predicate predicate = querydslMappingResult.getPredicate();

		QueryModifiers querydslPage = querydslMappingResult.getPage();

		List<OrderSpecifier> orderSpecifiers = querydslMappingResult.getOrderSpecifiers();
		Pageable pageable = SpringUtil.toPageable(orderSpecifiers, querydslPage);
		findAll(predicate, pageable);
	}
}
