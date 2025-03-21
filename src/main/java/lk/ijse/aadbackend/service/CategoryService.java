package lk.ijse.aadbackend.service;

import lk.ijse.aadbackend.dto.CategoryDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface CategoryService {
    int createCategory(CategoryDTO categoryDTO, MultipartFile image) throws IOException;
    List<CategoryDTO> getAllCategories();
    CategoryDTO getCategoryById(UUID id);
    void deleteCategory(UUID id);
}
