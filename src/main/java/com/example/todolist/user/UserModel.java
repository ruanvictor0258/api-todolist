package com.example.todolist.user;


import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Data // SERVE PARA CRIAR OS GETS E SETS PARA TODOS OS ATRIBUTOS
@Entity(name="usuarios") // Criando a tabela de usaarios no Banco de Dados;
public class UserModel {

    @Id
    @GeneratedValue(generator = "UUID") // Indica para gerar um valor unico de aleatorio de UUID
    private UUID id;

    @Getter @Setter // coloca o gett e o sett para um atributo especifico.
    private String name;
    @Column(unique=true) // Torna o email unico. (Apenas um usuario pode ter o acesso!)
    private String email;
    private String password;

    @CreationTimestamp // Indica quando aquele dado foi criado
    private LocalDateTime createdAt;


}
