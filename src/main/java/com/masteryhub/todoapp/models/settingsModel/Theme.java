package com.masteryhub.todoapp.models.settingsModel;

public class Theme {
  private Boolean isLight;
  private PrimaryColor primaryColor;

  public Theme() {
    this.isLight = true;
    this.primaryColor = PrimaryColor.RED;
  }

  public Theme(Boolean isLight, PrimaryColor primaryColor) {
    this.isLight = isLight;
    this.primaryColor = primaryColor;
  }

  public Boolean getIsLight() {
    return isLight;
  }

  public void setIsLight(Boolean isLight) {
    this.isLight = isLight;
  }

  public PrimaryColor getPrimaryColor() {
    return primaryColor;
  }

  public void setPrimaryColor(PrimaryColor primaryColor) {
    this.primaryColor = primaryColor;
  }
}
