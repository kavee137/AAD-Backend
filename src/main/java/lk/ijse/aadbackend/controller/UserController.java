package lk.ijse.aadbackend.controller;

import jakarta.validation.Valid;
import lk.ijse.aadbackend.dto.AuthDTO;
import lk.ijse.aadbackend.dto.ResponseDTO;
import lk.ijse.aadbackend.dto.UserDTO;
import lk.ijse.aadbackend.service.UserService;
import lk.ijse.aadbackend.util.JwtUtil;
import lk.ijse.aadbackend.util.VarList;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin(origins = "http://localhost:63342") // Allow frontend URL
@RestController
@RequestMapping("api/v1/user")
public class UserController {
    private final UserService userService;
    private final JwtUtil jwtUtil;

    //constructor injection
    public UserController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping(value = "/register", consumes = "multipart/form-data")
    public ResponseEntity<ResponseDTO> registerUser(
            @RequestPart @Valid UserDTO userDTO,
            @RequestPart(value = "image", required = false) MultipartFile image) {

        try {

            int res = userService.saveUser(userDTO, image);


            System.out.println("res: " + res);

            switch (res) {
                case VarList.Created -> {

                    System.out.println("In the switch case: " + userDTO);

                    String token = jwtUtil.generateToken(userDTO);
                    AuthDTO authDTO = new AuthDTO();
                    authDTO.setEmail(userDTO.getEmail());
                    authDTO.setToken(token);
                    return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDTO(VarList.Created, "Success", authDTO));
                }
                case VarList.Not_Acceptable -> {
                    return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                            .body(new ResponseDTO(VarList.Not_Acceptable, "Email or Phone Number is Already Used", null));
                }
                default -> {
                    return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                            .body(new ResponseDTO(VarList.Bad_Gateway, "Error", null));
                }
            }
        } catch (Exception e) {
            System.out.println("In the catch case: " + userDTO);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(VarList.Internal_Server_Error, e.getMessage(), null));
        }
    }




}
