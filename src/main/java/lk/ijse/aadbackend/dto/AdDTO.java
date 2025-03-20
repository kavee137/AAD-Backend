package lk.ijse.aadbackend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AdDTO {
    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Description is required")
    private String description;

    @NotBlank(message = "Price is required")
    private Double price;
    private String status;
    private Long categoryId;
    private Long locationId;
    private Long userId;
}