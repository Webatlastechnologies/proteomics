package guru.springframework.repositories;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import guru.springframework.domain.Project;


public interface ProjectRepository extends PagingAndSortingRepository<Project, Long>{
	List<Project> findAll();
}
