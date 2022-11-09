package bmv.coding.challenge.productservice.controller;

import bmv.coding.challenge.productservice.dto.ProductInfo;
import bmv.coding.challenge.productservice.service.ProductService;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class ProductController extends ControllerExceptionHandler {

  private static final String PRODUCT_NOT_FOUND_API_MESSAGE = "Product not found";

  private final ProductService productService;

  public ProductController(
      ProductService productService) {
    this.productService = productService;
  }

  @GetMapping(path = "/product/{product_id}")
  Mono<HttpEntity<ProductInfo>> getProductInfo(
      @PathVariable(name = "product_id") String productId) {
    return productService.getProductViaPublicAPI(productId)
        .flatMap(productInfo -> productService.getProductReviewStat(productId, productInfo))
        .map(productInfo -> PRODUCT_NOT_FOUND_API_MESSAGE.equals(productInfo.getMessage())
            ? new ResponseEntity<>(productInfo, HttpStatus.NOT_FOUND)
            : new ResponseEntity<>(productInfo, HttpStatus.OK));
  }

}
