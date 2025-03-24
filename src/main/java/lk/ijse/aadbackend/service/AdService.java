package lk.ijse.aadbackend.service;

import lk.ijse.aadbackend.dto.AdDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface AdService {
    int createAd(AdDTO adDTO, List<MultipartFile> imageFiles);

    int deleteAd(UUID adId);

    List<AdDTO> getAllActiveAds();

    List<AdDTO> getAdsByUserId(UUID userId);
}