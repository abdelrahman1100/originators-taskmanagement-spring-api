package com.masteryhub.todoapp.dtos.userDto;

import com.masteryhub.todoapp.models.settingsModel.Theme;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SettingsDto {
  private Theme theme;
}
