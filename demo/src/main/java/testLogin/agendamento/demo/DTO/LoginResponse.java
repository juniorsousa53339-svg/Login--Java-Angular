package testLogin.agendamento.demo.DTO;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import testLogin.agendamento.demo.domain.Role;

@Getter @Setter
@AllArgsConstructor
public class LoginResponse {

    private String token;
    private Role role;

}
 