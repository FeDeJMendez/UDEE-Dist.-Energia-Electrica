package edu.utn.udee.Udee.controller.client;

import edu.utn.udee.Udee.domain.Bill;
import edu.utn.udee.Udee.domain.Client;
import edu.utn.udee.Udee.dto.BillDto;
import edu.utn.udee.Udee.dto.UserDto;
import edu.utn.udee.Udee.exceptions.ClientNotExistsException;
import edu.utn.udee.Udee.service.UserService;
import edu.utn.udee.Udee.service.backoffice.AddressService;
import edu.utn.udee.Udee.service.backoffice.ClientService;
import edu.utn.udee.Udee.service.client.ClientBillService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/client/bills")
public class ClientBillController {

    private final ClientBillService clientBillService;
    private final ClientService clientService;
    private final UserService userService;
    private final AddressService addressService;
    private final ModelMapper modelMapper;

    @Autowired
    public ClientBillController(ClientBillService clientBillService, ClientService clientService, UserService userService, AddressService addressService, ModelMapper modelMapper) {
        this.clientBillService = clientBillService;
        this.clientService = clientService;
        this.userService = userService;
        this.addressService = addressService;
        this.modelMapper = modelMapper;
    }

    @GetMapping(value = "/dates/{start}/{end}", produces = "application/json")
    public ResponseEntity<List<BillDto>> getBillsBetweenDates(@PathVariable(value = "start") @DateTimeFormat(pattern = "dd-MM-yyyy")  LocalDate startDate, @PathVariable(value = "end") @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate endDate, Authentication auth){
        UserDto userDto = (UserDto) auth.getPrincipal();
        List<Bill> billList = clientBillService.getBillsBetweenDates(startDate, endDate, userDto.getClient_id());
        List<BillDto> billDtoList = billList.stream().map(x -> modelMapper.map(x, BillDto.class)).collect(Collectors.toList());

        return ResponseEntity.
                status(billDtoList.size() != 0 ? HttpStatus.OK : HttpStatus.NO_CONTENT).
                body(billDtoList);
    }

    @GetMapping(value="unpaid", produces = "application/json")
    public ResponseEntity<List<BillDto>> getUnpaidBills(Authentication auth) throws ClientNotExistsException {
        UserDto userDto = (UserDto) auth.getPrincipal();
        Client client = clientService.findClientById(userDto.getClient_id());
        List<Bill> billList = clientBillService.getUnpaidBills(client.getDni());
        List<BillDto> billDtoList = billList.stream().map(x -> modelMapper.map(x, BillDto.class)).collect(Collectors.toList());

        return ResponseEntity.
                status(billDtoList.size() != 0 ? HttpStatus.OK : HttpStatus.NO_CONTENT).
                body(billDtoList);
    }
}
