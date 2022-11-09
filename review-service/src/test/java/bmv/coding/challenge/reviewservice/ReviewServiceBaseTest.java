package bmv.coding.challenge.reviewservice;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import org.junit.jupiter.api.Assertions;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;

public class ReviewServiceBaseTest {

  static MySQLContainer<?> mysql;

  static {
    mysql = new MySQLContainer<>("mysql:8.0.12")
        .withDatabaseName("product_review")
        .withUsername("acc_pimp")
        .withPassword("acc_pimp")
        .withEnv("MYSQL_ROOT_HOST", "%");

    mysql.start();

    while (!testConnection()) {
      try {
        Thread.sleep(5000);
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      }
    }
  }

  @DynamicPropertySource
  static void databaseProperties(DynamicPropertyRegistry registry) {
    registry.add(
        "spring.datasource.url",
        () -> String.format(
            "jdbc:mysql://%s:%d/product_review?sslMode=DISABLED&allowPublicKeyRetrieval=true",
            mysql.getHost(), mysql.getFirstMappedPort()));
  }

  private static boolean testConnection() {
    try (Connection connection = mysql.createConnection("")) {
      Statement statement = connection.createStatement();
      statement.execute("SELECT 1;");
      try (ResultSet resultSet = statement.getResultSet()) {
        resultSet.next();
        int resultSetInt = resultSet.getInt(1);
        Assertions.assertEquals(1, resultSetInt, "A basic SELECT query succeeds");
        return true;
      }
    } catch (Exception ex) {
      return false;
    }
  }
}
