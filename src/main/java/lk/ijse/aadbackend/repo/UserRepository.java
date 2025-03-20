package lk.ijse.aadbackend.repo;

import lk.ijse.aadbackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User,Long> {

    User findByEmail(String userName);

    boolean existsByEmail(String email);

    int deleteByEmail(String userName);

}