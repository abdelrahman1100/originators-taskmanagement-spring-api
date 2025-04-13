package com.masteryhub.todoapp.dto.userDto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Base64;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProfileImageDto {
  private String imageBase64;
  private String imageType;

  public String getDataUrl() {
    if (imageBase64 == null || imageType == null) return null;
    return "data:" + imageType + ";base64," + imageBase64;
  }

  @JsonIgnore
  public boolean isValidImageType() {
    String extractedType = extractImageType(imageBase64);
    return extractedType != null
        && extractedType.equals(imageType)
        && ("image/png".equals(imageType) || "image/jpg".equals(imageType));
  }

  public String extractImageType(String base64String) {
    if (base64String != null && base64String.startsWith("data:")) {
      String prefix = base64String.split(",")[0];
      return prefix.split(":")[1].split(";")[0];
    }
    return null;
  }

  public byte[] getImageData() {
    if (imageBase64 != null && !imageBase64.isEmpty()) {
      return Base64.getDecoder().decode(imageBase64.split(",")[1]);
    }
    return null;
  }
}
