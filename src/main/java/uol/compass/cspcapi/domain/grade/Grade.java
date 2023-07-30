package uol.compass.cspcapi.domain.grade;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import uol.compass.cspcapi.domain.student.Student;

import java.text.DecimalFormat;


@Entity
@Table(name = "grades")
public class Grade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Double communication;
    private Double collaboration;
    private Double autonomy;
    private Double quiz;
    private Double individualChallenge;
    private Double squadChallenge;
    private Double finalGrade;

    public Grade() {
    }

    public Grade(Double communication, Double collaboration, Double autonomy, Double quiz, Double individualChallenge, Double squadChallenge) {
        this.communication = communication;
        this.collaboration = collaboration;
        this.autonomy = autonomy;
        this.quiz = quiz;
        this.individualChallenge = individualChallenge;
        this.squadChallenge = squadChallenge;
        this.finalGrade = calculateFinalGrade(communication, collaboration, autonomy, quiz, individualChallenge, squadChallenge);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getCommunication() {
        return communication;
    }

    public void setCommunication(Double communication) {
        this.communication = communication;
    }

    public Double getCollaboration() {
        return collaboration;
    }

    public void setCollaboration(Double collaboration) {
        this.collaboration = collaboration;
    }

    public Double getAutonomy() {
        return autonomy;
    }

    public void setAutonomy(Double autonomy) {
        this.autonomy = autonomy;
    }

    public Double getQuiz() {
        return quiz;
    }

    public void setQuiz(Double quiz) {
        this.quiz = quiz;
    }

    public Double getIndividualChallenge() {
        return individualChallenge;
    }

    public void setIndividualChallenge(Double individualChallenge) {
        this.individualChallenge = individualChallenge;
    }

    public Double getSquadChallenge() {
        return squadChallenge;
    }

    public void setSquadChallenge(Double squadChallenge) {
        this.squadChallenge = squadChallenge;
    }

    public Double getFinalGrade() {
        return finalGrade;
    }

    public void setFinalGrade(Double finalGrade) {
        this.finalGrade = finalGrade;
    }

    public Double calculateFinalGrade(Double communication, Double collaboration, Double autonomy, Double quiz, Double individualChallenge, Double squadChallenge) {
        Double result = ((communication * 1) + (collaboration * 1) + (autonomy * 1) + (quiz * 1) + (individualChallenge * 1) + (squadChallenge * 1)) / 6;
        return result;
    }
}
