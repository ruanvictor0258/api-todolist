package com.example.todolist.task;


import com.example.todolist.utils.Utils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.config.Task;
import org.springframework.web.bind.annotation.*;
import org.w3c.dom.html.HTMLTableCaptionElement;

import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/tarefas")
public class TaskController {

    @Autowired // Para mostrar que esta sobrescrevendo a Interface ITaskRepository
    private ITaskRepositorio taskRepositorio;

    @PostMapping("/")  // Post para poder criar uma aividade apartir desse metodo;
    public ResponseEntity creatATT(@RequestBody TaskModel atividade, HttpServletRequest request){ // O HttpServeletRequest serve para pegar o que esta vindo da minha requisiçao
        var idUsuario=request.getAttribute("usuarioId");
        atividade.setUsuarioId((UUID) idUsuario);

        var dataAtual= LocalDateTime.now();
        if(dataAtual.isAfter(atividade.getDataCriacao()) || dataAtual.isAfter(atividade.getDataFim())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("A data de inicio /  data de termino  deve ser maior que do que a data atual");}
        if(atividade.getDataCriacao().isAfter(atividade.getDataFim())){
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("A data de inicio não pode ser maior que a data de termino");
        }
        var tarefa=this.taskRepositorio.save(atividade);
          return ResponseEntity.status(HttpStatus.OK).body(atividade);


    }

    @GetMapping("/") // Listar as tarefas
   public List<TaskModel> list(HttpServletRequest request){
      var idUsuario=request.getAttribute("usuarioId");
       var tarefas = this.taskRepositorio.findByUsuarioId((UUID) idUsuario);
       return tarefas;
    }

    @PutMapping("/{id}") // Atualizar as tarefas
    public ResponseEntity update(@RequestBody TaskModel atividade,  @PathVariable UUID id, HttpServletRequest request){
        var task = this.taskRepositorio.findById(id).orElse(null); //  Busca a terefa pelo ID
        if(task == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Tarefa não encontrada.");

        }
        var idUser=request.getAttribute("usuarioId");
        if(!task.getUsuarioId().equals(idUser)){ // Verifica se o usuário dono da tarefa é o mesmo que está autenticado
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Usuario não tem permissão para alterar essa tarefa.");
        }
        Utils.copyNonNullProperties(atividade, task);
        var taskUpdate = taskRepositorio.save(task);
        return ResponseEntity.ok().body(atividade);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable UUID id, HttpServletRequest request){
        var tarefa = this.taskRepositorio.findById(id).orElse(null); // Busca a terefa pelo ID
        if(tarefa == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Tarefa nao encontrada.");
        }
        var idUser=request.getAttribute("usuarioId");
        if(!tarefa.getUsuarioId().equals(idUser)){ // Verifica se o usuário dono da tarefa é o mesmo que está autenticado
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Usuario sem permissão para apagar essa tarefa.");
        }
        this.taskRepositorio.delete(tarefa);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Tarefa deleta com sucesso.");
    }

}
