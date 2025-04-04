package fusionIQ.AI.V2.fusionIq.testdata;


import fusionIQ.AI.V2.fusionIq.data.GroupMember;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class GroupMemberTests {

    @Test
    void testGroupMemberInitialization() {
        // Create a GroupMember instance
        GroupMember member = new GroupMember(1L, "John Doe", "john.doe@example.com", true);

        // Assert that the member was initialized correctly
        assertThat(member.getId()).isEqualTo(1L);
        assertThat(member.getName()).isEqualTo("John Doe");
        assertThat(member.getEmail()).isEqualTo("john.doe@example.com");
        assertThat(member.getIsAdmin()).isTrue();
    }

    @Test
    void testGroupMemberSetters() {
        // Create a GroupMember instance
        GroupMember member = new GroupMember(1L, "Jane Doe", "jane.doe@example.com", false);

        // Use setters to change values
        member.setName("Jane Smith");
        member.setEmail("jane.smith@example.com");
        member.setIsAdmin(true);

        // Assert that the values were updated correctly
        assertThat(member.getName()).isEqualTo("Jane Smith");
        assertThat(member.getEmail()).isEqualTo("jane.smith@example.com");
        assertThat(member.getIsAdmin()).isTrue();
    }

    @Test
    void testGroupMemberEmailValidation() {
        // Create a GroupMember instance
        GroupMember member = new GroupMember(1L, "John Doe", "john.doe@example.com", true);

        // Test valid email
        assertThat(member.getEmail()).isEqualTo("john.doe@example.com");

        // Update with an invalid email and check if it allows to set (this would depend on your implementation)
        member.setEmail("invalid-email");
        assertThat(member.getEmail()).isEqualTo("invalid-email"); // Assuming no validation in setter
    }

    @Test
    void testGroupMemberAdminStatus() {
        // Create a GroupMember instance
        GroupMember member = new GroupMember(2L, "Alice Smith", "alice.smith@example.com", false);

        // Assert initial admin status
        assertThat(member.getIsAdmin()).isFalse();

        // Change admin status
        member.setIsAdmin(true);

        // Assert updated admin status
        assertThat(member.getIsAdmin()).isTrue();
    }

    @Test
    void testGroupMemberConstructorWithNullValues() {
        // Create a GroupMember instance with null values
        GroupMember member = new GroupMember(3L, null, null, null);

        // Assert that member initializes correctly with nulls
        assertThat(member.getId()).isEqualTo(3L);
        assertThat(member.getName()).isNull();
        assertThat(member.getEmail()).isNull();
        assertThat(member.getIsAdmin()).isNull();
    }

    @Test
    void testGroupMemberIdSetter() {
        // Create a GroupMember instance
        GroupMember member = new GroupMember(1L, "Bob Brown", "bob.brown@example.com", true);

        // Change the ID
        member.setId(2L);

        // Assert that the ID was updated correctly
        assertThat(member.getId()).isEqualTo(2L);
    }

    @Test
    void testGroupMemberNameSetterWithEmptyString() {
        // Create a GroupMember instance
        GroupMember member = new GroupMember(1L, "Charlie Green", "charlie.green@example.com", false);

        // Change the name to an empty string
        member.setName("");

        // Assert that the name was updated to empty string
        assertThat(member.getName()).isEqualTo("");
    }
}

