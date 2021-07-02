package edu.utn.udee.Udee.repository;

import edu.utn.udee.Udee.domain.Bill;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ClientBillRepository extends CrudRepository<Bill,Integer> {

    @Query(value = "SELECT * FROM bills WHERE date >= :startDate AND date <= :endDate and :id = id", nativeQuery = true)
    List<Bill> getAllBetweenDates(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("id") Integer dni);

    @Query(value = "SELECT * FROM BILLS WHERE DNI = :dni and PAID = FALSE", nativeQuery = true)
    List<Bill> findUnpaidBillsByDni(@Param("dni") Integer dni);
}
