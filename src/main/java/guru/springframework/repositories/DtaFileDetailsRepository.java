package guru.springframework.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;

import guru.springframework.domain.DataFile;

public interface DtaFileDetailsRepository extends PagingAndSortingRepository<DataFile, Long>{

}
