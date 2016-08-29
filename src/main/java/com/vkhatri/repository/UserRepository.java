package com.vkhatri.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.security.access.prepost.PostFilter;

import com.vkhatri.modal.User;

@RepositoryRestResource(collectionResourceRel = "user", path = "user", exported = true)
public interface UserRepository extends PagingAndSortingRepository<User, Long> {

	List<User> findByLastName(@Param("name") String name);
	User findByUsernameOrEmail(@Param("username") String username,@Param("email") String email);
	
	@Override
	@PostFilter ("filterObject.username == authentication.name")
	Iterable<User> findAll();
	
	@Override
	@PostFilter ("filterObject.username == authentication.name")
	Iterable<User> findAll(Iterable<Long> arg0);
	
	@Override
	@PostFilter ("filterObject.username == authentication.name")
	Page<User> findAll(Pageable arg0);
	
	@Override
	@PostFilter ("filterObject.username == authentication.name")
	Iterable<User> findAll(Sort arg0);
	
	
	
}