package org.raflab.studsluzbadesktopclient.controllers;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.raflab.studsluzbadesktopclient.dtos.StudentPodaciResponse;
import org.raflab.studsluzbadesktopclient.services.NavigationService;
import org.raflab.studsluzbadesktopclient.services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.TableRow;
import javafx.scene.layout.BorderPane;
import org.springframework.context.ApplicationContext;
import java.io.IOException;

import java.util.List;

@Component
public class SearchStudentController{

    private final StudentService studentService;

    @FXML
    private TextField imeStudentaTf;

    @FXML
    private TableView<StudentPodaciResponse> tabelaStudenti;

    public SearchStudentController(StudentService studentService, ApplicationContext applicationContext) {
        this.studentService = studentService;
        this.applicationContext = applicationContext;
    }
    private final ApplicationContext applicationContext; // Dodaj ovo

    @FXML
    void handleSearchStudent(ActionEvent event) {
        String ime = imeStudentaTf.getText();
        System.out.println("DEBUG: Pokrećem pretragu za ime: " + ime);

        List<StudentPodaciResponse> studenti = studentService.searchStudents(ime);

        if (studenti.isEmpty()) {
            System.out.println("DEBUG: Lista studenata je PRAZNA.");
        } else {
            System.out.println("DEBUG: Prvi student u listi JMBG: " + studenti.get(0).getJmbg());
            tabelaStudenti.setItems(FXCollections.observableArrayList(studenti));
            tabelaStudenti.refresh(); // Ponekad JavaFX zahteva refresh da bi prikazao nove kolone
        }
    }
    @Autowired
    private NavigationService navigationService;

    @FXML
    public void initialize() {
        // 1. Kolone
        TableColumn<StudentPodaciResponse, String> colIme = new TableColumn<>("Ime");
        colIme.setCellValueFactory(new PropertyValueFactory<>("ime"));
        TableColumn<StudentPodaciResponse, String> colPrezime = new TableColumn<>("Prezime");
        colPrezime.setCellValueFactory(new PropertyValueFactory<>("prezime"));
        tabelaStudenti.getColumns().setAll(colIme, colPrezime);

        // 2. DVOKLIK (Mora biti ovde!)
        tabelaStudenti.setRowFactory(tv -> {
            TableRow<StudentPodaciResponse> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    otvoriProfilStudenta(row.getItem());
                }
            });
            return row;
        });
    }

    private void otvoriProfilStudenta(StudentPodaciResponse student) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/studentPodaciTabPane.fxml"));
            loader.setControllerFactory(applicationContext::getBean);
            Parent profilView = loader.load();

            StudentController controller = loader.getController();
            controller.prikaziStudenta(student);

            // Uzimamo glavni BorderPane aplikacije
            BorderPane root = (BorderPane) tabelaStudenti.getScene().getRoot();
            navigationService.setMainRoot(root);
            navigationService.navigateTo(profilView);

            // Prečice aktiviramo na novoj sceni
            navigationService.setupShortcuts(profilView.getScene());

        } catch (Exception e) {
            System.err.println("GRESKA: Neuspešno učitavanje profila!");
            e.printStackTrace();
        }
    }
}