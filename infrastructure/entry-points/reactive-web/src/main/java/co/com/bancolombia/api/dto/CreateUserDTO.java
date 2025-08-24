package co.com.bancolombia.api.dto;

public record CreateUserDTO (
    String firstName,
    String lastName,
    String email,
    String identityDocument,
    String  phoneNumber){
}

