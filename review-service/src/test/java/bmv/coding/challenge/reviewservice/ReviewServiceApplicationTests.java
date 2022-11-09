package bmv.coding.challenge.reviewservice;

import bmv.coding.challenge.reviewservice.dto.AddReviewDto;
import bmv.coding.challenge.reviewservice.dto.ReviewStatDto;
import bmv.coding.challenge.reviewservice.entity.ReviewStat;
import bmv.coding.challenge.reviewservice.service.ReviewService;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ReviewServiceApplicationTests extends ReviewServiceBaseTest{

  @Autowired
  ReviewService reviewService;

  private final String productId = "testProduct" + UUID.randomUUID();

  @Test
  void contextLoads() {
  }

  @AfterEach
  void reset(){
    reviewService.resetReview(productId);
  }

  @Test
  void insertOrUpdateTest() {
    String userEmail = UUID.randomUUID() + "@test.com";
    AddReviewDto newItem = new AddReviewDto();
    newItem.setUserEmail(userEmail);
    newItem.setScore(6);
    reviewService.addReview(productId, newItem);

    ReviewStatDto reviewStat = reviewService.getReviewStatByProduct(productId);
    Assertions.assertEquals(6, reviewStat.getAverageScore());
    Assertions.assertEquals(1, reviewStat.getNumberOfReviews());

    newItem = new AddReviewDto();
    newItem.setUserEmail(userEmail);
    newItem.setScore(8);
    reviewService.addReview(productId, newItem);
    reviewStat = reviewService.getReviewStatByProduct(productId);
    Assertions.assertEquals(8, reviewStat.getAverageScore());
    Assertions.assertEquals(1, reviewStat.getNumberOfReviews());
  }

  @Test
  void getReviewStatAndResetTest() {
    for (int i = 0; i < 100; i++) {
      AddReviewDto newItem = new AddReviewDto();
      newItem.setUserEmail(UUID.randomUUID() + "@test.com");
      newItem.setScore(i % 10 + 1);
      reviewService.addReview(productId, newItem);
    }
    ReviewStatDto reviewStat = reviewService.getReviewStatByProduct(productId);
    Assertions.assertEquals(5.5, reviewStat.getAverageScore());
    Assertions.assertEquals(100, reviewStat.getNumberOfReviews());

    reviewService.resetReview(productId);
    reviewStat = reviewService.getReviewStatByProduct(productId);
    Assertions.assertEquals(0, reviewStat.getAverageScore());
    Assertions.assertEquals(0, reviewStat.getNumberOfReviews());
  }
}
