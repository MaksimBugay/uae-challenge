package bmv.coding.challenge.reviewservice.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

public class AddReviewDto {

  @NotBlank(message = "User Email is mandatory")
  @Email
  private String userEmail;

  @Min(1)
  @Max(10)
  private int score;

  public AddReviewDto() {
  }

  public AddReviewDto(String userEmail, int score) {
    this.userEmail = userEmail;
    this.score = score;
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
