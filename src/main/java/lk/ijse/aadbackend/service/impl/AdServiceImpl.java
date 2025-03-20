package lk.ijse.aadbackend.service.impl;

import lk.ijse.aadbackend.dto.AdDTO;
import lk.ijse.aadbackend.entity.Ad;
import lk.ijse.aadbackend.repo.AdRepository;
import lk.ijse.aadbackend.service.AdService;
import lk.ijse.aadbackend.util.VarList;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AdServiceImpl implements AdService {

    @Autowired
    private AdRepository adRepository;

    @Autowired
    private ModelMapper modelMapper;


    @Override
    public int createAd(AdDTO adDTO) {
        adRepository.save(modelMapper.map(adDTO, Ad.class));
        return VarList.Created;
    }
}
