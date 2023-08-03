package uol.compass.cspcapi.commons;

import uol.compass.cspcapi.application.api.student.dto.ResponseStudentDTO;
import uol.compass.cspcapi.application.api.user.dto.ResponseUserDTO;

public class StudentsConstants {

    public static final ResponseUserDTO RESPONSE_USER_1 = new ResponseUserDTO(1L, "User", "Third", "user.third@mail.com");
    public static final ResponseStudentDTO RESPONSE_STUDENT_1 = new ResponseStudentDTO(1L, RESPONSE_USER_1);

    public static final ResponseUserDTO RESPONSE_USER_2 = new ResponseUserDTO(2L, "User", "Fourth", "user.fourth@mail.com");
    public static final ResponseStudentDTO RESPONSE_STUDENT_2 = new ResponseStudentDTO(2L, RESPONSE_USER_2);

    public static final ResponseUserDTO RESPONSE_USER_3 = new ResponseUserDTO(3L, "User", "Student", "user.student@mail.com");
    public static final ResponseStudentDTO RESPONSE_STUDENT_3 = new ResponseStudentDTO(3L, RESPONSE_USER_3);
}
