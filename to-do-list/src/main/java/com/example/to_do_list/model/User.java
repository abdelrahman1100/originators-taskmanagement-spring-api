package com.example.to_do_list.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class User {

    @Id
    private Integre id;

    private String user_name;

    private String password;

}
