package lk.ijse.aadbackend.service.impl;

import lk.ijse.aadbackend.dto.AdDTO;
import lk.ijse.aadbackend.entity.Ad;
import lk.ijse.aadbackend.entity.AdImage;
import lk.ijse.aadbackend.entity.Category;
import lk.ijse.aadbackend.entity.Location;
import lk.ijse.aadbackend.entity.User;
import lk.ijse.aadbackend.repo.*;
import lk.ijse.aadbackend.service.AdService;
import lk.ijse.aadbackend.util.VarList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class AdServiceImpl implements AdService {

    @Autowired
    private AdRepository adRepository;

    @Autowired
    private AdImageRepository adImageRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Override
    public int createAd(AdDTO adDTO) {
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

            // Save Ad first to get the generated ID
            Ad savedAd = adRepository.save(ad);

            // Save AdImages if URLs are provided
            if (adDTO.getImageUrls() != null && !adDTO.getImageUrls().isEmpty()) {
                List<AdImage> adImages = adDTO.getImageUrls().stream().map(url -> {
                    AdImage adImage = new AdImage();
                    adImage.setImageUrl(url);
                    adImage.setAd(savedAd);
                    return adImage;
                }).collect(Collectors.toList());

                adImageRepository.saveAll(adImages);
            }

            return VarList.Created;
        } catch (Exception e) {
            throw new RuntimeException("Error while saving ad: " + e.getMessage(), e);
        }
    }
}
