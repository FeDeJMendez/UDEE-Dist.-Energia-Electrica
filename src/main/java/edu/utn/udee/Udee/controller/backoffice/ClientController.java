package edu.utn.udee.Udee.controller.backoffice;


import edu.utn.udee.Udee.config.Conf;
import edu.utn.udee.Udee.domain.Client;
import edu.utn.udee.Udee.dto.ClientDto;
import edu.utn.udee.Udee.exceptions.ClientExistsException;
import edu.utn.udee.Udee.exceptions.ClientNotExistsException;
import edu.utn.udee.Udee.service.UserService;
import edu.utn.udee.Udee.service.backoffice.ClientService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/backoffice/clients")
public class ClientController {

    private final ClientService clientService;
    private final UserService userService;
    private final ModelMapper modelMapper;

    @Autowired
    public ClientController (ClientService clientService, UserService userService, ModelMapper modelMapper){
        this.clientService = clientService;
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @PostMapping(consumes = "application/json")
    public ResponseEntity addClient(@RequestBody ClientDto clientDto) throws ClientExistsException {
        Client newClient = clientService.addClient(modelMapper.map(clientDto, Client.class));
        userService.addUser(newClient);
        return ResponseEntity.created(Conf.getLocation(newClient)).build();
    }


    @PutMapping(path = "/{dni}", produces = "application/json")
    public ResponseEntity editClient(@RequestBody ClientDto clientDto, @PathVariable Integer dni) throws ClientNotExistsException {
        Client editedClient = clientService.editClient(modelMapper.map(clientDto, Client.class), dni);

        return  ResponseEntity.ok().build();
    }


    @GetMapping(produces = "application/json")
    public ResponseEntity<List<ClientDto>> allClients(Pageable pageable){
        Page page = clientService.allClients(pageable);
        return response(page);
    }

    //***GET TEN CLIENTS MORE CONSUMERS BY DATETIME RANGE***//
    @GetMapping(value = "topten/{start}/{end}", produces = "application/json")
    public ResponseEntity<List<ClientDto>> getTenMoreConsumersByDateTimeRange (@PathVariable(value = "start") @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate startDate,
                                                                               @PathVariable(value = "end") @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate endDate){
        List<Client> tenClientsMoreConsumers = clientService.getTenMoreConsumersByDateRange(startDate, endDate);
        List<ClientDto> tenClientsMoreConsumersDto = listClientsToDto(tenClientsMoreConsumers);
        return ResponseEntity.
                status(tenClientsMoreConsumersDto.size() != 0 ? HttpStatus.OK : HttpStatus.NO_CONTENT).
                body(tenClientsMoreConsumersDto);
    }

    @DeleteMapping(value = "{id}", produces = "application/json")
    public ResponseEntity deleteClientById(@PathVariable(value = "id") Integer id) throws ClientNotExistsException {
        clientService.deleteClientById(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    public ResponseEntity response(Page page) {
        HttpStatus httpStatus = page.getContent().isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK;
        return ResponseEntity.
                status(httpStatus).
                header("X-Total-Count", Long.toString(page.getTotalElements())).
                header("X-Total-Pages", Long.toString(page.getTotalPages())).
                body(page.getContent());
    }

    public List<ClientDto> listClientsToDto (List<Client> list){
        return list.stream().
                map(x -> ClientDto.from(x)).
                collect(Collectors.toList());
    }
}
