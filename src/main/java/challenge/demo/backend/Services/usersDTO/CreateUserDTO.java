package challenge.demo.backend.Services.usersDTO;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class CreateUserDTO {
    @NotNull(message = "Email es obligatorio")
    private String email;
    @NotNull(message = "Password es obligatorio")
    private String password;
    @NotNull(message = "Nombre es obligatorio")
    private String name;
    @NotNull(message = "Apellido es obligatorio")
    private String lastName;
}
