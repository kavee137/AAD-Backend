package lk.ijse.aadbackend.controller;

import jakarta.validation.Valid;
import lk.ijse.aadbackend.dto.AdDTO;
import lk.ijse.aadbackend.dto.ResponseDTO;
import lk.ijse.aadbackend.service.AdService;
import lk.ijse.aadbackend.util.VarList;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/ad")
public class AdController {

    private final AdService adService;


    public AdController(AdService adService) {
        this.adService = adService;
    }

    @PostMapping("/createAd")
    public ResponseEntity<ResponseDTO> createAd(@RequestBody @Valid AdDTO adDTO) {

        try {
            int createdAd = adService.createAd(adDTO);


            if (createdAd == VarList.Created) {
                return ResponseEntity.status(HttpStatus.CREATED)
                        .body(new ResponseDTO(VarList.Created, "Success", null));
            }
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                    .body(new ResponseDTO(VarList.Bad_Gateway, "Error", null));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(VarList.Internal_Server_Error, e.getMessage(), null));
        }
    }
}