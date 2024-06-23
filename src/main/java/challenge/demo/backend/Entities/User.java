package challenge.demo.backend.Entities;


import challenge.demo.backend.Services.usersDTO.CreateUserDTO;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Table(name = "users")
@Entity
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column
    private String name;

    @Column(name = "last_name")
    private String lastName;

    public User (CreateUserDTO user){
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.name = user.getName();
        this.lastName = user.getLastName();
    }
}
