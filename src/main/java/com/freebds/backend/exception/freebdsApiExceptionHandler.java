package com.freebds.backend.exception;

import com.freebds.backend.dto.ApiErrorDTO;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Specific Exception Handler for API Rest Controllers
 */
@RestControllerAdvice
public class freebdsApiExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * How to proceed in case of an EntityNotFoundException
     *
     * @param ex the EntityNotFoundException
     * @return an API Error wrapped in a ResponseEntity with status 404.
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> handleEntityNotFoundException(EntityNotFoundException ex) {
        ApiErrorDTO apiError = new ApiErrorDTO(
                "001",
                "Get request with ID on Entity " + ex.getEntityName() + " returned no element",
                ex.getMessage());
        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }

    /**
     * How to proceed in case of an MethodArgumentNotValidException
     *
     * @param ex the MethodArgumentNotValidException
     * @param headers
     * @param status
     * @param request
     * @return an API Error wrapped in a ResponseEntity with status 400.
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ApiErrorDTO apiError = new ApiErrorDTO(
                "002",
                "Invalid input data",
                "The provided body request is not valid, some fields might be missing or not with a good format");
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    /**
     * How to proceed in case of an CollectionItemNotFoundException
     *
     * @param ex the CollectionItemNotFoundException
     * @return an API Error wrapped in a ResponseEntity with status 404.
     */
    @ExceptionHandler(CollectionItemNotFoundException.class)
    public ResponseEntity<Object> handleCollectionItemNotFoundException(CollectionItemNotFoundException ex) {
        ApiErrorDTO apiError = new ApiErrorDTO(
                "003",
                "Get request with item " + ex.getItem() + " returned no element in the associated collection " + ex.getCollection(),
                ex.getMessage());
        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }

}
