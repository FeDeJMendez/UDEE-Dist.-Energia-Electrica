package edu.utn.udee.Udee.controller.advice;

import edu.utn.udee.Udee.exceptions.BillNotExistsException;
import edu.utn.udee.Udee.exceptions.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class BillControllerAdvice {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(value = {BillNotExistsException.class})
    public ResponseEntity<ErrorMessage> billNotExists(){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).
                body(ErrorMessage.builder().code("BNE").message("THE BILL DOES NO EXIST.").
                        build());
    }
}
