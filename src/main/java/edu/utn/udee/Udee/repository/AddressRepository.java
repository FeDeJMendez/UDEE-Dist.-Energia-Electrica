package edu.utn.udee.Udee.repository;

import edu.utn.udee.Udee.domain.Address;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepository extends CrudRepository<Address,Integer> {
    Boolean existsByAddress(String address);
    Page<Address> findAll(Pageable pageable);
    List<Address> findAddressByClientId(Integer id);
    Address findAddressByAddress(String address);
}
