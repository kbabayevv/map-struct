package az.ingress.mapstruct.mapper;

import az.ingress.mapstruct.dto.request.UserRequest;
import az.ingress.mapstruct.dto.response.UserResponse;
import az.ingress.mapstruct.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class UserMapperTest {

    @Test
    void toEntityTest() {
        //Arrange
        var request = new UserRequest();
        request.setName("Kamran");
        request.setSurname("Babayev");
        request.setAge(24);

        var expected = new User();
        expected.setName("Kamran");
        expected.setSurname("Babayev");
        expected.setAge(24);

        //Act
        var actual = UserMapper.INSTANCE.mapUserRequestToUserEntity(request);

        //Assert
        assertThat(actual.getId()).isEqualTo(expected.getId());
        assertThat(actual.getName()).isEqualTo(expected.getName());
        assertThat(actual.getSurname()).isEqualTo(expected.getSurname());
        assertThat(actual.getAge()).isEqualTo(expected.getAge());
    }

    @Test
    void toResponseTest() {
        //Arrange
        var request = new User();
        request.setId(1L);
        request.setName("Kamran");
        request.setSurname("Babayev");
        request.setAge(24);

        var expected = new UserResponse();
        expected.setName("Kamran");
        expected.setSurname("Babayev");
        expected.setAge(24);

        //Act
        var actual = UserMapper.INSTANCE.mapEntityToUserResponse(request);

        //Assert
        assertThat(actual.getName()).isEqualTo(expected.getName());
        assertThat(actual.getSurname()).isEqualTo(expected.getSurname());
        assertThat(actual.getAge()).isEqualTo(expected.getAge());
    }

    @Test
    void toResponseListTest() {
        //Arrange
        List<User> users = new ArrayList<>();
        var request = new User();
        request.setId(1L);
        request.setName("Kamran");
        request.setSurname("Babayev");
        request.setAge(24);
        users.add(request);

        List<UserResponse> userResponses = new ArrayList<>();
        var expected = new UserResponse();
        expected.setName("Kamran");
        expected.setSurname("Babayev");
        expected.setAge(24);
        userResponses.add(expected);

        //Act
        var actual = UserMapper.INSTANCE.mapEntityListToUserResponseList(users);

        //Assert
        assertThat(actual.size()).isEqualTo(userResponses.size());
        assertThat(actual.get(0).getName()).isEqualTo(userResponses.get(0).getName());
        assertThat(actual.get(0).getSurname()).isEqualTo(userResponses.get(0).getSurname());
        assertThat(actual.get(0).getAge()).isEqualTo(userResponses.get(0).getAge());
        assertThat(actual).containsExactlyInAnyOrderElementsOf(userResponses);
    }
}