package org.EndiJean.rest.controller;

import org.EndiJean.entity.Usuario;
import org.EndiJean.exception.SenhaInvalidaException;
import org.EndiJean.rest.dto.CredenciaisDTO;
import org.EndiJean.rest.dto.TokenDTO;
import org.EndiJean.security.jwt.JwtService;
import org.EndiJean.service.impl.UsuarioServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

@RestController
@RequestMapping("api/usuarios")
public class UsuarioController {

    private UsuarioServiceImpl usuarioService;

    private PasswordEncoder passwordEncoder;

    private JwtService jwtService;

    public UsuarioController(UsuarioServiceImpl usuarioService, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.usuarioService = usuarioService;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    public Usuario salvar(@RequestBody @Valid Usuario usuario){
        String senhaCripitografada = passwordEncoder.encode(usuario.getSenha());
        usuario.setSenha(senhaCripitografada);
        return usuarioService.salvar(usuario);
    }

    @PostMapping("/auth")
    public TokenDTO autenticar(@RequestBody CredenciaisDTO credenciais){
        try{
            Usuario usuario = Usuario.builder()
                    .login(credenciais.getLogin())
                    .senha(credenciais.getSenha()).build();
            UserDetails usuarioAutenticado = usuarioService.autenticar(usuario);
            String token = jwtService.gerarToken(usuario);
            return new TokenDTO(usuario.getLogin(), token);
        } catch (UsernameNotFoundException | SenhaInvalidaException e ){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }
}
