package by.shareiko.chat.mapper;

import by.shareiko.chat.domain.User;
import by.shareiko.chat.dto.SimpleUserDTO;
import by.shareiko.chat.security.user.LoginUser;
import by.shareiko.chat.security.user.RegisterUser;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface UserMapper {
    RegisterUser userToRegisterUser(User user);

    User registerUserToUser(RegisterUser registerUser);

    LoginUser userToLoginUser(User user);

    User loginUserToUser(LoginUser loginUser);

    User simpleUserDTOToUser(SimpleUserDTO simpleUserDTO);

    SimpleUserDTO userToSimpleUserDTO(User user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    User updateUserFromSimpleUserDTO(SimpleUserDTO simpleUserDTO, @MappingTarget User user);
}
