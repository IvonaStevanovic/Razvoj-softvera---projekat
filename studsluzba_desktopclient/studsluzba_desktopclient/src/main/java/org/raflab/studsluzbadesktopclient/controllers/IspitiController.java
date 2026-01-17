package org.raflab.studsluzbadesktopclient.controllers;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.raflab.studsluzbadesktopclient.ClientAppConfig;
import org.raflab.studsluzbadesktopclient.dtos.IspitResponse;
import org.raflab.studsluzbadesktopclient.dtos.IspitniRokResponse;
import org.raflab.studsluzbadesktopclient.dtos.StudentIspitRezultatiResponse;
import org.raflab.studsluzbadesktopclient.services.IspitiService;
import org.raflab.studsluzbadesktopclient.services.NavigationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Comparator;

@Component
public class IspitiController {

    @FXML private ComboBox<IspitniRokResponse> comboRokovi; // Novo
    @FXML private ListView<IspitResponse> listIspiti;
    @FXML private TableView<StudentIspitRezultatiResponse> tableRezultati;

    @FXML private TableColumn<StudentIspitRezultatiResponse, String> colProgram, colIndeks, colImePrezime, colPoeni;
    @FXML private TableColumn<StudentIspitRezultatiResponse, Integer> colOcena;

    @Autowired private IspitiService ispitService;
    @Autowired private NavigationService navigationService;

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
        if (selected == null) {
            new Alert(Alert.AlertType.WARNING, "Izaberite ispit!").show();
            return;
        }

        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Prijava");
        dialog.setHeaderText("Prijava za: " + selected.getPredmetNaziv());
        dialog.setContentText("ID indeksa:");

        dialog.showAndWait().ifPresent(idStr -> {
            try {
                ispitService.prijaviStudenta(selected.getId(), Long.parseLong(idStr),
                        () -> ucitajRezultate(selected.getId()),
                        err -> new Alert(Alert.AlertType.ERROR, err).show()
                );
            } catch (NumberFormatException e) {
                new Alert(Alert.AlertType.ERROR, "ID mora biti broj!").show();
            }
        });
    }

    @FXML
    private void handlePrintZapisnik() {
        IspitResponse selected = listIspiti.getSelectionModel().getSelectedItem();
        if (selected != null) {
            System.out.println("Generišem izveštaj za ispit: " + selected.getId());
        }
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
}