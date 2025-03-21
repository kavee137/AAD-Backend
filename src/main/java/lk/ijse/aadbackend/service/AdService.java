package lk.ijse.aadbackend.service;

import lk.ijse.aadbackend.dto.AdDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AdService {
    int createAd(AdDTO adDTO, List<MultipartFile> imageFiles);
}
