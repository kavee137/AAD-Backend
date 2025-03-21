package lk.ijse.aadbackend.service.impl;

import lk.ijse.aadbackend.service.CategoryService;

import lk.ijse.aadbackend.dto.CategoryDTO;
import lk.ijse.aadbackend.entity.Category;
import lk.ijse.aadbackend.repo.CategoryRepository;
import lk.ijse.aadbackend.service.CategoryService;
import lk.ijse.aadbackend.util.VarList;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    private static final String UPLOAD_DIR = "uploadImages/";

    @Override
    public int createCategory(CategoryDTO categoryDTO, MultipartFile image) throws IOException {
        Category category = modelMapper.map(categoryDTO, Category.class);

        if (image != null && !image.isEmpty()) {
            String imageName = saveImage(image);
            category.setImageUrl(UPLOAD_DIR + imageName);
        }

        if (categoryDTO.getParentCategoryId() != null) {
            Category parentCategory = categoryRepository.findById(categoryDTO.getParentCategoryId())
                    .orElseThrow(() -> new RuntimeException("Parent category not found"));
            category.setParentCategory(parentCategory);
        }

        Category savedCategory = categoryRepository.save(category);
        modelMapper.map(savedCategory, CategoryDTO.class);
        return VarList.Created;
    }

    private String saveImage(MultipartFile image) throws IOException {
        File uploadDir = new File(UPLOAD_DIR);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        String fileExtension = getFileExtension(image.getOriginalFilename());
        String uniqueFileName = UUID.randomUUID().toString() + fileExtension;
        String filePath = UPLOAD_DIR + uniqueFileName;

        Files.write(Paths.get(filePath), image.getBytes());
        return uniqueFileName;
    }

    private String getFileExtension(String filename) {
        return filename.lastIndexOf('.') > 0 ? filename.substring(filename.lastIndexOf('.')) : "";
    }

    @Override
    public List<CategoryDTO> getAllCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(category -> modelMapper.map(category, CategoryDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDTO getCategoryById(UUID id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        return modelMapper.map(category, CategoryDTO.class);
    }

    @Override
    public void deleteCategory(UUID id) {
        if (!categoryRepository.existsById(id)) {
            throw new RuntimeException("Category not found");
        }
        categoryRepository.deleteById(id);
    }
}

