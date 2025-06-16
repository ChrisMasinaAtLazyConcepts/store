package com.example.store.mapper;

import com.example.store.dto.UserDTO;
import com.example.store.entity.User;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @BeanMapping(ignoreByDefault = true)
    @Mappings({
        @Mapping(target = "username", source = "username"),
        @Mapping(target = "email", source = "email"),
        @Mapping(target = "password", ignore = true)
    })
    UserDTO userToUserDTO(User user);

    @Mappings({@Mapping(target = "role", ignore = true)})
    User userDTOToUser(UserDTO userDTO);

    List<UserDTO> usersToUserDTOs(List<User> users);
}
