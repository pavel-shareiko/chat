package by.shareiko.chat.service;

import by.shareiko.chat.domain.Role;

public interface RoleService {
    Role findByName(String name);
}
