package lk.ijse.aadbackend.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Data
public class AdDTO {
    private UUID id;

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Description is required")
    private String description;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be greater than zero")
    private Double price;

    @NotBlank(message = "Status is required")
    private String status;

    @NotNull(message = "User ID is required")
    private UUID userId;

    @NotNull(message = "Category ID is required")
    private UUID categoryId;

    @NotNull(message = "Location ID is required")
    private UUID locationId;

    // List of image files to be uploaded
    private List<MultipartFile> imageFiles;
}
