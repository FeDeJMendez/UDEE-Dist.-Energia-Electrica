package edu.utn.udee.Udee.controller.advice;

import edu.utn.udee.Udee.exceptions.ErrorMessage;
import edu.utn.udee.Udee.exceptions.MeterIsRequiredException;
import edu.utn.udee.Udee.exceptions.MeterNotExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class MeterControllerAdvice {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(value = {MeterNotExistsException.class})
    public ResponseEntity<ErrorMessage> meterNotExists(){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).
                body(ErrorMessage.builder().code("MTNE").message("THE METER DOES NO EXISTS.").
                        build());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(value = {MeterIsRequiredException.class})
    public ResponseEntity<ErrorMessage> meterIsRequired(){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).
                body(ErrorMessage.builder().code("MTIR").message("THE METER IS REQUIRED.").
                        build());
    }

}
