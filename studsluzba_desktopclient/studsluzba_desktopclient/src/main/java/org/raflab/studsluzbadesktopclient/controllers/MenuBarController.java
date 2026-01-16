package org.raflab.studsluzbadesktopclient.controllers;

import javafx.scene.Parent;
import org.raflab.studsluzbadesktopclient.MainView;
import org.raflab.studsluzbadesktopclient.services.NavigationService;
import org.raflab.studsluzbadesktopclient.services.StudentService;
import org.springframework.stereotype.Component;

import javafx.fxml.FXML;
import javafx.scene.control.MenuBar;
@Component
public class MenuBarController {

    private final MainView mainView;
    private final NavigationService navigationService; // Dodato

    @FXML
    private MenuBar menuBar;

    // Inject-ujemo i NavigationService
    public MenuBarController(MainView mainView, NavigationService navigationService){
        this.mainView = mainView;
        this.navigationService = navigationService;
    }

    public void openSearchStudent() {
        // 1. Učitaj panel
        Parent view = mainView.loadPane("searchStudent");
        // 2. KLJUČ: Prosledi ga navigaciji da bi se sačuvao u history stack!
        navigationService.navigateTo(view);
    }

    public void openNewStudent() {
        Parent view = mainView.loadPane("newStudent");
        navigationService.navigateTo(view);
    }

    public void openEventsPage(){
        Parent view = mainView.loadPane("events");
        navigationService.navigateTo(view);
    }

    public void openReportsPage(){
        Parent view = mainView.loadPane("reports");
        navigationService.navigateTo(view);
    }

    @FXML
    public void initialize() {
    }
}