package lk.ijse.aadbackend.repo;

import lk.ijse.aadbackend.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LocationRepository extends JpaRepository<Location, UUID> {
}
