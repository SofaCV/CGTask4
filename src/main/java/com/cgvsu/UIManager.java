package com.cgvsu;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class UIManager {
    private final ThemeManager themeManager;
    private final AnimationManager animationManager;

    public UIManager(ThemeManager themeManager, AnimationManager animationManager) {
        this.themeManager = themeManager;
        this.animationManager = animationManager;
    }

    public Button createIconButton(String text, String tooltipText, String shortcut) {
        Button button = new Button(text);
        button.getStyleClass().add("icon-button");

        if (tooltipText != null) {
            String fullTooltip = shortcut != null ?
                    tooltipText + " (" + shortcut + ")" : tooltipText;
            Tooltip tooltip = new Tooltip(fullTooltip);
            tooltip.setShowDelay(Duration.millis(500));
            button.setTooltip(tooltip);
        }

        animationManager.addHoverAnimation(button);

        return button;
    }

    public Button createIconButton(String text, String tooltipText) {
        return createIconButton(text, tooltipText, null);
    }

    public Button createThemeButton(String text, ThemeManager.Theme theme) {
        Button button = new Button(text);
        button.getStyleClass().add("theme-button");

        button.setOnAction(e -> {
            themeManager.setTheme(theme);
            if (button.getScene() != null) {
                themeManager.applyThemeToScene(button.getScene());
            }

            animationManager.playThemeSwitchAnimation(button);
        });

        return button;
    }

    public ToggleButton createToggleButton(String text, String tooltip) {
        ToggleButton button = new ToggleButton(text);
        button.getStyleClass().add("toggle-button");

        if (tooltip != null) {
            Tooltip tip = new Tooltip(tooltip);
            button.setTooltip(tip);
        }

        animationManager.addToggleAnimation(button);

        return button;
    }

    public VBox createSectionPanel(String title) {
        VBox section = new VBox(10);
        section.getStyleClass().add("section-panel");

        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("section-title");

        Separator separator = new Separator();
        separator.getStyleClass().add("section-separator");

        section.getChildren().addAll(titleLabel, separator);

        return section;
    }

    public void showThemePreview() {
        System.out.println("UI Manager: Current theme - " + themeManager.getCurrentTheme());
    }

    public void showTooltipDemo() {
        System.out.println("UI Manager: Tooltip system active");
    }

    public Menu createMenu(String title, MenuItem... items) {
        Menu menu = new Menu(title);
        menu.getStyleClass().add("custom-menu");

        for (MenuItem item : items) {
            menu.getItems().add(item);
        }

        return menu;
    }

    public Slider createStyledSlider(double min, double max, double value) {
        Slider slider = new Slider(min, max, value);
        slider.getStyleClass().add("styled-slider");

        slider.valueProperty().addListener((obs, oldVal, newVal) -> {
            animationManager.playSliderChangeAnimation(slider);
        });

        return slider;
    }
}