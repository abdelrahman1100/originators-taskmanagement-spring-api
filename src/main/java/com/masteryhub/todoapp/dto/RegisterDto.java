package com.masteryhub.todoapp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RegisterDto {

  @NotBlank(message = "Username cannot be blank")
  private String username;

  @NotBlank(message = "Password cannot be blank")
  @Size(min = 8, message = "Password must be at least 8 characters long")
  @Pattern(
      regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
      message =
          "Password must contain at least one uppercase letter, one lowercase letter, one number, and one special character")
  private String password;

  @NotBlank(message = "Full name cannot be blank")
  private String fullName;

  @NotBlank(message = "Phone number cannot be blank")
  @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Invalid phone number format")
  private String phoneNumber;

  @NotBlank(message = "Address cannot be blank")
  private String address;

  @NotBlank(message = "Confirm password cannot be blank")
  private String confirmPassword;

  @NotBlank(message = "Email cannot be blank")
  @Pattern(regexp = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$", message = "Invalid email format")
  private String email;
}
