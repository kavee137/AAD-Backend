package lk.ijse.aadbackend.controller;

import jakarta.validation.Valid;
import lk.ijse.aadbackend.dto.AdDTO;
import lk.ijse.aadbackend.dto.ResponseDTO;
import lk.ijse.aadbackend.service.AdService;
import lk.ijse.aadbackend.util.VarList;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("api/v1/ad")
public class AdController {

    private final AdService adService;

    public AdController(AdService adService) {
        this.adService = adService;
    }

    @PostMapping(value = "/createAd", consumes = {"multipart/form-data"})
    public ResponseEntity<ResponseDTO> createAd(
            @RequestPart("adDTO") @Valid AdDTO adDTO,
            @RequestPart(value = "images", required = false) List<MultipartFile> imageFiles) {

        try {
            int createdAd = adService.createAd(adDTO, imageFiles);

            if (createdAd == VarList.Created) {
                return ResponseEntity.status(HttpStatus.CREATED)
                        .body(new ResponseDTO(VarList.Created, "Ad successfully created", null));
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseDTO(VarList.Bad_Gateway, "Error while creating ad", null));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(VarList.Internal_Server_Error, e.getMessage(), null));
        }
    }
}
