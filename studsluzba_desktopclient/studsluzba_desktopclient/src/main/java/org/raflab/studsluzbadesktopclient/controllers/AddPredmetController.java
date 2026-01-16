package org.raflab.studsluzbadesktopclient.controllers;

import javafx.fxml.FXML;

import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.raflab.studsluzbadesktopclient.dtos.PredmetRequest;
import org.raflab.studsluzbadesktopclient.dtos.StudijskiProgramResponse;
import org.raflab.studsluzbadesktopclient.services.StudProgramService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;



@Component
public class AddPredmetController {

    @FXML
    private TextField txtSifra;
    @FXML private TextField txtNaziv;
    @FXML private TextField txtEspb;
    @FXML private CheckBox chkObavezan;
    @FXML private Label lblProgramNaziv;

    @Autowired
    private StudProgramService studProgramService;

    private Long currentProgramId;

    public void setInitialProgram(StudijskiProgramResponse program) {
        this.currentProgramId = program.getId();
        lblProgramNaziv.setText("Program: " + program.getNaziv());
    }

    @FXML
    private void handleSave() {
        PredmetRequest request = new PredmetRequest();
        request.setSifra(txtSifra.getText());
        request.setNaziv(txtNaziv.getText());
        request.setEspb(Integer.parseInt(txtEspb.getText()));
        request.setObavezan(chkObavezan.isSelected());
        request.setStudijskiProgramId(currentProgramId);

        studProgramService.addPredmet(request,
                res -> {
                    // Uspeh
                    ((Stage) txtSifra.getScene().getWindow()).close();
                },
                err -> {
                    // Greška (npr. šifra već postoji)
                    Alert alert = new Alert(Alert.AlertType.ERROR, err);
                    alert.show();
                }
        );
    }
}
