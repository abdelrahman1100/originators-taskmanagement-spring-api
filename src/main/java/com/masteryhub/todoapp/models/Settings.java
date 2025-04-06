package com.masteryhub.todoapp.models;

public class Settings {
    private Theme theme;

    public Settings() {
        this.theme = new Theme();
    }

    public Settings(Theme theme) {
        this.theme = theme;
    }

    public Theme getTheme() {
        return theme;
    }

    public void setTheme(Theme theme) {
        this.theme = theme;
    }
}
