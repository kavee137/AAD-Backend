package lk.ijse.aadbackend.repo;

import lk.ijse.aadbackend.entity.Ad;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdRepository  extends JpaRepository<Ad,Long> {

}
