package uol.compass.cspcapi.domain.grade;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class GradeTest {

    private Grade grade;

    @BeforeEach
    public void setUp() {
        // Create a new Grade object before each test
        grade = new Grade(8.5, 7.0, 9.0, 6.5, 8.0, 7.5);
    }

    @Test
    public void testCalculateFinalGrade() {
        // Ensure that the final grade is calculated correctly
        Double expectedFinalGrade = (8.5 + 7.0 + 9.0 + 6.5 + 8.0 + 7.5) / 6;
        Double actualFinalGrade = grade.calculateFinalGrade(
                grade.getCommunication(),
                grade.getCollaboration(),
                grade.getAutonomy(),
                grade.getQuiz(),
                grade.getIndividualChallenge(),
                grade.getSquadChallenge()
        );

        assertEquals(expectedFinalGrade, actualFinalGrade, 0.001);
    }

    @Test
    public void testGettersAndSetters() {
        // Test the getters and setters for all fields
        Long id = 1L;
        Double communication = 9.0;
        Double collaboration = 8.0;
        Double autonomy = 7.5;
        Double quiz = 6.0;
        Double individualChallenge = 9.5;
        Double squadChallenge = 8.5;
        Double finalGrade = grade.calculateFinalGrade(communication, collaboration, autonomy, quiz, individualChallenge, squadChallenge);

        grade.setCommunication(communication);
        grade.setCollaboration(collaboration);
        grade.setAutonomy(autonomy);
        grade.setQuiz(quiz);
        grade.setIndividualChallenge(individualChallenge);
        grade.setSquadChallenge(squadChallenge);
        grade.setFinalGrade(finalGrade);
        grade.setId(id);

        assertEquals(id, grade.getId());
        assertEquals(communication, grade.getCommunication());
        assertEquals(collaboration, grade.getCollaboration());
        assertEquals(autonomy, grade.getAutonomy());
        assertEquals(quiz, grade.getQuiz());
        assertEquals(individualChallenge, grade.getIndividualChallenge());
        assertEquals(squadChallenge, grade.getSquadChallenge());

        assertEquals(finalGrade, grade.getFinalGrade());
    }
}
