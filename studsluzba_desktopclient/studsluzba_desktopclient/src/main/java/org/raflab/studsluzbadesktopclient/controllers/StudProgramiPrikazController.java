package org.raflab.studsluzbadesktopclient.controllers;

import javafx.application.Platform;
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
import org.raflab.studsluzbadesktopclient.dtos.StudijskiProgramResponse;
import org.raflab.studsluzbadesktopclient.dtos.PredmetResponse;
import org.raflab.studsluzbadesktopclient.services.IspitiService;
import org.raflab.studsluzbadesktopclient.services.StudProgramService;
import org.raflab.studsluzbadesktopclient.services.NavigationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Component
public class StudProgramiPrikazController {

    @FXML private ListView<StudijskiProgramResponse> listProgrami;
    @FXML private TableView<PredmetResponse> tablePredmeti;
    @FXML private TableColumn<PredmetResponse, String> colSifra;
    @FXML private TableColumn<PredmetResponse, String> colNaziv;
    @FXML private TableColumn<StudijskiProgramResponse, String> colProgram;
    @FXML private TableColumn<PredmetResponse, Integer> colEspb;
    @FXML private TabPane tabPaneDesno;
    @Autowired
    private StudProgramService studProgramService;
    @Autowired
    private IspitiService ispitService;

    @Autowired
    private NavigationService navigationService;
    @FXML private TextField txtGodinaOd;
    @FXML private TextField txtGodinaDo;
    @FXML private Label lblProsekRezultat;


    @FXML
    public void initialize() {
        // 1. Kolone tabele predmeta
        colSifra.setCellValueFactory(new PropertyValueFactory<>("sifra"));
        colNaziv.setCellValueFactory(new PropertyValueFactory<>("naziv"));
        colEspb.setCellValueFactory(new PropertyValueFactory<>("espb"));

        // 2. OVO JE ZA LISTU PROGRAMA (Prekopiraj onaj gornji deo ovde)
        listProgrami.setCellFactory(lv -> new ListCell<StudijskiProgramResponse>() {
            @Override
            protected void updateItem(StudijskiProgramResponse item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getNaziv());
                }
            }
        });

        // 3. Učitavanje podataka sa servera
        studProgramService.getAllPrograms(list -> {
            listProgrami.getItems().setAll(list);
        });

        // 4. Listener za promenu selekcije (prikaz predmeta desno)
        listProgrami.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                prikaziPredmeteZaProgram(newVal);
            }
        });
        listProgrami.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                // Pre nego što učitamo nove podatke, beležimo trenutno stanje u istoriju
                // Ako tvoj NavigationService podržava snimanje Node-a, pozivamo navigateTo ili record
                prikaziPredmeteZaProgram(newVal);

                // OVO AKTIVIRA BACK/FORWARD:
                // Pošto menjaš sadržaj istog ekrana, NavigationService treba da zna
                // da je ovo nova tačka u istoriji.
                navigationService.recordTabChange(null, null);
            }
        });
        if (tabPaneDesno != null) {
            tabPaneDesno.getSelectionModel().selectedItemProperty().addListener((obs, oldTab, newTab) -> {
                // Pozivamo tvoju već implementiranu metodu za tabove
                navigationService.recordTabChange(tabPaneDesno, oldTab);
            });
        }
    }
    @FXML
    private void handleShowProsekPopUp() {
        PredmetResponse selektovani = tablePredmeti.getSelectionModel().getSelectedItem();
        if (selektovani == null) {
            new Alert(Alert.AlertType.WARNING, "Prvo selektujte predmet!").show();
            return;
        }

        try {
            Integer odG = Integer.parseInt(txtGodinaOd.getText().trim());
            Integer doG = Integer.parseInt(txtGodinaDo.getText().trim());

            // Pozivamo novi endpoint (koji treba da dodaš u servis) koji vraća LISTU detalja
            studProgramService.getDetaljnaStatistika(selektovani.getId(), odG, doG, listaPodataka -> {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/statistikaPredmetaModal.fxml"));
                    loader.setControllerFactory(ClientAppConfig.getContext()::getBean);
                    Parent root = loader.load();

                    StatistikaPredmetaModalController controller = loader.getController();
                    controller.setData(selektovani.getNaziv(), odG + "-" + doG, listaPodataka);

                    Stage stage = new Stage();
                    stage.setTitle("Detaljna statistika");
                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.setScene(new Scene(root));
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.ERROR, "Unesite ispravne godine!").show();
        }
    }
    private void prikaziPredmeteZaProgram(StudijskiProgramResponse program) {
        studProgramService.getPredmetiByProgram(program.getId(), predmeti -> {
            tablePredmeti.getItems().setAll(predmeti);

            // ISTA LOGIKA KAO KOD STUDENTA: Beležimo promenu u navigaciji
            // Prosleđujemo null za tabPane jer ovde možda koristiš ListView,
            // ali NavigationService će zabeležiti stanje
            navigationService.recordTabChange(null, null);
        });
    }
    @FXML
    private void handleOpenAddPredmetPopUp() {
        StudijskiProgramResponse selected = listProgrami.getSelectionModel().getSelectedItem();

        if (selected == null) {
            new Alert(Alert.AlertType.WARNING, "Prvo izaberite program sa leve strane!").show();
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/addPredmet.fxml"));
            // Povezujemo Spring context
            loader.setControllerFactory(ClientAppConfig.getContext()::getBean);

            Parent root = loader.load();
            AddPredmetController controller = loader.getController();

            // Prosleđujemo selektovani program u pop-up
            controller.setInitialProgram(selected);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Novi predmet za: " + selected.getNaziv());
            stage.initModality(Modality.APPLICATION_MODAL); // Blokira glavni prozor
            stage.showAndWait();

            // Čim se pop-up zatvori, osvežavamo tabelu
            prikaziPredmeteZaProgram(selected);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void handleOpenAddPredmetForm() {
        try {
            // Kreiramo novi prozor (Dialog)
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL); // Blokira glavni prozor dok se ovaj ne zatvori
            stage.setTitle("Dodaj novi predmet");

            // Učitavamo FXML za formu (napravićemo ga u sledećem koraku)
            // Koristimo tvoj ContextFXMLLoader kao i u ostatku projekta
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/addPredmet.fxml"));

            // Postavljamo Spring context loader (važno zbog @Autowired u kontroleru forme)
            loader.setControllerFactory(ClientAppConfig.getContext()::getBean);

            Parent root = loader.load();

            // Ako tvoja forma treba da zna za koji program dodajemo predmet:
            AddPredmetController controller = loader.getController();
            StudijskiProgramResponse selectedProgram = listProgrami.getSelectionModel().getSelectedItem();

            if (selectedProgram != null) {
                controller.setInitialProgram(selectedProgram);
            }

            stage.setScene(new Scene(root));
            stage.showAndWait(); // Čekamo da korisnik završi unos

            // Osvežavamo tabelu nakon zatvaranja prozora ako je dodat predmet
            if (selectedProgram != null) {
                prikaziPredmeteZaProgram(selectedProgram);
            }

        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Neuspešno učitavanje forme za dodavanje.");
            alert.show();
        }
    }
    @FXML
    private void handleStampajIzvestaj() {
        System.out.println("DEBUG: Dugme za štampu kliknuto.");

        PredmetResponse selektovani = tablePredmeti.getSelectionModel().getSelectedItem();
        if (selektovani == null) {
            System.out.println("DEBUG: Predmet nije selektovan u tabeli.");
            new Alert(Alert.AlertType.WARNING, "Prvo selektujte predmet u tabeli!").show();
            return;
        }

        try {
            String odGStr = txtGodinaOd.getText();
            String doGStr = txtGodinaDo.getText();

            System.out.println("DEBUG: Uneto Od: " + odGStr + ", Do: " + doGStr);

            if (odGStr == null || odGStr.isEmpty() || doGStr == null || doGStr.isEmpty()) {
                new Alert(Alert.AlertType.WARNING, "Unesite godine!").show();
                return;
            }

            Integer odG = Integer.parseInt(odGStr.trim());
            Integer doG = Integer.parseInt(doGStr.trim());

            System.out.println("DEBUG: Šaljem zahtev klijentskom servisu za predmet ID: " + selektovani.getId());

            ispitService.preuzmiPDFSaParametrima(selektovani.getId(), odG, doG, bytes -> {
                System.out.println("DEBUG: Bajtovi stigli sa servera. Veličina: " + (bytes != null ? bytes.length : "NULL"));

                if (bytes == null || bytes.length == 0) {
                    Platform.runLater(() -> new Alert(Alert.AlertType.ERROR, "Server je vratio prazan fajl.").show());
                    return;
                }

                try {
                    File tempFile = File.createTempFile("izvestaj_prosek_", ".pdf");
                    java.nio.file.Files.write(tempFile.toPath(), bytes);
                    System.out.println("DEBUG: Fajl sačuvan na: " + tempFile.getAbsolutePath());

                    if (Desktop.isDesktopSupported()) {
                        Desktop.getDesktop().open(tempFile);
                    } else {
                        new ProcessBuilder("cmd", "/c", "start", tempFile.getAbsolutePath()).start();
                    }
                } catch (IOException e) {
                    System.err.println("DEBUG: Greška pri čuvanju/otvaranju fajla: " + e.getMessage());
                }
            });
        } catch (NumberFormatException e) {
            System.err.println("DEBUG: Greška u formatu godine: " + e.getMessage());
            new Alert(Alert.AlertType.ERROR, "Godine moraju biti brojevi!").show();
        } catch (Exception e) {
            System.err.println("DEBUG: Opšta greška: " + e.getMessage());
            e.printStackTrace();
        }
    }
}