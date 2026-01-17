package org.raflab.studsluzbadesktopclient.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListCell;
import javafx.stage.Stage;
import org.raflab.studsluzbadesktopclient.dtos.IspitRequest;
import org.raflab.studsluzbadesktopclient.dtos.NastavnikResponse;
import org.raflab.studsluzbadesktopclient.dtos.PredmetResponse;
import org.raflab.studsluzbadesktopclient.services.IspitiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DodajIspitModalController {

    @FXML
    private ComboBox<PredmetResponse> comboPredmet;
    @FXML private ComboBox<NastavnikResponse> comboNastavnik;
    @FXML private DatePicker datePicker;

    @Autowired
    private IspitiService ispitService; // Trebaće ti metode za predmete/nastavnike

    private Long selektovaniRokId;
    private Stage dialogStage;
    private Runnable onSuccess;

    public void setParams(Long rokId, Stage stage, Runnable onSuccess) {
        this.selektovaniRokId = rokId;
        this.dialogStage = stage;
        this.onSuccess = onSuccess;
    }

    @FXML
    public void initialize() {
        // 1. Podešavanje prikaza za PREDMETE
        comboPredmet.setCellFactory(lv -> new ListCell<PredmetResponse>() {
            @Override
            protected void updateItem(PredmetResponse item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getNaziv());
            }
        });
        // Ovo osigurava da se naziv vidi i kada je stavka selektovana (zatvoren combo)
        comboPredmet.setButtonCell(comboPredmet.getCellFactory().call(null));

        // 2. Podešavanje prikaza za NASTAVNIKE
        comboNastavnik.setCellFactory(lv -> new ListCell<NastavnikResponse>() {
            @Override
            protected void updateItem(NastavnikResponse item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getIme() + " " + item.getPrezime());
            }
        });
        comboNastavnik.setButtonCell(comboNastavnik.getCellFactory().call(null));

        // 3. Učitavanje podataka (ako već nisi dodao)
        ispitService.getAllPredmeti(list -> comboPredmet.getItems().setAll(list));
        ispitService.getAllNastavnici(list -> comboNastavnik.getItems().setAll(list));
    }
    // DodajIspitModalController.java

    @FXML
    private void handleSave() {
        PredmetResponse p = comboPredmet.getValue();
        NastavnikResponse n = comboNastavnik.getValue();
        LocalDate datum = datePicker.getValue();

        if (p == null || n == null || datum == null) {
            new Alert(Alert.AlertType.WARNING, "Popunite sva polja!").show();
            return;
        }

        // Pozivamo server uz dodatu lambdu za obradu greške
        ispitService.getDrziPredmetId(p.getId(), n.getId(),
                drziId -> {
                    // --- LOGIKA ZA USPEH ---
                    IspitRequest request = new IspitRequest();
                    request.setDrziPredmetId(drziId);
                    request.setPredmetId(p.getId());
                    request.setIspitniRokId(selektovaniRokId);
                    request.setDatumOdrzavanja(datum);

                    ispitService.saveIspit(request, () -> {
                        Platform.runLater(() -> {
                            if (onSuccess != null) onSuccess.run();
                            dialogStage.close();
                        });
                    });
                },
                errorMessage -> {
                    // --- LOGIKA ZA GREŠKU (404 Not Found) ---
                    // Umesto čudne poruke u konzoli, sada iskače tvoj popup
                    Platform.runLater(() -> {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Greška pri kreiranju ispita");
                        alert.setHeaderText(null);
                        alert.setContentText("Izabrani nastavnik ne drži izabrani predmet!");
                        alert.show();
                    });
                }
        );
    }
    @FXML private void handleCancel() { dialogStage.close(); }
}