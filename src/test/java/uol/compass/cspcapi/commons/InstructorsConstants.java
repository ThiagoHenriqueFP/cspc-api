package uol.compass.cspcapi.commons;

import uol.compass.cspcapi.application.api.instructor.dto.ResponseInstructorDTO;
import uol.compass.cspcapi.application.api.user.dto.ResponseUserDTO;

public class InstructorsConstants {
    public static final ResponseUserDTO RESPONSE_USER_1 = new ResponseUserDTO(1L, "Instructor", "Employee", "instructor.employee1@mail.com");
    public static final ResponseInstructorDTO RESPONSE_INSTRUCTOR_1 = new ResponseInstructorDTO(1L, RESPONSE_USER_1);

    public static final ResponseUserDTO RESPONSE_USER_2 = new ResponseUserDTO(2L, "Instructor", "Employee", "instructor.employee2@mail.com");
    public static final ResponseInstructorDTO RESPONSE_INSTRUCTOR_2 = new ResponseInstructorDTO(2L, RESPONSE_USER_2);

    public static final ResponseUserDTO RESPONSE_USER_3 = new ResponseUserDTO(3L, "Instructor", "Employee", "instructor.employee3@mail.com");
    public static final ResponseInstructorDTO RESPONSE_INSTRUCTOR_3 = new ResponseInstructorDTO(3L, RESPONSE_USER_3);
}
