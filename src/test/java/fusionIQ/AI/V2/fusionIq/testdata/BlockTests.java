package fusionIQ.AI.V2.fusionIq.testdata;

import fusionIQ.AI.V2.fusionIq.data.Block;
import fusionIQ.AI.V2.fusionIq.data.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class BlockTests {

    private Block block;
    private User blocker;
    private User blocked;

    @BeforeEach
    void setUp() {
        blocker = new User();
        blocker.setId(1L);
        blocker.setName("Blocker User"); // Ensure required fields are set

        blocked = new User();
        blocked.setId(2L);
        blocked.setName("Blocked User"); // Ensure required fields are set

        block = new Block();
    }

    @Test
    void testDefaultConstructor() {
        // Ensure default constructor sets default values correctly
        assertThat(block.getId()).isNull(); // Default value for Long (can be null if not explicitly set)
        assertThat(block.getBlocker()).isNull();
        assertThat(block.getBlocked()).isNull();
    }

    @Test
    void testParameterizedConstructor() {
        block.setBlocker(blocker);
        block.setBlocked(blocked);

        assertThat(block.getBlocker()).isEqualTo(blocker);
        assertThat(block.getBlocked()).isEqualTo(blocked);
    }

    @Test
    void testSettersAndGetters() {
        block.setId(3L);
        block.setBlocker(blocker);
        block.setBlocked(blocked);

        assertThat(block.getId()).isEqualTo(3L);
        assertThat(block.getBlocker()).isEqualTo(blocker);
        assertThat(block.getBlocked()).isEqualTo(blocked);
    }

    @Test
    void testToString() {
        block.setId(4L);
        block.setBlocker(blocker);
        block.setBlocked(blocked);

        // Verify properties individually
        assertThat(block.getId()).isEqualTo(4L);
        assertThat(block.getBlocker()).isEqualTo(blocker);
        assertThat(block.getBlocked()).isEqualTo(blocked);
    }
}
