package lk.ijse.aadbackend.service.impl;

import lk.ijse.aadbackend.dto.LocationDTO;
import lk.ijse.aadbackend.entity.Location;
import lk.ijse.aadbackend.repo.LocationRepository;
import lk.ijse.aadbackend.service.LocationService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class LocationServiceImpl implements LocationService {

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<LocationDTO> getLocationsByParentId(UUID parentLocationId) {
        List<Location> locations = locationRepository.findByParentLocationId(parentLocationId);
        return locations.stream()
                .map(location -> modelMapper.map(location, LocationDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<LocationDTO> getLocationsExcludingParent(UUID parentId) {
        List<Location> locations = locationRepository.findByParentLocationIdNot(parentId);

        if (locations.isEmpty()) {
            try {
                throw new ResourceNotFoundException("No locations found excluding parentId: " + parentId);
            } catch (ResourceNotFoundException e) {
                throw new RuntimeException(e);
            }
        }

        return locations.stream()
                .map(location -> modelMapper.map(location, LocationDTO.class))
                .collect(Collectors.toList());
    }

    private class ResourceNotFoundException extends Throwable {
        public ResourceNotFoundException(String s) {
        }
    }
}
