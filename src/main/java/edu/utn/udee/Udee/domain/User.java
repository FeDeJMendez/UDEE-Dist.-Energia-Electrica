package edu.utn.udee.Udee.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import edu.utn.udee.Udee.domain.enums.Rol;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name ="users")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column
    String username;

    @JsonIgnore
    @Column
    String password;

    @Column(name = "rol")
    @Enumerated(EnumType.STRING)
    Rol rol;

    @Column
    Integer client_id;

    public User(String surname, String dni, Rol roleClient, Integer client_id) {
        this.username = surname;
        this.password = dni;
        this.rol = roleClient;
        this.client_id = client_id;
    }
}
