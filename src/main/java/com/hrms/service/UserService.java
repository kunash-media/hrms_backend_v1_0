package com.hrms.service;

import com.hrms.dto.request.UserPatchDTO;
import com.hrms.dto.request.UserRegistrationDTO;
import com.hrms.dto.response.UserResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {

    UserResponseDTO registerUser(UserRegistrationDTO dto);

    UserResponseDTO getUserById(Long userId);

    Page<UserResponseDTO> getAllUsers(Pageable pageable);

    UserResponseDTO patchUser(Long userId, UserPatchDTO dto);

    void deleteUser(Long userId);

    UserResponseDTO registerGoogleUser(String name, String email);

}