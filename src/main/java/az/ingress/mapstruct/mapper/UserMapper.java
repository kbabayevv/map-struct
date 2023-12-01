package az.ingress.mapstruct.mapper;

import az.ingress.mapstruct.dto.request.UserRequest;
import az.ingress.mapstruct.dto.response.UserResponse;
import az.ingress.mapstruct.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public abstract class UserMapper {

    public final static UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    public abstract User mapUserRequestToUserEntity(UserRequest request);

    public abstract UserResponse mapEntityToUserResponse(User user);

    public abstract List<UserResponse> mapEntityListToUserResponseList(List<User> userList);
}
