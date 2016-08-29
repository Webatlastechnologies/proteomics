package com.vkhatri.repository;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.vkhatri.modal.Transaction;

@RepositoryRestResource(collectionResourceRel = "transaction", path = "transaction", exported = true)
public interface TransactionRepository extends PagingAndSortingRepository<Transaction, Long> {

	List<Transaction> findByBenificary(@Param("name") String name);

}