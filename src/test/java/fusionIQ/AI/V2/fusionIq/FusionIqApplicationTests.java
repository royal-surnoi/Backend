package fusionIQ.AI.V2.fusionIq;

import fusionIQ.AI.V2.fusionIq.data.Course;
import fusionIQ.AI.V2.fusionIq.data.User;
import fusionIQ.AI.V2.fusionIq.service.CourseService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
class FusionIqApplicationTests {

	@Autowired
	private CourseService courseService; // Assuming you have this service

	@Test
	void contextLoads() {
		assertThat(courseService).isNotNull();
	}
}
