package org.raflab.studsluzbadesktopclient.controllers;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.MenuBar;
import org.raflab.studsluzbadesktopclient.MainView;
import org.raflab.studsluzbadesktopclient.services.NavigationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
    public void handleBack() {
        navigationService.goBack();
    }
}