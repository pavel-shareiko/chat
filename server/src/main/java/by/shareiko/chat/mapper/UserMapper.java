package by.shareiko.chat.mapper;

import by.shareiko.chat.domain.User;
import by.shareiko.chat.domain.dto.LoginUser;
import by.shareiko.chat.domain.dto.RegisterUser;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    RegisterUser userToRegisterUser(User user);
    User registerUserToUser(RegisterUser registerUser);

    LoginUser userToLoginUser(User user);
    User loginUserToUser(LoginUser loginUser);
}
