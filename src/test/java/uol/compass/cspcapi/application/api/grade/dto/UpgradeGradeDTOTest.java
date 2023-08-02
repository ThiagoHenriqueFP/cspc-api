package uol.compass.cspcapi.application.api.grade.dto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class UpgradeGradeDTOTest {

    @Test
    public void testCalculateFinalGrade() {
        // Test the calculateFinalGrade method with some sample values
        Double communication = 8.0;
        Double collaboration = 9.0;
        Double autonomy = 7.5;
        Double quiz = 6.0;
        Double individualChallenge = 8.5;
        Double squadChallenge = 7.0;
        Double expectedFinalGrade = (communication + collaboration + autonomy + quiz + individualChallenge + squadChallenge) / 6;

        UpdateGradeDTO updateGradeDTO = new UpdateGradeDTO(communication, collaboration, autonomy, quiz, individualChallenge, squadChallenge);

        Double calculatedFinalGrade = updateGradeDTO.calculateFinalGrade(
                communication, collaboration, autonomy, quiz, individualChallenge, squadChallenge);

        // Assert that the calculated final grade matches the expected final grade
        Assertions.assertEquals(expectedFinalGrade, calculatedFinalGrade, 0.001);
    }

    @Test
    public void testGetters() {
        // Test the getter methods for each field in the class

        Double communication = 8.0;
        Double collaboration = 9.0;
        Double autonomy = 7.5;
        Double quiz = 6.0;
        Double individualChallenge = 8.5;
        Double squadChallenge = 7.0;
        Double finalGrade = 7.667;

        UpdateGradeDTO updateGradeDTO = new UpdateGradeDTO(communication, collaboration, autonomy, quiz, individualChallenge, squadChallenge);

        // Assert that the getter methods return the expected values
        Assertions.assertEquals(communication, updateGradeDTO.getCommunication());
        Assertions.assertEquals(collaboration, updateGradeDTO.getCollaboration());
        Assertions.assertEquals(autonomy, updateGradeDTO.getAutonomy());
        Assertions.assertEquals(quiz, updateGradeDTO.getQuiz());
        Assertions.assertEquals(individualChallenge, updateGradeDTO.getIndividualChallenge());
        Assertions.assertEquals(squadChallenge, updateGradeDTO.getSquadChallenge());
        Assertions.assertEquals(finalGrade, updateGradeDTO.getFinalGrade(), 0.001);
    }
}

