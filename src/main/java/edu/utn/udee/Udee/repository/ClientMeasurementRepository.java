package edu.utn.udee.Udee.repository;

import edu.utn.udee.Udee.domain.Measurement;
import edu.utn.udee.Udee.projections.KwhAndAmount;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ClientMeasurementRepository extends CrudRepository<Measurement,Integer> {

    @Query(value = "SELECT SUM(ms.kwh) as kwh, SUM(ms.kwh) * rt.amount as amount " +
            "FROM measurements ms " +
            "INNER JOIN meters mt " +
            "ON ms.meter_serial_number = mt.serial_number " +
            "INNER JOIN addresses ad " +
            "ON ad.id = mt.id_address " +
            "INNER JOIN rates rt " +
            "ON ad.rate = rt.id " +
            "WHERE ad.client_id = :idClient " +
            "AND ms.date_time BETWEEN :startDate AND :endDate",
            nativeQuery = true)
    KwhAndAmount findTotalKwhAndAmountByClient(@Param("idClient") Integer idClient,
                                               @Param("startDate") LocalDate startDate,
                                               @Param("endDate") LocalDate endDate);


    @Query(value = "SELECT * " +
            "FROM measurements ms " +
            "INNER JOIN meters mt " +
            "ON ms.meter_serial_number = mt.serial_number " +
            "INNER JOIN addresses ad " +
            "ON ad.id = mt.id_address " +
            "WHERE ad.client_id = :idClient " +
            "AND ms.date_time BETWEEN :startDate AND :endDate",
            nativeQuery = true)
    List<Measurement> findBetweenDates(@Param("idClient") Integer idClient,
                                       @Param("startDate") LocalDate startDate,
                                       @Param("endDate") LocalDate endDate);
}
