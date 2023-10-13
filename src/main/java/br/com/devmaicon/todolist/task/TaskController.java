package br.com.devmaicon.todolist.task;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.devmaicon.todolist.utils.Utils;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private ITaskRepository taskRepository;

    @PostMapping("/")
    public ResponseEntity<Object> create(@Valid @RequestBody TaskModel taskModel, HttpServletRequest request) {

        try {
            var idUser = request.getAttribute("idUser");
            taskModel.setIdUser((UUID) idUser);

            var currentDate = LocalDateTime.now();

            if (currentDate.isAfter(taskModel.getStartAt()))
                throw new Exception("A data e hora atual nao pode ser menor que a data e hora de ínicio");

            if (currentDate.isAfter(taskModel.getEndAt()))
                throw new Exception("A data e hora atual nao pode ser maior que a data e hora de término");

            if (taskModel.getStartAt().isAfter(taskModel.getEndAt()))
                throw new Exception("A data e hora de ínicio nao pode ser maior que a data e hora de término");

            var task = this.taskRepository.save(taskModel);
            return ResponseEntity.status(HttpStatus.OK).body(task);

        } catch (Exception e) {

            System.err.println(e);
            var errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);

        }
    }

    @GetMapping("/")
    public List<TaskModel> list(HttpServletRequest request) {
        var idUser = (UUID) request.getAttribute("idUser");
        var tasks = this.taskRepository.findByIdUser(idUser);

        return tasks;

    }

    @PutMapping("/{id}")
    public ResponseEntity update(@RequestBody TaskModel taskModel, HttpServletRequest request, @PathVariable UUID id) {
        try {

            var taskCurrent = this.taskRepository.findById(id);

            if (!taskCurrent.isPresent()) {
                throw new Exception("Tarefa com id informado não encontrada");
            }

            var taskModelCurrent = taskCurrent.get();
            Utils.copyNonNullProperties(taskModel, taskModelCurrent);

            var idUser = request.getAttribute("idUser");

            if (!idUser.equals(taskModelCurrent.getIdUser())) {
                throw new Exception("Tarefa não pertence ao usuário logado");
            }

            var currentDate = LocalDateTime.now();

            if (currentDate.isAfter(taskModelCurrent.getStartAt()))
                throw new Exception("A data e hora atual nao pode ser menor que a data e hora de ínicio");

            if (currentDate.isAfter(taskModelCurrent.getEndAt()))
                throw new Exception("A data e hora atual nao pode ser maior que a data e hora de término");

            if (taskModelCurrent.getStartAt().isAfter(taskModelCurrent.getEndAt()))
                throw new Exception("A data e hora de ínicio nao pode ser maior que a data e hora de término");

            var task = this.taskRepository.save(taskModelCurrent);

            return ResponseEntity.status(HttpStatus.OK).body(task);

        } catch (Exception e) {
            var errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);

        }
    }
}
