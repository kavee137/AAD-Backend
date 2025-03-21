package lk.ijse.aadbackend.service.impl;

import lk.ijse.aadbackend.dto.AdDTO;
import lk.ijse.aadbackend.entity.Ad;
import lk.ijse.aadbackend.entity.Category;
import lk.ijse.aadbackend.entity.Location;
import lk.ijse.aadbackend.entity.User;
import lk.ijse.aadbackend.repo.*;
import lk.ijse.aadbackend.service.AdService;
import lk.ijse.aadbackend.util.VarList;
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

    @Override
    public int createAd(AdDTO adDTO, List<MultipartFile> imageFiles) {
        try {
            // Create new Ad instance and set properties manually
            Ad ad = new Ad();
            ad.setTitle(adDTO.getTitle());
            ad.setDescription(adDTO.getDescription());
            ad.setPrice(adDTO.getPrice());
            ad.setStatus(adDTO.getStatus());
            ad.setCreatedAt(LocalDateTime.now());
            ad.setUpdatedAt(LocalDateTime.now());

            // Fetch related entities and set them
            User user = userRepository.findById(adDTO.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            Category category = categoryRepository.findById(adDTO.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            Location location = locationRepository.findById(adDTO.getLocationId())
                    .orElseThrow(() -> new RuntimeException("Location not found"));

            ad.setUser(user);
            ad.setCategory(category);
            ad.setLocation(location);

            // Save the Ad first to get the generated ID
            Ad savedAd = adRepository.save(ad);

            // Image Upload Processing
            if (imageFiles != null && !imageFiles.isEmpty()) {
                StringBuilder imageNames = new StringBuilder();
                String uploadDir = "uploadImages/";

                File uploadFolder = new File(uploadDir);
                if (!uploadFolder.exists()) {
                    uploadFolder.mkdirs();
                }

                for (MultipartFile file : imageFiles) {
                    // Generate unique filename
                    String uniqueFileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
                    File destinationFile = new File(uploadDir + uniqueFileName);

                    // Save file to the directory
                    file.transferTo(destinationFile);

                    // Append filename to the images column
                    if (imageNames.length() > 0) {
                        imageNames.append(",");
                    }
                    imageNames.append(uniqueFileName);
                }

                // Store image names in the Ad entity
                savedAd.setImages(imageNames.toString());
                adRepository.save(savedAd);
            }

            return VarList.Created;
        } catch (Exception e) {
            throw new RuntimeException("Error while saving ad: " + e.getMessage(), e);
        }
    }

}
