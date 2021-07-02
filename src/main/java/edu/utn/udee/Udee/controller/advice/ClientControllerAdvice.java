package edu.utn.udee.Udee.controller.advice;

import edu.utn.udee.Udee.exceptions.ClientExistsException;
import edu.utn.udee.Udee.exceptions.ClientIsRequiredException;
import edu.utn.udee.Udee.exceptions.ClientNotExistsException;
import edu.utn.udee.Udee.exceptions.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ClientControllerAdvice {

    @ExceptionHandler(value = {ClientExistsException.class})
    public ResponseEntity<ErrorMessage> clientExists(){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ErrorMessage.builder().code("CAE").message("Client already exist!").build());
    }

    @ExceptionHandler(value = {ClientNotExistsException.class})
    public ResponseEntity<ErrorMessage> clientNotExists(){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorMessage.builder().code("CNE").message("Client does no exist!").build());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(value = {ClientIsRequiredException.class})
    public ResponseEntity<ErrorMessage> clientIsRequired(){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).
                body(ErrorMessage.builder().code("CIR").message("THE CLIENT IS REQUIRED!").
                        build());
    }
}
