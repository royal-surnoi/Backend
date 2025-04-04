package fusionIQ.AI.V2.fusionIq.testdata;

import fusionIQ.AI.V2.fusionIq.data.MockTestInterview;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class MockTestInterviewTests {

    private MockTestInterview mockTestInterview;

    @BeforeEach
    void setUp() {
        mockTestInterview = new MockTestInterview();
        mockTestInterview.setId(1L);
        mockTestInterview.setTitle("Sample Mock Test");
        mockTestInterview.setDescription("A description of the mock test.");
        mockTestInterview.setTestType(MockTestInterview.TestType.ASSIGNMENT);
        mockTestInterview.setFee(new BigDecimal("20.00"));
        mockTestInterview.setFreeAttempts(3);
    }

    @Test
    void testMockTestInterviewCreation() {
        assertThat(mockTestInterview).isNotNull();
        assertThat(mockTestInterview.getId()).isEqualTo(1L);
        assertThat(mockTestInterview.getTitle()).isEqualTo("Sample Mock Test");
        assertThat(mockTestInterview.getDescription()).isEqualTo("A description of the mock test.");
        assertThat(mockTestInterview.getTestType()).isEqualTo(MockTestInterview.TestType.ASSIGNMENT);
        assertThat(mockTestInterview.getFee()).isEqualTo(new BigDecimal("20.00"));
        assertThat(mockTestInterview.getFreeAttempts()).isEqualTo(3);
    }

    @Test
    void testSettersAndGetters() {
        mockTestInterview.setTitle("Updated Title");
        assertThat(mockTestInterview.getTitle()).isEqualTo("Updated Title");

        mockTestInterview.setDescription("Updated Description");
        assertThat(mockTestInterview.getDescription()).isEqualTo("Updated Description");

        mockTestInterview.setTestType(MockTestInterview.TestType.ASSIGNMENT);
        assertThat(mockTestInterview.getTestType()).isEqualTo(MockTestInterview.TestType.ASSIGNMENT);

        mockTestInterview.setFee(new BigDecimal("30.00"));
        assertThat(mockTestInterview.getFee()).isEqualTo(new BigDecimal("30.00"));

        mockTestInterview.setFreeAttempts(5);
        assertThat(mockTestInterview.getFreeAttempts()).isEqualTo(5);

        LocalDateTime now = LocalDateTime.now();
        mockTestInterview.setCreatedAt(now);
        assertThat(mockTestInterview.getCreatedAt()).isEqualTo(now);

        mockTestInterview.setUpdatedAt(now);
        assertThat(mockTestInterview.getUpdatedAt()).isEqualTo(now);

        byte[] image = new byte[]{1, 2, 3};
        mockTestInterview.setImage(image);
        assertThat(mockTestInterview.getImage()).isEqualTo(image);
    }

    @Test
    void testDefaultValues() {
        MockTestInterview defaultInterview = new MockTestInterview();
        assertThat(defaultInterview.getFee()).isEqualTo(BigDecimal.ZERO);
        assertThat(defaultInterview.getFreeAttempts()).isEqualTo(2);
    }

    @Test
    void testPrePersistAndPreUpdate() throws Exception {
        LocalDateTime now = LocalDateTime.now();

        // Use reflection to invoke the protected onCreate method
        Method onCreate = MockTestInterview.class.getDeclaredMethod("onCreate");
        onCreate.setAccessible(true);
        onCreate.invoke(mockTestInterview);

        assertThat(mockTestInterview.getCreatedAt()).isAfterOrEqualTo(now);
        assertThat(mockTestInterview.getUpdatedAt()).isNull();

        // Use reflection to invoke the protected onUpdate method
        Method onUpdate = MockTestInterview.class.getDeclaredMethod("onUpdate");
        onUpdate.setAccessible(true);
        onUpdate.invoke(mockTestInterview);

        assertThat(mockTestInterview.getUpdatedAt()).isAfterOrEqualTo(now);
    }
}
