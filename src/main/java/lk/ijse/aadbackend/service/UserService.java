package lk.ijse.aadbackend.service;


import lk.ijse.aadbackend.dto.UserDTO;


public interface UserService {
    int saveUser(UserDTO userDTO);
    UserDTO searchUser(UserDTO userDTO);
}