package bmv.coding.challenge.reviewservice.entity;

public class ReviewStat {

  private String productId;

  private Double averageScore;

  private Integer numberOfReviews;

  public ReviewStat() {
  }

  public ReviewStat(String productId, Double averageScore, Integer numberOfReviews) {
    this.productId = productId;
    this.averageScore = averageScore;
    this.numberOfReviews = numberOfReviews;
  }

  public String getProductId() {
    return productId;
  }

  public void setProductId(String productId) {
    this.productId = productId;
  }

  public Double getAverageScore() {
    return averageScore;
  }

  public void setAverageScore(Double averageScore) {
    this.averageScore = averageScore;
  }

  public Integer getNumberOfReviews() {
    return numberOfReviews;
  }

  public void setNumberOfReviews(Integer numberOfReviews) {
    this.numberOfReviews = numberOfReviews;
  }
}
