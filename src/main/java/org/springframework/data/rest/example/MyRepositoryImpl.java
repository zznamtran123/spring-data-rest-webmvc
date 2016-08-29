package org.springframework.data.rest.example;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.QueryDslJpaRepository;

import com.github.vineey.rql.RqlInput;
import com.github.vineey.rql.querydsl.DefaultQuerydslRqlParser;
import com.github.vineey.rql.querydsl.QuerydslMappingParam;
import com.github.vineey.rql.querydsl.QuerydslMappingResult;
import com.github.vineey.rql.querydsl.spring.SpringUtil;
import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.Path;

public class MyRepositoryImpl<T, ID extends Serializable> extends QueryDslJpaRepository<T, ID>
		implements MyRepository<T, ID> {

	public MyRepositoryImpl(JpaEntityInformation<T, ID> entityInformation, EntityManager entityManager) {
		super(entityInformation, entityManager);
	}

	public Page<T> findByRsql(String query) {
		return findByRsql(query, null);
	}

	@Override
	public Page<T> findByRsql(String query, Pageable pageable) {
		System.out.println(query);
		String limit = "limit(0, 1000)";
		if (query.contains("&limit(")) {
			int index = query.indexOf("&limit(") + 1;
			int indexTo = query.indexOf("&", index);
			limit = query.substring(index, indexTo == -1 ? query.length() : indexTo);
			query = query.replace("&" + limit, "");
		}
		RqlInput rqlInput = new RqlInput().setLimit(limit);
		if (query.contains("&sort(")) {
			int index = query.indexOf("&sort(") + 1;
			int indexTo = query.indexOf("&", index);
			rqlInput.setSort(query.substring(index, indexTo == -1 ? query.length() : indexTo));
			query = query.replace("&" + rqlInput.getSort(), "");
		}
		if (query.contains("&select(")) {
			int index = query.indexOf("&select(") + 1;
			int indexTo = query.indexOf("&", index);
			rqlInput.setSelect(query.substring(index, indexTo == -1 ? query.length() : indexTo));
			query = query.replace("&" + rqlInput.getSelect(), "");
		}
		System.out.println(query);
		rqlInput.setFilter(query);
		Map<String, Path> pathMapping = new HashMap<>();
		final String path = StringUtils.uncapitalize(getDomainClass().getSimpleName());
		try {
			final Class<?> qclass = Class
					.forName(getDomainClass().getPackage().getName() + ".Q" + getDomainClass().getSimpleName());
			Field field = FieldUtils.getDeclaredField(qclass, path + "1");
			if (field == null) {
				field = FieldUtils.getDeclaredField(qclass, path);
			}
			EntityPath<?> ePath = (EntityPath<?>) field.get(null);
			for (Field fld : FieldUtils.getAllFields(qclass)) {
				if (java.lang.reflect.Modifier.isPublic(fld.getModifiers())
						&& !java.lang.reflect.Modifier.isStatic(fld.getModifiers())
						&& Path.class.isAssignableFrom(fld.getType()))
					pathMapping.put(fld.getName(), (Path<?>) fld.get(ePath));
			}
			System.out.println(pathMapping);
			QuerydslMappingResult mapResult = new DefaultQuerydslRqlParser().parse(rqlInput,
					new QuerydslMappingParam().setRootPath(ePath).setPathMapping(pathMapping));
			if (pageable == null)
				pageable = mapResult.getOrderSpecifiers() == null
						? new PageRequest(mapResult.getPage().getOffsetAsInteger(),
								mapResult.getPage().getLimitAsInteger())
						: SpringUtil.toPageable(mapResult.getOrderSpecifiers(), mapResult.getPage());
			return findAll(mapResult.getPredicate(), pageable);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
