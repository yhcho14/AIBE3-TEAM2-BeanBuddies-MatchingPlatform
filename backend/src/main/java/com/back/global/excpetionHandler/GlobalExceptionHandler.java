package com.back.global.excpetionHandler;

import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
//    @ExceptionHandler(NoSuchElementException.class)
//    public ResponseEntity<ApiResponse<Void>> handle(NoSuchElementException e) {
//        return new ResponseEntity<>(
//                new ApiResponse<>(
//                        "404-1",
//                        "존재하지 않는 데이터에 접근했습니다."
//                ),
//                NOT_FOUND
//        );
//    }
//
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<ApiResponse<Void>> handle(MethodArgumentNotValidException e) {
//        String message = e.getBindingResult()
//                .getAllErrors()
//                .stream()
//                .filter(error -> error instanceof FieldError)
//                .map(error -> (FieldError) error)
//                .map(error -> error.getField() + "-" + error.getCode() + "-" + error.getDefaultMessage())
//                .sorted(Comparator.comparing(String::toString))
//                .collect(Collectors.joining("\n"));
//
//        return new ResponseEntity<>(
//                new ApiResponse<>(
//                        "400-1",
//                        message
//                ),
//                BAD_REQUEST
//        );
//    }
}