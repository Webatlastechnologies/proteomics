package guru.springframework.repositories;

import org.springframework.data.repository.CrudRepository;

import guru.springframework.domain.Organism;


public interface OrganismRepository extends CrudRepository<Organism, Integer>{

}
