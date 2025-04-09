package com.masteryhub.todoapp.dto;

import com.masteryhub.todoapp.models.Settings;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserSettingsDto {
  private Settings settings;
}
