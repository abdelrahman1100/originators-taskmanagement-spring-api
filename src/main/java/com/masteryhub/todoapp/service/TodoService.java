package com.masteryhub.todoapp.service;

import com.masteryhub.todoapp.dto.MessageDto;
import com.masteryhub.todoapp.dto.RequestTodoDto;
import com.masteryhub.todoapp.dto.ResponseTodoDto;
import com.masteryhub.todoapp.models.TodoEntity;
import com.masteryhub.todoapp.models.UserEntity;
import com.masteryhub.todoapp.repository.TodoRepository;
import com.masteryhub.todoapp.repository.UserRepository;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class TodoService {

  private final UserRepository userRepository;
  private final TodoRepository todoRepository;

  @Autowired
  public TodoService(UserRepository userRepository, TodoRepository todoRepository) {
    this.userRepository = userRepository;
    this.todoRepository = todoRepository;
  }

  public ResponseEntity<ResponseTodoDto> createTodo(RequestTodoDto requestTodoDto) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String username = authentication.getName();
    Optional<UserEntity> user = userRepository.findByUsername(username);
    TodoEntity todo = new TodoEntity(requestTodoDto);
    long customId = user.get().getTodosIds().size() + 1;
    todo.setCustomId(customId);
    todoRepository.save(todo);
    user.get().addTodoId(todo.getId());
    userRepository.save(user.get());
    return new ResponseEntity<>(ResponseTodoDto.from(todo), HttpStatus.CREATED);
  }

  public ResponseEntity<ResponseTodoDto> getTodo(Long id) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String username = authentication.getName();
    Optional<UserEntity> user = userRepository.findByUsername(username);
    List<TodoEntity> todos = todoRepository.findByCustomId(id);
    if (todos.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    ResponseTodoDto userTodos =
        todos.stream()
            .filter(
                todo ->
                    user.get().getTodosIds().contains(todo.getId()) && todo.getDeletedAt() == null)
            .findFirst()
            .map(ResponseTodoDto::from)
            .orElse(null);
    return new ResponseEntity<>(userTodos, HttpStatus.OK);
  }

  public ResponseEntity<List<ResponseTodoDto>> getTodos(int page, int size) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String username = authentication.getName();
    Optional<UserEntity> user = userRepository.findByUsername(username);
    if (page > 0) page--;
    List<String> todoIds = user.get().getTodosIds();
    Pageable pageable = PageRequest.of(page, size);
    Page<TodoEntity> todoPage = todoRepository.findAllByIdInAndDeletedAtIsNull(todoIds, pageable);
    Page<ResponseTodoDto> responsePage = todoPage.map(ResponseTodoDto::from);
    return new ResponseEntity<>(responsePage.getContent(), HttpStatus.OK);
  }

  public ResponseEntity<List<ResponseTodoDto>> filterTodos(
      RequestTodoDto requestTodoDto, int page, int size) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String username = authentication.getName();
    Optional<UserEntity> user = userRepository.findByUsername(username);
    List<String> todosids = user.get().getTodosIds();
    if (page > 0) page--;
    Pageable pageable = PageRequest.of(page, size);
    Page<TodoEntity> todoPage =
        todoRepository.findByTitleContainingIgnoreCaseAndStatusAndTagsInAndDeletedAtIsNull(
            todosids,
            requestTodoDto.getTitle(),
            requestTodoDto.getStatus(),
            requestTodoDto.getTags(),
            pageable);
    return ResponseEntity.ok(todoPage.map(ResponseTodoDto::from).getContent());
  }

  public ResponseEntity<ResponseTodoDto> editTodo(RequestTodoDto requestTodoDto, Long id) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String username = authentication.getName();
    Optional<UserEntity> user = userRepository.findByUsername(username);
    List<String> todosids = user.get().getTodosIds();
    TodoEntity todoRes =
        todoRepository.findByCustomId(id).stream()
            .filter(todo -> todosids.contains(todo.getId().toString()))
            .findFirst()
            .orElse(null);
    if (todoRes == null) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    todoRes.setTitle(requestTodoDto.getTitle());
    todoRes.setDescription(requestTodoDto.getDescription());
    todoRes.setStatus(requestTodoDto.getStatus());
    todoRes.setDueDate(requestTodoDto.getDueDate());
    todoRes.setTags(requestTodoDto.getTags());
    todoRepository.save(todoRes);
    return new ResponseEntity<>(ResponseTodoDto.from(todoRes), HttpStatus.OK);
  }

  public ResponseEntity<List<ResponseTodoDto>> editManyTodos(List<RequestTodoDto> todoDtoList) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String username = authentication.getName();
    Optional<UserEntity> user = userRepository.findByUsername(username);
    List<String> todosids = user.get().getTodosIds();
    List<Long> requestedTodoIds = todoDtoList.stream().map(RequestTodoDto::getId).toList();
    List<TodoEntity> todos =
        todoRepository.findAllByCustomIdIn(requestedTodoIds).stream()
            .filter(todo -> todosids.contains(todo.getId().toString()))
            .toList();
    if (todos.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    for (RequestTodoDto todoDto : todoDtoList) {
      for (TodoEntity todo : todos) {
        if (todo.getCustomId().equals(todoDto.getId())) {
          todo.setTitle(todoDto.getTitle());
          todo.setDescription(todoDto.getDescription());
          todo.setStatus(todoDto.getStatus());
          todo.setDueDate(todoDto.getDueDate());
          todo.setTags(todoDto.getTags());
        }
      }
    }
    todoRepository.saveAll(todos);
    List<ResponseTodoDto> responseDtos = todos.stream().map(ResponseTodoDto::from).toList();
    return ResponseEntity.ok(responseDtos);
  }

  public ResponseEntity<String> hardDeleteTodo(Long id) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String username = authentication.getName();
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
    List<TodoEntity> remainingTodos =
        todoRepository.findAll().stream()
            .sorted(Comparator.comparing(TodoEntity::getCreatedAt))
            .toList();
    AtomicLong counter = new AtomicLong(1);
    remainingTodos.forEach(todo -> todo.setCustomId(counter.getAndIncrement()));
    todoRepository.saveAll(remainingTodos);
    return new ResponseEntity<>("Todo deleted successfully", HttpStatus.OK);
  }

  public ResponseEntity<String> hardDeleteManyTodos(String ids) {
    if (ids == null || ids.isEmpty()) {
      return new ResponseEntity<>("No ids provided", HttpStatus.BAD_REQUEST);
    }
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String username = authentication.getName();
    Optional<UserEntity> user = userRepository.findByUsername(username);
    List<Long> idList =
        Arrays.stream(ids.split(","))
            .map(String::trim)
            .map(Long::parseLong)
            .collect(Collectors.toList());
    List<String> todosids = user.get().getTodosIds();
    List<TodoEntity> todoRes =
        todoRepository.findAllByCustomIdIn(idList).stream()
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
    List<TodoEntity> remainingTodos =
        todoRepository.findAll().stream()
            .sorted(Comparator.comparing(TodoEntity::getCreatedAt))
            .toList();
    AtomicLong counter = new AtomicLong(1);
    remainingTodos.forEach(todo -> todo.setCustomId(counter.getAndIncrement()));
    todoRepository.saveAll(remainingTodos);
    return new ResponseEntity<>("Todos deleted successfully", HttpStatus.OK);
  }

  public ResponseEntity<String> hardDeleteAllTodos() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String username = authentication.getName();
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
    List<TodoEntity> remainingTodos =
        todoRepository.findAll().stream()
            .sorted(Comparator.comparing(TodoEntity::getCreatedAt))
            .toList();
    AtomicLong counter = new AtomicLong(1);
    remainingTodos.forEach(todo -> todo.setCustomId(counter.getAndIncrement()));
    todoRepository.saveAll(remainingTodos);
    return new ResponseEntity<>("All todos deleted successfully", HttpStatus.OK);
  }

  public ResponseEntity<MessageDto> softDeleteTodo(Long id) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String username = authentication.getName();
    Optional<UserEntity> user = userRepository.findByUsername(username);
    List<String> todosids = user.get().getTodosIds();
    TodoEntity todoRes =
        todoRepository.findByCustomId(id).stream()
            .filter(todo -> todosids.contains(todo.getId()))
            .findFirst()
            .orElse(null);
    if (todoRes == null) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    todoRes.setDeletedAt(Instant.now());
    todoRepository.save(todoRes);
    MessageDto message = new MessageDto();
    message.setMessage("Todo Deleted Successfully");
    return new ResponseEntity<>(message, HttpStatus.OK);
  }

  public ResponseEntity<MessageDto> softDeleteManyTodo(List<RequestTodoDto> todoDtoList) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String username = authentication.getName();
    Optional<UserEntity> user = userRepository.findByUsername(username);
    List<Long> idList = todoDtoList.stream().map(RequestTodoDto::getId).toList();
    List<String> todosids = user.get().getTodosIds();
    List<TodoEntity> todoRes =
        todoRepository.findAllByCustomIdIn(idList).stream()
            .filter(todo -> todosids.contains(todo.getId()))
            .toList();
    if (todoRes.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    for (TodoEntity todo : todoRes) {
      todo.setDeletedAt(Instant.now());
    }
    todoRepository.saveAll(todoRes);
    MessageDto message = new MessageDto();
    message.setMessage("Todos Deleted Successfully");
    return new ResponseEntity<>(message, HttpStatus.OK);
  }

  public ResponseEntity<MessageDto> softDeleteAllTodo() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String username = authentication.getName();
    Optional<UserEntity> user = userRepository.findByUsername(username);
    List<String> todosids = user.get().getTodosIds();
    List<TodoEntity> todoRes = todoRepository.findAllById(todosids);
    if (todoRes.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    for (TodoEntity todo : todoRes) {
      todo.setDeletedAt(Instant.now());
    }
    todoRepository.saveAll(todoRes);
    MessageDto message = new MessageDto();
    message.setMessage("All Todos Deleted Successfully");
    return new ResponseEntity<>(message, HttpStatus.OK);
  }

  public ResponseEntity<MessageDto> restoreTodo(Long id) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String username = authentication.getName();
    Optional<UserEntity> user = userRepository.findByUsername(username);
    List<TodoEntity> todos = todoRepository.findByCustomId(id);
    if (todos.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    TodoEntity userTodo =
        todos.stream()
            .filter(
                todo ->
                    user.get().getTodosIds().contains(todo.getId()) && todo.getDeletedAt() != null)
            .findFirst()
            .orElse(null);
    if (userTodo == null) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    userTodo.setDeletedAt(null);
    todoRepository.save(userTodo);
    MessageDto message = new MessageDto();
    message.setMessage("Todo Restored Successfully");
    return new ResponseEntity<>(message, HttpStatus.OK);
  }

  public ResponseEntity<MessageDto> restoreManyTodo(List<RequestTodoDto> todoDtoList) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String username = authentication.getName();
    Optional<UserEntity> user = userRepository.findByUsername(username);
    List<Long> idList = todoDtoList.stream().map(RequestTodoDto::getId).toList();
    List<String> todosids = user.get().getTodosIds();
    List<TodoEntity> todos =
        todoRepository.findAllByCustomIdIn(idList).stream()
            .filter(todo -> todosids.contains(todo.getId()))
            .toList();
    if (todos.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    for (TodoEntity todo : todos) {
      todo.setDeletedAt(null);
    }
    todoRepository.saveAll(todos);
    MessageDto message = new MessageDto();
    message.setMessage("Todos Restored Successfully");
    return new ResponseEntity<>(message, HttpStatus.OK);
  }

  public ResponseEntity<MessageDto> restoreAllTodo() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String username = authentication.getName();
    Optional<UserEntity> user = userRepository.findByUsername(username);
    List<String> todosids = user.get().getTodosIds();
    List<TodoEntity> todos = todoRepository.findAllById(todosids);
    for (TodoEntity todo : todos) {
      todo.setDeletedAt(null);
    }
    todoRepository.saveAll(todos);
    userRepository.save(user.get());
    MessageDto message = new MessageDto();
    message.setMessage("All Todos Restored Successfully");
    return new ResponseEntity<>(message, HttpStatus.OK);
  }
}
