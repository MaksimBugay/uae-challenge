package bmv.coding.challenge.reviewservice.dao;

import bmv.coding.challenge.reviewservice.entity.Review;
import bmv.coding.challenge.reviewservice.entity.ReviewStat;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@DependsOn("liquibase")
public interface ReviewRepository extends JpaRepository<Review, Integer> {

  @Modifying
  @Query(name = "add_review_for_product", nativeQuery = true)
  void addReview(@Param("productId") String productId,
      @Param("userEmail") String userEmail, @Param("score") Integer score);

  default void addReview(Review review) {
    addReview(review.getProductId(), review.getUserEmail(), review.getScore());
  }

  @Modifying
  @Query(name = "reset_review_for_product", nativeQuery = true)
  void resetReview(@Param("productId") String productId);

  @Query(name = "find_review_stat_dto", nativeQuery = true)
  ReviewStat getReviewStatByProduct(@Param("productId") String productId);
}
