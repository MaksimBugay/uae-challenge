package bmv.coding.challenge.productservice.stub;

import bmv.coding.challenge.productservice.dto.ReviewStatDto;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@Profile("test")
public class ReviewServiceController {

  @GetMapping(path = "/review/{product_id}")
  Mono<ResponseEntity<?>> getReviewStatByProduct(
      @PathVariable(name = "product_id") String productId) {
    if ("AC7836".equals(productId)) {
      return Mono.just(new ResponseEntity<>(new ReviewStatDto(productId, 5.0, 100), HttpStatus.OK));
    }
    if ("reviewServiceIsNotAvailable".equals(productId)) {
      return Mono.just(new ResponseEntity<>("Cannot load reviews", HttpStatus.SERVICE_UNAVAILABLE));
    }
    return Mono.just(new ResponseEntity<>(new ReviewStatDto(productId, 0.0, 0), HttpStatus.OK));
  }
}
