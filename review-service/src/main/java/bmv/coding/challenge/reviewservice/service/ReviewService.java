package bmv.coding.challenge.reviewservice.service;

import static bmv.coding.challenge.reviewservice.config.CacheConfig.REVIEW_STAT_BY_PRODUCT_ID;

import bmv.coding.challenge.reviewservice.dao.ReviewRepository;
import bmv.coding.challenge.reviewservice.dto.AddReviewDto;
import bmv.coding.challenge.reviewservice.dto.ReviewStatDto;
import bmv.coding.challenge.reviewservice.entity.Review;
import bmv.coding.challenge.reviewservice.entity.ReviewStat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ReviewService {

  private final ReviewRepository reviewRepository;

  @Autowired
  public ReviewService(ReviewRepository reviewRepository) {
    this.reviewRepository = reviewRepository;
  }

  @Caching(
      evict = {
          @CacheEvict(value = REVIEW_STAT_BY_PRODUCT_ID, key = "#productId")
      })
  public void addReview(String productId, AddReviewDto addReviewDto) {
    Review review = new Review();
    review.setProductId(productId);
    review.setUserEmail(addReviewDto.getUserEmail());
    review.setScore(addReviewDto.getScore());
    reviewRepository.addReview(review);
  }

  @Caching(
      evict = {
          @CacheEvict(value = REVIEW_STAT_BY_PRODUCT_ID, key = "#productId")
      })
  public void resetReview(String productId) {
    reviewRepository.resetReview(productId);
  }

  @Transactional(readOnly = true)
  @Cacheable(value = REVIEW_STAT_BY_PRODUCT_ID)
  public ReviewStatDto getReviewStatByProduct(String productId) {
    ReviewStat reviewStat = reviewRepository.getReviewStatByProduct(productId);
    if (reviewStat == null) {
      reviewStat = new ReviewStat(productId, 0.0, 0);
    }
    return new ReviewStatDto(reviewStat.getProductId(), reviewStat.getAverageScore(),
        reviewStat.getNumberOfReviews());
  }
}
