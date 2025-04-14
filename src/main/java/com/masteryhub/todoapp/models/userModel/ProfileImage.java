package com.masteryhub.todoapp.models.userModel;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProfileImage {
  private byte[] imageData;
  private String imageType;

  public String getDataUrl() {
    if (imageData == null || imageType == null) return null;
    return "data:"
        + imageType
        + ";base64,"
        + java.util.Base64.getEncoder().encodeToString(imageData);
  }
}
