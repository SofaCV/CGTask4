package com.cgvsu;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

public class ThemeManager {
    public enum Theme {
        DARK("Dark Theme"),
        LIGHT("Light Theme");

        private final String displayName;

        Theme(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    private Theme currentTheme = Theme.DARK;

    public ThemeManager() {
        System.out.println("ThemeManager: Dark/Light themes only");
    }

    public void setTheme(Theme theme) {
        this.currentTheme = theme;
    }

    public Theme getCurrentTheme() {
        return currentTheme;
    }

    public void applyThemeToScene(Scene scene) {
        if (scene == null) return;

        if (scene.getRoot() != null) {
            if (currentTheme == Theme.DARK) {
                scene.getRoot().setStyle(getDarkStyles());
            } else {
                scene.getRoot().setStyle(getLightStyles());
            }
            applyThemeToControls(scene.getRoot());
        }
    }

    private String getDarkStyles() {
        return
                "-fx-background-color: #2d2d30; " +
                        "-fx-text-fill: #ffffff; " +
                        "-fx-base: #2d2d30; " +
                        "-fx-control-inner-background: #3e3e42; " +
                        "-fx-accent: #007acc; " +
                        "-fx-focus-color: #007acc;";
    }

    private String getLightStyles() {
        return
                "-fx-background-color: #f5f5f5; " +
                        "-fx-text-fill: #333333; " +
                        "-fx-base: #f5f5f5; " +
                        "-fx-control-inner-background: #ffffff; " +
                        "-fx-accent: #007acc; " +
                        "-fx-focus-color: #007acc;";
    }

    private void styleButton(Button btn) {
        btn.setStyle(
                "-fx-background-color: #007acc; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-weight: bold; " +
                        "-fx-padding: 8 15; " +
                        "-fx-background-radius: 4; " +
                        "-fx-border-radius: 4; " +
                        "-fx-cursor: hand;"
        );
    }

    private void applyThemeToControls(javafx.scene.Node node) {
        if (node instanceof Pane) {
            Pane pane = (Pane) node;
            for (javafx.scene.Node child : pane.getChildren()) {
                applyThemeToControls(child);

                if (child instanceof Button) {
                    styleButton((Button) child);
                }

                if (child instanceof ListView) {
                    ListView<?> list = (ListView<?>) child;
                    if (currentTheme == Theme.DARK) {
                        list.setStyle(
                                "-fx-background-color: #2d2d30; " +
                                        "-fx-control-inner-background: #2d2d30; " +
                                        "-fx-text-fill: #ffffff;"
                        );
                    } else {
                        list.setStyle(
                                "-fx-background-color: #ffffff; " +
                                        "-fx-control-inner-background: #ffffff; " +
                                        "-fx-text-fill: #333333;"
                        );
                    }
                }
            }
        }
    }

    public void applyTheme(Pane root) {
        if (root.getScene() != null) {
            applyThemeToScene(root.getScene());
        }
    }

    public void applyThemeToRoot(javafx.scene.Parent root) {
        applyTheme((Pane) root);
    }
}