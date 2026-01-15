package org.raflab.studsluzbadesktopclient.services;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import org.springframework.stereotype.Service;

import java.util.Stack;

@Service
public class NavigationService {

    private final Stack<Parent> history = new Stack<>();
    private final Stack<Parent> forwardStack = new Stack<>();
    private BorderPane mainRoot;

    /**
     * Postavlja glavni BorderPane iz main.fxml.
     * Ovo mora da pozove MainView u createScene metodi.
     */
    public void setMainRoot(BorderPane root) {
        this.mainRoot = root;
    }

    public void navigateTo(Parent view) {
        if (mainRoot != null) {
            Node current = mainRoot.getCenter();
            if (current instanceof Parent) {
                history.push((Parent) current);
                forwardStack.clear(); // Brišemo forward istoriju kod nove navigacije
            }
            mainRoot.setCenter(view);

            // Moramo zahtevati fokus da bi prečice radile na novom ekranu
            Platform.runLater(view::requestFocus);
        }
    }

    public void goBack() {
        if (mainRoot != null && !history.isEmpty()) {
            Parent previous = history.pop();
            forwardStack.push((Parent) mainRoot.getCenter());
            mainRoot.setCenter(previous);

            // Vraćamo fokus na prethodni ekran
            Platform.runLater(previous::requestFocus);
            System.out.println("Navigacija: Povratak nazad uspešan.");
        } else {
            System.out.println("Navigacija: Nema istorije za povratak.");
        }
    }

    public void goForward() {
        if (mainRoot != null && !forwardStack.isEmpty()) {
            Parent next = forwardStack.pop();
            history.push((Parent) mainRoot.getCenter());
            mainRoot.setCenter(next);

            Platform.runLater(next::requestFocus);
        }
    }

    /**
     * Registruje prečice na nivou cele scene.
     * Koristimo Accelerators jer su pouzdaniji od EventFilter-a.
     */
    public void setupShortcuts(Scene scene) {
        if (scene == null) return;

        // Ctrl + [ (Back)
        scene.getAccelerators().put(
                new KeyCodeCombination(KeyCode.OPEN_BRACKET, KeyCombination.CONTROL_ANY),
                this::goBack
        );

        // Ctrl + ] (Forward)
        scene.getAccelerators().put(
                new KeyCodeCombination(KeyCode.CLOSE_BRACKET, KeyCombination.CONTROL_ANY),
                this::goForward
        );

        // ESCAPE (Kao dodatni Back za lakši rad)
        scene.getAccelerators().put(
                new KeyCodeCombination(KeyCode.ESCAPE),
                this::goBack
        );
    }
}