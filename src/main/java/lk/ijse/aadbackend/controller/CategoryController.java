package lk.ijse.aadbackend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lk.ijse.aadbackend.dto.CategoryDTO;
import lk.ijse.aadbackend.dto.ResponseDTO;
import lk.ijse.aadbackend.service.CategoryService;
import lk.ijse.aadbackend.util.VarList;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/category")
public class CategoryController {

    private final CategoryService categoryService;
    private final ObjectMapper objectMapper; // JSON parser

    public CategoryController(CategoryService categoryService, ObjectMapper objectMapper) {
        this.categoryService = categoryService;
        this.objectMapper = objectMapper;
    }

    @PostMapping(value = "/create", consumes = "multipart/form-data")
    public ResponseEntity<ResponseDTO> createCategory(
            @RequestPart("category") String categoryJson,
            @RequestPart(value = "image", required = false) MultipartFile image) {

        try {
            // Convert JSON string to CategoryDTO
            CategoryDTO categoryDTO = objectMapper.readValue(categoryJson, CategoryDTO.class);

            int createdCategory = categoryService.createCategory(categoryDTO, image);

            if (createdCategory == VarList.Created) {
                return ResponseEntity.status(HttpStatus.CREATED)
                        .body(new ResponseDTO(VarList.Created, "Category successfully created", null));
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseDTO(VarList.Bad_Gateway, "Error while creating category", null));

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(VarList.Internal_Server_Error, e.getMessage(), null));
        }
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<CategoryDTO>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO> getCategoryById(@PathVariable UUID id) {
        return ResponseEntity.ok(categoryService.getCategoryById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable UUID id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok("Category deleted successfully");
    }
}
