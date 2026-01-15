package org.raflab.studsluzbadesktopclient.controllers;

import org.raflab.studsluzbadesktopclient.services.NavigationService;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;


import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;

@Component
public class MainWindowController {
	
    private ApplicationContext context;
    private final NavigationService navigationService;
	@FXML
	private BorderPane mainPane;

    public MainWindowController(NavigationService navigationService) {
        this.navigationService = navigationService;
    }

    @FXML
    public void initialize() {
        // OVO JE KLJUČNA LINIJA: Povezujemo FXML sa servisom
        if (navigationService != null) {
            navigationService.setMainRoot(mainPane);
        }
    }
}