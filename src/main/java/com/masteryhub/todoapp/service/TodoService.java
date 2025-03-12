package com.masteryhub.todoapp.service;

import com.masteryhub.todoapp.dto.TodoDto;
import com.masteryhub.todoapp.models.Status;
import com.masteryhub.todoapp.models.TodoEntity;
import com.masteryhub.todoapp.models.UserEntity;
import com.masteryhub.todoapp.repository.UserRepository;
import com.masteryhub.todoapp.security.JwtGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TodoService {

    private final JwtGenerator jwtGenerator;
    private final UserRepository userRepository;

    @Autowired
    public TodoService(JwtGenerator jwtGenerator, UserRepository userRepository) {
        this.jwtGenerator = jwtGenerator;
        this.userRepository = userRepository;
    }

    public ResponseEntity<?> getTodos(String token, int page, int size) {
        token = token.substring(7);
        String username = jwtGenerator.getUsernameFromJWT(token);
        Optional<UserEntity> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            return ResponseEntity.badRequest().body("User not found");
        }
        List<TodoEntity> todos = user.get().getTodolist();
        if (todos == null || todos.isEmpty()) {
            return new ResponseEntity<>(List.of(), HttpStatus.OK);
        }
        if (page > 0) page--;
        int start = Math.min(page * size, todos.size());
        int end = Math.min(start + size, todos.size());
        List<TodoEntity> paginatedTodos = todos.subList(start, end);
        List<TodoDto> todoDtosList = paginatedTodos.stream()
                .filter(todo -> todo.getDeletedAt() == null)
                .map(TodoDto::new)
                .collect(Collectors.toList());
        return new ResponseEntity<>(todoDtosList, HttpStatus.OK);
    }

    public ResponseEntity<?> createTodo(String token, TodoDto todoDto) {
        token = token.substring(7);
        String username = jwtGenerator.getUsernameFromJWT(token);
        Optional<UserEntity> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            return ResponseEntity.badRequest().body("User not found");
        }
        TodoEntity todo = new TodoEntity();
        todo.setTitle(todoDto.getTitle());
        todo.setDescription(todoDto.getDescription());
        todo.setStatus(todoDto.getStatus());
        todo.setCreatedAt(Instant.now().toString());
        todo.setUpdatedAt(Instant.now().toString());
        if (user.get().getTodolist() == null) {
            todo.setId(1L);
        } else {
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
        token = token.substring(7);
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
                todo.setUpdatedAt(Instant.now().toString());
                userRepository.save(user.get());
                return new ResponseEntity<>("Todo updated successfully", HttpStatus.OK);
            }
        }
        return ResponseEntity.badRequest().body("Todo not found");
    }

    public ResponseEntity<?> deleteTodo(String token, Long id) {
        token = token.substring(7);
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

    public ResponseEntity<?> getTodo(String token, Long id) {
        token = token.substring(7);
        String username = jwtGenerator.getUsernameFromJWT(token);
        Optional<UserEntity> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            return new ResponseEntity<>("User not found", HttpStatus.BAD_REQUEST);
        }
        List<TodoEntity> todos = user.get().getTodolist();
        for (TodoEntity todo : todos) {
            if (todo.getId().equals(id)) {
                if (todo.getDeletedAt() != null) {
                    return new ResponseEntity<>("Todo not found", HttpStatus.BAD_REQUEST);
                }
                TodoDto todoDto = new TodoDto(todo);
                return new ResponseEntity<>(todoDto, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>("Todo not found", HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<?> deleteAllTodos(String token) {
        token = token.substring(7);
        String username = jwtGenerator.getUsernameFromJWT(token);
        Optional<UserEntity> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            return new ResponseEntity<>("User not found", HttpStatus.BAD_REQUEST);
        }
        user.get().setTodolist(null);
        userRepository.save(user.get());
        return new ResponseEntity<>("All todos deleted successfully", HttpStatus.OK);
    }

    public ResponseEntity<?> getTodosByStatus(String token, String status, int page, int size) {
        token = token.substring(7);
        Status statusEnum;
        try {
            statusEnum = Status.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>("Invalid status", HttpStatus.BAD_REQUEST);
        }
        String username = jwtGenerator.getUsernameFromJWT(token);
        Optional<UserEntity> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            return new ResponseEntity<>("User not found", HttpStatus.BAD_REQUEST);
        }
        List<TodoEntity> todos = user.get().getTodolist();
        if (todos == null || todos.isEmpty()) {
            return new ResponseEntity<>(List.of(), HttpStatus.OK);
        }
        if (page > 0) page--;
        int start = Math.min(page * size, todos.size());
        int end = Math.min(start + size, todos.size());
        List<TodoEntity> paginatedTodos = todos.stream()
                .filter(todo -> todo.getStatus().equals(statusEnum))
                .filter(todo -> todo.getDeletedAt() == null)
                .toList();
        paginatedTodos = paginatedTodos.subList(start, Math.min(end, paginatedTodos.size()));
        List<TodoDto> todoDtosList = paginatedTodos.stream()
                .map(TodoDto::new)
                .collect(Collectors.toList());
        return new ResponseEntity<>(todoDtosList, HttpStatus.OK);
    }

    public ResponseEntity<?> deleteManyTodos(String token, String ids) {
        token = token.substring(7);
        if (ids == null || ids.isEmpty()) {
            return new ResponseEntity<>("No ids provided", HttpStatus.BAD_REQUEST);
        }
        List<Long> idList;
        try {
            idList = Arrays.stream(ids.split(","))
                    .map(String::trim) // Remove spaces
                    .map(Long::parseLong) // Convert to Long
                    .collect(Collectors.toList());
        } catch (NumberFormatException e) {
            return new ResponseEntity<>("Invalid ID format. IDs must be numbers.", HttpStatus.BAD_REQUEST);
        }
        String username = jwtGenerator.getUsernameFromJWT(token);
        Optional<UserEntity> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            return new ResponseEntity<>("User not found", HttpStatus.BAD_REQUEST);
        }
        List<TodoEntity> todos = user.get().getTodolist();
        todos.removeIf(todo -> idList.contains(todo.getId()));
        userRepository.save(user.get());return new ResponseEntity<>("Todos deleted successfully", HttpStatus.OK);}

    public ResponseEntity<?> editManyTodos(String token, List<TodoDto> todoDtoList) {
        token = token.substring(7);
        String username = jwtGenerator.getUsernameFromJWT(token);
        Optional<UserEntity> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            return new ResponseEntity<>("User not found", HttpStatus.BAD_REQUEST);
        }
        List<TodoEntity> todos = user.get().getTodolist();
        for (TodoDto todoDto : todoDtoList) {
            for (TodoEntity todo : todos) {
                if (todo.getId().equals(todoDto.getId())) {
                    todo.setTitle(todoDto.getTitle());
                    todo.setDescription(todoDto.getDescription());
                    todo.setStatus(todoDto.getStatus());
                    todo.setUpdatedAt(Instant.now().toString());
                }
            }
        }
        userRepository.save(user.get());
        return new ResponseEntity<>("Todos updated successfully", HttpStatus.OK);
    }

    public ResponseEntity<?> softDeleteTodo(String token, Long id) {
        token = token.substring(7);
        String username = jwtGenerator.getUsernameFromJWT(token);
        Optional<UserEntity> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            return new ResponseEntity<>("User not found", HttpStatus.BAD_REQUEST);
        }
        List<TodoEntity> todos = user.get().getTodolist();
        for (TodoEntity todo : todos) {
            if (todo.getId().equals(id)) {
                todo.setDeletedAt(Instant.now().toString());
                userRepository.save(user.get());
                return new ResponseEntity<>("Todo soft deleted successfully", HttpStatus.OK);
            }
        }
        return new ResponseEntity<>("Todo not found", HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<?> softDeleteAllTodo(String token) {
        token = token.substring(7);
        String username = jwtGenerator.getUsernameFromJWT(token);
        Optional<UserEntity> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            return new ResponseEntity<>("User not found", HttpStatus.BAD_REQUEST);
        }
        List<TodoEntity> todos = user.get().getTodolist();
        for (TodoEntity todo : todos) {
            todo.setDeletedAt(Instant.now().toString());
        }
        userRepository.save(user.get());
        return new ResponseEntity<>("All todos soft deleted successfully", HttpStatus.OK);
    }

    public ResponseEntity<?> softDeleteManyTodo(String token, String ids) {
        token = token.substring(7);
        String username = jwtGenerator.getUsernameFromJWT(token);
        Optional<UserEntity> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            return new ResponseEntity<>("User not found", HttpStatus.BAD_REQUEST);
        }
        List<Long> idList;
        try {
            idList = Arrays.stream(ids.split(","))
                    .map(String::trim) // Remove spaces
                    .map(Long::parseLong) // Convert to Long
                    .collect(Collectors.toList());
        } catch (NumberFormatException e) {
            return new ResponseEntity<>("Invalid ID format. IDs must be numbers.", HttpStatus.BAD_REQUEST);
        }
        List<TodoEntity> todos = user.get().getTodolist();
        for (TodoEntity todo : todos) {
            if (idList.contains(todo.getId())) {
                todo.setDeletedAt(Instant.now().toString());
            }
        }
        userRepository.save(user.get());
        return new ResponseEntity<>("All todos soft deleted successfully", HttpStatus.OK);
    }

    public ResponseEntity<?> restoreTodo(String token, Long id) {
        token = token.substring(7);
        String username = jwtGenerator.getUsernameFromJWT(token);
        Optional<UserEntity> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            return new ResponseEntity<>("User not found", HttpStatus.BAD_REQUEST);
        }
        List<TodoEntity> todos = user.get().getTodolist();
        for (TodoEntity todo : todos) {
            if (todo.getId().equals(id)) {
                todo.setDeletedAt(null);
                userRepository.save(user.get());
                return new ResponseEntity<>("Todo restored successfully", HttpStatus.OK);
            }
        }
        return new ResponseEntity<>("Todo not found", HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<?> restoreAllTodo(String token) {
        token = token.substring(7);
        String username = jwtGenerator.getUsernameFromJWT(token);
        Optional<UserEntity> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            return new ResponseEntity<>("User not found", HttpStatus.BAD_REQUEST);
        }
        List<TodoEntity> todos = user.get().getTodolist();
        for (TodoEntity todo : todos) {
            todo.setDeletedAt(null);
        }
        userRepository.save(user.get());
        return new ResponseEntity<>("All todos restored successfully", HttpStatus.OK);
    }

    public ResponseEntity<?> restoreManyTodo(String token, String ids) {
        token = token.substring(7);
        String username = jwtGenerator.getUsernameFromJWT(token);
        Optional<UserEntity> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            return new ResponseEntity<>("User not found", HttpStatus.BAD_REQUEST);
        }
        List<Long> idList;
        try {
            idList = Arrays.stream(ids.split(","))
                    .map(String::trim) // Remove spaces
                    .map(Long::parseLong) // Convert to Long
                    .collect(Collectors.toList());
        } catch (NumberFormatException e) {
            return new ResponseEntity<>("Invalid ID format. IDs must be numbers.", HttpStatus.BAD_REQUEST);
        }
        List<TodoEntity> todos = user.get().getTodolist();
        for (TodoEntity todo : todos) {
            if (idList.contains(todo.getId())) {
                todo.setDeletedAt(null);
            }
        }
        userRepository.save(user.get());
        return new ResponseEntity<>("All todos restored successfully", HttpStatus.OK);
    }

}
