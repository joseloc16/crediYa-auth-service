package co.com.bancolombia.api.dto;

public record EditUserDTO (
    String firstName,
    String lastName,
    String email,
    String identityDocument,
    String  phoneNumber){
}
