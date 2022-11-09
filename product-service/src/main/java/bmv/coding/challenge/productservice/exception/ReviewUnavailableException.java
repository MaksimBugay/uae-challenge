package bmv.coding.challenge.productservice.exception;

public class ReviewUnavailableException extends RuntimeException{

  private final String productId;

  public ReviewUnavailableException(String message, String productId) {
    super(message);
    this.productId = productId;
  }

  public String getProductId() {
    return productId;
  }
}
