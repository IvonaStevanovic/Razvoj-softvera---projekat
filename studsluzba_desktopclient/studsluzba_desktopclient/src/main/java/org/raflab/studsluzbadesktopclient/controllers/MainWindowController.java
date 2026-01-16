package org.raflab.studsluzbadesktopclient.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import org.raflab.studsluzbadesktopclient.ContextFXMLLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.IOException;

@Controller
public class MainWindowController {

    @FXML
    private StackPane contentArea;

    @Autowired
    private ContextFXMLLoader contextLoader;

    private static MainWindowController instance;
    private java.util.Stack<Node> backStack = new java.util.Stack<>();
    public MainWindowController() {
        instance = this;
    }

    public static MainWindowController getInstance() {
        return instance;
    }

    @FXML
    public void initialize() {
        // Na početku možemo učitati pretragu ili ostaviti prazno
        openSearchStudent();
        contentArea.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.setOnKeyPressed(event -> {
                    // Prečica: Ctrl + [
                    if (event.isControlDown() && event.getCode() == KeyCode.OPEN_BRACKET) {
                        goBack();
                    }
                });
            }
        });
    }

    // --- Navigacija ---

    public void goBack() {
        if (!backStack.isEmpty()) {
            javafx.scene.Node previousView = backStack.pop();
            setView(previousView);
        }
    }
    /**
     * Glavna metoda za promenu ekrana.
     * Kasnije ćemo ovde dodati HistoryManager.record() za KORAK 2.
     */
    public void setView(javafx.scene.Node node) {
        contentArea.getChildren().clear();
        contentArea.getChildren().add(node);
    }

    // --- Akcije iz Menija ---

    @FXML
    public void handleSearchStudent() {
        openSearchStudent();
    }

    @FXML
    public void handleNewStudent() {
        // Implementacija za novi student view
        System.out.println("Otvaranje forme za novog studenta...");
    }

    // --- Helperi za učitavanje ---

    private void openSearchStudent() {
        try {
            if (contextLoader != null) {
                // FIX: Dodata puna putanja /fxml/ ispred imena fajla
                FXMLLoader loader = contextLoader.getLoader("/fxml/searchStudent.fxml");
                Parent view = loader.load();
                setView(view);
            } else {
                System.err.println("ContextFXMLLoader nije inject-ovan!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Metoda koju ćemo zvati kada kliknemo na studenta u pretrazi
     */
    public void openStudentProfile(Long studentId) {
        try {
            if (contextLoader != null) {
                // 1. Logika za Back (History) - prema specifikaciji
                // Ako imaš NavigationService, koristi ga. Ako ne, ostavi push.
                if (!contentArea.getChildren().isEmpty()) {
                    backStack.push(contentArea.getChildren().get(0));
                }

                // 2. Učitavanje FXML-a (proveri da li je putanja tačno ova)
                // Napomena: u prošloj poruci si poslala /fxml/student_profile.fxml,
                // a ovde studentPodaciTabPane.fxml. Koristi ono što ti je u resursima!
                FXMLLoader loader = contextLoader.getLoader("/fxml/studentPodaciTabPane.fxml");
                Parent view = loader.load();

                // 3. Povezivanje sa ispravnim kontrolerom
                // Izbrisali smo StudentProfileController, sada koristimo samo StudentController
                StudentController controller = loader.getController();

                if (controller != null) {
                    // Ova metoda u StudentController-u sada povlači podatke iz baze i zaključava polja
                    controller.loadStudentData(studentId);
                } else {
                    System.err.println("GRESKA: Kontroler nije uspešno učitan iz FXML-a!");
                }

                // 4. Prikaz na ekranu
                setView(view);

                // 5. Fokusiranje za prečice (Ctrl + [)
                view.requestFocus();
            }
        } catch (IOException e) {
            System.err.println("GRESKA prilikom otvaranja profila: " + e.getMessage());
            e.printStackTrace();
        }
    }
}