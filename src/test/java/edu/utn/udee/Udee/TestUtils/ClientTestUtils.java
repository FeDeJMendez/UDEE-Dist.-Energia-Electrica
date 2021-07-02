package edu.utn.udee.Udee.TestUtils;

import edu.utn.udee.Udee.domain.Address;
import edu.utn.udee.Udee.domain.Client;
import edu.utn.udee.Udee.dto.ClientDto;

import java.util.List;

import static java.util.Collections.emptyList;

public class ClientTestUtils {

    public static ClientDto getClientDto() {
        return ClientDto.builder()
                .name("Matias")
                .surname("Amoruso")
                .dni(1234)
                .address(null)
                .build();
    }

    public static Client getClientWithoutId() {
        return Client.builder()
                .name("Matias")
                .surname("Amoruso")
                .dni(1234)
                .address(null)
                .build();
    }

    public static Client getClientWithId(){
        return Client.builder()
                .id(1)
                .name("Matias")
                .surname("Amoruso")
                .dni(1234)
                .address(null)
                .build();
    }


    public static ClientDto getClientDtoWithId(){
        return ClientDto.builder()
                .id(1)
                .name("Matias")
                .surname("Amoruso")
                .dni(1234)
                .address(null)
                .build();
    }

    public static Client getClientWithAddresses(){
        List<Address> addresses = AddressTestUtils.getAddressList();
        return Client.builder()
                .id(1)
                .name("Matias")
                .surname("Amoruso")
                .dni(1234)
                .address(addresses)
                .build();
    }

    public static List<Client> getClientsList(){
        List<Client> clientList = List.of(Client.builder().name("Matias").surname("Amoruso").dni(1234).address(null).build(),
                Client.builder().name("Federico").surname("Mendez").dni(2345).address(null).build()
        );

        return clientList;
    }

    public static List<Client> getEmptyClientList(){
        return emptyList();
    }

    public static List<ClientDto> getClientsDtoList(){
        List<ClientDto> clientDtoList = List.of(ClientDto.builder().name("Matias").surname("Amoruso").dni(1234).address(null).build(),
                ClientDto.builder().name("Federico").surname("Mendez").dni(2345).address(null).build()
        );

        return clientDtoList;
    }
}
