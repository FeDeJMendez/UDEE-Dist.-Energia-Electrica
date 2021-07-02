package edu.utn.udee.Udee.TestUtils;

import edu.utn.udee.Udee.domain.User;
import edu.utn.udee.Udee.domain.enums.Rol;
import edu.utn.udee.Udee.dto.UserDto;

import static edu.utn.udee.Udee.domain.enums.Rol.*;

public class UserTestUtils {

    public static User getUser(){
        return User.builder().
                id(1).
                username("employee1").
                password("1234").
                rol(ROLE_BACKOFFICE).
                client_id(null).
                build();
    }

    public static UserDto getUserDto(){
        return UserDto.builder().
                id(1).
                username("employee1").
                rol(ROLE_BACKOFFICE).
                client_id(null).
                build();
    }
}
