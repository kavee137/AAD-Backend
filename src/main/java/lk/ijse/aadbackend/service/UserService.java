package lk.ijse.aadbackend.service;


import lk.ijse.aadbackend.dto.UserDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


public interface UserService {
    int saveUser(UserDTO userDTO, MultipartFile image) throws IOException;
    UserDTO searchUser(UserDTO userDTO);
}