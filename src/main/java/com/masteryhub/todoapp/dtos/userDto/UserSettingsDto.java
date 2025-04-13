package com.masteryhub.todoapp.dtos.userDto;

import com.masteryhub.todoapp.models.settingsModel.Settings;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserSettingsDto {
  private Settings settings;
}
