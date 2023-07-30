package uol.compass.cspcapi.application.api.grade.dto;

import java.text.DecimalFormat;

public class UpdateGradeDTO {
    private Double communication;
    private Double collaboration;
    private Double autonomy;
    private Double quiz;
    private Double individualChallenge;
    private Double squadChallenge;
    private Double finalGrade;

    public UpdateGradeDTO(Double communication, Double collaboration, Double autonomy, Double quiz, Double individualChallenge, Double squadChallenge, Double finalGrade) {
        this.communication = communication;
        this.collaboration = collaboration;
        this.autonomy = autonomy;
        this.quiz = quiz;
        this.individualChallenge = individualChallenge;
        this.squadChallenge = squadChallenge;

        this.finalGrade = calculateFinalGrade(communication, collaboration, autonomy, quiz, individualChallenge, squadChallenge);
    }

    public Double calculateFinalGrade(Double communication, Double collaboration, Double autonomy, Double quiz, Double individualChallenge, Double squadChallenge) {
        Double result = ((communication * 1) + (collaboration * 1) + (autonomy * 1) + (quiz * 1) + (individualChallenge * 1) + (squadChallenge * 1)) / 6;
        return result;
    }

    public Double getCommunication() {
        return communication;
    }

    public Double getCollaboration() {
        return collaboration;
    }

    public Double getAutonomy() {
        return autonomy;
    }

    public Double getQuiz() {
        return quiz;
    }

    public Double getIndividualChallenge() {
        return individualChallenge;
    }

    public Double getSquadChallenge() {
        return squadChallenge;
    }

    public Double getFinalGrade() {
        return finalGrade;
    }
}
