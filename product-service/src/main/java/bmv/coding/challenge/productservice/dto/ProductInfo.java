package bmv.coding.challenge.productservice.dto;

public class ProductInfo {

  private String productId;

  private String message;

  private String location;

  private Double averageScore;

  private Integer numberOfReviews;

  public ProductInfo() {
  }

  public ProductInfo(String productId, String message, String location, Double averageScore,
      Integer numberOfReviews) {
    this.productId = productId;
    this.message = message;
    this.location = location;
    this.averageScore = averageScore;
    this.numberOfReviews = numberOfReviews;
  }

  public String getProductId() {
    return productId;
  }

  public void setProductId(String productId) {
    this.productId = productId;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
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
