package org.raflab.studsluzbadesktopclient.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.raflab.studsluzbadesktopclient.dtos.ProsekIzvestajResponse;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class StatistikaPredmetaModalController {

    @FXML private Label lblNaslov;
    @FXML private Label lblUkupanProsek;
    @FXML private TableView<ProsekIzvestajResponse> tableStatistika;
    @FXML private TableColumn<ProsekIzvestajResponse, String> colIndeks;
    @FXML private TableColumn<ProsekIzvestajResponse, String> colStudent;
    @FXML private TableColumn<ProsekIzvestajResponse, Integer> colOcena;

    @FXML
    public void initialize() {
        colIndeks.setCellValueFactory(new PropertyValueFactory<>("studentIndeks"));
        colStudent.setCellValueFactory(new PropertyValueFactory<>("studentImePrezime"));
        colOcena.setCellValueFactory(new PropertyValueFactory<>("ocena"));
    }

    public void setData(String predmet, String period, List<ProsekIzvestajResponse> podaci) {
        lblNaslov.setText("Statistika za: " + predmet + " (" + period + ")");
        tableStatistika.getItems().setAll(podaci);

        if (!podaci.isEmpty()) {
            lblUkupanProsek.setText(String.format("Ukupan prosek u ovom periodu: %.2f", podaci.get(0).getProsek()));
        }
    }

    @FXML private void handleClose() {
        ((Stage) lblNaslov.getScene().getWindow()).close();
    }
}