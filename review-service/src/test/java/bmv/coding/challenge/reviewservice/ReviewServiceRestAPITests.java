package bmv.coding.challenge.reviewservice;

import static org.junit.Assert.assertEquals;
import static org.springframework.http.HttpStatus.OK;

import bmv.coding.challenge.reviewservice.dto.AddReviewDto;
import bmv.coding.challenge.reviewservice.dto.ReviewStatDto;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ReviewServiceRestAPITests extends ReviewServiceBaseTest {

  @LocalServerPort
  private int port;

  @Value("${server.servlet.context-path}")
  private String contextPath;

  @Value("${review-service.basic-auth.user}")
  private String basicAuthUser;

  @Value("${review-service.basic-auth.password}")
  private String basicAuthPassword;

  @Autowired
  private TestRestTemplate restTemplate;

  private String baseUrl;

  @BeforeEach
  void init() {
    baseUrl = "http://localhost:" + port + contextPath + "/review/{product_id}";
  }

  @Test
  void resetAllReviewsTest() {
    String productId = UUID.randomUUID().toString();

    //not authorised
    Assertions
        .assertEquals("HTTP Status 401 - Full authentication is required to access this resource",
            restTemplate.exchange(baseUrl, HttpMethod.DELETE, new HttpEntity<>(new HttpHeaders()),
                String.class, productId).getBody().trim());

    for (int i = 0; i < 10; i++) {
      addReview(productId, i + 1);
    }
    ReviewStatDto response =
        restTemplate.getForObject(baseUrl, ReviewStatDto.class, productId);

    Assertions.assertEquals(productId, response.getProductId());
    Assertions.assertEquals(5.5, response.getAverageScore());
    Assertions.assertEquals(10, response.getNumberOfReviews());

    resetAllReview(productId);

    response =
        restTemplate.getForObject(baseUrl, ReviewStatDto.class, productId);

    Assertions.assertEquals(productId, response.getProductId());
    Assertions.assertEquals(0.0, response.getAverageScore());
    Assertions.assertEquals(0, response.getNumberOfReviews());
  }

  @Test
  void addReviewTest() {
    String productId = UUID.randomUUID().toString();

    //not authorised
    Assertions.assertThrowsExactly(ResourceAccessException.class,
        () -> restTemplate.postForEntity(baseUrl, null, AddReviewDto.class, productId));
    //empty email
    Assertions.assertThrows(AssertionFailedError.class,
        () -> addReview(productId, new AddReviewDto("", 1)),
        "expected: <200 OK> but was: <400 BAD_REQUEST>"
    );
    //malformed email
    Assertions.assertThrows(AssertionFailedError.class,
        () -> addReview(productId, new AddReviewDto("mbugai", 1)),
        "expected: <200 OK> but was: <400 BAD_REQUEST>"
    );
    //score not from range < 1
    Assertions.assertThrows(RestClientException.class,
        () -> addReview(productId, new AddReviewDto("mbugai@test.ee", 0)),
        "must be greater than or equal to 1"
    );
    //score not from range > 10
    Assertions.assertThrows(RestClientException.class,
        () -> addReview(productId, new AddReviewDto("mbugai@test.ee", 11)),
        "must be greater than or equal to 1"
    );
    //malformed score
    Assertions.assertThrows(AssertionFailedError.class,
        () -> addReview(productId, "{\"userEmail\":\"mbugai@test.ee\",\"score\":aaa}"),
        "expected: <200 OK> but was: <400 BAD_REQUEST>"
    );

    for (int i = 0; i < 10; i++) {
      addReview(productId, i + 1);
    }
    ReviewStatDto response =
        restTemplate.getForObject(baseUrl, ReviewStatDto.class, productId);

    Assertions.assertEquals(productId, response.getProductId());
    Assertions.assertEquals(5.5, response.getAverageScore());
    Assertions.assertEquals(10, response.getNumberOfReviews());

    resetAllReview(productId);
  }

  @Test
  void getReviewStatByProductTest() {
    String productId = "M20324";

    ReviewStatDto response =
        restTemplate.getForObject(baseUrl, ReviewStatDto.class, productId);

    Assertions.assertEquals(productId, response.getProductId());
    Assertions.assertEquals(5.0, response.getAverageScore());
    Assertions.assertEquals(3, response.getNumberOfReviews());

    productId = UUID.randomUUID().toString();
    response =
        restTemplate.getForObject(baseUrl, ReviewStatDto.class, productId);

    Assertions.assertEquals(productId, response.getProductId());
    Assertions.assertEquals(0.0, response.getAverageScore());
    Assertions.assertEquals(0, response.getNumberOfReviews());
  }

  private AddReviewDto addReview(String productId, int index) {
    AddReviewDto dto = new AddReviewDto(String.format("mbugai%d@test.ee", index), index);
    return addReview(productId, dto);
  }

  private AddReviewDto addReview(String productId, AddReviewDto dto) {
    ResponseEntity<?> response =
        restTemplate.withBasicAuth(basicAuthUser, basicAuthPassword)
            .postForEntity(baseUrl, dto, AddReviewDto.class, productId);
    Assertions.assertEquals(OK, response.getStatusCode());
    return dto;
  }

  private AddReviewDto addReview(String productId, String dtoJson) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<String> entity = new HttpEntity<>(dtoJson, headers);

    ResponseEntity<?> response = restTemplate.withBasicAuth(basicAuthUser, basicAuthPassword)
        .postForEntity(baseUrl, entity, String.class, productId);
    Assertions.assertEquals(OK, response.getStatusCode());
    return null;
  }

  private void resetAllReview(String productId) {
    ResponseEntity<?> response =
        restTemplate.withBasicAuth(basicAuthUser, basicAuthPassword)
            .exchange(baseUrl, HttpMethod.DELETE, new HttpEntity<>(new HttpHeaders()), Object.class,
                productId);
    Assertions.assertEquals(OK, response.getStatusCode());
  }
}
