package org.raflab.studsluzbadesktopclient.controllers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import org.raflab.studsluzbadesktopclient.MainView;
import org.raflab.studsluzbadesktopclient.coder.CoderFactory;
import org.raflab.studsluzbadesktopclient.coder.CoderType;
import org.raflab.studsluzbadesktopclient.coder.SimpleCode;
import org.raflab.studsluzbadesktopclient.dtos.*;
import org.raflab.studsluzbadesktopclient.services.NavigationService;
import org.raflab.studsluzbadesktopclient.services.SifarniciService;
import org.raflab.studsluzbadesktopclient.services.StudentService;
import org.springframework.stereotype.Component;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.raflab.studsluzbadesktopclient.dtos.*;

import java.util.ArrayList;
import java.util.List;


@Component
public class StudentController {

    private final StudentService studentService;
    private final CoderFactory coderFactory;
    private final SifarniciService sifarniciService;
    private final NavigationService navigationService;

    // FXML Elementi (ostaju isti)
    @FXML private TextField imeTf, prezimeTf, srednjeImeTf, jmbgTf, nacionalnostTf, brojLicneKarteTf, adresaTf, emailPrivatniTf, emailFakultetTf, brojTelefonaTf, godinaUpisaTf, brojIndeksaTf, godinaIndeksaTf, uspehSrednjaSkolaTf, uspehPrijemniTf;
    @FXML private RadioButton muski, zenski;
    @FXML private DatePicker datumRodjenjaDp, datumAktivacijeDp;
    @FXML private ComboBox<SimpleCode> mestoRodjenjaCb, mestoStanovanjaCb, drzavaRodjenjaCb, drzavljanstvoCb;
    @FXML private ComboBox<SrednjaSkolaResponse> srednjaSkolaCb;
    @FXML private Label labelError, ukupnoEspbLabel, prosekLabel;
    @FXML private VBox korenskiVBox;
    @FXML private TabPane profilTabPane;
    @FXML private TableView<PolozeniPredmetiResponse> polozeniTable;
    @FXML private TableView<NepolozeniPredmetDTO> nepolozeniTable;
    @FXML private TableView<UplataResponse> uplateTable;
    @FXML private TableColumn<PolozeniPredmetiResponse, String> predmetPolozioCol, datumPolaganjaCol;
    @FXML private TableColumn<PolozeniPredmetiResponse, Integer> ocenaCol, espbPolozioCol;
    @FXML private TableColumn<NepolozeniPredmetDTO, String> predmetNepolozenCol;
    @FXML private TableColumn<NepolozeniPredmetDTO, Integer> espbNepolozenCol;
    @FXML private TableColumn<UplataResponse, String> datumUplateCol, svrhaCol;
    @FXML private TableColumn<UplataResponse, Double> iznosCol;

    public StudentController(StudentService studentService, CoderFactory coderFactory,
                             SifarniciService sifarniciService,
                             NavigationService navigationService) {
        this.studentService = studentService;
        this.coderFactory = coderFactory;
        this.sifarniciService = sifarniciService;
        this.navigationService = navigationService;
    }

    @FXML
    public void initialize() {
        setupCoders();
        setupTableColumns();
        updateSrednjeSkole();
        if (profilTabPane != null) {
            profilTabPane.getSelectionModel().selectedItemProperty().addListener((obs, oldTab, newTab) -> {
                // Servis će sam proveriti da li treba da snimi promenu na osnovu isNavigating flega
                navigationService.recordTabChange(profilTabPane, oldTab);
            });
        }
    }

    @FXML
    public void handleBack(ActionEvent event) {
        // Sada pozivamo servis koji ima sačuvanu pretragu u steku
        navigationService.goBack();
    }

    public void loadStudentData(Long studentId) {
        try {
            StudentPodaciResponse student = studentService.getStudentById(studentId);
            if (student == null) return;

            popuniFormuIzBaze(student);

            List<PolozeniPredmetiResponse> polozeni = studentService.getPolozeniIspiti(studentId);
            List<UplataResponse> uplate = studentService.getUplate(studentId);

            if (polozeniTable != null) {
                polozeniTable.setItems(FXCollections.observableArrayList(polozeni != null ? polozeni : FXCollections.emptyObservableList()));
                obracunajAkademskiStatus(polozeni);
            }

            if (student.getBrojIndeksa() > 0 && nepolozeniTable != null) {
                List<NepolozeniPredmetDTO> nepolozeni = studentService.getNepolozeniIspiti(student.getBrojIndeksa());
                nepolozeniTable.setItems(FXCollections.observableArrayList(nepolozeni != null ? nepolozeni : FXCollections.emptyObservableList()));
            }

            if (uplateTable != null) {
                uplateTable.setItems(FXCollections.observableArrayList(uplate != null ? uplate : FXCollections.emptyObservableList()));
            }

            // Zahtevanje fokusa da bi prečice radile odmah po učitavanju
            Platform.runLater(() -> {
                if (korenskiVBox != null) {
                    korenskiVBox.setFocusTraversable(true);
                    korenskiVBox.requestFocus();
                }
            });

        } catch (Exception e) {
            if (labelError != null) labelError.setText("Greška pri osvežavanju podataka.");
            e.printStackTrace();
        }
    }
    @FXML
    void handleForward(ActionEvent event) {
        navigationService.goForward();
    }
    private void obracunajAkademskiStatus(List<PolozeniPredmetiResponse> polozeni) {
        int ukupnoEspb = 0;
        double sumaOcena = 0;
        int brojPolozenih = 0;

        if (polozeni != null) {
            for (PolozeniPredmetiResponse p : polozeni) {
                if (p.getOcena() != null && p.getOcena() > 5) {
                    ukupnoEspb += (p.getEspb() != null) ? p.getEspb() : 0;
                    sumaOcena += p.getOcena();
                    brojPolozenih++;
                }
            }
        }
        if (ukupnoEspbLabel != null) ukupnoEspbLabel.setText(String.valueOf(ukupnoEspb));
        if (prosekLabel != null) prosekLabel.setText(brojPolozenih > 0 ? String.format("%.2f", sumaOcena / brojPolozenih) : "0.00");
    }

    private void setupCoders() {
        if (drzavaRodjenjaCb != null) drzavaRodjenjaCb.setItems(FXCollections.observableArrayList(coderFactory.getSimpleCoder(CoderType.DRZAVA).getCodes()));
        if (drzavljanstvoCb != null) drzavljanstvoCb.setItems(FXCollections.observableArrayList(coderFactory.getSimpleCoder(CoderType.DRZAVA).getCodes()));
        if (mestoRodjenjaCb != null) mestoRodjenjaCb.setItems(FXCollections.observableArrayList(coderFactory.getSimpleCoder(CoderType.MESTO).getCodes()));
        if (mestoStanovanjaCb != null) mestoStanovanjaCb.setItems(FXCollections.observableArrayList(coderFactory.getSimpleCoder(CoderType.MESTO).getCodes()));
    }

    private void setupTableColumns() {
        if (predmetPolozioCol != null) predmetPolozioCol.setCellValueFactory(new PropertyValueFactory<>("nazivPredmeta"));
        if (ocenaCol != null) ocenaCol.setCellValueFactory(new PropertyValueFactory<>("ocena"));
        if (datumPolaganjaCol != null) datumPolaganjaCol.setCellValueFactory(new PropertyValueFactory<>("datumPolaganja"));
        if (espbPolozioCol != null) espbPolozioCol.setCellValueFactory(new PropertyValueFactory<>("espb"));
        if (predmetNepolozenCol != null) predmetNepolozenCol.setCellValueFactory(new PropertyValueFactory<>("nazivPredmeta"));
        if (espbNepolozenCol != null) espbNepolozenCol.setCellValueFactory(new PropertyValueFactory<>("espb"));
    }

    public void popuniFormuIzBaze(StudentPodaciResponse student) {
        postaviTekstIZakljucaj(imeTf, student.getIme());
        postaviTekstIZakljucaj(prezimeTf, student.getPrezime());
        postaviTekstIZakljucaj(jmbgTf, student.getJmbg());
        postaviTekstIZakljucaj(brojIndeksaTf, String.valueOf(student.getBrojIndeksa()));
        postaviTekstIZakljucaj(godinaUpisaTf, String.valueOf(student.getGodinaUpisa()));
        postaviTekstIZakljucaj(srednjeImeTf, student.getSrednjeIme());
        postaviTekstIZakljucaj(adresaTf, student.getAdresa());
        postaviTekstIZakljucaj(nacionalnostTf, student.getNacionalnost());
        postaviTekstIZakljucaj(brojLicneKarteTf, student.getBrojLicneKarte());

        if (datumRodjenjaDp != null) {
            datumRodjenjaDp.setValue(student.getDatumRodjenja());
            datumRodjenjaDp.setDisable(true);
        }

        if (student.getPol() != null) {
            String polStr = String.valueOf(student.getPol());
            if (polStr.equalsIgnoreCase("M")) {
                if (muski != null) { muski.setSelected(true); muski.setDisable(true); }
                if (zenski != null) zenski.setDisable(true);
            } else {
                if (zenski != null) { zenski.setSelected(true); zenski.setDisable(true); }
                if (muski != null) muski.setDisable(true);
            }
        }
    }

    private void postaviTekstIZakljucaj(TextField tf, String vrednost) {
        if (tf != null) {
            tf.setText(vrednost != null ? vrednost : "");
            tf.setEditable(false);
            tf.setStyle("-fx-background-color: #eeeeee;");
        }
    }

    public void updateSrednjeSkole() {
        try {
            srednjaSkolaCb.setItems(FXCollections.observableArrayList(sifarniciService.getSrednjeSkole()));
        } catch (Exception e) {
            if (labelError != null) labelError.setText(e.getMessage());
        }
    }
    @FXML
    public void handleUverenje(ActionEvent event) {
        // Ovdje ćeš kasnije dodati logiku za JasperReports
        System.out.println("Uverenje generisanje pokrenuto...");
    }
    @FXML
    public void handleKeyPressed(KeyEvent event) {
        if (event.isControlDown() && event.getCode().toString().equals("OPEN_BRACKET")) {
            navigationService.goBack();
        }
    }
    @FXML
    void handleUpisGodine(ActionEvent event) {
        System.out.println("Upis godine kliknut.");
    }

    @FXML
    void handleObnovaGodine(ActionEvent event) {
        System.out.println("Obnova godine kliknuta.");
    }
}