package challenge.demo.Services;


import challenge.demo.Entities.User;
import challenge.demo.Repository.UserRepository;
import challenge.demo.Services.exceptions.BadRequestException;
import challenge.demo.Services.exceptions.ResourceNotFoundException;
import challenge.demo.Services.usersDTO.CreateUserDTO;
import challenge.demo.Services.usersDTO.LoginUserDTO;
import challenge.demo.Services.usersDTO.ReadUserDTO;
import lombok.Locked;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UserService {

    private UserRepository userRepository;
    private PasswordGeneratorService passwordGeneratorService;

    private EmailService emailService;
    private static final String EMAIL_REGEX = "^(.+)@(.+)$";
    private static final String PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);
    private static final Pattern PASSWORD_PATTERN = Pattern.compile(PASSWORD_REGEX);

    public UserService(UserRepository estacionRepository) {
        this.userRepository = userRepository;
    }

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
            throw new BadRequestException("\"Usuario/contraseña incorrectos");
        }

        if (userRepository.existsByEmail(emailValidated)) {
            throw new BadRequestException("Email ya esta registrado");
        }

        User user = new User(userToCreate);
        User userSaved = userRepository.save(user);

        return Optional.of(new ReadUserDTO(userSaved));
    }

    public void changePassword(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        user.setPassword(passwordGeneratorService.generatePassword());
        userRepository.save(user);
        emailService.sendEmail(user.getEmail(), user.getPassword());

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
