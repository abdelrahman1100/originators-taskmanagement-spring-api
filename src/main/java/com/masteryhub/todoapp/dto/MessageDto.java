package com.masteryhub.todoapp.dto;

// TODO: add getter and setter methods to the class
public class MessageDto {
  private String message;

  public MessageDto() {}

  public MessageDto(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}
