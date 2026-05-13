
package testLogin.agendamento.demo.controller;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import testLogin.agendamento.demo.DTO.LoginRequest;
import testLogin.agendamento.demo.DTO.LoginResponse;
import testLogin.agendamento.demo.DTO.RegisterRequest;
import testLogin.agendamento.demo.domain.User;
import testLogin.agendamento.demo.security.JwtService;
import testLogin.agendamento.demo.service.UserService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public AuthController
            (
                    UserService userService,
                    AuthenticationManager authenticationManager,
                    JwtService jwtService,
                    BCryptPasswordEncoder bCryptPasswordEncoder
            ) {

        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }


    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest loginRequest) {

        // verifica username e senha
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        // busca o User no BD
        User user = (User)  userService.loadUserByUsername(loginRequest.getUsername());

        // gera o token e devolve
        String token = jwtService.generateToken(user);
        return  new LoginResponse(token, user.getRole());
    }

    @PostMapping("/register")
    public void register(@RequestBody RegisterRequest registerRequest) {


        // cria  usuário
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setPassword(bCryptPasswordEncoder.encode(registerRequest.getPassword())); // criptografa
        user.setRole(registerRequest.getRole());

        // salva no Bd
        userService.save(user);

    }

}
