package fusionIQ.AI.V2.fusionIq.data;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;

class JobAdminTest {

    private JobAdmin jobAdmin;

    @BeforeEach
    void setUp() {
        jobAdmin = new JobAdmin();
        jobAdmin.setId(1L);
        jobAdmin.setJobAdminName("John Doe");
        jobAdmin.setJobAdminEmail("john.doe@example.com");
        jobAdmin.setRole("Admin");
        jobAdmin.setJobAdminCompanyName("FusionIQ");
        jobAdmin.setJobAdminPassword("password123");
        jobAdmin.setJobAdminConfirmPassword("password123");
        jobAdmin.setCompanyDescription("Leading tech company.");
        jobAdmin.setCompanyStrength(500);
        jobAdmin.setCompanyPhoneNumber("1234567890");
        jobAdmin.setCompanyLogo(new byte[]{1, 2, 3});
    }

    @Test
    void testJobAdminFields() {
        assertNotNull(jobAdmin.getId());
        assertEquals("John Doe", jobAdmin.getJobAdminName());
        assertEquals("john.doe@example.com", jobAdmin.getJobAdminEmail());
        assertEquals("Admin", jobAdmin.getRole());
        assertEquals("FusionIQ", jobAdmin.getJobAdminCompanyName());
        assertEquals("password123", jobAdmin.getJobAdminPassword());
        assertEquals("password123", jobAdmin.getJobAdminConfirmPassword());
    }

    @Test
    void testCompanyDetails() {
        assertEquals("Leading tech company.", jobAdmin.getCompanyDescription());
        assertEquals(500, jobAdmin.getCompanyStrength());
        assertEquals("1234567890", jobAdmin.getCompanyPhoneNumber());
        assertTrue(Arrays.equals(new byte[]{1, 2, 3}, jobAdmin.getCompanyLogo()));
    }

    @Test
    void testToString() {
        String jobAdminString = jobAdmin.toString();
        assertTrue(jobAdminString.contains("John Doe"));
        assertTrue(jobAdminString.contains("john.doe@example.com"));
        assertTrue(jobAdminString.contains("FusionIQ"));
    }

    @Test
    void testPasswordMatching() {
        assertEquals(jobAdmin.getJobAdminPassword(), jobAdmin.getJobAdminConfirmPassword(),
                "Password and confirm password should match.");
    }

    @Test
    void testSettersAndGetters() {
        jobAdmin.setJobAdminName("Jane Doe");
        jobAdmin.setJobAdminEmail("jane.doe@example.com");

        assertEquals("Jane Doe", jobAdmin.getJobAdminName());
        assertEquals("jane.doe@example.com", jobAdmin.getJobAdminEmail());
    }

    @Test
    void testDefaultConstructor() {
        JobAdmin defaultJobAdmin = new JobAdmin();
        assertNull(defaultJobAdmin.getId());
        assertNull(defaultJobAdmin.getJobAdminName());
        assertNull(defaultJobAdmin.getJobAdminEmail());
    }
}