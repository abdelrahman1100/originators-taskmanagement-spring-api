package com.masteryhub.todoapp.service;

import com.masteryhub.todoapp.dto.RequestTodoDto;
import com.masteryhub.todoapp.dto.ResponseTodoDto;
import com.masteryhub.todoapp.models.Status;
import com.masteryhub.todoapp.models.TodoEntity;
import com.masteryhub.todoapp.models.UserEntity;
import com.masteryhub.todoapp.repository.TodoRepository;
import com.masteryhub.todoapp.repository.UserRepository;
import com.masteryhub.todoapp.security.JwtGenerator;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class TodoService {

  private final JwtGenerator jwtGenerator;
  private final UserRepository userRepository;
  private final TodoRepository todoRepository;

  @Autowired
  public TodoService(
      JwtGenerator jwtGenerator, UserRepository userRepository, TodoRepository todoRepository) {
    this.jwtGenerator = jwtGenerator;
    this.userRepository = userRepository;
    this.todoRepository = todoRepository;
  }

  public ResponseEntity<ResponseTodoDto> createTodo(String token, RequestTodoDto requestTodoDto) {
    token = token.substring(7);
    String username = jwtGenerator.getUsernameFromJWT(token);
    Optional<UserEntity> user = userRepository.findByUsername(username);
    TodoEntity todo = new TodoEntity(requestTodoDto);
    long customId = user.get().getTodosIds().size() + 1;
    todo.setCustomId(customId);
    todoRepository.save(todo);
    user.get().addTodoId(todo.getId());
    userRepository.save(user.get());
    return new ResponseEntity<>(ResponseTodoDto.from(todo), HttpStatus.CREATED);
  }

  public ResponseEntity<ResponseTodoDto> getTodo(String token, Long id) {
    String username = jwtGenerator.getUsernameFromJWT(token.substring(7));
    Optional<UserEntity> user = userRepository.findByUsername(username);
    List<TodoEntity> todos = todoRepository.findByCustomId(id);
    if (todos.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    ResponseTodoDto userTodos =
        todos.stream()
            .filter(todo -> user.get().getTodosIds().contains(todo.getId()))
            .findFirst()
            .map(ResponseTodoDto::from)
            .orElse(null);
    return new ResponseEntity<>(userTodos, HttpStatus.OK);
  }

  public ResponseEntity<List<ResponseTodoDto>> getTodos(String token, int page, int size) {
    token = token.substring(7);
    String username = jwtGenerator.getUsernameFromJWT(token);
    Optional<UserEntity> user = userRepository.findByUsername(username);
    if (page > 0) page--;
    List<String> todoIds = user.get().getTodosIds();
    Pageable pageable = PageRequest.of(page, size);
    Page<TodoEntity> todoPage = todoRepository.findAllByIdIn(todoIds, pageable);
    Page<ResponseTodoDto> responsePage = todoPage.map(ResponseTodoDto::from);
    return new ResponseEntity<>(responsePage.getContent(), HttpStatus.OK);
  }

  public ResponseEntity<List<ResponseTodoDto>> getTodosByStatus(
      String token, String status, int page, int size) {
    token = token.substring(7);
    Status statusEnum;
    statusEnum = Status.valueOf(status.toUpperCase());
    String username = jwtGenerator.getUsernameFromJWT(token);
    Optional<UserEntity> user = userRepository.findByUsername(username);
    List<String> todosids = user.get().getTodosIds();
    if (page > 0) page--;
    Pageable pageable = PageRequest.of(page, size);
    Page<TodoEntity> todoPage = todoRepository.findByIdInAndStatus(todosids, statusEnum, pageable);
    return ResponseEntity.ok(todoPage.map(ResponseTodoDto::from).getContent());
  }

  public ResponseEntity<ResponseTodoDto> editTodo(String token, RequestTodoDto requestTodoDto) {
    token = token.substring(7);
    String username = jwtGenerator.getUsernameFromJWT(token);
    Optional<UserEntity> user = userRepository.findByUsername(username);
    List<String> todosids = user.get().getTodosIds();
    TodoEntity todoRes =
        todoRepository.findByCustomId(requestTodoDto.getCustomId()).stream()
            .filter(todo -> todosids.contains(todo.getId()))
            .findFirst()
            .orElse(null);
    if (todoRes == null) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    todoRes.setTitle(requestTodoDto.getTitle());
    todoRes.setDescription(requestTodoDto.getDescription());
    todoRes.setStatus(requestTodoDto.getStatus());
    todoRes.setDueDate(requestTodoDto.getDueDate());
    todoRepository.save(todoRes);
    return new ResponseEntity<>(ResponseTodoDto.from(todoRes), HttpStatus.OK);
  }

  public ResponseEntity<List<ResponseTodoDto>> editManyTodos(
      String token, List<RequestTodoDto> todoDtoList) {
    token = token.substring(7);
    String username = jwtGenerator.getUsernameFromJWT(token);
    Optional<UserEntity> user = userRepository.findByUsername(username);
    List<String> todosids = user.get().getTodosIds();
    List<Long> requestedTodoIds = todoDtoList.stream().map(RequestTodoDto::getCustomId).toList();
    List<TodoEntity> todos =
        todoRepository.findAllByCustomId(requestedTodoIds).stream()
            .filter(todo -> todosids.contains(todo.getId()))
            .toList();
    if (todos.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    for (RequestTodoDto todoDto : todoDtoList) {
      for (TodoEntity todo : todos) {
        if (todo.getCustomId().equals(todoDto.getCustomId())) {
          todo.setTitle(todoDto.getTitle());
          todo.setDescription(todoDto.getDescription());
          todo.setStatus(todoDto.getStatus());
          todo.setDueDate(todoDto.getDueDate());
        }
      }
    }
    todoRepository.saveAll(todos);
    List<ResponseTodoDto> responseDtos = todos.stream().map(ResponseTodoDto::from).toList();

    return ResponseEntity.ok(responseDtos);
  }

  public ResponseEntity<String> hardDeleteTodo(String token, Long id) {
    token = token.substring(7);
    String username = jwtGenerator.getUsernameFromJWT(token);
    Optional<UserEntity> user = userRepository.findByUsername(username);
    List<String> todosids = user.get().getTodosIds();
    TodoEntity todoRes =
        todoRepository.findByCustomId(id).stream()
            .findFirst()
            .filter(todo -> todosids.contains(todo.getId()))
            .orElse(null);
    if (todoRes == null) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    user.get().removeTodoId(todoRes.getId());
    userRepository.save(user.get());
    todoRepository.delete(todoRes);
    return new ResponseEntity<>("Todo deleted successfully", HttpStatus.OK);
  }

  public ResponseEntity<String> hardDeleteManyTodos(String token, String ids) {
    if (ids == null || ids.isEmpty()) {
      return new ResponseEntity<>("No ids provided", HttpStatus.BAD_REQUEST);
    }
    token = token.substring(7);
    String username = jwtGenerator.getUsernameFromJWT(token);
    Optional<UserEntity> user = userRepository.findByUsername(username);
    List<Long> idList =
        Arrays.stream(ids.split(","))
            .map(String::trim)
            .map(Long::parseLong)
            .collect(Collectors.toList());
    List<String> todosids = user.get().getTodosIds();
    List<TodoEntity> todoRes =
        todoRepository.findAllByCustomId(idList).stream()
            .filter(todo -> todosids.contains(todo.getId()))
            .toList();
    if (todoRes.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    for (TodoEntity todo : todoRes) {
      user.get().removeTodoId(todo.getId());
      todoRepository.delete(todo);
    }
    userRepository.save(user.get());
    return new ResponseEntity<>("Todos deleted successfully", HttpStatus.OK);
  }

  public ResponseEntity<String> hardDeleteAllTodos(String token) {
    token = token.substring(7);
    String username = jwtGenerator.getUsernameFromJWT(token);
    Optional<UserEntity> user = userRepository.findByUsername(username);
    List<String> todosids = user.get().getTodosIds();
    List<TodoEntity> todoRes = todoRepository.findAllById(todosids);
    if (todoRes.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    for (TodoEntity todo : todoRes) {
      todoRepository.delete(todo);
    }
    user.get().clearTodosIds();
    userRepository.save(user.get());
    return new ResponseEntity<>("All todos deleted successfully", HttpStatus.OK);
  }

  //    public ResponseEntity<?> softDeleteTodo(String token, Long id) {
  //        token = token.substring(7);
  //        String username = jwtGenerator.getUsernameFromJWT(token);
  //        Optional<UserEntity> user = userRepository.findByUsername(username);
  //        if (user.isEmpty()) {
  //            return new ResponseEntity<>("User not found", HttpStatus.BAD_REQUEST);
  //        }
  //        List<TodoEntity> todos = user.get().getTodoListIds();
  //        for (TodoEntity todo : todos) {
  //            if (todo.getCustomId().equals(id)) {
  //                todo.setDeletedAt(Instant.now().toString());
  //                userRepository.save(user.get());
  //                return new ResponseEntity<>("Todo soft deleted successfully", HttpStatus.OK);
  //            }
  //        }
  //        return new ResponseEntity<>("Todo not found", HttpStatus.BAD_REQUEST);
  //    }
  //
  //    public ResponseEntity<?> softDeleteAllTodo(String token) {
  //        token = token.substring(7);
  //        String username = jwtGenerator.getUsernameFromJWT(token);
  //        Optional<UserEntity> user = userRepository.findByUsername(username);
  //        if (user.isEmpty()) {
  //            return new ResponseEntity<>("User not found", HttpStatus.BAD_REQUEST);
  //        }
  //        List<TodoEntity> todos = user.get().getTodoListIds();
  //        for (TodoEntity todo : todos) {
  //            todo.setDeletedAt(Instant.now().toString());
  //        }
  //        userRepository.save(user.get());
  //        return new ResponseEntity<>("All todos soft deleted successfully", HttpStatus.OK);
  //    }
  //
  //    public ResponseEntity<?> softDeleteManyTodo(String token, String ids) {
  //        token = token.substring(7);
  //        String username = jwtGenerator.getUsernameFromJWT(token);
  //        Optional<UserEntity> user = userRepository.findByUsername(username);
  //        if (user.isEmpty()) {
  //            return new ResponseEntity<>("User not found", HttpStatus.BAD_REQUEST);
  //        }
  //        List<Long> idList;
  //        try {
  //            idList =
  //                    Arrays.stream(ids.split(","))
  //                            .map(String::trim) // Remove spaces
  //                            .map(Long::parseLong) // Convert to Long
  //                            .collect(Collectors.toList());
  //        } catch (NumberFormatException e) {
  //            return new ResponseEntity<>(
  //                    "Invalid ID format. IDs must be numbers.", HttpStatus.BAD_REQUEST);
  //        }
  //        List<TodoEntity> todos = user.get().getTodoListIds();
  //        for (TodoEntity todo : todos) {
  //            if (idList.contains(todo.getCustomId())) {
  //                todo.setDeletedAt(Instant.now().toString());
  //            }
  //        }
  //        userRepository.save(user.get());
  //        return new ResponseEntity<>("All todos soft deleted successfully", HttpStatus.OK);
  //    }
  //
  //    public ResponseEntity<?> restoreTodo(String token, Long id) {
  //        token = token.substring(7);
  //        String username = jwtGenerator.getUsernameFromJWT(token);
  //        Optional<UserEntity> user = userRepository.findByUsername(username);
  //        if (user.isEmpty()) {
  //            return new ResponseEntity<>("User not found", HttpStatus.BAD_REQUEST);
  //        }
  //        List<TodoEntity> todos = user.get().getTodoListIds();
  //        for (TodoEntity todo : todos) {
  //            if (todo.getCustomId().equals(id)) {
  //                todo.setDeletedAt(null);
  //                userRepository.save(user.get());
  //                return new ResponseEntity<>("Todo restored successfully", HttpStatus.OK);
  //            }
  //        }
  //        return new ResponseEntity<>("Todo not found", HttpStatus.BAD_REQUEST);
  //    }
  //
  //    public ResponseEntity<?> restoreAllTodo(String token) {
  //        token = token.substring(7);
  //        String username = jwtGenerator.getUsernameFromJWT(token);
  //        Optional<UserEntity> user = userRepository.findByUsername(username);
  //        if (user.isEmpty()) {
  //            return new ResponseEntity<>("User not found", HttpStatus.BAD_REQUEST);
  //        }
  //        List<TodoEntity> todos = user.get().getTodoListIds();
  //        for (TodoEntity todo : todos) {
  //            todo.setDeletedAt(null);
  //        }
  //        userRepository.save(user.get());
  //        return new ResponseEntity<>("All todos restored successfully", HttpStatus.OK);
  //    }
  //
  //    public ResponseEntity<?> restoreManyTodo(String token, String ids) {
  //        token = token.substring(7);
  //        String username = jwtGenerator.getUsernameFromJWT(token);
  //        Optional<UserEntity> user = userRepository.findByUsername(username);
  //        if (user.isEmpty()) {
  //            return new ResponseEntity<>("User not found", HttpStatus.BAD_REQUEST);
  //        }
  //        List<Long> idList;
  //        try {
  //            idList =
  //                    Arrays.stream(ids.split(","))
  //                            .map(String::trim) // Remove spaces
  //                            .map(Long::parseLong) // Convert to Long
  //                            .collect(Collectors.toList());
  //        } catch (NumberFormatException e) {
  //            return new ResponseEntity<>(
  //                    "Invalid ID format. IDs must be numbers.", HttpStatus.BAD_REQUEST);
  //        }
  //        List<TodoEntity> todos = user.get().getTodoListIds();
  //        for (TodoEntity todo : todos) {
  //            if (idList.contains(todo.getCustomId())) {
  //                todo.setDeletedAt(null);
  //            }
  //        }
  //        userRepository.save(user.get());
  //        return new ResponseEntity<>("All todos restored successfully", HttpStatus.OK);
  //    }
}
