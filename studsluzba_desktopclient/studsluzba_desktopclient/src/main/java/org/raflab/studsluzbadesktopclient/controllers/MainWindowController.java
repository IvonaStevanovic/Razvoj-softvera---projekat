package org.raflab.studsluzbadesktopclient.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import org.raflab.studsluzbadesktopclient.MainView;
import org.raflab.studsluzbadesktopclient.ContextFXMLLoader;
import org.raflab.studsluzbadesktopclient.services.NavigationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.IOException;

@Controller
public class MainWindowController {

    @FXML
    private StackPane contentArea;

    @Autowired
    private MainView mainView;

    @Autowired
    private NavigationService navigationService;

    @Autowired
    private ContextFXMLLoader contextLoader; // Potrebno za koleginicinu logiku profiliranja

    private static MainWindowController instance;

    public MainWindowController() {
        instance = this;
    }

    public static MainWindowController getInstance() {
        return instance;
    }

    @FXML
    public void initialize() {
        // 1. TVOJA LOGIKA: Automatsko otvaranje pretrage na startu
        Parent searchView = mainView.loadPane("searchStudent");
        if (searchView != null) {
            contentArea.getChildren().setAll(searchView);
            navigationService.navigateTo(searchView);
        }

        // 2. KOLEGINICINA LOGIKA: Slušalac za prečicu Ctrl + [
        contentArea.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.setOnKeyPressed(event -> {
                    if (event.isControlDown() && event.getCode() == KeyCode.OPEN_BRACKET) {
                        navigationService.goBack(); // Koristimo tvoj servis umesto njenog stack-a
                    }
                });
            }
        });
    }

    /**
     * KOLEGINICINA METODA: Otvaranje profila studenta.
     * Integrisana da koristi tvoj NavigationService radi konzistentnosti.
     */
    public void openStudentProfile(Long studentId) {
        try {
            if (contextLoader != null) {
                // Učitavanje preko contextLoader-a kako bi se sačuvao Spring context
                FXMLLoader loader = contextLoader.getLoader("/fxml/studentPodaciTabPane.fxml");
                Parent view = loader.load();

                // Povezivanje sa kontrolerom i učitavanje podataka (non-editable)
                StudentController controller = loader.getController();
                if (controller != null) {
                    controller.loadStudentData(studentId);
                }

                // Navigacija preko tvog servisa (ovo menja njen setView i backStack.push)
                navigationService.navigateTo(view);

                view.requestFocus();
            }
        } catch (IOException e) {
            System.err.println("GRESKA prilikom otvaranja profila: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Pomoćne metode koje poziva MenuBar ili drugi delovi koda
    @FXML
    public void handleSearchStudent() {
        Parent view = mainView.loadPane("searchStudent");
        navigationService.navigateTo(view);
    }

    public void setView(javafx.scene.Node node) {
        contentArea.getChildren().clear();
        contentArea.getChildren().add(node);
    }
}