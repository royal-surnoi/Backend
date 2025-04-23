package fusionIQ.AI.V2.fusionIq;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.TimeZone;

@SpringBootApplication
@EnableCaching
public class

FusionIqApplication {

	@Value("${aws.accessKey}")
	private String accessKey;

	@Value("${aws.secretKey}")
	private String secretKey;

	@PostConstruct
	public void init() {
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Kolkata"));
	}

	public static void main(String[] args) {
		SpringApplication.run(FusionIqApplication.class, args);
	}

	@Bean
	public AmazonS3 amazonS3() {
		BasicAWSCredentials awsCreds = new BasicAWSCredentials(accessKey, secretKey);
		return AmazonS3ClientBuilder.standard()
				.withRegion(Regions.AP_SOUTH_1)
				.withCredentials(new AWSStaticCredentialsProvider(awsCreds))
				.build();
	}

	@Bean
	public WebMvcConfigurer mvcConfigurer() {
		return new WebMvcConfigurer() {
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**")
						.allowedOrigins("http://fusioniq-v3-fe.s3-website-us-east-1.amazonaws.com", "http://localhost:4200")
						.allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH")
						.allowedHeaders("*");
			}
		};
	}
}

