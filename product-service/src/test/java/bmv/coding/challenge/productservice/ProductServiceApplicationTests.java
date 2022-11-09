package bmv.coding.challenge.productservice;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT;

import bmv.coding.challenge.productservice.dto.ErrorResponse;
import bmv.coding.challenge.productservice.dto.ProductInfo;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;

@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
@DirtiesContext
@SpringBootTest(classes = {
    ProductServiceApplication.class}, webEnvironment = DEFINED_PORT)
class ProductServiceApplicationTests {

  @LocalServerPort
  private String port;

  private WebTestClient client;

  @BeforeEach
  void init() {
    client = WebTestClient
        .bindToServer()
        .baseUrl("http://localhost:" + port)
        .responseTimeout(Duration.of(1, ChronoUnit.MINUTES))
        .build();
  }

  @Test
  void contextLoads() {
  }

  @Test
  void getProductInfoTest() {
    String productId = "AC7836";
    HttpStatus expectedStatus = HttpStatus.OK;
    final WebTestClient.ResponseSpec response = client.get()
        .uri("/product/{product_id}", productId)
        .exchange()
        .expectStatus().isEqualTo(expectedStatus.value());
    ProductInfo productInfo = response.returnResult(ProductInfo.class).getResponseBody()
        .blockFirst();
    Assertions.assertNotNull(productInfo);
    Assertions.assertEquals("Product redirect", productInfo.getMessage());
    Assertions.assertEquals("/ultraboost", productInfo.getLocation());
    Assertions.assertEquals(5.0, productInfo.getAverageScore());
    Assertions.assertEquals(100, productInfo.getNumberOfReviews());
  }

    @Test
  void getProductInfoNoReviewsTest() {
    String productId = "C77154";
    HttpStatus expectedStatus = HttpStatus.OK;
    final WebTestClient.ResponseSpec response = client.get()
        .uri("/product/{product_id}", productId)
        .exchange()
        .expectStatus().isEqualTo(expectedStatus.value());
    ProductInfo productInfo = response.returnResult(ProductInfo.class).getResponseBody()
        .blockFirst();
    Assertions.assertNotNull(productInfo);
    Assertions.assertEquals("Product redirect", productInfo.getMessage());
    Assertions.assertEquals("/superstar", productInfo.getLocation());
    Assertions.assertEquals(0.0, productInfo.getAverageScore());
    Assertions.assertEquals(0, productInfo.getNumberOfReviews());
  }

  @Test
  void reviewServiceIsNotAvailableTest() {
    String productId = "reviewServiceIsNotAvailable";
    HttpStatus expectedStatus = HttpStatus.SERVICE_UNAVAILABLE;
    final WebTestClient.ResponseSpec response = client.get()
        .uri("/product/{product_id}", productId)
        .exchange()
        .expectStatus().isEqualTo(expectedStatus.value());
    ErrorResponse errorResponse = response.returnResult(ErrorResponse.class).getResponseBody()
        .blockFirst();
    Assertions.assertNotNull(errorResponse);
    Assertions.assertEquals("ERROR", errorResponse.getStatus());
    Assertions.assertNotNull(errorResponse.getMessage());
  }

  @Test
  void getUnknownProductInfoTest() {
    String productId = "C77154CCC";
    HttpStatus expectedStatus = HttpStatus.NOT_FOUND;
    final WebTestClient.ResponseSpec response = client.get()
        .uri("/product/{product_id}", productId)
        .exchange()
        .expectStatus().isEqualTo(expectedStatus.value());
    ProductInfo productInfo = response.returnResult(ProductInfo.class).getResponseBody()
        .blockFirst();
    Assertions.assertNotNull(productInfo);
    Assertions.assertEquals("Product not found", productInfo.getMessage());
    Assertions.assertNull(productInfo.getLocation());
    Assertions.assertEquals(0.0, productInfo.getAverageScore());
    Assertions.assertEquals(0, productInfo.getNumberOfReviews());
  }

  @Test
  void getMalformedProductIdTest() {
    String productId = UUID.randomUUID().toString();
    HttpStatus expectedStatus = HttpStatus.UNPROCESSABLE_ENTITY;
    final WebTestClient.ResponseSpec response = client.get()
        .uri("/product/{product_id}", productId)
        .exchange()
        .expectStatus().isEqualTo(expectedStatus.value());
    ErrorResponse errorResponse = response.returnResult(ErrorResponse.class).getResponseBody()
        .blockFirst();
    Assertions.assertNotNull(errorResponse);
    Assertions.assertEquals("ERROR", errorResponse.getStatus());
    Assertions.assertNotNull(errorResponse.getMessage());
  }
}
