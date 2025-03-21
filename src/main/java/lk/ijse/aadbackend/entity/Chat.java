package lk.ijse.aadbackend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "VARCHAR(36)" , nullable = false , unique = true )
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private UUID id;

    @NotBlank(message = "Message cannot be empty")
    @Lob
    private String message;

    @NotBlank(message = "Status is required")
    private String status;

    private LocalDateTime timestamp;

    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;

    @ManyToOne
    @JoinColumn(name = "receiver_id", nullable = false)
    private User receiver;

    @ManyToOne
    @JoinColumn(name = "ad_id", nullable = false)
    private Ad ad;
}
