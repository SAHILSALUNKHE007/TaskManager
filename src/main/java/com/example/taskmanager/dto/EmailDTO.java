package com.example.taskmanager.dto;

import com.example.taskmanager.entity.Task;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailDTO {

    private String email;
    private String subject;
    private List<String> taskList;

}
