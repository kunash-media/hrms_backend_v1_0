package com.hrms.service.serviceImpl;

import com.hrms.config.BcryptEncoderConfig;
import com.hrms.dto.request.UserPatchDTO;
import com.hrms.dto.request.UserRegistrationDTO;
import com.hrms.dto.response.UserResponseDTO;

import com.hrms.entity.UserEntity;

import com.hrms.repository.UserRepository;
import com.hrms.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);


    private final UserRepository userRepository;
    private final BcryptEncoderConfig passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, BcryptEncoderConfig passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public UserResponseDTO registerUser(UserRegistrationDTO dto) {
        return null;
    }

    @Override
    public UserResponseDTO getUserById(Long userId) {
        return null;
    }

    @Override
    public Page<UserResponseDTO> getAllUsers(Pageable pageable) {
        return null;
    }

    @Override
    public UserResponseDTO patchUser(Long userId, UserPatchDTO dto) {
        return null;
    }

    @Override
    public void deleteUser(Long userId) {

    }

    @Override
    public UserResponseDTO registerGoogleUser(String name, String email) {
        return null;
    }
}