package edu.utn.udee.Udee.repository;

import edu.utn.udee.Udee.domain.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {
    User findByUsernameAndPassword(String username , String password);
}
