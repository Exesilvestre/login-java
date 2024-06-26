package challenge.demo.backend.Controller;

import challenge.demo.backend.Services.UserService;
import challenge.demo.backend.Services.exceptions.BadRequestException;
import challenge.demo.backend.Services.exceptions.InternalServerErrorException;
import challenge.demo.backend.Services.exceptions.ResourceNotFoundException;
import challenge.demo.backend.Services.usersDTO.CreateUserDTO;
import challenge.demo.backend.Services.usersDTO.LoginUserDTO;
import challenge.demo.backend.Services.usersDTO.ReadUserDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<ReadUserDTO> authUser (@RequestBody LoginUserDTO userToLogin) {
        try {
            Optional<ReadUserDTO> userLogged = userService.authUser(userToLogin);

            if (userLogged.isEmpty()) {
                throw new ResourceNotFoundException("Usuario/contraseña incorrectos");
            }
            return ResponseEntity.ok(userLogged.get());

        }catch (BadRequestException e) {
            throw e;
        }  catch (ResourceNotFoundException e) {
            throw e;
        }catch (Exception e) {
            throw new InternalServerErrorException("Internal server error");
        }
    }

    @PostMapping("/signUp")
    public ResponseEntity<ReadUserDTO> createUser (@Valid @RequestBody CreateUserDTO userToLogin, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new BadRequestException("Campos faltantes");
        }
        try {
            Optional<ReadUserDTO> userCreated = userService.createUser(userToLogin);
            return userCreated.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.badRequest().build());
        } catch (BadRequestException e) {
            throw e;
        }catch (Exception e) {
            throw new InternalServerErrorException("Internal server error");
        }
    }

    @PostMapping("/changePassword/")
    public ResponseEntity<String> changePassword(@RequestParam String email) {
        try{
            userService.changePassword(email);
            return ResponseEntity.ok("Password changed successfully");
        }catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }catch (Exception e){
            throw new InternalServerErrorException("Internal server error");
        }
    }
}
