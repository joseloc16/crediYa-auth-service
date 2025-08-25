package co.com.bancolombia.api.mapper;

import co.com.bancolombia.api.dto.CreateUserDTO;
import co.com.bancolombia.api.dto.EditUserDTO;
import co.com.bancolombia.api.dto.UserDTO;
import co.com.bancolombia.model.user.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserDTOMapper {
    UserDTO toResponse(User user);
    List<UserDTO> toResponseList(List<User> users);
    User toModel(CreateUserDTO createUserDTO);
    User toModel(EditUserDTO editUserDTO);
}
