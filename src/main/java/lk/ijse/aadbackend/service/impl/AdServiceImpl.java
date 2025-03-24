package lk.ijse.aadbackend.service.impl;

import lk.ijse.aadbackend.dto.AdDTO;
import lk.ijse.aadbackend.entity.Ad;
import lk.ijse.aadbackend.entity.Category;
import lk.ijse.aadbackend.entity.Location;
import lk.ijse.aadbackend.entity.User;
import lk.ijse.aadbackend.enums.Status;
import lk.ijse.aadbackend.repo.*;
import lk.ijse.aadbackend.service.AdService;
import lk.ijse.aadbackend.util.VarList;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class AdServiceImpl implements AdService {

    @Autowired
    private AdRepository adRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private ModelMapper modelMapper;


    @Override
    public int createAd(AdDTO adDTO, List<MultipartFile> imageFiles) {
        try {
            // Create new Ad instance
            Ad ad = new Ad();
            ad.setTitle(adDTO.getTitle());
            ad.setDescription(adDTO.getDescription());
            ad.setPrice(adDTO.getPrice());
            ad.setStatus(adDTO.getStatus());
            ad.setCreatedAt(LocalDateTime.now());
            ad.setUpdatedAt(LocalDateTime.now());

            // Fetch related entities
            User user = userRepository.findById(adDTO.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            Category category = categoryRepository.findById(adDTO.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            Location location = locationRepository.findById(adDTO.getLocationId())
                    .orElseThrow(() -> new RuntimeException("Location not found"));

            ad.setUser(user);
            ad.setCategory(category);
            ad.setLocation(location);

            Ad savedAd = adRepository.save(ad);

            if (imageFiles != null && !imageFiles.isEmpty()) {
                StringBuilder imageNames = new StringBuilder();

                String uploadDir = System.getProperty("user.dir") + "/uploadImages/";
                File uploadFolder = new File(uploadDir);
                if (!uploadFolder.exists() && !uploadFolder.mkdirs()) {
                    throw new RuntimeException("Failed to create upload directory!");
                }

                for (MultipartFile file : imageFiles) {
                    if (!file.isEmpty()) {
                        String uniqueFileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
                        File destinationFile = new File(uploadDir + uniqueFileName);

                        System.out.println("Saving file to: " + destinationFile.getAbsolutePath());

                        file.transferTo(destinationFile);

                        if (imageNames.length() > 0) {
                            imageNames.append(",");
                        }
                        imageNames.append(uniqueFileName);
                    }
                }

                savedAd.setImages(imageNames.toString());
                adRepository.save(savedAd);
            }

            return VarList.Created;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error while saving ad: " + e.getMessage(), e);
        }
    }


    @Override
    public int deleteAd(UUID adId) {
        try {
            Ad ad = adRepository.findById(adId)
                    .orElseThrow(() -> new RuntimeException("Ad not found"));

            ad.setStatus(Status.DELETED.toString());
            adRepository.save(ad);

            return VarList.Created;
        } catch (Exception e) {
            e.printStackTrace();
            return VarList.Internal_Server_Error;
        }
    }


    @Override
    public List<AdDTO> getAllActiveAds() {
        List<Ad> activeAds = adRepository.findByStatus("ACTIVE");

        return activeAds.stream()
                .map(ad -> modelMapper.map(ad, AdDTO.class))
                .toList();
    }


    @Override
    public List<AdDTO> getAdsByUserId(UUID userId) {
        List<Ad> ads = adRepository.findByUserId(userId);
        return ads.stream()
                .map(ad -> modelMapper.map(ad, AdDTO.class))
                .toList();
    }



}
