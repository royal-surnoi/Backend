package fusionIQ.AI.V2.fusionIq.testdata;

import fusionIQ.AI.V2.fusionIq.data.GroupMessages;
import fusionIQ.AI.V2.fusionIq.data.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;

class GroupMessagesTests {

    private User admin;
    private User member1;
    private User member2;
    private GroupMessages groupMessages;

    @BeforeEach
    void setUp() {
        // Set up a sample admin and members
        admin = new User();
        admin.setId(1L);
        admin.setName("Admin User");

        member1 = new User();
        member1.setId(2L);
        member1.setName("Member User 1");

        member2 = new User();
        member2.setId(3L);
        member2.setName("Member User 2");

        // Create a new GroupMessages instance
        groupMessages = new GroupMessages();
        groupMessages.setId(1L);
        groupMessages.setName("Test Group");
        groupMessages.setAdmin(admin);
        groupMessages.setMembers(new HashSet<>());
    }

    @Test
    void testGroupMessagesCreation() {
        // Assert that the group is created with correct attributes
        assertThat(groupMessages).isNotNull();
        assertThat(groupMessages.getId()).isEqualTo(1L);
        assertThat(groupMessages.getName()).isEqualTo("Test Group");
        assertThat(groupMessages.getAdmin()).isEqualTo(admin);
        assertThat(groupMessages.getMembers()).isEmpty();
    }

    @Test
    void testAddMember() {
        // Add a member to the group
        groupMessages.getMembers().add(member1);

        // Assert that the member was added
        assertThat(groupMessages.getMembers()).contains(member1);
        assertThat(groupMessages.getMembers()).doesNotContain(member2);
    }

    @Test
    void testRemoveMember() {
        // Add member1 to the group
        groupMessages.getMembers().add(member1);

        // Remove member1 from the group
        groupMessages.getMembers().remove(member1);

        // Assert that the member was removed
        assertThat(groupMessages.getMembers()).doesNotContain(member1);
    }

    @Test
    void testUpdateGroupName() {
        // Update the group's name
        groupMessages.setName("Updated Group Name");

        // Assert that the name was updated
        assertThat(groupMessages.getName()).isEqualTo("Updated Group Name");
    }

    @Test
    void testGroupToString() {
        // Assert that toString method returns expected format
        String expectedString = "GroupMessages{id=1, name='Test Group', admin=" + admin + ", members=[]}";
        assertThat(groupMessages.toString()).isEqualTo(expectedString);
    }

    @Test
    void testSetMembers() {
        // Set members of the group
        HashSet<User> newMembers = new HashSet<>();
        newMembers.add(member1);
        newMembers.add(member2);
        groupMessages.setMembers(newMembers);

        // Assert that the members were set correctly
        assertThat(groupMessages.getMembers()).containsExactlyInAnyOrder(member1, member2);
    }
}
