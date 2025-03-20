package lk.ijse.aadbackend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Column(unique = true)
    private String email;

    @NotBlank(message = "Password is required")
    private String password;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^[0-9]{10}$", message = "Invalid phone number")
    private String phone;

    @Value("${user.default.role:USER}")
    @NotBlank(message = "Role is required")
    private String role;

    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Ad> ads;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Favourite> favourites;

    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL)
    private List<Chat> sentMessages;

    @OneToMany(mappedBy = "receiver", cascade = CascadeType.ALL)
    private List<Chat> receivedMessages;
}
