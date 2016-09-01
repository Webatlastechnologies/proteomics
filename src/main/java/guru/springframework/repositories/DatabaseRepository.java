package 	guru.springframework.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;

import guru.springframework.domain.LabDatabase;


public interface DatabaseRepository extends PagingAndSortingRepository<LabDatabase, Integer>{

}
