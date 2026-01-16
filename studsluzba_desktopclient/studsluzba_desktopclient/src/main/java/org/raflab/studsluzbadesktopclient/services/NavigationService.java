package org.raflab.studsluzbadesktopclient.services;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Stack;


@Service
public class NavigationService {

    private final Stack<Object> history = new Stack<>();
    private final Stack<Object> forwardStack = new Stack<>();
    private BorderPane mainRoot;

    private boolean isNavigating = false;

    @Value("${navigation.max-history:10}")
    private int maxHistorySize;

    public void setMainRoot(BorderPane root) {
        this.mainRoot = root;
    }

    public void recordTabChange(TabPane tabPane, Tab oldTab) {
        if (!isNavigating && oldTab != null) {
            addToHistory(new TabNavigationAction(tabPane, oldTab));
            forwardStack.clear();
        }
    }

    public void navigateTo(Parent view) {
        if (view == null || mainRoot == null) return;

        Node current = mainRoot.getCenter();
        if (current instanceof Parent) {
            addToHistory(current);
            forwardStack.clear();
        }

        mainRoot.setCenter(view);
        Platform.runLater(view::requestFocus);
    }

    private void addToHistory(Object state) {
        history.push(state);
        if (history.size() > maxHistorySize) {
            history.remove(0);
        }
    }

    public void goBack() {
        if (mainRoot == null || history.isEmpty()) {
            System.out.println("Navigacija: Istorija je prazna.");
            return;
        }

        isNavigating = true;
        try {
            Object previousState = history.pop();

            Object currentState = getCurrentState();
            if (currentState != null) forwardStack.push(currentState);

            if (previousState instanceof TabNavigationAction) {
                ((TabNavigationAction) previousState).restore();
            } else if (previousState instanceof Parent) {
                mainRoot.setCenter((Parent) previousState);
                ((Parent) previousState).requestFocus();
            }
        } finally {
            isNavigating = false;
        }
    }

    public void goForward() {
        if (mainRoot == null || forwardStack.isEmpty()) {
            System.out.println("Navigacija: Nema forward istorije.");
            return;
        }

        isNavigating = true;
        try {
            Object nextState = forwardStack.pop();

            Object currentState = getCurrentState();
            if (currentState != null) history.push(currentState);

            if (nextState instanceof TabNavigationAction) {
                ((TabNavigationAction) nextState).restore();
            } else if (nextState instanceof Parent) {
                mainRoot.setCenter((Parent) nextState);
                ((Parent) nextState).requestFocus();
            }
        } finally {
            isNavigating = false;
        }
    }

    private Object getCurrentState() {
        Node center = mainRoot.getCenter();
        if (center instanceof Parent) {
            TabPane tp = findTabPane((Parent) center);
            if (tp != null) {
                return new TabNavigationAction(tp, tp.getSelectionModel().getSelectedItem());
            }
            return center;
        }
        return null;
    }

    private TabPane findTabPane(Parent root) {
        if (root instanceof TabPane) return (TabPane) root;
        for (Node node : root.getChildrenUnmodifiable()) {
            if (node instanceof TabPane) return (TabPane) node;
            if (node instanceof Parent) {
                TabPane found = findTabPane((Parent) node);
                if (found != null) return found;
            }
        }
        return null;
    }

    /**
     * Registracija svih prečica (Tastatura + Miš)
     */
    public void setupShortcuts(Scene scene) {
        if (scene == null) return;

        // 1. TASTATURA (Ctrl + [ i Ctrl + ])
        scene.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.isControlDown() && event.getCode() == KeyCode.OPEN_BRACKET) {
                goBack();
                event.consume();
            } else if (event.isControlDown() && event.getCode() == KeyCode.CLOSE_BRACKET) {
                goForward();
                event.consume();
            } else if (event.getCode() == KeyCode.ESCAPE) {
                goBack();
                event.consume();
            }
        });

        // 2. MIŠ (Button 4 i Button 5)
        scene.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
            if (event.getButton() == MouseButton.BACK) {
                goBack();
                event.consume();
            } else if (event.getButton() == MouseButton.FORWARD) {
                goForward();
                event.consume();
            }
        });
    }
}