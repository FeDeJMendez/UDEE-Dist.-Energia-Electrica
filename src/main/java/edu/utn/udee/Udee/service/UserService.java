package edu.utn.udee.Udee.service;

import edu.utn.udee.Udee.domain.Client;
import edu.utn.udee.Udee.domain.User;
import edu.utn.udee.Udee.domain.enums.Rol;
import edu.utn.udee.Udee.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User login(String username, String password) {
        return userRepository.findByUsernameAndPassword(username, password);
    }

    public void addUser(Client client) {
        User user = new User(client.getSurname(), client.getDni().toString(), Rol.ROLE_CLIENT, client.getId());
        userRepository.save(user);
    }
}
