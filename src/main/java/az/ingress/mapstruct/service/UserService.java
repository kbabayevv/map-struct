package az.ingress.mapstruct.service;

import az.ingress.mapstruct.dto.request.UserRequest;
import az.ingress.mapstruct.dto.response.UserResponse;
import az.ingress.mapstruct.error.UserNotFound;
import az.ingress.mapstruct.mapper.UserMapper;
import az.ingress.mapstruct.model.User;
import az.ingress.mapstruct.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;

    public UserResponse create(UserRequest request) {
        log.info("Start my create method");
        User user = UserMapper.INSTANCE.mapUserRequestToUserEntity(request);
        User saved = userRepository.save(user);
        log.info("My create method is ended");
        return UserMapper.INSTANCE.mapEntityToUserResponse(saved);
    }

    public UserResponse getById(Long id) {
        log.info("getById method is start");
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFound("User not found with id: " + id));
        log.info("getById method is ended");
        return UserMapper.INSTANCE.mapEntityToUserResponse(user);
    }

    public List<UserResponse> getAllUsers() {
        log.info("My getAllUsers method start");
        List<User> all = userRepository.findAll();
        log.info("My getAllUsers method is ended");
        return UserMapper.INSTANCE.mapEntityListToUserResponseList(all);
    }

    public void deleteUserById(Long id) {
        log.info("My deleteUserById method start");
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFound("User not found with id: " + id));
        userRepository.deleteById(id);
        log.info("My deleteUserById method is ended");
    }

    public UserResponse updateUser(Long id, UserRequest request) {
        log.info("My updateUser method start");
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFound("User not found with id: " + id));
        user.setName(request.getName());
        user.setSurname(request.getSurname());
        user.setAge(request.getAge());
        User saved = userRepository.save(user);
        log.info("My updateUser method is ended");
        return UserMapper.INSTANCE.mapEntityToUserResponse(saved);
    }
}
