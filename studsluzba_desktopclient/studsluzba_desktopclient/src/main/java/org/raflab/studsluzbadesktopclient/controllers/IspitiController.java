package org.raflab.studsluzbadesktopclient.controllers;

import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.raflab.studsluzbadesktopclient.ClientAppConfig;
import org.raflab.studsluzbadesktopclient.dtos.IspitResponse;
import org.raflab.studsluzbadesktopclient.dtos.IspitniRokResponse;
import org.raflab.studsluzbadesktopclient.dtos.PredmetResponse;
import org.raflab.studsluzbadesktopclient.dtos.StudentIspitRezultatiResponse;
import org.raflab.studsluzbadesktopclient.services.IspitiService;
import org.raflab.studsluzbadesktopclient.services.NavigationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Comparator;

@Component
public class IspitiController {

    @FXML private ComboBox<IspitniRokResponse> comboRokovi; // Novo
    @FXML private ListView<IspitResponse> listIspiti;
    @FXML private TableView<StudentIspitRezultatiResponse> tableRezultati;

    @FXML private TableColumn<StudentIspitRezultatiResponse, String> colProgram, colIndeks, colImePrezime, colPoeni;
    @FXML private TableColumn<StudentIspitRezultatiResponse, Integer> colOcena;
    @FXML
    private ComboBox<PredmetResponse> comboPredmet;
    @Autowired private IspitiService ispitService;
    @Autowired private NavigationService navigationService;
    @FXML private TextField txtGodinaOd; // Za početnu godinu (npr. 2020)
    @FXML private TextField txtGodinaDo; // Za krajnju godinu (npr. 2026)
    @FXML private Label lblProsekRezultat;
    @FXML
    private TableView<PredmetResponse> tablePredmeti;
    @FXML
    public void initialize() {
        setupTables();

        // 1. IZBOR ROKA PREKO COMBO BOX-A
        comboRokovi.getSelectionModel().selectedItemProperty().addListener((obs, oldV, newV) -> {
            if (newV != null) {
                // Kada izabereš rok, učitavaju se ispiti za taj rok
                ispitService.getIspitiByRok(newV.getId(), list -> {
                    listIspiti.getItems().setAll(list);
                    tableRezultati.getItems().clear(); // Čisti tabelu jer još nije izabran konkretan ispit
                });
                navigationService.recordTabChange(null, null);
            }
        });

        // 3. Ubaci listener za listu ispita (ako već nisi)
        listIspiti.getSelectionModel().selectedItemProperty().addListener((obs, oldV, newV) -> {
            if (newV != null) {
                // Kada izabereš konkretan ispit, učitava se zapisnik (rezultati)
                ucitajRezultate(newV.getId());
                navigationService.recordTabChange(null, null);
            }
        });
        ispitService.getAllPredmeti(predmeti -> {
            comboPredmet.setItems(FXCollections.observableArrayList(predmeti));
        });

        // Podešavanje kako se prikazuju predmeti u ComboBox-u (da piše naziv, a ne adresa u memoriji)
        comboPredmet.setCellFactory(lv -> new ListCell<PredmetResponse>() {
            @Override
            protected void updateItem(PredmetResponse item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getNaziv());
            }
        });
        comboPredmet.setButtonCell(comboPredmet.getCellFactory().call(null));
        // 4. Inicijalno učitaj sve rokove iz baze da bi ComboBox bio pun
        ispitService.getAllRokovi(list -> comboRokovi.getItems().setAll(list));
    }
    private void ucitajRezultate(Long ispitId) {
        ispitService.getRezultati(ispitId, rezultati -> {
            // SORTIRANJE (Specifikacija: Program -> Godina -> Broj)
            rezultati.sort(Comparator.comparing(StudentIspitRezultatiResponse::getStudijskiProgram)
                    .thenComparing(StudentIspitRezultatiResponse::getGodinaUpisa)
                    .thenComparing(res -> {
                        try { return Integer.parseInt(res.getBrojIndeksa()); }
                        catch (Exception e) { return 0; }
                    }));

            tableRezultati.getItems().setAll(rezultati);
        });
    }

    private void setupTables() {
        // Podesi kako se prikazuju rokovi u ComboBox-u
        comboRokovi.setCellFactory(lv -> new ListCell<IspitniRokResponse>() {
            @Override
            protected void updateItem(IspitniRokResponse item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getNaziv());
            }
        });
        comboRokovi.setButtonCell(comboRokovi.getCellFactory().call(null));

        // Podesi CellFactory za listIspiti (da piše naziv predmeta)
        listIspiti.setCellFactory(lv -> new ListCell<IspitResponse>() {
            @Override
            protected void updateItem(IspitResponse item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getPredmetNaziv());
            }
        });

        // Konfiguracija kolona tabele (koristeći tvoju novu DTO klasu)
        colProgram.setCellValueFactory(new PropertyValueFactory<>("studijskiProgram"));
        colImePrezime.setCellValueFactory(new PropertyValueFactory<>("studentImePrezime"));
        colIndeks.setCellValueFactory(new PropertyValueFactory<>("formatiranIndeks"));
        colPoeni.setCellValueFactory(new PropertyValueFactory<>("ukupnoPoena"));

        if (colOcena != null) {
            colOcena.setCellValueFactory(new PropertyValueFactory<>("ocena"));
        }
    }
    @FXML
    private void handlePrijava() {
        IspitResponse selected = listIspiti.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        TextInputDialog dialog = new TextInputDialog();
        dialog.setHeaderText("Prijava za: " + selected.getPredmetNaziv());
        dialog.setContentText("Unesite indeks (format: broj/godina):");

        dialog.showAndWait().ifPresent(unos -> {
            ispitService.prijaviStudenta(selected.getId(), unos,
                    () -> {
                        ucitajRezultate(selected.getId());
                        // Opciono: obaveštenje o uspehu
                        Platform.runLater(() -> new Alert(Alert.AlertType.INFORMATION, "Uspešna prijava!").show());
                    },
                    err -> {
                        // 'err' je sada očišćena poruka sa servera (npr. "Student ne sluša predmet")
                        new Alert(Alert.AlertType.ERROR, err).show();
                    }
            );
        });
    }
    @FXML
    private void handlePrintZapisnik() {
        IspitResponse selektovani = listIspiti.getSelectionModel().getSelectedItem();
        if (selektovani == null) {
            new Alert(Alert.AlertType.WARNING, "Prvo selektujte ispit iz liste levo!").show();
            return;
        }

        ispitService.preuzmiZapisnikPDF(selektovani.getId(), bytes -> {
            try {
                File tempFile = File.createTempFile("zapisnik_ispita_", ".pdf");
                java.nio.file.Files.write(tempFile.toPath(), bytes);

                if (java.awt.Desktop.isDesktopSupported()) {
                    java.awt.Desktop.getDesktop().open(tempFile);
                } else {
                    new ProcessBuilder("cmd", "/c", "start", tempFile.getAbsolutePath()).start();
                }
            } catch (IOException e) {
                new Alert(Alert.AlertType.ERROR, "Greška pri otvaranju PDF-a: " + e.getMessage()).show();
            }
        });
    }
    @FXML
    private void handleOpenAddIspit(ActionEvent event) {
        IspitniRokResponse trenutniRok = comboRokovi.getValue();
        if (trenutniRok == null) {
            new Alert(Alert.AlertType.WARNING, "Prvo izaberite ispitni rok!").show();
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/dodajIspitModal.fxml"));
            loader.setControllerFactory(ClientAppConfig.getContext()::getBean);
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Novi ispit");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));

            DodajIspitModalController controller = loader.getController();
            // Prosleđujemo ID roka i šta da uradi kad završi (refresh liste)
            controller.setParams(trenutniRok.getId(), stage, () -> {
                ispitService.getIspitiByRok(trenutniRok.getId(), list -> listIspiti.getItems().setAll(list));
            });

            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void handleOpenAddRok(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/dodajIspitniRokModal.fxml"));
            loader.setControllerFactory(org.raflab.studsluzbadesktopclient.ClientAppConfig.getContext()::getBean);
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Novi ispitni rok");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));

            // Ovo je onaj modalni kontroler koji smo ranije napravili
            DodajIspitniRokModalController controller = loader.getController();
            controller.setParams(stage, () -> {
                // Osvežavamo listu nakon dodavanja
                ispitService.getAllRokovi(list -> comboRokovi.getItems().setAll(list));
            });

            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void handlePrikaziPrijavljene() {
        // 1. Provera selekcije
        IspitResponse selected = listIspiti.getSelectionModel().getSelectedItem();
        if (selected == null) {
            new Alert(Alert.AlertType.WARNING, "Molimo izaberite ispit iz liste!").show();
            return;
        }

        // 2. Poziv servisa
        ispitService.getPrijavljeniZaIspit(selected.getId(), lista -> {
            try {
                // 3. Učitavanje popup-a
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/prijavljeniStudentiModal.fxml"));
                loader.setControllerFactory(ClientAppConfig.getContext()::getBean);
                Parent root = loader.load();

                // 4. Slanje podataka u kontroler popupa
                PrijavljeniStudentiModalController controller = loader.getController();
                controller.setData(lista);

                // 5. Kreiranje i prikaz prozora
                Stage stage = new Stage();
                stage.setTitle("Zapisnik - Prijavljeni studenti");
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setScene(new Scene(root));
                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Greška pri otvaranju prozora: " + e.getMessage()).show();
            }
        });
    }
    @FXML
    private void handleStampajIzvestaj() {
        PredmetResponse selektovani = tablePredmeti.getSelectionModel().getSelectedItem();

        if (selektovani == null) {
            new Alert(Alert.AlertType.WARNING, "Prvo selektujte predmet u tabeli!").show();
            return;
        }

        try {
            // Izvlačenje godina iz TextField-ova
            Integer odG = Integer.parseInt(txtGodinaOd.getText().trim());
            Integer doG = Integer.parseInt(txtGodinaDo.getText().trim());

            ispitService.preuzmiPDFSaParametrima(selektovani.getId(), odG, doG, bytes -> {
                try {
                    // Pravimo privremeni fajl
                    File tempFile = File.createTempFile("statistika_predmeta_", ".pdf");
                    // ISPRAVKA: Koristimo java.nio.file.Files.write sa ispravnim Path-om
                    java.nio.file.Files.write(tempFile.toPath(), bytes);

                    if (java.awt.Desktop.isDesktopSupported()) {
                        java.awt.Desktop.getDesktop().open(tempFile);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.ERROR, "Unesite ispravne godine u polja 'Od' i 'Do'!").show();
        }
    }

}