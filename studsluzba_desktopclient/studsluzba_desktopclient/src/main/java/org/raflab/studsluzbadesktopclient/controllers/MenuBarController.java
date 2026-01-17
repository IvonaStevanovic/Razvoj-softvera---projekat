package org.raflab.studsluzbadesktopclient.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuBar;
import org.raflab.studsluzbadesktopclient.ClientAppConfig;
import org.raflab.studsluzbadesktopclient.MainView;
import org.raflab.studsluzbadesktopclient.services.NavigationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class MenuBarController {

    private final MainView mainView;
    private final NavigationService navigationService;

    @FXML
    private MenuBar menuBar;

    @Autowired
    public MenuBarController(MainView mainView, NavigationService navigationService) {
        this.mainView = mainView;
        this.navigationService = navigationService;
    }

    @FXML
    public void openSearchStudent() {
        Parent view = mainView.loadPane("searchStudent");
        navigationService.navigateTo(view);
    }

    @FXML
    public void openNewStudent() {
        Parent view = mainView.loadPane("newStudent");
        navigationService.navigateTo(view);
    }

    @FXML
    public void openReportsPage() {
        Parent view = mainView.loadPane("reports");
        navigationService.navigateTo(view);
    }
    @FXML
    private void otvoriStudijskePrograme() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/programiPrikaz.fxml"));
            loader.setControllerFactory(ClientAppConfig.getContext()::getBean);
            Parent view = loader.load(); // Ovde definišeš 'view'
            navigationService.navigateTo(view); // Ovde ga šalješ u navigaciju
        } catch (IOException e) { e.printStackTrace(); }
    }
    @FXML
    private void otvoriIspite() {
        try {
            // 1. Učitavamo FXML za ispite
            // Koristimo tvoj appFXMLLoader ili standardni loader u zavisnosti od tvoje implementacije
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ispitiPrikaz.fxml"));

            // 2. Povezujemo Spring context (ako koristiš Spring)
            loader.setControllerFactory(ClientAppConfig.getContext()::getBean);

            Parent view = loader.load();

            // 3. Pozivamo navigaciju
            // navigateTo će staviti trenutni ekran na backStack i prikazati Ispite
            navigationService.navigateTo(view);

        } catch (IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Greška pri otvaranju ekrana za ispite!").show();
        }
    }
    @FXML
    public void handleBack() {
        navigationService.goBack();
    }
}