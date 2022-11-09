package bmv.coding.challenge.productservice.exception;

public class ProductUnavailableException extends RuntimeException{

  private final String productId;

  public ProductUnavailableException(String message, String productId) {
    super(message);
    this.productId = productId;
  }

  public String getProductId() {
    return productId;
  }
}
