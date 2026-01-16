package org.raflab.studsluzbadesktopclient.controllers;

import javafx.fxml.FXML;

import javafx.scene.control.*;
import javafx.stage.Stage;
import org.raflab.studsluzbadesktopclient.dtos.PredmetRequest;
import org.raflab.studsluzbadesktopclient.dtos.StudijskiProgramResponse;
import org.raflab.studsluzbadesktopclient.services.StudProgramService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class AddPredmetController {

    @FXML private TextField txtSifra;
    @FXML private TextField txtNaziv;
    @FXML private TextField txtEspb;
    @FXML private TextField txtFondPredavanja; // NOVO
    @FXML private TextField txtFondVezbe;      // NOVO
    @FXML private TextArea txtOpis;            // NOVO (promeni u FXML u TextArea ako želiš više teksta)
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
        // Provera obaveznih polja pre nego što krenemo dalje
        if (txtSifra.getText().isEmpty() || txtNaziv.getText().isEmpty() || txtEspb.getText().isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Polja Šifra, Naziv i ESPB su obavezna!").show();
            return;
        }

        PredmetRequest request = new PredmetRequest();
        request.setSifra(txtSifra.getText());
        request.setNaziv(txtNaziv.getText());
        request.setObavezan(chkObavezan.isSelected());
        request.setStudijskiProgramId(currentProgramId);

        // Bezbedno parsiranje ESPB
        try {
            request.setEspb(Integer.parseInt(txtEspb.getText()));
        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.WARNING, "ESPB mora biti broj!").show();
            return;
        }

        // BEZBEDNO ČITANJE OPCIONIH POLJA (Ovo sprečava NullPointerException)
        // Ako polja ne postoje u FXML-u, postaviće se podrazumevane vrednosti (null ili 0)
        request.setOpis(txtOpis != null ? txtOpis.getText() : "");
        request.setFondPredavanja(txtFondPredavanja != null ? parseInteger(txtFondPredavanja.getText()) : 0);
        request.setFondVezbe(txtFondVezbe != null ? parseInteger(txtFondVezbe.getText()) : 0);

        studProgramService.addPredmet(request,
                res -> {
                    // Uspeh - zatvaramo pop-up
                    ((Stage) txtSifra.getScene().getWindow()).close();
                },
                err -> {
                    // Greška sa servera
                    Alert alert = new Alert(Alert.AlertType.ERROR, err);
                    alert.show();
                }
        );
    }

    // Pomoćna metoda za bezbedno parsiranje brojeva
    private Integer parseInteger(String value) {
        try {
            return (value == null || value.trim().isEmpty()) ? 0 : Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    // Provera pre slanja
    private boolean validateInput() {
        if (txtSifra.getText().isEmpty() || txtNaziv.getText().isEmpty() || txtEspb.getText().isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Polja Šifra, Naziv i ESPB su obavezna!").show();
            return false;
        }
        try {
            Integer.parseInt(txtEspb.getText());
        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.WARNING, "ESPB mora biti broj!").show();
            return false;
        }
        return true;
    }

    @FXML
    private void handleCancel() {
        ((Stage) txtSifra.getScene().getWindow()).close();
    }

}