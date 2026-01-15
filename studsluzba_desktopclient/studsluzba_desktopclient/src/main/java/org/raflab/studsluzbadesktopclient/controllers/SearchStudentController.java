package org.raflab.studsluzbadesktopclient.controllers;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.raflab.studsluzbadesktopclient.MainView;
import org.raflab.studsluzbadesktopclient.dtos.SrednjaSkolaDTO;
import org.raflab.studsluzbadesktopclient.dtos.StudentPodaciResponse;
import org.raflab.studsluzbadesktopclient.services.NavigationService;
import org.raflab.studsluzbadesktopclient.services.SifarniciService;
import org.raflab.studsluzbadesktopclient.services.StudentService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SearchStudentController {

    private final StudentService studentService;
    private final SifarniciService sifarniciService;
    private final MainView mainView;
    private final StudentController studentController;

    // USKLAĐENO SA FXML-om: fx:id="imeStudentaTf"
    @FXML private TextField imeStudentaTf;

    // USKLAĐENO SA FXML-om: fx:id="tabelaStudenti"
    @FXML private TableView<StudentPodaciResponse> tabelaStudenti;

    @FXML private TableColumn<StudentPodaciResponse, String> imeColumn;
    @FXML private TableColumn<StudentPodaciResponse, String> prezimeColumn;
    @FXML private TableColumn<StudentPodaciResponse, Integer> brojIndeksaColumn;
    @FXML private TableColumn<StudentPodaciResponse, String> jmbgColumn;

    // Dodaj NavigationService u konstruktor (ubrizgaj ga)
    private final NavigationService navigationService;

    public SearchStudentController(StudentService studentService, SifarniciService sifarniciService,
                                   MainView mainView, StudentController studentController,
                                   NavigationService navigationService) {
        this.studentService = studentService;
        this.sifarniciService = sifarniciService;
        this.mainView = mainView;
        this.studentController = studentController;
        this.navigationService = navigationService; // Dodato
    }

    private void otvoriProfilStudenta(StudentPodaciResponse student) {
        // 1. Učitaj samo panel (VBox), nemoj menjati ceo root scene!
        Parent profilView = (Parent) mainView.loadPane("studentPodaciTabPane");

        // 2. Reci navigaciji da pređe na taj view (ovo puni istoriju/history stack)
        navigationService.navigateTo(profilView);

        // 3. Popuni podatke
        studentController.prikaziStudenta(student);
    }

    @FXML
    public void initialize() {
        // Povezivanje kolona sa poljima iz StudentPodaciResponse
        imeColumn.setCellValueFactory(new PropertyValueFactory<>("ime"));
        prezimeColumn.setCellValueFactory(new PropertyValueFactory<>("prezime"));
        brojIndeksaColumn.setCellValueFactory(new PropertyValueFactory<>("brojIndeksa"));
        jmbgColumn.setCellValueFactory(new PropertyValueFactory<>("jmbg"));

        // Implementacija dvoklika na tabelu (Sekcija 1 specifikacije)
        tabelaStudenti.setRowFactory(tv -> {
            TableRow<StudentPodaciResponse> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    StudentPodaciResponse selectedStudent = row.getItem();
                    otvoriProfilStudenta(selectedStudent);
                }
            });
            return row;
        });
    }

    @FXML
    public void handleSearchStudent() {
        // Uzimamo tekst iz polja koje se zove imeStudentaTf (kao u FXML)
        String filterIme = imeStudentaTf.getText();

        // Pozivamo servis za pretragu
        List<StudentPodaciResponse> rezultati = studentService.searchStudents(filterIme);

        // Punimo tabelu koja se zove tabelaStudenti (kao u FXML)
        tabelaStudenti.setItems(FXCollections.observableArrayList(rezultati));
    }

    @FXML
    public void handleClear() {
        imeStudentaTf.clear();
        tabelaStudenti.getItems().clear();
    }
}