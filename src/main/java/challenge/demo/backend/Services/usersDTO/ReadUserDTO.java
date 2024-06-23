package challenge.demo.backend.Services.usersDTO;
import lombok.AllArgsConstructor;
import challenge.demo.backend.Entities.User;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReadUserDTO {
    private String email;
    private String name;
    private String lastName;

    public ReadUserDTO(User user) {
        this.email = user.getEmail();
        this.name = user.getName();
        this.lastName = user.getLastName();
    }
}
