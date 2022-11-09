package bmv.coding.challenge.reviewservice.entity;

import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"productId", "userEmail"})})
@NamedNativeQuery(
    name = "add_review_for_product",
    query = "INSERT INTO review(product_id, user_email, score) "
        + " VALUES (:productId, :userEmail, :score) ON DUPLICATE KEY UPDATE score=:score "
)
@NamedNativeQuery(
    name = "reset_review_for_product",
    query = "DELETE FROM review where product_id = :productId "
)
@NamedNativeQuery(
    name = "find_review_stat_dto",
    query =
        "SELECT " +
            "  product_id AS productId, " +
            "  AVG(score) AS averageScore, " +
            "  COUNT(id) as numberOfReviews " +
            "FROM review " +
            "where product_id = :productId " +
            "GROUP BY product_id",
    resultSetMapping = "review_stat_dto"
)
@SqlResultSetMapping(
    name = "review_stat_dto",
    classes = @ConstructorResult(
        targetClass = ReviewStat.class,
        columns = {
            @ColumnResult(name = "productId", type = String.class),
            @ColumnResult(name = "averageScore", type = Double.class),
            @ColumnResult(name = "numberOfReviews", type = Integer.class)
        }
    )
)
public class Review {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer id;

  @NotBlank(message = "Product id is mandatory")
  private String productId;

  @NotBlank(message = "User Email is mandatory")
  @Email
  private String userEmail;

  @Min(1)
  @Max(10)
  private int score;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getProductId() {
    return productId;
  }

  public void setProductId(String productId) {
    this.productId = productId;
  }

  public String getUserEmail() {
    return userEmail;
  }

  public void setUserEmail(String userEmail) {
    this.userEmail = userEmail;
  }

  public int getScore() {
    return score;
  }

  public void setScore(int score) {
    this.score = score;
  }
}
