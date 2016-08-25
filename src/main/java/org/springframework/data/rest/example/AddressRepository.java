package org.springframework.data.rest.example;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

public interface AddressRepository extends PagingAndSortingRepository<Address, Long> {
	public List<Address> findByCity(@Param("city") String city);
}
