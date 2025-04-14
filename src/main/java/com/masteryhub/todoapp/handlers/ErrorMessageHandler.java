package com.masteryhub.todoapp.handlers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.InputStream;

public class ErrorMessageHandler {

  private static JsonNode root;

  static {
    try (InputStream is =
        ErrorMessageHandler.class.getClassLoader().getResourceAsStream("errorMessages.json")) {
      ObjectMapper mapper = new ObjectMapper();
      root = mapper.readTree(is);
    } catch (Exception e) {
      throw new RuntimeException("Failed to load error messages", e);
    }
  }

  public static class Auth {
    public static String get_missing_token() {
      return root.path("auth_errors").path("missing_token").asText();
    }

    public static String get_invalid_token_format() {
      return root.path("auth_errors").path("invalid_token_format").asText();
    }

    public static String get_blacklisted_token() {
      return root.path("auth_errors").path("blacklisted_token").asText();
    }

    public static String get_unauthorized() {
      return root.path("auth_errors").path("unauthorized").asText();
    }

    public static String get_login_google_error() {
      return root.path("auth_errors").path("login_google_error").asText();
    }
  }

  public static class User {
    public static String get_invalid_email_or_password() {
      return root.path("user_errors").path("invalid_email_or_password").asText();
    }

    public static String get_user_exist() {
      return root.path("user_errors").path("user_exist").asText();
    }

    public static String get_user_creation_failed() {
      return root.path("user_errors").path("user_creation_failed").asText();
    }

    public static String get_user_not_found() {
      return root.path("user_errors").path("user_not_found").asText();
    }
  }

  public static class Todo {
    public static String get_todo_not_found() {
      return root.path("todo_errors").path("todo_not_found").asText();
    }

    public static String get_user_already_friend() {
      return root.path("todo_errors").path("user_already_friend").asText();
    }
  }

  public static class Permission {
    public static String get_only_owner() {
      return root.path("permission_errors").path("only_owner").asText();
    }

    public static String get_read_only() {
      return root.path("permission_errors").path("read_only").asText();
    }

    public static String get_not_accessible() {
      return root.path("permission_errors").path("not_accessible").asText();
    }
  }

  public static class RequestBody {
    public static String get_missing_todo_id() {
      return root.path("request_body_errors").path("missing_todo_id").asText();
    }

    public static String get_date_format_error() {
      return root.path("request_body_errors").path("date_format_error").asText();
    }
  }
}
