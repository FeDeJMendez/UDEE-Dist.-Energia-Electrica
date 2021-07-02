package edu.utn.udee.Udee.repository;

import edu.utn.udee.Udee.domain.Rate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RateRepository extends CrudRepository<Rate, Integer> {

    Page<Rate> findAll(Pageable pageable);
    Boolean existsByDescription(String description);
}
