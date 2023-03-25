package by.shareiko.chat.mapper;

import by.shareiko.chat.domain.User;
import by.shareiko.chat.dto.SimpleUserDTO;
import by.shareiko.chat.security.user.LoginUser;
import by.shareiko.chat.security.user.RegisterUser;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    RegisterUser userToRegisterUser(User user);
    User registerUserToUser(RegisterUser registerUser);

    LoginUser userToLoginUser(User user);
    User loginUserToUser(LoginUser loginUser);
}
