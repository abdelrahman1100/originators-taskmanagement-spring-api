package com.example.to_do_list.service;

import com.example.to_do_list.dto.TodoDto;
import com.example.to_do_list.models.TodoEntity;
import com.example.to_do_list.models.UserEntity;
import com.example.to_do_list.repository.UserRepository;
import com.example.to_do_list.security.JwtGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TodoService {

    private final JwtGenerator jwtGenerator;
    private final UserRepository userRepository;

    @Autowired
    public TodoService(JwtGenerator jwtGenerator, UserRepository userRepository) {
        this.jwtGenerator = jwtGenerator;
        this.userRepository = userRepository;
    }

    public ResponseEntity<?> getTodos(String token) {
        String username = jwtGenerator.getUsernameFromJWT(token);
        Optional<UserEntity> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            return ResponseEntity.badRequest().body("User not found");
        }
        List<TodoEntity> todos = user.get().getTodolist();
        return new ResponseEntity<>(todos, HttpStatus.OK);
    }

    public ResponseEntity<?> createTodo(String token, TodoDto todoDto) {
        String username = jwtGenerator.getUsernameFromJWT(token);
        Optional<UserEntity> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            return ResponseEntity.badRequest().body("User not found");
        }
        TodoEntity todo = new TodoEntity();
        todo.setTitle(todoDto.getTitle());
        todo.setDescription(todoDto.getDescription());
        todo.setStatus(todoDto.getStatus());
        if (user.get().getTodolist() == null) {
            todo.setId(1L);
        }
        else {
            todo.setId(user.get().getTodolist().size() + 1L);
        }
        if (user.get().getTodolist() == null) {
            user.get().setTodolist(List.of(todo));
        } else {
            user.get().getTodolist().add(todo);
        }
        userRepository.save(user.get());
        return new ResponseEntity<>("Todo created successfully", HttpStatus.OK);
    }

    public ResponseEntity<?> editTodo(String token, TodoDto todoDto) {
        String username = jwtGenerator.getUsernameFromJWT(token);
        Optional<UserEntity> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            return ResponseEntity.badRequest().body("User not found");
        }
        System.out.println(username);
        List<TodoEntity> todos = user.get().getTodolist();
        for (TodoEntity todo : todos) {
            if (todo.getId().equals(todoDto.getId())) {
                todo.setTitle(todoDto.getTitle());
                todo.setDescription(todoDto.getDescription());
                todo.setStatus(todoDto.getStatus());
                userRepository.save(user.get());
                return new ResponseEntity<>("Todo updated successfully", HttpStatus.OK);
            }
        }
        return ResponseEntity.badRequest().body("Todo not found");
    }

    public ResponseEntity<?> deleteTodo(String token, Long id) {
        String username = jwtGenerator.getUsernameFromJWT(token);
        Optional<UserEntity> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            return ResponseEntity.badRequest().body("User not found");
        }
        List<TodoEntity> todos = user.get().getTodolist();
        for (TodoEntity todo : todos) {
            if (todo.getId().equals(id)) {
                todos.remove(todo);
                userRepository.save(user.get());
                return new ResponseEntity<>("Todo deleted successfully", HttpStatus.OK);
            }
        }
        return ResponseEntity.badRequest().body("Todo not found");
    }
}
