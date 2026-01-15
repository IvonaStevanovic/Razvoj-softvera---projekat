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
                // A) PAMTITMO ISTORIJU (Back funkcionalnost)
                if (!contentArea.getChildren().isEmpty()) {
                    backStack.push(contentArea.getChildren().get(0));
                }

                // B) UČITAVAMO FXML KOJI SI POSLALA
                FXMLLoader loader = contextLoader.getLoader("/fxml/studentPodaciTabPane.fxml");
                Parent view = loader.load();

                // C) PROSLEĐUJEMO PODATKE KONTROLERU
                Object controller = loader.getController();

                // *** OVDE JE BILA GREŠKA - SADA JE ISPRAVLJENO ***
                // Tvoj FXML kaže da je kontroler 'StudentController', pa to i koristimo.
                if (controller instanceof StudentController) {
                    ((StudentController) controller).loadStudentData(studentId);
                } else {
                    System.err.println("Greska: Ocekivan StudentController, dobijen: " + controller.getClass().getName());
                }

                // D) PRIKAZUJEMO NOVI EKRAN
                setView(view);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}