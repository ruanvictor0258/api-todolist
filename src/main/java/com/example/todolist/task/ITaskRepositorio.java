package com.example.todolist.task;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;
import java.util.List;

//  JpaRepository É uma interface genérica do Spring Data JPA que fornece métodos prontos para trabalhar com o banco de dados (sem precisar escrever SQL).
// Usada para criar seu repositório com operações como:
// save() – salva ou atualiza um registro
//findById() – busca por ID
//findAll() – lista tudo
//delete() – apaga um registro

public interface ITaskRepositorio extends JpaRepository<TaskModel, UUID> {
   List<TaskModel> findByUsuarioId(UUID idUser);

}
