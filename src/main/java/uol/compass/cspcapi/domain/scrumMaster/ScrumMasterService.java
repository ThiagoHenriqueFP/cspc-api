package uol.compass.cspcapi.domain.scrumMaster;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import uol.compass.cspcapi.application.api.scrumMaster.dto.CreateScrumMasterDTO;
import uol.compass.cspcapi.application.api.scrumMaster.dto.ResponseScrumMasterDTO;
import uol.compass.cspcapi.application.api.scrumMaster.dto.UpdateScrumMasterDTO;
import uol.compass.cspcapi.domain.classroom.Classroom;
import uol.compass.cspcapi.domain.role.RoleService;
import uol.compass.cspcapi.domain.user.User;
import uol.compass.cspcapi.domain.user.UserService;
import uol.compass.cspcapi.infrastructure.config.passwordEncrypt.PasswordEncoder;

import java.util.List;
import java.util.Optional;

@Service
public class ScrumMasterService {

    private final ScrumMasterRepository scrumMasterRepository;

    // Aqui eu não posso acessar diretamente o user repository
    // Essa abordagem serve para manter as classes protegidas
    private final UserService userService;

    private final PasswordEncoder passwordEncrypt;
    private final RoleService roleService;

    @Autowired
    public ScrumMasterService(ScrumMasterRepository scrumMasterRepository, UserService userService, PasswordEncoder passwordEncrypt, RoleService roleService) {
        this.scrumMasterRepository = scrumMasterRepository;
        this.userService = userService;
        this.passwordEncrypt = passwordEncrypt;
        this.roleService = roleService;
    }

    @Transactional
    public ScrumMaster save(CreateScrumMasterDTO scrumMaster) {
        Optional<User> alreadyExists = userService.findByEmail(scrumMaster.getEmail());

        if(alreadyExists.isPresent()){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "scrumMaster already exists"
            );
        }

        User newUser = new User(
                scrumMaster.getFirstName(),
                scrumMaster.getLastName(),
                scrumMaster.getEmail(),
                passwordEncrypt.encoder().encode(scrumMaster.getPassword())
        );

        User savedUSer = userService.saveUser(newUser);

        savedUSer.getRoles().add(roleService.findRoleByName("ROLE_SCRUM_MASTER"));

        ScrumMaster newScrumMaster = new ScrumMaster(
                savedUSer
        );

        return scrumMasterRepository.save(newScrumMaster);

    }

    public ScrumMaster getById(Long id){
        return scrumMasterRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "user not found"
                )
        );
    }

    public List<ScrumMaster> getAll(){
        return scrumMasterRepository.findAll();
    }

    public ResponseScrumMasterDTO update(Long id, UpdateScrumMasterDTO scrumMasterDTO) {
        ScrumMaster scrumMaster = scrumMasterRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "scrummaster not found"
                )
        );

        User user = scrumMaster.getUser();

        user.setFirstName(scrumMasterDTO.getFirstName());
        user.setLastName(scrumMasterDTO.getLastName());
        user.setEmail(scrumMasterDTO.getEmail());

        scrumMaster.setUser(user);

        scrumMasterRepository.save(scrumMaster);

        return new ResponseScrumMasterDTO (
                scrumMaster.getId(),
                scrumMaster.getUser().getFirstName(),
                scrumMaster.getUser().getLastName(),
                scrumMaster.getUser().getEmail()
        );
    }

    public boolean delete(Long id){
        ScrumMaster scrumMaster = scrumMasterRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "user not found"
                )
        );

        scrumMasterRepository.delete(scrumMaster);
        return true;
    }

    public List<ScrumMaster> getAllScrumMastersById(List<Long> scrumMastersIds) {
        return scrumMasterRepository.findAllByIdIn(scrumMastersIds);
    }

    @Transactional
    public List<ScrumMaster> attributeScrumMastersToClassroom(Classroom classroom, List<ScrumMaster> scrumMasters) {
        for (ScrumMaster scrumMaster : scrumMasters) {
            scrumMaster.setClassroom(classroom);
        }
        return scrumMasterRepository.saveAll(scrumMasters);
    }


}
