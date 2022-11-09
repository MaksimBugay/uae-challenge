package bmv.coding.challenge.reviewservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;


@SpringBootApplication
@EnableWebSecurity
@EnableCaching
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ReviewServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(ReviewServiceApplication.class, args);
  }

}
