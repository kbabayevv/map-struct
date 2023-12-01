package az.ingress.mapstruct.service;

import az.ingress.mapstruct.dto.request.UserRequest;
import az.ingress.mapstruct.dto.response.UserResponse;
import az.ingress.mapstruct.error.UserNotFound;
import az.ingress.mapstruct.mapper.UserMapper;
import az.ingress.mapstruct.model.User;
import az.ingress.mapstruct.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;


    private User mockUser;

    @BeforeEach
    void setUp() {
        mockUser = User.builder()
                .id(1L)
                .name("Kamran")
                .surname("Babayev")
                .age(24)
                .build();

    }

    @Test
    void givenValidRequestWhenCreateUserThenSuccess() {
        //Arrange
        UserRequest userRequest = UserRequest.builder()
                .name("Ali")
                .surname("Valiyev")
                .age(26)
                .build();
        User userEntity = UserMapper.INSTANCE.mapUserRequestToUserEntity(userRequest);
        User saved = User.builder()
                .id(2L)
                .name("Ali")
                .surname("Valiyev")
                .age(26)
                .build();
        when(userRepository.save(any())).thenReturn(saved);

        //Act
        UserResponse response = userService.create(userRequest);

        //Assert
        assertThat(response.getName()).isEqualTo(userRequest.getName());
        assertThat(response.getSurname()).isEqualTo(userRequest.getSurname());
        assertThat(response.getAge()).isEqualTo(userRequest.getAge());
        var captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository, times(1)).save(captor.capture());

        assertThat(captor.getValue().getId()).isNull();
        assertThat(captor.getValue().getName()).isEqualTo("Ali");
        assertThat(captor.getValue().getSurname()).isEqualTo("Valiyev");
        assertThat(captor.getValue().getAge()).isEqualTo(26);
    }

    @Test
    void givenValidIdWhenGetUserThenSuccess() {
        //Arrange
        Long id = 1L;
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(mockUser));
        UserResponse userResponse = UserMapper.INSTANCE.mapEntityToUserResponse(mockUser);

        //Act
        UserResponse response = userService.getById(id);

        //Assert
        assertThat(response.getName()).isEqualTo("Kamran");
        assertThat(response.getSurname()).isEqualTo("Babayev");
        assertThat(response.getAge()).isEqualTo(24);
        verify(userRepository, times(1)).findById(id);
    }

    @Test
    void givenInvalidWhenGetUserThenNotFound() {
        //Arrange
        long id = 1L;
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        //Act & assert
        assertThatThrownBy(() -> userService.getById(id))
                .isInstanceOf(UserNotFound.class)
                .hasMessage("User not found with id: " + id);
    }

    @Test
    void givenValidUsersWhenGetAllUsersThenSuccess() {
        // Arrange
        List<User> users = new ArrayList<>();
        users.add(User.builder().id(1L).name("Araz").surname("Mammadov").age(25).build());
        users.add(User.builder().id(2L).name("Anar").surname("Eliyev").age(26).build());
        List<UserResponse> userResponses = UserMapper.INSTANCE.mapEntityListToUserResponseList(users);
        when(userRepository.findAll()).thenReturn(users);

        // Act
        List<UserResponse> allAccounts = userService.getAllUsers();

        // Assert
        assertThat(allAccounts).hasSize(2);
        assertThat(allAccounts).containsExactlyInAnyOrderElementsOf(userResponses);
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void givenNoUsersWhenGetAllUsersThenEmptyList() {
        // Arrange
        List<User> accounts = Collections.emptyList();
        List<UserResponse> collect = Collections.emptyList();
        when(userRepository.findAll()).thenReturn(accounts);

        // Act
        List<UserResponse> allAccounts = userService.getAllUsers();

        // Assert
        assertThat(allAccounts).isEmpty();
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void givenValidUserWhenUpdateUserThenSuccess() {
        // Arrange
        Long id = 1L;
        User accountFromDb = User.builder()
                .id(1L)
                .name("Anar")
                .surname("Zeynalov")
                .age(24)
                .build();
        UserRequest accountRequest = UserRequest.builder()
                .name("Kazim")
                .surname("Ayxanov")
                .age(25)
                .build();
        when(userRepository.findById(id)).thenReturn(Optional.of(accountFromDb));
        accountFromDb.setName(accountRequest.getName());
        accountFromDb.setSurname(accountRequest.getSurname());
        accountFromDb.setAge(accountRequest.getAge());
        when(userRepository.save(any(User.class))).thenReturn(accountFromDb);
        UserResponse userResponse = UserMapper.INSTANCE.mapEntityToUserResponse(accountFromDb);
        // Act
        UserResponse response = userService.updateUser(id, accountRequest);

        // Assert
        assertThat(response.getName()).isEqualTo("Kazim");
        assertThat(response.getSurname()).isEqualTo("Ayxanov");
        assertThat(response.getAge()).isEqualTo(25);
        assertThat(accountFromDb.getId()).isEqualTo(id);
        verify(userRepository, times(1)).findById(id);
        verify(userRepository, times(1)).save(accountFromDb);
    }

    @Test
    void givenInvalidUserWhenUpdateUserThenNotFound() {
        // Arrange
        Long id = 1L;
        User accountFromDb = User.builder()
                .id(1L)
                .name("Anar")
                .surname("Zeynalov")
                .age(24)
                .build();
        UserRequest accountRequest = UserRequest.builder()
                .name("Kazim")
                .surname("Ayxanov")
                .age(25)
                .build();
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        //Act & assert
        assertThatThrownBy(() -> userService.updateUser(id, accountRequest))
                .isInstanceOf(UserNotFound.class)
                .hasMessage("User not found with id: " + id);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void givenValidIdWhenDeleteUserThenSuccess() {
        // Arrange
        Long id = 1L;
        when(userRepository.findById(id)).thenReturn(Optional.of(mockUser));

        // Act
        userService.deleteUserById(id);

        // Assert
        verify(userRepository, times(1)).findById(id);
        verify(userRepository, times(1)).deleteById(id);
    }

    @Test
    void givenInvalidIdWhenDeleteUserThenNotFound() {
        // Arrange
        Long id = 1L;
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        // Act & assert
        assertThatThrownBy(() -> userService.deleteUserById(id))
                .isInstanceOf(UserNotFound.class)
                .hasMessage("User not found with id: " + id);
        verify(userRepository, never()).deleteById(anyLong());
    }
}