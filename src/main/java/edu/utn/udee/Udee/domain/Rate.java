package edu.utn.udee.Udee.domain;

import edu.utn.udee.Udee.Intarfaces.URIinterface;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "rates")
public class Rate implements URIinterface {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String description;

    @Column
    private Double amount;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "rate")
    private List<Address> addresses;

    @Override
    public Integer getId() {
        return id;
    }
}
