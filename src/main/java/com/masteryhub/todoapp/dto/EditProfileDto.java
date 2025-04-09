package com.masteryhub.todoapp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class EditProfileDto {
  @NotBlank(message = "Full name cannot be blank")
  private String fullName;

  @NotBlank(message = "Phone number cannot be blank")
  @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Invalid phone number format")
  private String phoneNumber;

  @NotBlank(message = "Address cannot be blank")
  private String address;
}
