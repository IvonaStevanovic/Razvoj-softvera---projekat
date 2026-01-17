package org.raflab.studsluzbadesktopclient.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.raflab.studsluzbadesktopclient.dtos.StudentPodaciResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PrijavljeniStudentiModalController {

    @FXML private TableView<StudentPodaciResponse> tablePrijavljeni;
    @FXML private TableColumn<StudentPodaciResponse, String> colIndeks, colIme, colPrezime;

    @FXML
    public void initialize() {
        // Podesavamo kako se pune kolone
        colIndeks.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getBrojIndeksa() + "/" + cellData.getValue().getGodinaUpisa()));
        colIme.setCellValueFactory(new PropertyValueFactory<>("ime"));
        colPrezime.setCellValueFactory(new PropertyValueFactory<>("prezime"));
    }

    public void setData(List<StudentPodaciResponse> lista) {
        tablePrijavljeni.getItems().setAll(lista);
    }

    @FXML
    private void handleClose(ActionEvent event) {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.close();
    }
}