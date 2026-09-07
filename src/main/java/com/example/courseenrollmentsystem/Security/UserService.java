package com.example.courseenrollmentsystem.Security;

import com.example.courseenrollmentsystem.DTO.UserDto.CreateUserDto;
import com.example.courseenrollmentsystem.Entity.User;
import com.example.courseenrollmentsystem.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public String registerUser(CreateUserDto createUserDto) throws UsernameNotFoundException {

        Boolean isExists = userRepository.
                existsByusername(
                createUserDto.getUsername());
        if (!isExists) {
            throw new UsernameNotFoundException("User Already Registered");
        }
        User user = modelMapper.map(createUserDto, User.class);
        User user1 = userRepository.save(user);
        return "Registration Successful";
    }

    @Override
    @NonNull
    public UserDetails loadUserByUsername(@NonNull String username) throws UsernameNotFoundException {
        User user = userRepository.findByusername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        return  new UserPrinciples(user);
    }
}
