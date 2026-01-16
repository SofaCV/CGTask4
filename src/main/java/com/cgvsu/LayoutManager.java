package com.cgvsu;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.*;

public class LayoutManager {

    public VBox createStyledVBox(double spacing, Insets padding) {
        VBox vbox = new VBox(spacing);
        vbox.setPadding(padding);
        vbox.setAlignment(Pos.TOP_CENTER);
        vbox.getStyleClass().add("styled-vbox");
        return vbox;
    }

    public HBox createStyledHBox(double spacing, Insets padding) {
        HBox hbox = new HBox(spacing);
        hbox.setPadding(padding);
        hbox.setAlignment(Pos.CENTER_LEFT);
        hbox.getStyleClass().add("styled-hbox");
        return hbox;
    }

    public GridPane createEqualColumnGrid(int columns, double hgap, double vgap) {
        GridPane grid = new GridPane();
        grid.setHgap(hgap);
        grid.setVgap(vgap);

        for (int i = 0; i < columns; i++) {
            ColumnConstraints colConst = new ColumnConstraints();
            colConst.setPercentWidth(100.0 / columns);
            grid.getColumnConstraints().add(colConst);
        }

        return grid;
    }

    public void centerInParent(Node node, Pane parent) {
        StackPane.setAlignment(node, Pos.CENTER);

        if (parent instanceof StackPane) {
            ((StackPane) parent).getChildren().add(node);
        }
    }

    public BorderPane createResponsiveLayout(Node top, Node left, Node center, Node right, Node bottom) {
        BorderPane layout = new BorderPane();

        if (top != null) {
            layout.setTop(top);
            BorderPane.setMargin(top, new Insets(5));
        }

        if (left != null) {
            layout.setLeft(left);
            BorderPane.setMargin(left, new Insets(5));
        }

        if (center != null) {
            layout.setCenter(center);
            BorderPane.setMargin(center, new Insets(5));
        }

        if (right != null) {
            layout.setRight(right);
            BorderPane.setMargin(right, new Insets(5));
        }

        if (bottom != null) {
            layout.setBottom(bottom);
            BorderPane.setMargin(bottom, new Insets(5));
        }

        return layout;
    }
}