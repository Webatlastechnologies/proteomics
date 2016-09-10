package guru.springframework.repositories;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import guru.springframework.domain.Experiment;
import guru.springframework.domain.Project;


public interface ExperimentRepository extends PagingAndSortingRepository<Experiment, Long>{
	List<Experiment> findAll();
	List<Experiment> findByProject(Project p);
}
