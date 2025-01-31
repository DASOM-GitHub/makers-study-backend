package dmu.dasom.miniproject.service;

import dmu.dasom.miniproject.domain.Users;
import dmu.dasom.miniproject.dto.UserDto;
import dmu.dasom.miniproject.exception.UserNotFoundException;
import dmu.dasom.miniproject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserDto> getAllUser(){
        return userRepository.findAll()
                .stream()
                .map(UserDto::fromEntity)
                .collect(Collectors.toList());
    }

    public UserDto getUserById(Long id){
        return userRepository.findById(id)
                .map(UserDto::fromEntity)
                .orElseThrow(() -> new UserNotFoundException("User not found with id" + id));
    }

    public UserDto getUserByEmail(String email){
        return userRepository.findByEmail(email)
                .map(UserDto::fromEntity)
                .orElseThrow(() -> new UserNotFoundException("User not found with email" + email));
    }

    public boolean isEmailDuplicate(String email){
        return userRepository.existsByEmail(email);
    }

    public UserDto registerUser(UserDto userDto){
        if(isEmailDuplicate(userDto.getUserEmail())){
            // 유저 Email이 중복
            throw new IllegalArgumentException("Email already exists" + userDto.getUserEmail());
        }
        Users user = userDto.toEntity();
        return UserDto.fromEntity(userRepository.save(user));
    }

    public boolean login(UserDto userDto){
        return userRepository.findByEmail(userDto.getUserEmail())
                .map(user -> user.getUserPassword().equals(userDto.getUserPassword()))
                .orElse(false);
    }

}
