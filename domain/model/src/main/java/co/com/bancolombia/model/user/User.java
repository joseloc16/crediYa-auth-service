package co.com.bancolombia.model.user;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class User {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String identityDocument;
    private String phoneNumber;
    private Long roleId;
    private BigDecimal baseSalary;
    //private LocalDate birthDate;
}
