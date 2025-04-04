package fusionIQ.AI.V2.fusionIq.testdata;



import fusionIQ.AI.V2.fusionIq.data.Criteria;
import fusionIQ.AI.V2.fusionIq.data.Grade;
import fusionIQ.AI.V2.fusionIq.data.Project;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class GradeTest {

    @Test
    void testDefaultConstructor() {
        Grade grade = new Grade();
        assertThat(grade).isNotNull();
        assertThat(grade.getId()).isNull();
        assertThat(grade.getPoints()).isEqualTo(0); // Default points can be checked if set
        assertThat(grade.getProject()).isNull();
        assertThat(grade.getCriteria()).isNull();
    }

    @Test
    void testParameterizedConstructor() {
        Project project = new Project(); // Replace with actual initialization if necessary
        Criteria criteria = new Criteria(); // Replace with actual initialization if necessary

        Grade grade = new Grade(1L, 85, project, criteria);

        assertThat(grade).isNotNull();
        assertThat(grade.getId()).isEqualTo(1L);
        assertThat(grade.getPoints()).isEqualTo(85);
        assertThat(grade.getProject()).isEqualTo(project);
        assertThat(grade.getCriteria()).isEqualTo(criteria);
    }

    @Test
    void testSettersAndGetters() {
        Grade grade = new Grade();
        Project project = new Project(); // Replace with actual initialization if necessary
        Criteria criteria = new Criteria(); // Replace with actual initialization if necessary

        grade.setId(2L);
        grade.setPoints(90);
        grade.setProject(project);
        grade.setCriteria(criteria);

        assertThat(grade.getId()).isEqualTo(2L);
        assertThat(grade.getPoints()).isEqualTo(90);
        assertThat(grade.getProject()).isEqualTo(project);
        assertThat(grade.getCriteria()).isEqualTo(criteria);
    }

    @Test
    void testToString() {
        Project project = new Project(); // Replace with actual initialization if necessary
        Criteria criteria = new Criteria(); // Replace with actual initialization if necessary
        Grade grade = new Grade(1L, 75, project, criteria);

        String expectedString = "Grade{" +
                "id=1" +
                ", points=75" +
                ", project=" + project +
                ", criteria=" + criteria +
                '}';
        assertThat(grade.toString()).isEqualTo(expectedString);
    }
}

