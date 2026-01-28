package com.nikhil.ecommerce.exception;


import com.nikhil.ecommerce.dto.ErrorResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleNotFound(NotFoundException ex) {
        return ResponseEntity
                .status(404)
                .body(new ErrorResponseDTO(ex.getMessage()));
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponseDTO> handleUnauthorized(UnauthorizedException ex) {
        return ResponseEntity
                .status(403)
                .body(new ErrorResponseDTO(ex.getMessage()));
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponseDTO> handleBadRequest(BadRequestException ex) {
        return ResponseEntity
                .status(400)
                .body(new ErrorResponseDTO(ex.getMessage()));
    }

    // fallback
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleGeneric(Exception ex) {
        return ResponseEntity
                .status(500)
                .body(new ErrorResponseDTO("Internal Server Error: " + ex.getMessage()));
    }
}
