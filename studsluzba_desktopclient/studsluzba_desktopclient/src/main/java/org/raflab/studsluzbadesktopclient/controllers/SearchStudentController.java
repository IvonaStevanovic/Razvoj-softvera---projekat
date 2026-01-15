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
        colEmail.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getEmailFakultet() != null ? data.getValue().getEmailFakultet() : "")
        );

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

    @FXML
    public void handleSearch() {
        String ime = filterIme.getText();
        String prezime = filterPrezime.getText();
        String skola = (comboSrednjaSkola.getValue() != null) ? comboSrednjaSkola.getValue().getNaziv() : null;
        String indeks = filterIndeks.getText();

        if (studentService != null) {
            // Moraš ažurirati metodu u servisu na klijentu da prima i indeks!
            // Pretpostavka: tvoj klijentski servis sada ima parametar za indeks
            List<StudentPodaciResponse> rezultati = studentService.searchStudents(ime, prezime, indeks, null);

            studentsTable.setItems(FXCollections.observableArrayList(rezultati));
        }
        System.out.println("Pretraga baze: " + ime + " " + prezime);

        if (studentService != null) {
            // Pozivamo pravi servis
            List<StudentPodaciResponse> rezultati = studentService.searchStudents(ime, prezime, null, skola);
            studentsTable.setItems(FXCollections.observableArrayList(rezultati));
        } else {
            System.err.println("StudentService nije inject-ovan!");
        }
    }

    @FXML
    public void handleClear() {
        filterIndeks.clear();
        filterIme.clear();
        filterPrezime.clear();
        comboSrednjaSkola.getSelectionModel().clearSelection();
        handleSearch(); // Osveži tabelu (prikaži sve)
    }

    private void openProfile(StudentPodaciResponse student) {
        if (MainWindowController.getInstance() != null) {
            MainWindowController.getInstance().openStudentProfile(student.getId());
        }
    }
}