package edu.utn.udee.Udee.controller.advice;

import edu.utn.udee.Udee.exceptions.AddressExistsException;
import edu.utn.udee.Udee.exceptions.AddressNotExistsException;
import edu.utn.udee.Udee.exceptions.AddressWithMeterException;
import edu.utn.udee.Udee.exceptions.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class AddressControllerAdvice {

    @ExceptionHandler(value = {AddressExistsException.class})
    public ResponseEntity<ErrorMessage> addressExists(){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ErrorMessage.builder().code("AAE").message("Address already exist!").build());
    }

    @ExceptionHandler(value = {AddressNotExistsException.class})
    public ResponseEntity<ErrorMessage> addressNotExists(){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorMessage.builder().code("ANE").message("Address no exist!").build());
    }

    @ExceptionHandler(value = {AddressWithMeterException.class})
    public ResponseEntity<ErrorMessage> addressWithMeter(){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ErrorMessage.builder().code("AWM").message("The address already has a meter!").build());
    }
}
