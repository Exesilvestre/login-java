package challenge.demo.Services.usersDTO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
public class LoginUserDTO {
    private String email;
    private String password;
}
