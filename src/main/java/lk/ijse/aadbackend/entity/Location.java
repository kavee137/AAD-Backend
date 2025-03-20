package lk.ijse.aadbackend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Location name is required")
    private String name;

    @ManyToOne
    @JoinColumn(name = "parent_location_id")
    private Location parentLocation;

    @OneToMany(mappedBy = "location", cascade = CascadeType.ALL)
    private List<Ad> ads;
}
