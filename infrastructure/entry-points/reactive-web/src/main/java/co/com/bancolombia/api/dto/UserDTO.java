package co.com.bancolombia.api.dto;

public record UserDTO (
    String firstName,
    String lastName,
    String email,
    String identityDocument,
    String  phoneNumber){
}
