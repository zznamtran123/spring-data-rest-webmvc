package org.springframework.data.rest.example;

import java.util.List;

import org.springframework.data.repository.query.Param;

public interface PersonRepository extends MyRepository<Person, Long> {
	public List<Person> findByName(@Param("name") String name);
	public List<Person> findById(@Param("id") Long id);
}
