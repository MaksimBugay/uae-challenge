package bmv.coding.challenge.reviewservice.config;

import static org.slf4j.LoggerFactory.getLogger;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AutoConfigureBefore({LiquibaseAutoConfiguration.class})
public class LiquibaseSourceConfig {

  private static final Logger LOGGER = getLogger(LiquibaseSourceConfig.class);

  @Value("${review-service.liquibase.enabled:true}")
  private boolean liquibaseEnabled;

  @Bean
  public LiquibaseProperties properties() {
    LiquibaseProperties properties = new LiquibaseProperties();
    properties.setEnabled(liquibaseEnabled);
    return properties;
  }
}
