package challenge.demo.Services;


import challenge.demo.Entities.User;
import challenge.demo.Repository.UserRepository;
import challenge.demo.Services.exceptions.BadRequestException;
import challenge.demo.Services.exceptions.DataIntegrityViolationException;
import challenge.demo.Services.exceptions.InternalServerErrorException;
import challenge.demo.Services.exceptions.ResourceNotFoundException;
import challenge.demo.Services.usersDTO.CreateUserDTO;
import challenge.demo.Services.usersDTO.LoginUserDTO;
import challenge.demo.Services.usersDTO.ReadUserDTO;
import jakarta.transaction.Transactional;
import lombok.Locked;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UserService {

    private UserRepository userRepository;
    private PasswordGeneratorService passwordGeneratorService;
    private EmailService emailService;

    public UserService(UserRepository userRepository, PasswordGeneratorService passwordGeneratorService, EmailService emailService) {
        this.userRepository = userRepository;
        this.passwordGeneratorService = passwordGeneratorService;
        this.emailService = emailService;
    }
    private static final String EMAIL_REGEX = "^(.+)@(.+)$";
    private static final String PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=-])(?=\\S+$).{8,}$";
    private static final Pattern PASSWORD_PATTERN = Pattern.compile(PASSWORD_REGEX);
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    public Optional<ReadUserDTO> authUser(LoginUserDTO userToLogin){
        String emailValidated = validateEmail(userToLogin.getEmail());
        String passwordValidated = validatePassword(userToLogin.getPassword());

        if (emailValidated == null || passwordValidated == null) {
            throw new BadRequestException("\"Usuario/contraseña incorrectos");
        }

        Optional<User> existingUser = userRepository.findUserByEmailAndPassword(emailValidated, passwordValidated);
        return existingUser.map(ReadUserDTO::new);


    }

    public Optional<ReadUserDTO> createUser(CreateUserDTO userToCreate) {
        String emailValidated = validateEmail(userToCreate.getEmail());
        String passwordValidated = validatePassword(userToCreate.getPassword());


        if (emailValidated == null || passwordValidated == null) {
            System.out.println(emailValidated + passwordValidated);
            throw new BadRequestException("\"Usuario/contraseña incorrectos");
        }
        try {
            User user = new User(userToCreate);
            User userSaved = userRepository.save(user);
            return Optional.of(new ReadUserDTO(userSaved));
        } catch (DataIntegrityViolationException e) {
            throw new BadRequestException("Email ya esta registrado");
        } catch (Exception e) {
            throw new InternalServerErrorException("Error creating user: " + e.getMessage());
        }
    }

    @Transactional
    public void changePassword(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        String newPassword = passwordGeneratorService.generatePassword();
        user.setPassword(newPassword);
        userRepository.save(user);
        emailService.sendSimpleEmail(user.getEmail(), user.getPassword());
    }


    public String validateEmail(String emailToValidate) {
        Matcher matcher = EMAIL_PATTERN.matcher(emailToValidate);
        return matcher.matches() ? emailToValidate : null;
    }

    public String validatePassword(String pwdToValidate) {
        Matcher matcher = PASSWORD_PATTERN.matcher(pwdToValidate);
        return matcher.matches() ? pwdToValidate : null;
    }

}
