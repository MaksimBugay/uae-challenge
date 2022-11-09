package bmv.coding.challenge.reviewservice.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import java.util.concurrent.TimeUnit;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CacheConfig {

  public static final String REVIEW_STAT_BY_PRODUCT_ID = "review-stat-by-product-id";

  @Bean
  public Caffeine<Object, Object> caffeineConfig() {
    return Caffeine.newBuilder().expireAfterWrite(1, TimeUnit.DAYS);
  }

  @Bean
  public CacheManager cacheManager(Caffeine<Object, Object> caffeine) {
    CaffeineCacheManager caffeineCacheManager = new CaffeineCacheManager();
    caffeineCacheManager.setCaffeine(caffeine);
    return caffeineCacheManager;
  }
}
