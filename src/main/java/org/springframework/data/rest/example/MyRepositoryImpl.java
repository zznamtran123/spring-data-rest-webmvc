package org.springframework.data.rest.example;

import java.io.Serializable;

import javax.persistence.EntityManager;

import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.QueryDslJpaRepository;

public class MyRepositoryImpl<T, ID extends Serializable> extends QueryDslJpaRepository<T, ID>
		implements MyRepository<T, ID> {

	private final EntityManager entityManager;

	public MyRepositoryImpl(JpaEntityInformation entityInformation, EntityManager entityManager) {
		super(entityInformation, entityManager);
		this.entityManager = entityManager;
	}

	public void sharedCustomMethod(ID id) {
		// implementation goes here
	}
}
