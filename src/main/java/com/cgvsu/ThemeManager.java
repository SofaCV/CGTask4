package com.cgvsu;

import javafx.scene.paint.Color;
import java.util.HashMap;
import java.util.Map;

public class ThemeManager {
    public enum Theme {
        DARK, LIGHT, BLUE
    }

    private Theme currentTheme = Theme.DARK;
    private final Map<Theme, Map<String, String>> themeStyles = new HashMap<>();

    public ThemeManager() {
        initThemes();
    }

    private void initThemes() {
        Map<String, String> darkTheme = new HashMap<>();
        darkTheme.put("background", "#2d2d30");
        darkTheme.put("foreground", "#ffffff");
        darkTheme.put("panel-bg", "#3e3e42");
        darkTheme.put("panel-fg", "#cccccc");
        darkTheme.put("accent", "#007acc");
        darkTheme.put("hover", "#1c97ea");
        darkTheme.put("grid", "#404040");
        darkTheme.put("selected", "#ff6b6b");
        darkTheme.put("border", "#555555");
        themeStyles.put(Theme.DARK, darkTheme);

        Map<String, String> lightTheme = new HashMap<>();
        lightTheme.put("background", "#f5f5f5");
        lightTheme.put("foreground", "#333333");
        lightTheme.put("panel-bg", "#ffffff");
        lightTheme.put("panel-fg", "#333333");
        lightTheme.put("accent", "#2196f3");
        lightTheme.put("hover", "#1976d2");
        lightTheme.put("grid", "#e0e0e0");
        lightTheme.put("selected", "#ff5252");
        lightTheme.put("border", "#dddddd");
        themeStyles.put(Theme.LIGHT, lightTheme);

        Map<String, String> blueTheme = new HashMap<>();
        blueTheme.put("background", "#0d2b45");
        blueTheme.put("foreground", "#ffffff");
        blueTheme.put("panel-bg", "#1a3c5c");
        blueTheme.put("panel-fg", "#e0f7fa");
        blueTheme.put("accent", "#00bcd4");
        blueTheme.put("hover", "#00acc1");
        blueTheme.put("grid", "#1e3a5f");
        blueTheme.put("selected", "#ff4081");
        blueTheme.put("border", "#2a4a6e");
        themeStyles.put(Theme.BLUE, blueTheme);
    }

    public void setTheme(Theme theme) {
        this.currentTheme = theme;
    }

    public Theme getCurrentTheme() {
        return currentTheme;
    }

    public String getColor(String key) {
        return themeStyles.get(currentTheme).get(key);
    }

    public String getStyle(String element) {
        switch (element) {
            case "root":
                return String.format("-fx-background-color: %s;", getColor("background"));
            case "panel":
                return String.format("-fx-background-color: %s; -fx-text-fill: %s; -fx-border-color: %s; -fx-border-width: 1;",
                        getColor("panel-bg"), getColor("panel-fg"), getColor("border"));
            case "button":
                return String.format("-fx-background-color: %s; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 15; -fx-background-radius: 4;",
                        getColor("accent"));
            case "button:hover":
                return String.format("-fx-background-color: %s;", getColor("hover"));
            case "menu-bar":
                return String.format("-fx-background-color: %s;", getColor("panel-bg"));
            case "list-view":
                return String.format("-fx-background-color: %s; -fx-text-fill: %s; -fx-control-inner-background: %s;",
                        getColor("panel-bg"), getColor("panel-fg"), getColor("panel-bg"));
            case "text-field":
                return String.format("-fx-background-color: %s; -fx-text-fill: %s; -fx-border-color: %s; -fx-border-radius: 3;",
                        getColor("background"), getColor("foreground"), getColor("border"));
            case "label-title":
                return String.format("-fx-text-fill: %s; -fx-font-weight: bold; -fx-font-size: 14;", getColor("foreground"));
            case "label-subtitle":
                return String.format("-fx-text-fill: %s; -fx-font-weight: bold; -fx-font-size: 12;", getColor("panel-fg"));
            case "toggle-button":
                return String.format("-fx-background-color: %s; -fx-text-fill: %s; -fx-padding: 8 15; -fx-background-radius: 4;",
                        getColor("panel-bg"), getColor("panel-fg"));
            case "toggle-button:selected":
                return String.format("-fx-background-color: %s; -fx-text-fill: white; -fx-padding: 8 15; -fx-background-radius: 4;",
                        getColor("accent"));
            case "status-bar":
                return String.format("-fx-background-color: %s; -fx-text-fill: %s;", getColor("accent"), "white");
            default:
                return "";
        }
    }
}