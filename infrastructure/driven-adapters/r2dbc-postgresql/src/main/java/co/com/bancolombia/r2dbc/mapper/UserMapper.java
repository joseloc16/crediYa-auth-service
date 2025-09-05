package co.com.bancolombia.r2dbc.mapper;

import co.com.bancolombia.model.user.User;
import co.com.bancolombia.r2dbc.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    @Mapping(target = "roles",
        expression = "java(e.getRoles()==null ? java.util.Set.of() : new java.util.HashSet<>(e.getRoles()))")
    User toDomain(UserEntity e);

    @Mapping(target = "passwordHash", source = "passwordHash")
    @Mapping(target = "roles",
        expression = "java(u.roles()==null ? java.util.Set.of() : new java.util.LinkedHashSet<>(u.roles()))")
    UserEntity toEntity(User u);
}
