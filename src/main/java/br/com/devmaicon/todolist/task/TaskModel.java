package br.com.devmaicon.todolist.task;

import java.time.LocalDateTime;
import java.util.UUID;


import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;



import lombok.Data;

@Data
@Entity(name = "tb_tasks")
public class TaskModel {

    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;

    @Column(nullable = false, length = 50)
    @NotBlank(message = "Título não pode ser vazio")
    @Size(min = 4, max = 50, message = "Título deve ter entre 4 e 50 caracteres")
    private String title;

    private String description;

    @NotNull(message = "Data de início não pode ser vazia")
    private LocalDateTime startAt;

    @NotNull(message = "Data final não pode ser vazia")
    private LocalDateTime endAt;

    @Column(nullable = false, length = 15)
    @NotBlank(message = "Prioridade não pode ser vazio")
    @Size(min = 4, max = 15, message = "Prioridade deve ter entre 4 e 15 caracteres")
    private String priority;

    @CreationTimestamp
    private LocalDateTime createdAt;

    private UUID idUser;

    // public void setTitle(String title) throws Exception {

    // if (title == null || title.isEmpty())
    // throw new IllegalArgumentException("Título não pode ser vazio");

    // if (title.length() > 50)
    // throw new IllegalArgumentException("Título deve conter máximo de 50
    // caracteres");

    // this.title = title;

    // }

}