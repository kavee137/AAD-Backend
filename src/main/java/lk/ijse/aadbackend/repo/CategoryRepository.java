package lk.ijse.aadbackend.repo;

import lk.ijse.aadbackend.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
