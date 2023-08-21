package uol.compass.cspcapi.application.api.auth;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import uol.compass.cspcapi.application.api.auth.dto.AuthenticatedResponseDTO;
import uol.compass.cspcapi.application.api.auth.dto.LoginDTO;
import uol.compass.cspcapi.application.api.generalPourposeDTO.ResponseDTO;
import uol.compass.cspcapi.application.api.user.dto.CreateUserDTO;
import uol.compass.cspcapi.domain.role.Role;
import uol.compass.cspcapi.domain.role.RoleService;
import uol.compass.cspcapi.domain.user.User;
import uol.compass.cspcapi.domain.user.UserService;
import uol.compass.cspcapi.infrastructure.config.jwt.JwtTokenProvider;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;

    private final JwtTokenProvider jwtTokenProvider;

    private final UserService userService;

    private final RoleService roleService;

    public AuthController(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, UserService userService, RoleService roleService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
        this.roleService = roleService;
    }

    @PostMapping("/signin")
    public ResponseEntity<?> login(@Valid @RequestBody LoginDTO login){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        login.email(),
                        login.password()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtTokenProvider.createToken(authentication);
        return ResponseEntity.ok(
                ResponseDTO.ok(new AuthenticatedResponseDTO(token))
        );
    }

    // this route only create admin users - may be careful
    @PostMapping("/register")
    public ResponseEntity<?> saveAdminUser(@Valid @RequestBody CreateUserDTO createUserDTO){
        User user = new User(
                createUserDTO.firstName(),
                createUserDTO.lastName(),
                createUserDTO.email(),
                createUserDTO.password()
        );

        Role role = roleService.findRoleByName("ROLE_ADMIN");
        if (role == null) {
            // Role not found, throw internal server error
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error creating admin user.");
        }

        user.getRoles().add(role);

//        return ResponseEntity.ok(
//                ResponseDTO.ok(userService.saveUser(user))
//        );

        return new ResponseEntity<>(
                new ResponseDTO<>(userService.saveUser(user)),
                HttpStatus.CREATED
        );
    }
}
