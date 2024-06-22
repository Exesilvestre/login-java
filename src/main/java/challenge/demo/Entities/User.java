package challenge.demo.Entities;


import challenge.demo.Services.usersDTO.CreateUserDTO;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

@Table(name = "users")
@Data
@Entity
@NoArgsConstructor
public class User {

    @Column(name = "id")
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private String idUser;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column
    private String name;

    @Column
    private String lastName;

    public User (CreateUserDTO user){
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.name = user.getName();
        this.lastName = user.getLastName();
    }
}
