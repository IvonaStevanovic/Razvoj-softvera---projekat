package org.raflab.studsluzbadesktopclient.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.raflab.studsluzbadesktopclient.dtos.IspitniRokRequest;
import org.raflab.studsluzbadesktopclient.services.IspitiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DodajIspitniRokModalController {
    @FXML
    private TextField txtNaziv;
    @FXML private DatePicker dpPocetak, dpKraj;

    @Autowired
    private IspitiService ispitService;

    private Stage stage;
    private Runnable onSuccess;

    public void setParams(Stage stage, Runnable onSuccess) {
        this.stage = stage;
        this.onSuccess = onSuccess;
    }

    @FXML
    private void handleSave() {
        if (txtNaziv.getText().isEmpty() || dpPocetak.getValue() == null || dpKraj.getValue() == null) {
            new Alert(Alert.AlertType.WARNING, "Sva polja su obavezna!").show();
            return;
        }

        IspitniRokRequest req = new IspitniRokRequest();
        req.setNaziv(txtNaziv.getText());
        req.setPocetak(dpPocetak.getValue()); // Proveri da li je datumPocetka ili samo pocetak
        req.setKraj(dpKraj.getValue());
        req.setSkolskaGodinaId(1L); // Privremeno hardkodovano, kasnije poveži sa bazom

        ispitService.saveIspitniRok(req, () -> {
            Platform.runLater(() -> {
                onSuccess.run(); // Ovo osvežava ComboBox u glavnom prozoru
                stage.close();
            });
        });
    }
    @FXML private void handleCancel() { stage.close(); }
}
