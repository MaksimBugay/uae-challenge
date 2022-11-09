package bmv.coding.challenge.productservice.service;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import bmv.coding.challenge.productservice.dto.ProductInfo;
import bmv.coding.challenge.productservice.exception.ProductUnavailableException;
import bmv.coding.challenge.productservice.exception.ReviewUnavailableException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import java.util.List;
import java.util.function.Function;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class ProductService {

  private final WebClient webClient;
  private final List<HttpStatus> acceptableStatuses = List.of(OK, NOT_FOUND);

  @Value("${product-service.public-api.url:https://www.adidas.co.uk/api/products/{product_id}}")
  private String publicApiUrl;

  @Value("${review-service.base-path}")
  private String reviewServiceUrl;

  public ProductService(WebClient webClient) {
    this.webClient = webClient;
  }

  @CircuitBreaker(name = "productReviewStat", fallbackMethod = "fallback")
  public Mono<ProductInfo> getProductReviewStat(String productId) {
    return getProductReviewStat(productId,
        clientResponse -> {
          if (!OK.equals(clientResponse.statusCode())) {
            return clientResponse.bodyToMono(String.class)
                .flatMap(errorBody -> Mono.error(
                    getReviewUnavailableException(
                        errorBody,
                        productId)
                ));
          }
          return clientResponse.bodyToMono(ProductInfo.class);
        }
    );
  }

  public <T> Mono<T> getProductReviewStat(String productId,
      Function<ClientResponse, ? extends Mono<T>> responseHandler) {
    return webClient.get()
        .uri(reviewServiceUrl, productId)
        .exchangeToMono(responseHandler);
  }

  public Mono<ProductInfo> getProductReviewStat(String productId, ProductInfo productInfo) {
    return getProductReviewStat(productId).map(reviewsInfo -> new ProductInfo(
        productId,
        productInfo.getMessage(),
        productInfo.getLocation(),
        reviewsInfo.getAverageScore(),
        reviewsInfo.getNumberOfReviews()
    ));
  }

  @CircuitBreaker(name = "productViaPublicAPI", fallbackMethod = "fallback")
  public Mono<ProductInfo> getProductViaPublicAPI(String productId) {
    return getProductViaPublicAPI(productId,
        clientResponse -> {
          boolean success = true;
          if (!acceptableStatuses.contains(clientResponse.statusCode())) {
            success = false;
          }
          List<String> contentTypeHeaders = clientResponse.headers().header("Content-Type");
          if (CollectionUtils.isEmpty(contentTypeHeaders)) {
            success = false;
          }
          if (!contentTypeHeaders.get(0).contains(APPLICATION_JSON_VALUE)) {
            success = false;
          }
          if (!success) {
            return clientResponse.bodyToMono(String.class)
                .flatMap(errorBody -> Mono.error(
                    getProductUnavailableException(
                        errorBody,
                        productId)
                ));
          }
          return clientResponse.bodyToMono(ProductInfo.class);
        });
  }

  public Mono<ProductInfo> fallback(Throwable ex) {
    return Mono.just(new ProductInfo());
  }

  public <T> Mono<T> getProductViaPublicAPI(String productId,
      Function<ClientResponse, ? extends Mono<T>>
          responseHandler) {
    return webClient.get()
        .uri(publicApiUrl, productId)
        .exchangeToMono(responseHandler);
  }

  private ProductUnavailableException getProductUnavailableException(
      String body, String productId) {
    return new ProductUnavailableException(
        String.format(
            "Cannot get data for product id %s, details: %s",
            productId, body), productId);
  }

  private ReviewUnavailableException getReviewUnavailableException(
      String body, String productId) {
    return new ReviewUnavailableException(
        String.format(
            "Cannot get reviews for product id %s, details: %s",
            productId, body), productId);
  }
}
