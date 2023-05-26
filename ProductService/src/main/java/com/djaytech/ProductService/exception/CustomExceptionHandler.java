package com.djaytech.ProductService.exception;

import com.djaytech.ProductService.exception.product.DeletedProductException;
import com.djaytech.ProductService.exception.product.EmptyProductNameException;
import com.djaytech.ProductService.exception.product.InvalidPriceException;
import com.djaytech.ProductService.exception.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        Map<String, String> errorMessage = new HashMap<>();

        bindingResult.getFieldErrors().forEach(fieldError ->
                errorMessage.put(fieldError.getField(), fieldError.getDefaultMessage())
        );

        ErrorResponse errorResponse = ErrorResponse.builder()
                .errorMessage(errorMessage)
                .errorCode("VALIDATION_ERROR")
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }




    @ExceptionHandler(DeletedProductException.class)
    public ResponseEntity<ErrorResponse> handleDeletedProductException(DeletedProductException ex) {
        Map<String, String> errorMessage = new HashMap<>();
        errorMessage.put("error", ex.getMessage());

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrorMessage(errorMessage);
        errorResponse.setErrorCode("DELETED_PRODUCT_ERROR");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }


    @ExceptionHandler(InvalidPriceException.class)
    public ResponseEntity<ErrorResponse> handleInvalidPriceException(InvalidPriceException ex) {
        Map<String, String> errorMessage = new HashMap<>();
        errorMessage.put("error", ex.getMessage());

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrorMessage(errorMessage);
        errorResponse.setErrorCode("INVALID_PRICE_ERROR");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(EmptyProductNameException.class)
    public ResponseEntity<ErrorResponse> handleEmptyProductNameException(EmptyProductNameException ex) {
        Map<String, String> errorMessage = new HashMap<>();
        errorMessage.put("error", ex.getMessage());

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrorMessage(errorMessage);
        errorResponse.setErrorCode("EMPTY_PRODUCT_NAME_ERROR");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

}

