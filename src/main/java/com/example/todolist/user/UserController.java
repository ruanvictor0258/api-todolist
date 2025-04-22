package com.example.todolist.user;


import at.favre.lib.crypto.bcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired // Para ele gerenciar;
    private UserRepository userRepository; // Chamando a interface



    @PostMapping("/")
    public ResponseEntity creat(@RequestBody UserModel usuario){ // O uso do @RequestBody serve pro spring entender que esses dados vem do corpo da anotaçao
        var user =this.userRepository.findByEmail(usuario.getEmail());
        if(user != null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuario ja existe");
        }

        var senhaCrip= BCrypt.withDefaults().hashToString(12, usuario.getPassword().toCharArray()); // Criptografando a senha do usuario;
        usuario.setPassword(senhaCrip); //Alterando o valor da senha do usuario para a senha criptografada;

        var userCriado =  this.userRepository.save(usuario);
          return ResponseEntity.status(HttpStatus.CREATED).body(userCriado);

    }
}