package co.com.bancolombia.model.user;
import lombok.*;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class User {
    private String idUsuario;
    private String firstName;
    private String lastName;
    @With private String email;
    private String document;
    private String phoneNumber;
    private String roleId;
    private BigDecimal baseSalary;
    private Timestamp birthDate;
}
