package com.masteryhub.todoapp.models;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Settings {
    private Theme theme;

    public Settings() {
        this.theme = new Theme();
    }

    public Settings(Theme theme) {
        this.theme = theme;
    }
}
