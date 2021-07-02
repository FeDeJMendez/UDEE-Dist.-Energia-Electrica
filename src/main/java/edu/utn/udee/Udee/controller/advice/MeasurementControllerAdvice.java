package edu.utn.udee.Udee.controller.advice;

import edu.utn.udee.Udee.exceptions.ErrorMessage;
import edu.utn.udee.Udee.exceptions.MeasurementNotExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class MeasurementControllerAdvice {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(value = {MeasurementNotExistsException.class})
    public ResponseEntity<ErrorMessage> measurementNotExists(){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).
                body(ErrorMessage.builder().code("MTNE").message("THE MEASUREMENT DOES NO EXIST.").
                        build());
    }

}
