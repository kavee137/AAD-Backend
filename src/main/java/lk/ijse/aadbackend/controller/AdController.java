package lk.ijse.aadbackend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lk.ijse.aadbackend.dto.AdDTO;
import lk.ijse.aadbackend.dto.ResponseDTO;
import lk.ijse.aadbackend.service.AdService;
import lk.ijse.aadbackend.util.VarList;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@CrossOrigin(origins = "http://localhost:63342") // Allow frontend URL
@RestController
@RequestMapping("api/v1/ad")
public class AdController {

    private final AdService adService;

    public AdController(AdService adService) {
        this.adService = adService;
    }


    @GetMapping("/getAllActiveAds")
    public ResponseEntity<ResponseDTO> getAllActiveAds() {
        List<AdDTO> activeAds = adService.getAllActiveAds();

        return ResponseEntity.ok(new ResponseDTO(VarList.Created, "Active ads retrieved successfully", activeAds));
    }




    @PostMapping(value = "/createAd", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ResponseDTO> createAd(
            @RequestParam("adDTO") String adDTOJson,  // JSON as a String
            @RequestPart(value = "images", required = false) List<MultipartFile> imageFiles) {

        try {
            // Convert JSON String to AdDTO object
            ObjectMapper objectMapper = new ObjectMapper();
            AdDTO adDTO = objectMapper.readValue(adDTOJson, AdDTO.class);

            // Call service to create ad
            int createdAd = adService.createAd(adDTO, imageFiles);

            if (createdAd == VarList.Created) {
                return ResponseEntity.status(HttpStatus.CREATED)
                        .body(new ResponseDTO(VarList.Created, "Ad successfully created", adDTO));
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseDTO(VarList.Bad_Gateway, "Error while creating ad", null));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(VarList.Internal_Server_Error, e.getMessage(), null));
        }
    }


    @PutMapping("/deleteAd/{adId}")
    public ResponseEntity<ResponseDTO> deleteAd(@PathVariable UUID adId) {
        int result = adService.deleteAd(adId);

        if (result == VarList.Created) {
            return ResponseEntity.ok(new ResponseDTO(VarList.Created, "Ad deleted successfully!", adId));
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(VarList.Internal_Server_Error, "Error deleting ad", null));
        }
    }

}