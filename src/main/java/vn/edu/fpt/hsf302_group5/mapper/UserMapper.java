package vn.edu.fpt.hsf302_group5.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import vn.edu.fpt.hsf302_group5.dto.user.UserRequertDTO;
import vn.edu.fpt.hsf302_group5.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(source = "password", target = "passwordHash")
    User toUserEntity(UserRequertDTO userRequertDTO);

    @Mapping(source = "passwordHash", target = "password")
    UserRequertDTO toUserDTO(User user);
}
