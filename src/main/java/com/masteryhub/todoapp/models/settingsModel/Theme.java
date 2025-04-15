package com.masteryhub.todoapp.models.settingsModel;

public class Theme {
  private Boolean DisplayMode;
  private ThemeColor themeColor;

  public Theme() {
    this.DisplayMode = true;
    this.themeColor = ThemeColor.RED;
  }

  public Theme(Boolean DisplayMode, ThemeColor themeColor) {
    this.DisplayMode = DisplayMode;
    this.themeColor = themeColor;
  }

  public Boolean getDisplayMode() {
    return DisplayMode;
  }

  public void setDisplayMode(Boolean displayMode) {
    this.DisplayMode = displayMode;
  }

  public ThemeColor getPrimaryColor() {
    return themeColor;
  }

  public void setPrimaryColor(ThemeColor themeColor) {
    this.themeColor = themeColor;
  }
}
