package com.cgvsu;

import javafx.animation.*;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class AnimationManager {

    public void addHoverAnimation(Button button) {
        ScaleTransition hoverScale = new ScaleTransition(Duration.millis(150), button);
        hoverScale.setToX(1.05);
        hoverScale.setToY(1.05);

        ScaleTransition leaveScale = new ScaleTransition(Duration.millis(150), button);
        leaveScale.setToX(1.0);
        leaveScale.setToY(1.0);

        button.setOnMouseEntered(e -> hoverScale.play());
        button.setOnMouseExited(e -> leaveScale.play());
    }

    public void addToggleAnimation(ToggleButton button) {
        button.selectedProperty().addListener((obs, oldVal, newVal) -> {
            FadeTransition fade = new FadeTransition(Duration.millis(200), button);
            fade.setFromValue(0.8);
            fade.setToValue(1.0);
            fade.play();
        });
    }

    public void playThemeSwitchAnimation(Node node) {
        FadeTransition fadeOut = new FadeTransition(Duration.millis(200), node);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.5);

        FadeTransition fadeIn = new FadeTransition(Duration.millis(200), node);
        fadeIn.setFromValue(0.5);
        fadeIn.setToValue(1.0);

        fadeOut.setOnFinished(e -> fadeIn.play());
        fadeOut.play();
    }

    public void playWelcomeAnimation(Pane root) {
        Timeline welcomeTimeline = new Timeline();

        int delay = 0;
        for (Node node : root.getChildren()) {
            if (node instanceof Pane) {
                FadeTransition fade = new FadeTransition(Duration.millis(300), node);
                fade.setFromValue(0);
                fade.setToValue(1);
                fade.setDelay(Duration.millis(delay));
                fade.play();
                delay += 100;
            }
        }
    }

    public void playSliderChangeAnimation(Node slider) {
        ScaleTransition pulse = new ScaleTransition(Duration.millis(100), slider);
        pulse.setFromX(1.0);
        pulse.setToX(1.02);
        pulse.setFromY(1.0);
        pulse.setToY(1.02);
        pulse.setAutoReverse(true);
        pulse.setCycleCount(2);
        pulse.play();
    }

    public void setupAnimations(Pane root) {
        for (Node node : root.getChildren()) {
            if (node instanceof Pane) {
                setupPaneAnimations((Pane) node);
            }
        }
    }

    private void setupPaneAnimations(Pane pane) {
        FadeTransition fadeIn = new FadeTransition(Duration.millis(400), pane);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.play();
    }
}