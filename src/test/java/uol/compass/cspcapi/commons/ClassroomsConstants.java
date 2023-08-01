package uol.compass.cspcapi.commons;

import uol.compass.cspcapi.application.api.classroom.dto.ResponseClassroomDTO;
import uol.compass.cspcapi.application.api.coordinator.dto.ResponseCoordinatorDTO;
import uol.compass.cspcapi.application.api.instructor.dto.ResponseInstructorDTO;
import uol.compass.cspcapi.application.api.scrumMaster.dto.ResponseScrumMasterDTO;
import uol.compass.cspcapi.application.api.squad.dto.ResponseSquadDTO;
import uol.compass.cspcapi.application.api.student.dto.ResponseStudentDTO;
import uol.compass.cspcapi.application.api.user.dto.ResponseUserDTO;
import uol.compass.cspcapi.domain.classroom.Classroom;
import uol.compass.cspcapi.domain.coordinator.Coordinator;
import uol.compass.cspcapi.domain.user.User;

import java.util.ArrayList;

public class ClassroomsConstants {
    public static final Coordinator VALID_COORDINATOR = new Coordinator(new User("Julio", "Bragança", "juliobraganca@mail.com", "juliobraganca"));
    public static final Classroom VALID_CLASSROOM = new Classroom("Spring Boot AWS Junho", VALID_COORDINATOR);


    public static final ResponseUserDTO RESPONSE_USER = new ResponseUserDTO(1L, "Gilson", "Ribeiro", "gilson.ribeiro@mail.com");
    public static final ResponseCoordinatorDTO RESPONSE_COORDINATOR = new ResponseCoordinatorDTO(1L, RESPONSE_USER);
    public static final ResponseClassroomDTO RESPONSE_CLASSROOM = new ResponseClassroomDTO(
            1L,
            "Classroom Spring",
            RESPONSE_COORDINATOR,
            new ArrayList<>(),
            new ArrayList<>(),
            new ArrayList<>(),
            new ArrayList<>()
    );

    public static final ResponseUserDTO RESPONSE_USER2 = new ResponseUserDTO(2L, "Gabriela", "Monatana", "gabriela.montana@mail.com");
    public static final ResponseCoordinatorDTO RESPONSE_COORDINATOR2 = new ResponseCoordinatorDTO(2L, RESPONSE_USER2);
    public static final ResponseClassroomDTO RESPONSE_CLASSROOM2 = new ResponseClassroomDTO(
            2L,
            "Classroom Spring React",
            RESPONSE_COORDINATOR2,
            new ArrayList<>(),
            new ArrayList<>(),
            new ArrayList<>(),
            new ArrayList<>()
    );

    public static final ResponseUserDTO RESPONSE_USER_3 = new ResponseUserDTO(3L, "User", "Third", "user.third@mail.com");
    public static final ResponseStudentDTO RESPONSE_STUDENT_1 = new ResponseStudentDTO(1L, RESPONSE_USER_3);

    public static final ResponseUserDTO RESPONSE_USER_4 = new ResponseUserDTO(4L, "User", "Fourth", "user.fourth@mail.com");
    public static final ResponseStudentDTO RESPONSE_STUDENT_2 = new ResponseStudentDTO(2L, RESPONSE_USER_4);

    public static final ResponseUserDTO RESPONSE_USER_6 = new ResponseUserDTO(6L, "User", "Student", "user.student@mail.com");
    public static final ResponseStudentDTO RESPONSE_STUDENT_3 = new ResponseStudentDTO(3L, RESPONSE_USER_6);

    public static final ResponseUserDTO RESPONSE_USER_5 = new ResponseUserDTO(5L, "User", "Surname", "user.surname@mail.com");
    public static final ResponseCoordinatorDTO RESPONSE_COORDINATOR_3 = new ResponseCoordinatorDTO(3L, RESPONSE_USER_5);

    public static final ResponseUserDTO RESPONSE_USER_7 = new ResponseUserDTO(7L, "Scrum", "Master", "scrum.master1@mail.com");
    public static final ResponseScrumMasterDTO RESPONSE_SCRUMMASTER_1 = new ResponseScrumMasterDTO(1L, RESPONSE_USER_7);

    public static final ResponseUserDTO RESPONSE_USER_8 = new ResponseUserDTO(8L, "Scrum", "Master", "scrum.master2@mail.com");
    public static final ResponseScrumMasterDTO RESPONSE_SCRUMMASTER_2 = new ResponseScrumMasterDTO(2L, RESPONSE_USER_8);

    public static final ResponseUserDTO RESPONSE_USER_9 = new ResponseUserDTO(9L, "Scrum", "Master", "scrum.master3@mail.com");
    public static final ResponseScrumMasterDTO RESPONSE_SCRUMMASTER_3 = new ResponseScrumMasterDTO(3L, RESPONSE_USER_9);

    public static final ResponseUserDTO RESPONSE_USER_10 = new ResponseUserDTO(10L, "Instructor", "Employee", "instructor.employee1@mail.com");
    public static final ResponseInstructorDTO RESPONSE_INSTRUCTOR_1 = new ResponseInstructorDTO(1L, RESPONSE_USER_10);

    public static final ResponseUserDTO RESPONSE_USER_11 = new ResponseUserDTO(11L, "Instructor", "Employee", "instructor.employee2@mail.com");
    public static final ResponseInstructorDTO RESPONSE_INSTRUCTOR_2 = new ResponseInstructorDTO(2L, RESPONSE_USER_11);

    public static final ResponseUserDTO RESPONSE_USER_12 = new ResponseUserDTO(12L, "Instructor", "Employee", "instructor.employee3@mail.com");
    public static final ResponseInstructorDTO RESPONSE_INSTRUCTOR_3 = new ResponseInstructorDTO(3L, RESPONSE_USER_12);

    public static final ResponseSquadDTO RESPONSE_SQUAD_1 = new ResponseSquadDTO(1L, "Springforce");
    public static final ResponseSquadDTO RESPONSE_SQUAD_2 = new ResponseSquadDTO(2L, "Modern Bugs");
    public static final ResponseSquadDTO RESPONSE_SQUAD_3 = new ResponseSquadDTO(3L, "Cyberchase");
}
