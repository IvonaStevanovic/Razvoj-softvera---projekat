package org.raflab.studsluzbadesktopclient.services;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import org.springframework.stereotype.Service;

import java.util.Stack;
@Service
public class NavigationService {
    private final Stack<Parent> history = new Stack<>();
    private final Stack<Parent> forwardStack = new Stack<>();
    private BorderPane mainRoot;

    public void setMainRoot(BorderPane root) {
        this.mainRoot = root;
    }

    public void navigateTo(Parent view) {
        if (mainRoot != null && mainRoot.getCenter() != null) {
            history.push((Parent) mainRoot.getCenter());
            forwardStack.clear(); // Brišemo forward kad idemo na novi profil
        }
        mainRoot.setCenter(view);
    }

    public void goBack() {
        if (!history.isEmpty() && mainRoot != null) {
            forwardStack.push((Parent) mainRoot.getCenter());
            mainRoot.setCenter(history.pop());
        }
    }

    public void goForward() {
        if (!forwardStack.isEmpty() && mainRoot != null) {
            history.push((Parent) mainRoot.getCenter());
            mainRoot.setCenter(forwardStack.pop());
        }
    }

    public void setupShortcuts(Scene scene) {
        if (scene == null) return;
        scene.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.isControlDown()) {
                if (event.getCode() == KeyCode.B) {
                    goBack();
                    event.consume();
                } else if (event.getCode() == KeyCode.R) {
                    goForward();
                    event.consume();
                }
            }
        });
    }
}