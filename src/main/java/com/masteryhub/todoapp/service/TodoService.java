package com.masteryhub.todoapp.service;

import com.masteryhub.todoapp.dto.MessageDto;
import com.masteryhub.todoapp.dto.RequestTodoDto;
import com.masteryhub.todoapp.dto.ResponseTodoDto;
import com.masteryhub.todoapp.models.Status;
import com.masteryhub.todoapp.models.TodoEntity;
import com.masteryhub.todoapp.models.UserEntity;
import com.masteryhub.todoapp.repository.TodoRepository;
import com.masteryhub.todoapp.repository.UserRepository;
import com.masteryhub.todoapp.security.UserDetailsImpl;

import java.time.Instant;
import java.util.*;
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
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String email = userDetails.getEmail();
        Optional<UserEntity> user = userRepository.findByEmail(email);
        TodoEntity todo = new TodoEntity(requestTodoDto);
        long customId = user.get().getTodosIds().size() + 1;
        todo.setCustomId(customId);
        todo.setUserId(user.get().getId());
        todoRepository.save(todo);
        user.get().addTodoId(todo.getId());
        userRepository.save(user.get());
        return new ResponseEntity<>(ResponseTodoDto.from(todo), HttpStatus.CREATED);
    }

    public ResponseEntity<ResponseTodoDto> getTodo(Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String email = userDetails.getEmail();
        Optional<UserEntity> user = userRepository.findByEmail(email);
        Optional<TodoEntity> todoOptional =
                todoRepository.findByUserIdAndCustomIdAndDeletedAtIsNull(user.get().getId(), id);
        if (todoOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return new ResponseEntity<>(ResponseTodoDto.from(todoOptional.get()), HttpStatus.OK);
    }

    public ResponseEntity<List<ResponseTodoDto>> getTodos(int page, int size) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String email = userDetails.getEmail();
        Optional<UserEntity> user = userRepository.findByEmail(email);
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
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String email = userDetails.getEmail();
        Optional<UserEntity> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        UserEntity user = userOptional.get();
        if (page > 0) page--;
        Pageable pageable = PageRequest.of(page, size);
        String title = (requestTodoDto.getTitle() != null) ? requestTodoDto.getTitle() : "";
        Status status = (requestTodoDto.getStatus() != null) ? requestTodoDto.getStatus() : null;
        List<String> tags =
                (requestTodoDto.getTags() != null) ? requestTodoDto.getTags() : Collections.emptyList();
        Page<TodoEntity> todoPage;
        if (status == null && tags.isEmpty()) {
            todoPage =
                    todoRepository.findByUserIdAndTitleContainingIgnoreCaseAndDeletedAtIsNull(
                            user.getId(), title, pageable);
        } else if (status == null) {
            todoPage =
                    todoRepository.findByUserIdAndTitleContainingIgnoreCaseAndTagsInAndDeletedAtIsNull(
                            user.getId(), title, tags, pageable);
        } else if (tags.isEmpty()) {
            todoPage =
                    todoRepository.findByUserIdAndTitleContainingIgnoreCaseAndStatusAndDeletedAtIsNull(
                            user.getId(), title, status, pageable);
        } else {
            todoPage =
                    todoRepository
                            .findByUserIdAndTitleContainingIgnoreCaseAndStatusAndTagsInAndDeletedAtIsNull(
                                    user.getId(), title, status, tags, pageable);
        }
        return ResponseEntity.ok(todoPage.map(ResponseTodoDto::from).getContent());
    }

    public ResponseEntity<ResponseTodoDto> editTodo(RequestTodoDto requestTodoDto, Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String email = userDetails.getEmail();
        Optional<UserEntity> user = userRepository.findByEmail(email);
        Optional<TodoEntity> todoOptional =
                todoRepository.findByUserIdAndCustomId(user.get().getId(), id);
        System.out.println(user.get().getEmail());
        if (todoOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        TodoEntity todoRes = todoOptional.get();
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
        if (!(authentication.getPrincipal() instanceof UserDetailsImpl userDetails)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String email = userDetails.getEmail();
        Optional<UserEntity> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        UserEntity user = userOptional.get();
        List<Long> requestedTodoIds = todoDtoList.stream().map(RequestTodoDto::getId).toList();
        List<TodoEntity> todos =
                todoRepository.findByUserIdAndCustomIdIn(user.getId(), requestedTodoIds);
        if (todos.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        Map<Long, RequestTodoDto> updateMap =
                todoDtoList.stream().collect(Collectors.toMap(RequestTodoDto::getId, todo -> todo));
        for (TodoEntity todo : todos) {
            RequestTodoDto updateData = updateMap.get(todo.getCustomId());
            if (updateData != null) {
                todo.setTitle(updateData.getTitle());
                todo.setDescription(updateData.getDescription());
                todo.setStatus(updateData.getStatus());
                todo.setDueDate(updateData.getDueDate());
                todo.setTags(updateData.getTags());
            }
        }
        todoRepository.saveAll(todos);
        List<ResponseTodoDto> responseDtos = todos.stream().map(ResponseTodoDto::from).toList();
        return ResponseEntity.ok(responseDtos);
    }

    public ResponseEntity<MessageDto> softDeleteTodo(Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication.getPrincipal() instanceof UserDetailsImpl userDetails)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String email = userDetails.getEmail();
        Optional<UserEntity> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        UserEntity user = userOptional.get();
        Optional<TodoEntity> todoOptional =
                todoRepository.findByUserIdAndCustomIdAndDeletedAtIsNull(user.getId(), id);
        if (todoOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        TodoEntity todoRes = todoOptional.get();
        todoRes.setDeletedAt(Instant.now());
        todoRepository.save(todoRes);
        MessageDto message = new MessageDto("Todo Deleted Successfully");
        return ResponseEntity.ok(message);
    }

    public ResponseEntity<MessageDto> softDeleteManyTodo(List<RequestTodoDto> todoDtoList) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication.getPrincipal() instanceof UserDetailsImpl userDetails)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String email = userDetails.getEmail();
        Optional<UserEntity> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        UserEntity user = userOptional.get();
        List<Long> idList = todoDtoList.stream().map(RequestTodoDto::getId).toList();
        List<TodoEntity> todos =
                todoRepository.findByUserIdAndCustomIdInAndDeletedAtIsNull(user.getId(), idList);
        if (todos.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        todos.forEach(todo -> todo.setDeletedAt(Instant.now()));
        todoRepository.saveAll(todos);
        MessageDto message = new MessageDto("Todos Deleted Successfully");
        return ResponseEntity.ok(message);
    }

    public ResponseEntity<MessageDto> softDeleteAllTodo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String email = userDetails.getEmail();
        Optional<UserEntity> user = userRepository.findByEmail(email);
        List<String> todosIds = user.get().getTodosIds();
        List<TodoEntity> todoRes = todoRepository.findAllById(todosIds);
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
        if (!(authentication.getPrincipal() instanceof UserDetailsImpl userDetails)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String email = userDetails.getEmail();
        Optional<UserEntity> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        UserEntity user = userOptional.get();
        Optional<TodoEntity> todoOptional =
                todoRepository.findByUserIdAndCustomIdAndDeletedAtIsNotNull(user.getId(), id);

        if (todoOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        TodoEntity todo = todoOptional.get();
        todo.setDeletedAt(null);
        todoRepository.save(todo);
        MessageDto message = new MessageDto("Todo Restored Successfully");
        return ResponseEntity.ok(message);
    }

    public ResponseEntity<MessageDto> restoreManyTodo(List<RequestTodoDto> todoDtoList) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication.getPrincipal() instanceof UserDetailsImpl userDetails)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String email = userDetails.getEmail();
        Optional<UserEntity> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        UserEntity user = userOptional.get();
        List<Long> idList = todoDtoList.stream().map(RequestTodoDto::getId).toList();
        List<TodoEntity> todos =
                todoRepository.findByUserIdAndCustomIdInAndDeletedAtIsNotNull(user.getId(), idList);
        if (todos.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        todos.forEach(todo -> todo.setDeletedAt(null));
        todoRepository.saveAll(todos);
        return ResponseEntity.ok(new MessageDto("Todos Restored Successfully"));
    }

    public ResponseEntity<MessageDto> restoreAllTodo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String email = userDetails.getEmail();
        Optional<UserEntity> user = userRepository.findByEmail(email);
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
