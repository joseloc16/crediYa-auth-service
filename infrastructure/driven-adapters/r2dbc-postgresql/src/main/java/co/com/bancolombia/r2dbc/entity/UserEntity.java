package co.com.bancolombia.r2dbc.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Set;

@Table("Usuario")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserEntity {

    @Id @Column("id_usuario") private String userId;

    @Column("nombre") private String name;
    @Column("apellido") private String lastName;
    @Column("email") private String email;
    @Column("documento_identidad") private String document;
    @Column("telefono") private String phoneNumber;
    @Column("salario_base") private BigDecimal baseSalary;
    @Column("fecha_nacimiento") private Timestamp birthDate;
    @Column("password_hash") private String passwordHash;
    @Column("roles") private Set<String> roles;
}
