package co.com.bancolombia.api.mapper;

import co.com.bancolombia.api.dto.LogInRequest;
import co.com.bancolombia.api.dto.SignUpRequest;
import co.com.bancolombia.api.dto.UserResponse;
import co.com.bancolombia.model.dto.Credentials;
import co.com.bancolombia.model.dto.SignUpCommand;
import co.com.bancolombia.model.user.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserEntryMapper {

    SignUpCommand toCommand(SignUpRequest request);

    Credentials toCredentials(LogInRequest request);

    UserResponse toResponse(User user);
}
