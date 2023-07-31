package uol.compass.cspcapi;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import uol.compass.cspcapi.application.api.instructor.dto.CreateInstructorDTO;
import uol.compass.cspcapi.application.api.instructor.dto.ResponseInstructorDTO;
import uol.compass.cspcapi.application.api.instructor.dto.UpdateInstructorDTO;
import uol.compass.cspcapi.domain.instructor.Instructor;
import uol.compass.cspcapi.domain.instructor.InstructorRepository;
import uol.compass.cspcapi.domain.instructor.InstructorService;
import uol.compass.cspcapi.domain.role.Role;
import uol.compass.cspcapi.domain.role.RoleService;
import uol.compass.cspcapi.domain.user.User;
import uol.compass.cspcapi.domain.user.UserService;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static net.bytebuddy.matcher.ElementMatchers.any;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class InstructorServiceTest {

    @Mock
    private InstructorRepository instructorRepository;

    @InjectMocks
    private InstructorService instructorService;

    @Mock
    private UserService userService;

    @Mock
    private RoleService roleService;

    //Save
    @Test
    public void testSaveInstructor_Success() {
        // Criar um objeto CreateInstructorDTO simulado
        CreateInstructorDTO instructorDTO = new CreateInstructorDTO();
        // Configurar as propriedades do DTO simulado, como o primeiro nome, sobrenome, email e senha do usuário
        instructorDTO.getUser().setFirstName("John");
        instructorDTO.getUser().setLastName("Doe");
        instructorDTO.getUser().setEmail("john.doe@example.com");
        instructorDTO.getUser().setPassword("password123");

        // Configurar o comportamento simulado do serviço de usuário para retornar um usuário não existente
        when(userService.findByEmail(instructorDTO.getUser().getEmail())).thenReturn(Optional.empty());

        // Configurar o comportamento simulado do serviço de papel para retornar um papel simulado de instrutor
        when(roleService.findRoleByName("ROLE_INSTRUCTOR")).thenReturn(new Role("ROLE_INSTRUCTOR"));

        // Criar um objeto Instructor simulado
        Instructor instructor = new Instructor();
        instructor.setId(1L);
        // Configurar outras propriedades do instrutor, se necessário

        // Configurar o comportamento simulado do repositório para retornar o instrutor simulado ao salvá-lo
        when(instructorRepository.save(instructor)).thenReturn(instructor);

        // Executar o método sendo testado
        ResponseInstructorDTO result = instructorService.save(instructorDTO);

        // Verificar se o instrutor foi salvo corretamente no repositório
        verify(instructorRepository).save(instructor);

        // Verificar se a resposta possui as informações corretas do instrutor mapeado para a DTO
        assertEquals(instructor.getId(), result.getId());
        // Verificar outras propriedades da DTO, se houver
    }

    @Test
    public void testSaveInstructor_Error_UserAlreadyExists() {
        // Criar um objeto CreateInstructorDTO simulado
        CreateInstructorDTO instructorDTO = new CreateInstructorDTO();
        // Configurar as propriedades do DTO simulado, como o primeiro nome, sobrenome, email e senha do usuário
        instructorDTO.getUser().setFirstName("John");
        instructorDTO.getUser().setLastName("Doe");
        instructorDTO.getUser().setEmail("john.doe@example.com");
        instructorDTO.getUser().setPassword("password123");

        // Configurar o comportamento simulado do serviço de usuário para retornar um usuário existente
        when(userService.findByEmail(instructorDTO.getUser().getEmail()))
                .thenReturn(Optional.of(new User()));

        // Executar o método sendo testado e verificar se ele lança uma exceção
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> instructorService.save(instructorDTO));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("user already exists", exception.getReason());
    }



    //GetById
    @Test
    public void testGetInstructorById_Success() {
        Long instructorId = 1L;
        Instructor instructor = new Instructor();
        instructor.setId(instructorId);
        when(instructorRepository.findById(instructorId)).thenReturn(Optional.of(instructor));

        ResponseInstructorDTO result = instructorService.getById(instructorId);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(instructorId);
    }

    @Test
    public void testGetInstructorById_Failure() {
        Long instructorId = 2L;
        when(instructorRepository.findById(instructorId)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> instructorService.getById(instructorId));
    }



    //GetAll
    @Test
    public void testGetAll_Success() {
        Instructor instructor1 = new Instructor(new User("John", "Doe", "johndoe@compass.com", "senha"));
        Instructor instructor2 = new Instructor(new User("Jane", "Doe", "janedoe@compass.com", "senha"));
        List<Instructor> mockInstructors = Arrays.asList(instructor1, instructor2);

        when(instructorRepository.findAll()).thenReturn(mockInstructors);

        List<ResponseInstructorDTO> result = instructorService.getAll();

        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
    }

    @Test
    public void testGetAll_Failure() {
        when(instructorRepository.findAll()).thenReturn(Collections.emptyList());

        instructorService.getAll();
    }



    //GetAllById
    @Test
    public void testGetAllInstructorsById_Success() {
        List<Long> instructorIds = Arrays.asList(1L, 2L, 3L);

        List<Instructor> mockInstructors = Arrays.asList(new Instructor(), new Instructor(), new Instructor());

        when(instructorRepository.findAllByIdIn(instructorIds)).thenReturn(mockInstructors);

        List<Instructor> result = instructorService.getAllInstructorsById(instructorIds);

        assertEquals(mockInstructors, result);
    }

    @Test
    public void testGetAllInstructorsById_Error() {
        List<Long> instructorIds = Arrays.asList(1L, 2L, 3L);

        when(instructorRepository.findAllByIdIn(instructorIds)).thenReturn(Arrays.asList(new Instructor(), new Instructor()));

        Throwable exception = assertThrows(ResponseStatusException.class, () -> {
            instructorService.getAllInstructorsById(instructorIds);
        });

        assertEquals(HttpStatus.NOT_FOUND, ((ResponseStatusException) exception).getStatusCode());
        assertEquals("One or more instructors not found", ((ResponseStatusException) exception).getReason());
    }



    //DeleteById
    @Test
    public void testDeleteById_Success() {
        Long instructorId = 1L;

        Instructor instructor = new Instructor(new User("John", "Doe", "johndoe@compass.com", "senha"));
        instructor.setId(instructorId);

        when(instructorRepository.findById(instructorId)).thenReturn(Optional.of(instructor));

        instructorService.deleteById(instructorId);

        verify(instructorRepository).delete(instructor);
    }

    @Test
    public void testDeleteById_Error() {
        Long instructorId = 1L;

        when(instructorRepository.findById(instructorId)).thenReturn(Optional.empty());

        Throwable exception = assertThrows(ResponseStatusException.class, () -> {
            instructorService.deleteById(instructorId);
        });

        assertEquals(HttpStatus.NOT_FOUND, ((ResponseStatusException) exception).getStatusCode());
        assertEquals("instructor not found", ((ResponseStatusException) exception).getReason());
    }


}

