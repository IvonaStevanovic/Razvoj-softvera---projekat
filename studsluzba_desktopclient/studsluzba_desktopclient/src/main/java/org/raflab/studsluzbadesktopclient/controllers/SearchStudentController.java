package org.raflab.studsluzbadesktopclient.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.raflab.studsluzbadesktopclient.dtos.SrednjaSkolaDTO;
import org.raflab.studsluzbadesktopclient.dtos.StudentPodaciResponse;
import org.raflab.studsluzbadesktopclient.services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class SearchStudentController {

    @FXML private TextField filterIndeks;
    @FXML private TextField filterIme;
    @FXML private TextField filterPrezime;
    @FXML private ComboBox<SrednjaSkolaDTO> comboSrednjaSkola;

    @FXML private TableView<StudentPodaciResponse> studentsTable;
    @FXML private TableColumn<StudentPodaciResponse, String> colIndeks;
    @FXML private TableColumn<StudentPodaciResponse, String> colIme;
    @FXML private TableColumn<StudentPodaciResponse, String> colPrezime;
    @FXML private TableColumn<StudentPodaciResponse, String> colEmail;
    @FXML private TableColumn<StudentPodaciResponse, String> colSrednjaSkola;
    @FXML private TableColumn<StudentPodaciResponse, String> colJmbg;
    @Autowired
    private StudentService studentService; // Inject-ovan servis

    @FXML
    public void initialize() {
        setupTableColumns();
        setupRowFactory();
        loadSrednjeSkole();
    }

    private void setupTableColumns() {
        // 1. Kolona za INDEKS (Format: Broj / Godina)
        colIndeks.setCellValueFactory(cellData -> {
            StudentPodaciResponse s = cellData.getValue();

            // IZMENA: Proveravamo da li je != 0 umesto != null
            if (s.getBrojIndeksa() != 0 && s.getGodinaUpisa() != 0) {
                return new SimpleStringProperty(s.getBrojIndeksa() + "/" + s.getGodinaUpisa());
            }
            return new SimpleStringProperty("");
        });
        // 2. Standardne kolone
        colIme.setCellValueFactory(new PropertyValueFactory<>("ime"));
        colPrezime.setCellValueFactory(new PropertyValueFactory<>("prezime"));

        // 3. Email (sa zaštitom od null vrednosti)
        colEmail.setCellValueFactory(cellData -> {
            String email = cellData.getValue().getEmailFakultet();
            // Ako je email null, prikazujemo prazno, inače prikazujemo vrednost
            return new SimpleStringProperty(email != null ? email : "");
        });
        colJmbg.setCellValueFactory(cellData ->
                        new SimpleStringProperty(cellData.getValue().getJmbg() != null ? cellData.getValue().getJmbg() : ""));
        // 4. Srednja škola
        colSrednjaSkola.setCellValueFactory(new PropertyValueFactory<>("srednjaSkola"));
    }

    private void setupRowFactory() {
        studentsTable.setRowFactory(tv -> {
            TableRow<StudentPodaciResponse> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    StudentPodaciResponse rowData = row.getItem();
                    openProfile(rowData);
                }
            });
            return row;
        });
    }

    private void loadSrednjeSkole() {
        // TODO: Povezati sa SrednjaSkolaService
    }

    public void handleSearch() {
        String indeksUnos = filterIndeks.getText();
        String ime = filterIme.getText();
        String prezime = filterPrezime.getText();
        String skola = (comboSrednjaSkola.getValue() != null) ? comboSrednjaSkola.getValue().getNaziv() : null;

        if (studentService == null) {
            System.err.println("StudentService nije inject-ovan!");
            return;
        }

        // LOGIKA: Ako je unet indeks, on ima prioritet
        if (indeksUnos != null && !indeksUnos.trim().isEmpty()) {
            System.out.println("Vrsim pretragu iskljucivo po indeksu: " + indeksUnos);

            // Pozivamo tvoju POSTOJEĆU metodu iz StudentService-a
            // Šaljemo null za ime i prezime da bi server znao da filtrira samo po indeksu
            List<StudentPodaciResponse> rezultati = studentService.searchStudents(null, null, indeksUnos, null);

            // Ručno popravljamo prikaz godine na klijentu ako server vrati 0
            if (indeksUnos.contains("/") && !rezultati.isEmpty()) {
                try {
                    int unesenaGodina = Integer.parseInt(indeksUnos.split("/")[1].trim());
                    rezultati.get(0).setGodinaUpisa(unesenaGodina);
                } catch (Exception e) { /* Ignorisemo format */ }
            }

            studentsTable.setItems(FXCollections.observableArrayList(rezultati));
        }
        // Inače, radi standardnu pretragu po imenu/školi
        else {
            System.out.println("Vrsim standardnu pretragu: " + ime + " " + prezime);
            List<StudentPodaciResponse> rezultati = studentService.searchStudents(ime, prezime, null, skola);
            studentsTable.setItems(FXCollections.observableArrayList(rezultati));
        }
    }
    @FXML
    public void handleClear() {
        filterIndeks.clear();
        filterIme.clear();
        filterPrezime.clear();
        if (comboSrednjaSkola != null) comboSrednjaSkola.getSelectionModel().clearSelection();
        studentsTable.setItems(FXCollections.emptyObservableList());
    }

    private void openProfile(StudentPodaciResponse student) {
        if (MainWindowController.getInstance() != null) {
            MainWindowController.getInstance().openStudentProfile(student.getId());
        }
    }
}