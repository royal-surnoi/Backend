package fusionIQ.AI.V2.fusionIq.service;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class CacheConfig {

    @Bean
    public CacheManager cacheManager() {
        // Add "userDetailsCache" to the cache manager's list of cache names
        CaffeineCacheManager cacheManager = new CaffeineCacheManager("feedRecommendations", "feedShortVideos", "userDetailsCache","shortVideoDetailsCache","articleDetailsCache","imageDetailsCache","videoDetailsCache","messagesCache","conversationsCache");
        cacheManager.setCaffeine(Caffeine.newBuilder()
                .expireAfterWrite(5, TimeUnit.MINUTES) // Cache expiration time
                .maximumSize(100)); // Cache size limit
        return cacheManager;
    }
}
