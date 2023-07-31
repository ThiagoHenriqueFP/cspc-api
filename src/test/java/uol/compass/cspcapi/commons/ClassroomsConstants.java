package uol.compass.cspcapi.commons;

import uol.compass.cspcapi.domain.classroom.Classroom;
import uol.compass.cspcapi.domain.coordinator.Coordinator;
import uol.compass.cspcapi.domain.user.User;

public class ClassroomsConstants {
    public static final Coordinator VALID_COORDINATOR = new Coordinator(new User("Julio", "Bragança", "juliobraganca@mail.com", "juliobraganca"));
    public static final Classroom VALID_CLASSROOM = new Classroom("Spring Boot AWS Junho", VALID_COORDINATOR);
}
