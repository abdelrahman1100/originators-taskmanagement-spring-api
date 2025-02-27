package com.example.to_do_list.dto;


import lombok.Data;

@Data
public class TodoDto {

    private String task;

    private String description;

    private Enum status;

}
