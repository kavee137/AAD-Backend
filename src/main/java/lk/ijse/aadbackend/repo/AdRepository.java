package lk.ijse.aadbackend.repo;

import lk.ijse.aadbackend.entity.Ad;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AdRepository  extends JpaRepository<Ad, UUID> {
    List<Ad> findByStatus(String status);

    List<Ad> findByUserId(UUID userId);

}