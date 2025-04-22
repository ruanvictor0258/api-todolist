package com.example.todolist.task;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;


@Data
@Entity(name="Tarefas")
public class TaskModel { // Classe que define os atributos da tarefa;



    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;
    private String descricao;

    @Column(length = 50) //Defininindo o tamanho maximo de caracteres qeue seram usados no titulo;
    private String titulo;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataFim;
    private String prioridade;
    private UUID usuarioId;

    @CreationTimestamp // Para Definir A Data Hora que foi Criado;
    private LocalDateTime dataTarefaCriada;

    public void setTitulo(String titulo) throws Exception {
        if(titulo.length() > 50){
            throw new Exception("O campo titulo deve conter apenas 50 caracteres.");
        }
        this.titulo = titulo;}


}
