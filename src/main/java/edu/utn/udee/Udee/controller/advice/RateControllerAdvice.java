package edu.utn.udee.Udee.controller.advice;

import edu.utn.udee.Udee.exceptions.ErrorMessage;
import edu.utn.udee.Udee.exceptions.RateExistsException;
import edu.utn.udee.Udee.exceptions.RateIsRequiredException;
import edu.utn.udee.Udee.exceptions.RateNotExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class RateControllerAdvice {

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(value = {RateExistsException.class})
    public ResponseEntity<ErrorMessage> rateExists(){
        return ResponseEntity.status(HttpStatus.CONFLICT).
                body(ErrorMessage.builder().code("RE").message("THE RATE ALREADY EXISTING.").
                        build());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(value = {RateNotExistsException.class})
    public ResponseEntity<ErrorMessage> rateNotExists(){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).
                body(ErrorMessage.builder().code("RNE").message("THE RATE DOES NO EXIST.").
                        build());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(value = {RateIsRequiredException.class})
    public ResponseEntity<ErrorMessage> rateIsRequired(){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).
                body(ErrorMessage.builder().code("RIR").message("THE RATE IS REQUIRED!").
                        build());
    }
}
