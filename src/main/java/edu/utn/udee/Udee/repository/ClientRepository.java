package edu.utn.udee.Udee.repository;

import edu.utn.udee.Udee.domain.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ClientRepository extends CrudRepository<Client, Integer> {
    Boolean existsByDni(Integer dni);
    Client getByDni(Integer dni);
    Page<Client> findAll(Pageable pageable);

    @Query(value = "SELECT TOP 10 cl.* " +
            "FROM clients cl " +
            "INNER JOIN addresses ad " +
            "ON cl.id = ad.client_id " +
            "INNER JOIN meters mt " +
            "ON ad.id = mt.id_address " +
            "INNER JOIN measurements ms " +
            "ON ms.meter_serial_number = mt.serial_number " +
            "WHERE ms.date_time between ?1 and ?2 " +
            "GROUP BY cl.id " +
            "ORDER BY sum(ms.kwh) DESC", nativeQuery = true)
    List<Client> findTenMoreConsumersByDateTimeRange(LocalDate beginDate, LocalDate endDate);


}
