package bmv.coding.challenge.productservice.controller;

import bmv.coding.challenge.productservice.dto.ErrorResponse;
import bmv.coding.challenge.productservice.exception.ProductUnavailableException;
import bmv.coding.challenge.productservice.exception.ReviewUnavailableException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import reactor.core.publisher.Mono;

public abstract class ControllerExceptionHandler {

  private static final Logger LOGGER =
      LoggerFactory.getLogger("Product-API");

  @ExceptionHandler
  public Mono<ResponseEntity<ErrorResponse>> handle(Throwable ex) {
    LOGGER.error("Unexpected error", ex);
    ErrorResponse body = new ErrorResponse();
    body.setStatus("ERROR");
    body.setMessage(ex.getMessage());
    return Mono.just(new ResponseEntity<>(body,
        HttpStatus.EXPECTATION_FAILED));
  }

  @ExceptionHandler
  public Mono<ResponseEntity<ErrorResponse>> handle(
      ProductUnavailableException ex) {
    LOGGER.error("Cannot get data for product id {}", ex.getProductId(), ex);
    ErrorResponse body = new ErrorResponse();
    body.setStatus("ERROR");
    body.setMessage(ex.getMessage());
    return Mono.just(new ResponseEntity<>(body, HttpStatus.UNPROCESSABLE_ENTITY));
  }

    @ExceptionHandler
  public Mono<ResponseEntity<ErrorResponse>> handle(
      ReviewUnavailableException ex) {
    LOGGER.error("Cannot get reviews for product id {}", ex.getProductId(), ex);
    ErrorResponse body = new ErrorResponse();
    body.setStatus("ERROR");
    body.setMessage(ex.getMessage());
    return Mono.just(new ResponseEntity<>(body, HttpStatus.SERVICE_UNAVAILABLE));
  }

}
